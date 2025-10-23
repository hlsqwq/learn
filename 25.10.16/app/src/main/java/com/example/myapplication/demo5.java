package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class demo5 extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private TextView textView;
    private EditText editText;
    private Button button;
    private CheckBox checkBox;
    private ArrayList<String> strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo5);

        RadioGroup radioGroup = findViewById(R.id.group);
        radioGroup.setOnCheckedChangeListener(this);
        textView = findViewById(R.id.txt);
        editText = findViewById(R.id.ed2);
        button = findViewById(R.id.btn1);
        checkBox = findViewById(R.id.remember);


        SharedPreferences config = getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = config.edit();
        edit.putString("str","hls");
        edit.apply();

        String string = config.getString("str", "");
        textView.setText(string);


        strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        Spinner spinner = findViewById(R.id.sp);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.text, strings);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.pw){
            textView.setText("验证码：");
            editText.setHint("请输入验证码");
            button.setText("获取验证码");
            checkBox.setVisibility(TextView.GONE);
        }
    }

    @Override
    public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
        if(checkedId== R.id.code){
            textView.setText("验证码：");
            editText.setHint("请输入验证码");
            button.setText("获取验证码");
            checkBox.setVisibility(TextView.GONE);
        } else if (checkedId== R.id.pw) {
            textView.setText("密码：");
            editText.setHint("请输入密码");
            button.setText("忘记密码");
            checkBox.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        textView.setText(strings.get(position)+" "+id+" "+position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
