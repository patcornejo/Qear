package com.patcornejo.qear.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patcornejo.qear.R;


public class SplashFragment extends Fragment {

    public static final String TAG = "SplashFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);

        return v;
    }


}
