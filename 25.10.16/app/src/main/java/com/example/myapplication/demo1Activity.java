package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class demo1Activity extends AppCompatActivity implements View.OnClickListener {


    private ActivityResultLauncher<Intent> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);
        Button b = findViewById(R.id.btn);
        b.setOnClickListener(this);
        result = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , o -> {
                    if (o != null) {
                        Intent data = o.getData();
                        if (data != null && o.getResultCode() == RESULT_OK) {
                            Bundle extras = data.getExtras();
                            String format = String.format("the receive info success，time：%s，data：%s", extras.getString("time")
                                    , extras.getString("data"));
                            TextView tv = findViewById(R.id.text);
                            tv.setText(format);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            Button b = (Button) v;
            Intent intent = new Intent(this, demo2.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                bundle.putString("time", String.valueOf(LocalDateTime.now()));
            }
            bundle.putString("text", "the weather is good today");
            intent.putExtras(bundle);
            result.launch(intent);
//            startActivity(intent);
        }
    }
}
