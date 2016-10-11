package com.alpha.android.video2send;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class IntroFragment extends Fragment {
    private final int INTRO_DURATION = 2 * 1000;

    private final  IntroFragment intro=null;
    private CountDownTimer mTimer = new CountDownTimer(INTRO_DURATION,1000) {
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
//        super.onViewCreated(view, savedInstanceState);
        mTimer.start();
    }

    void startCamera2Video(){
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
