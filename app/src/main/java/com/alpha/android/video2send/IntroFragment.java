package com.alpha.android.video2send;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class IntroFragment extends Fragment implements
        FragmentCompat.OnRequestPermissionsResultCallback{


    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.GET_ACCOUNTS
    };

    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int  REQUEST_PERMISSIONS_CODE =3;
    private final int INTRO_DURATION = 2 * 1000;


    private CountDownTimer mTimer = new CountDownTimer(INTRO_DURATION, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            startCamera2Video();
        }
    };


    public IntroFragment() {
        // Required empty public constructor

    }

    // TODO: Rename and change types and number of parameters
    public static IntroFragment newInstance() {
        return new IntroFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(!hasPermissionsGranted(REQUIRED_PERMISSIONS)){
            requestPermissions(REQUIRED_PERMISSIONS);
            return;
        }
        mTimer.start();
    }

    void startCamera2Video(){
        if(getActivity()  != null ) {

            FragmentManager manager = getActivity().getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            this.onDestroy();
            ft.remove(this);
            ft.replace(R.id.container, Camera2VideoFragment.newInstance());
            //container is the ViewGroup of current fragment
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private void requestPermissions(String[] permissions) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            new ConfirmationDialog().show(getFragmentManager(), FRAGMENT_DIALOG);
        } else {
            FragmentCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (FragmentCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult permissions = " + permissions[0]);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.length == permissions.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        ErrorDialog.newInstance(getString(R.string.request_permission))
                                .show(getFragmentManager(), FRAGMENT_DIALOG);
                        break;
                    }
                }
                mTimer.start();
            } else {
                ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getFragmentManager(), FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }


    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    public static class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                                    REQUEST_PERMISSIONS_CODE);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    parent.getActivity().finish();
                                }
                            })
                    .create();
        }

    }
}
