package com.patcornejo.qear.utils;

import android.util.Log;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by patcornejo on 14-09-15.
 */
public class SendMessageThread extends Thread {
    String path;
    String message;

    // Constructor to send a message to the data layer
    public SendMessageThread(String p, String msg) {
        path = p;
        message = msg;
    }

    public void run() {
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(Google.getInstance().getGac()).await();
        for (Node node : nodes.getNodes()) {
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(Google.getInstance().getGac(),
                    node.getId(), path, message.getBytes()).await();

            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "Message: {" + message + "} sent to: " + node.getDisplayName());
            } else {
                Log.v("myTag", "ERROR: failed to send Message");
            }
        }
    }
}
