package com.example.myapplication;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class demo4 extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private TextView textView;
    private com.example.myapplication.databinding.ActivityMainBinding inflate;
    private View viewById;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo4);
        findViewById(R.id.btn1).setOnClickListener(this);
        textView = findViewById(R.id.txt1);
        inflate = ActivityMainBinding.inflate(getLayoutInflater());
        findViewById(R.id.btn2).setOnClickListener(this);
        viewById = findViewById(R.id.txt2);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn2){
            LocalDateTime now = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                now = LocalDateTime.now();
                int hour = now.getHour();
                int minute = now.getMinute();
                int second = now.getSecond();
                TimePickerDialog d = new TimePickerDialog(this, this, hour, minute, true);
                d.show();
                return;
            }

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("dialog box");
        builder.setMessage("确定离开吗");
        builder.setPositiveButton("离开",(d,w)->{
           textView.setText("choose to leave");
        });
        builder.setNegativeButton("我在想想",(d,w)->{
            textView.setText("choose to stay");
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textView.setText("choose to time is "+hourOfDay+":"+minute);
    }
}
