package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;

public class demo2 extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2);
        Button b = findViewById(R.id.back);
        b.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        String format = String.format("the receive info success，time：%s，data：%s", extras.getString("time")
                , extras.getString("text"));
        TextView tv = findViewById(R.id.text);
        tv.setText(format);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.back){
            Intent intent = new Intent(this, demo1Activity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                bundle.putString("time", String.valueOf(LocalDateTime.now()));
            }
            bundle.putString("data","it is looking really good");
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            finish();
//            startActivity(intent);
        }
    }
}
