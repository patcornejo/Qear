package com.patcornejo.qear.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;

import com.patcornejo.qear.MainActivity;
import com.patcornejo.qear.R;
import com.patcornejo.qear.utils.Constants;
import com.patcornejo.qear.utils.SendMessageThread;


public class NextFragment extends Fragment {

    public static final String TAG = "NextFragment";

    public Boolean go = false;

    DelayedConfirmationView dv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_next, container, false);

        WatchViewStub stub = (WatchViewStub) v.findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(stubl);
        return v;
    }

    private View.OnApplyWindowInsetsListener windowl = new View.OnApplyWindowInsetsListener() {
        @Override
        public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {

            if(insets.isRound()) {
                Button next = (Button) v.findViewById(R.id.next);
                next.setOnClickListener(clickl);
            } else {
                dv.setOnClickListener(clickl);
            }

            return insets;
        }
    };

    private WatchViewStub.OnLayoutInflatedListener stubl = new WatchViewStub.OnLayoutInflatedListener() {
        @Override
        public void onLayoutInflated(WatchViewStub watchViewStub) {
            watchViewStub.setOnApplyWindowInsetsListener(windowl);
            dv = (DelayedConfirmationView) watchViewStub.findViewById(R.id.delayed_confirm);

            dv.setListener(delayl);
            dv.setTotalTimeMs(5000);
            dv.start();

            new SendMessageThread(Constants.QUERY_PATH, "getQuestion").start();
        }
    };

    private DelayedConfirmationView.DelayedConfirmationListener delayl = new DelayedConfirmationView.DelayedConfirmationListener() {
        @Override
        public void onTimerFinished(View view) {
            if(!go) {
                ((DelayedConfirmationView) view).reset();
                ((DelayedConfirmationView) view).start();
            } else {
                ((MainActivity) getActivity()).changeFragment(new QuestionFragment(), QuestionFragment.TAG);
            }
        }

        @Override
        public void onTimerSelected(View view) {}
    };

    private View.OnClickListener clickl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).changeFragment(new RouletteFragment(), RouletteFragment.TAG);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        dv.reset();
    }

}
