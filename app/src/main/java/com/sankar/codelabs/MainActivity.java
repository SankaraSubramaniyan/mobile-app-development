package com.sankar.codelabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sankar.codelabs.receivers.CustomReceiver;
import com.sankar.codelabs.receivers.PowerReceiver;

public class MainActivity extends AppCompatActivity {
    private PowerReceiver receiver = new PowerReceiver();
    private CustomReceiver customReceiver = new CustomReceiver();
    EditText phoneNumber, smsText;
    final static private String TAG = "MainActivity Log";
    final private int SMS_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        smsText = (EditText)findViewById(R.id.smsText);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(receiver, filter);
        LocalBroadcastManager.getInstance(this).registerReceiver(customReceiver, new IntentFilter("MY_CUSTOM_ACTION"));
    }

    public void sendBroadcast(View view){
        Intent customReceiverIntent = new Intent("MY_CUSTOM_ACTION");
        customReceiverIntent.putExtra("data", "Custom Broadcast message success");
        LocalBroadcastManager.getInstance(this).sendBroadcast(customReceiverIntent);
    }

    public void sendSMSApp(View view){
        assert phoneNumber.getText().toString()!=null;
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto: "+phoneNumber.getText().toString()));
        smsIntent.putExtra("sms_body", smsText.getText().toString());
        Toast.makeText(this, "Opening SMS App", Toast.LENGTH_SHORT).show();

        if(smsIntent.resolveActivity(getPackageManager())!=null){
            startActivity(smsIntent);
        }
        else{
            Log.d(TAG, "No sms app installed");
            Toast.makeText(this, "No sms app installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(View view){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},SMS_REQUEST_CODE);
        }
        else{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber.getText().toString(), null, smsText.getText().toString(), null, null);
            Toast.makeText(this, "Sending SMS", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocation(View view){
        Intent locationActivityIntent = new Intent(this, LocationActivity.class);
        startActivity(locationActivityIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case SMS_REQUEST_CODE:
                if(permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS) &&
                grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    return ;
                }
                else{
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(customReceiver);
    }
}