package com.sankar.codelabs.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PowerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null) {
            String message = "Unknown Broadcast";
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    message = "Power Connected";
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    message = "Power Disconnected";
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
