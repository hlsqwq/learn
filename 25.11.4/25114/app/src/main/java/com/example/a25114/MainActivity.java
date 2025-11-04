package com.example.a25114;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String ACTION="com.hls.testBroadcast";

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.send);
        button.setOnClickListener(this);
        demoBroadcast demoBroadcast = new demoBroadcast();
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction(com.example.a25114.demoBroadcast.ALARM_ACTION);
        demoBroadcast.send(getApplicationContext());
        registerReceiver(demoBroadcast,intentFilter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ACTION);
        sendBroadcast(intent);



    }
}