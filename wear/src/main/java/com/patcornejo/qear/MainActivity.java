package com.patcornejo.qear;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.patcornejo.qear.events.GoogleListener;
import com.patcornejo.qear.fragments.NextFragment;
import com.patcornejo.qear.fragments.RouletteFragment;
import com.patcornejo.qear.fragments.SplashFragment;
import com.patcornejo.qear.models.Config;
import com.patcornejo.qear.models.Data;
import com.patcornejo.qear.models.Question;
import com.patcornejo.qear.utils.Constants;
import com.patcornejo.qear.utils.Globals;
import com.patcornejo.qear.utils.Google;
import com.patcornejo.qear.utils.SendMessageThread;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Google.getInstance().init(gl);
        changeFragment(new SplashFragment(), SplashFragment.TAG);
    }

    public void changeFragment(Fragment fragment, String tag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        transaction.commitAllowingStateLoss();
    }

    private GoogleListener gl = new GoogleListener() {
        @Override
        public void onConnected() {
            Wearable.MessageApi.addListener(Google.getInstance().getGac(), msgl).setResultCallback(resl);
        }
    };

    private ResultCallback<Status> resl = new ResultCallback<Status>() {
        @Override
        public void onResult(Status status) {
            Log.v("myTag", String.valueOf(status.getStatus().isSuccess()));

            if(status.isSuccess())
                new SendMessageThread(Constants.CONFIG_PATH, "activate").start();
        }
    };

    private MessageApi.MessageListener msgl = new MessageApi.MessageListener() {
        @Override
        public void onMessageReceived(final MessageEvent messageEvent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("myTag", "onMessageReceived: " + messageEvent.getPath() + " - " + new String(messageEvent.getData()));
                    String msg = new String(messageEvent.getData());

                    switch (messageEvent.getPath()) {
                        case Constants.CONFIG_PATH:
                            if (msg.equals("connected"))
                                new SendMessageThread(Constants.QUERY_PATH, "getConfig").start();

                            break;
                        case Constants.QUERY_PATH:
                            Log.i("myTag", msg);

                            Data data = new Gson().fromJson(msg, Data.class);

                            if (data.success) {
                                if (data.method.contains("configs")) {
                                    Globals.config = new Gson().fromJson(new Gson().toJson(data.item), Config.class);
                                    changeFragment(new RouletteFragment(), RouletteFragment.TAG);
                                } else if (data.method.contains("questions")) {
                                    Log.i("myTag", data.item.toString());
                                    Globals.question = new Gson().fromJson(new Gson().toJson(data.item), Question.class);

                                    if (getFragment(RouletteFragment.TAG) instanceof RouletteFragment)
                                        ((RouletteFragment) getFragment(RouletteFragment.TAG)).go = true;
                                    else if (getFragment(NextFragment.TAG) instanceof NextFragment)
                                        ((NextFragment) getFragment(NextFragment.TAG)).go = true;
                                }
                            }

                            break;
                    }
                }
            });
        }
    };

    private Fragment getFragment(String TAG) {
        FragmentManager fm = getFragmentManager();
        return fm.findFragmentByTag(TAG);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(Google.getInstance().isConnected()) {
            Google.getInstance().disconnect();
            Wearable.MessageApi.removeListener(Google.getInstance().getGac(), msgl);
        }
    }
}
