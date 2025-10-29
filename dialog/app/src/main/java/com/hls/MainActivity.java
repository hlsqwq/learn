package com.hls;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements onDialogClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDialog myDialog = new myDialog(this);
        myDialog.addClickListener(this);
        myDialog.show();

    }


    @Override
    public void onConfirm(View b) {

    }

    @Override
    public void onCancel(View b) {

    }
}