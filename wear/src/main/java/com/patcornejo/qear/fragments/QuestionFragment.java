package com.patcornejo.qear.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.TextView;

import com.patcornejo.qear.MainActivity;
import com.patcornejo.qear.R;
import com.patcornejo.qear.adapters.AnswersAdapter;
import com.patcornejo.qear.utils.Globals;

public class QuestionFragment extends Fragment {

    public static final String TAG = "QuestionFragment";
    DelayedConfirmationView dv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        WatchViewStub stub = (WatchViewStub) v.findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(stubl);

        return v;
    }

    private WatchViewStub.OnLayoutInflatedListener stubl = new WatchViewStub.OnLayoutInflatedListener() {
        @Override
        public void onLayoutInflated(WatchViewStub watchViewStub) {
            dv = (DelayedConfirmationView) watchViewStub.findViewById(R.id.delayed_confirm);

            GridViewPager gvp = (GridViewPager) watchViewStub.findViewById(R.id.pager);
            DotsPageIndicator dots = (DotsPageIndicator) watchViewStub.findViewById(R.id.page_indicator);
            TextView title = (TextView) watchViewStub.findViewById(R.id.title);
            TextView category = (TextView) watchViewStub.findViewById(R.id.category);

            dv.setListener(delayl);
            dv.setTotalTimeMs(10000);
            dv.start();

            title.setText(Globals.question.Title);
            category.setText(Globals.config.Categories.get(Globals.question.CategoryID - 1).get("name"));

            gvp.setAdapter(new AnswersAdapter(getActivity()));
            dots.setPager(gvp);
        }
    };

    private DelayedConfirmationView.DelayedConfirmationListener delayl = new DelayedConfirmationView.DelayedConfirmationListener() {
        @Override
        public void onTimerFinished(View view) {
            Intent intent = new Intent(getActivity(), ConfirmationActivity.class);
            intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
            intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Correcto!");
            startActivity(intent);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    ((MainActivity) getActivity()).changeFragment(new NextFragment(), NextFragment.TAG);
                }
            }, 1666);

        }

        @Override
        public void onTimerSelected(View view) {}
    };

    @Override
    public void onStop() {
        super.onStop();
        dv.reset();
    }
}
