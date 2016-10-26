package com.alpha.android.video2send;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by alpha on 10/24/16.
 */

public class UploadingService extends Service implements  LocationListener{

    private final static String  TAG = "UploadingService";
    public final static String UPLOAD_FILENAME = "upload_filename";
    public final static String UPLOAD_LOCATION = "upload_location";
    private static final String UPLOAD_URL = "http://api.playgroundz.tv/api/video/appupload";
    private final static int MSG_NEXT_UPLOAD = 1;
    private final static int MSG_UPLOAD_DONE = 2;
    private final static int MSG_GET_ACCOUNT = 3;

    private LocationManager mLm;
    private IBinder mBinder = new LocalBinder();
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_NEXT_UPLOAD:
                    handleUploadVideo((String)msg.obj);
                    break;
                case MSG_UPLOAD_DONE:
                    handleUploadDone((String)msg.obj);
                    break;
                case MSG_GET_ACCOUNT:
                    handleGetGoogleAccount();
                    break;
                default:
                    break;
            }
        }
    };

    Location mLocation;
    Account mGoogleAccount;


    public void setLocation(Location location){
        mLocation = location;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LocalBinder extends Binder {
        UploadingService getService() {
            return UploadingService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        super.onCreate();
        mLm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        mLocation = mLm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        Message msg = mHandler.obtainMessage(MSG_GET_ACCOUNT);
        mHandler.sendMessage(msg);
    }

    public void uploadNextVideo(String uploadFile){
        Message msg = mHandler.obtainMessage(MSG_NEXT_UPLOAD,uploadFile);
        mHandler.sendMessage(msg);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void handleUploadVideo(String fileName){
        Log.d(TAG,"next upload file = " + fileName);
        new UploadVideoTask().execute(fileName);
    }

    private void handleUploadDone(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            file.delete();
        }
        showToast("Upload Finished");
    }

    private void handleGetGoogleAccount(){
        AccountManager am = (AccountManager)getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = am.getAccounts();
        for(Account a : accounts){
            Log.d(TAG,"Account = " + a.name + " type = " + a.type);
            if(a.type.equals("com.google")){
                mGoogleAccount = a;
            }
        }
    }
    private class UploadVideoTask extends AsyncTask<String,Void,Void> {
        HttpURLConnection mConn;
        DataOutputStream mDos;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        private int serverResponseCode = 0;
        String androidId;

        @Override
        protected Void doInBackground(String... params) {
            try {
                final String videoFile  =  params[0];
                Log.d(TAG,"doInBackground file = "+ params[0]);
                FileInputStream fileInputStream = new FileInputStream(new File(videoFile));
                URL url = new URL(UPLOAD_URL);
                mConn = (HttpURLConnection)url.openConnection();
                mConn.setDoInput(true); // Allow Inputs
                mConn.setDoOutput(true); // Allow Outputs
                mConn.setUseCaches(false); // Don't use a Cached Copy
                mConn.setRequestMethod("POST");
                mConn.setRequestProperty("Connection", "Keep-Alive");
                mConn.setRequestProperty("ENCTYPE", "multipart/form-data");
                mConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                mConn.setRequestProperty("file", videoFile);
                mConn.setRequestProperty("account","test");
                if(mGoogleAccount != null) {
                    mConn.setRequestProperty("email", mGoogleAccount.name);
                }
                mConn.setRequestProperty("androidid",androidId);
                if(mLocation != null) {
                    mConn.setRequestProperty("lat", String.valueOf(mLocation.getLatitude()));
                    mConn.setRequestProperty("long", String.valueOf(mLocation.getLongitude()));
                    mConn.setRequestProperty("accuracy", String.valueOf(mLocation.getAccuracy()));
                }


                mDos = new DataOutputStream(mConn.getOutputStream());

                mDos.writeBytes(twoHyphens + boundary + lineEnd);
                mDos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                        + videoFile + "\"" + lineEnd);

                mDos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    mDos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math
                            .min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0,
                            bufferSize);

                }

                // send multipart form data necesssary after file
                // data...
                mDos.writeBytes(lineEnd);
                mDos.writeBytes(twoHyphens + boundary + twoHyphens
                        + lineEnd);
                serverResponseCode = mConn.getResponseCode();
                String serverResponseMessage = mConn.getResponseMessage();
                Log.i(TAG, "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if (serverResponseCode == 200) {
                    //FIX ME  delete the file
//                    showToast("Upload Finished");
                    Message msg = mHandler.obtainMessage(MSG_UPLOAD_DONE,videoFile);
                    mHandler.sendMessage(msg);
                }

                // close the streams //
                fileInputStream.close();
                mDos.flush();
                mDos.close();


            }catch (IOException ioe){
                Log.e(TAG,ioe.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            androidId = Settings.System.getString(getApplicationContext().getContentResolver(), Settings.System.ANDROID_ID);
            Log.d(TAG,"androidId = " + androidId);
        }

    }


    private void showToast(final String text) {
        Log.d(TAG,"show Toast" + text);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}
