package com.sankar.codelabs.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null) {
            String receivedText = intent.getExtras().getString("data");
            Toast.makeText(context, "Intent Received "+receivedText, Toast.LENGTH_SHORT).show();
        }
    }
}
