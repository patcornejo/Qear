package com.patcornejo.qear.fragments;

import android.animation.Animator;
import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.patcornejo.qear.MainActivity;
import com.patcornejo.qear.R;
import com.patcornejo.qear.utils.Constants;
import com.patcornejo.qear.utils.Globals;
import com.patcornejo.qear.utils.SendMessageThread;

import java.util.Random;


public class RouletteFragment extends Fragment {

    public static final String TAG = "RouletteFragment";

    private ImageView iv;

    public Boolean go = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_roulette, container, false);

        iv = (ImageView) v.findViewById(R.id.roulette);
        iv.setOnClickListener(clikl);
        return v;
    }

    private View.OnClickListener clikl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SendMessageThread(Constants.QUERY_PATH, "getQuestion").start();
            //iv.setOnClickListener(null);
            v.setRotation(0.0f);

            v.animate()
                    .rotationBy(360f)
                    .setDuration(800)
                    .setInterpolator(new LinearInterpolator())
            .setListener(anil);
        }
    };

    private Animator.AnimatorListener anil = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {}

        @Override
        public void onAnimationEnd(Animator animation) {
            if(!go) {
                iv.animate()
                        .rotationBy(360f)
                        .setDuration(800)
                        .setInterpolator(new LinearInterpolator());
            } else {
                iv.animate()
                        .rotationBy(Float.valueOf(Globals.config.Categories.get(Globals.question.CategoryID - 1).get("angle")) + 360f)
                        .setDuration(2000)
                        .setListener(null)
                        .setInterpolator(new DecelerateInterpolator());

                ((MainActivity) getActivity()).changeFragment(new QuestionFragment(), QuestionFragment.TAG);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        iv.clearAnimation();
    }
}
