
package com.alpha.android.video2send;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class CameraActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private final static String TAG = "CameraActivity";

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.GET_ACCOUNTS
    };

    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int  REQUEST_PERMISSIONS_CODE =3;

    private IntroFragment sInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sInstance = IntroFragment.newInstance();

        setContentView(R.layout.activity_camera);
        if(null == savedInstanceState){
            ready2Go();
        }
    }

    void ready2Go(){
        Log.d(TAG,"ready2Go");
        getFragmentManager().beginTransaction()
                .replace(R.id.container, sInstance)
                .commit();
    }
}
