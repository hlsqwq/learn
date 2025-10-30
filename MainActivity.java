package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private myBaseAdapt myBaseAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<item> items = new ArrayList<>();
        items.add(new item(R.drawable.p1,"qwer","hbsdfbhsdhfbh"));
        items.add(new item(R.drawable.p1234,"qwer","hbsdfbhsdhfbh"));
        GridView spinner = findViewById(R.id.list);
        myBaseAdapt = new myBaseAdapt(items, this);
        spinner.setAdapter(myBaseAdapt);
//        spinner.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        spinner.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),position+"", Toast.LENGTH_SHORT).show();
        myBaseAdapt.add(new item(R.drawable.q2,"qwer","hbsdfbhsdhfbh"));
    }
}