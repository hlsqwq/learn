package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.SurfaceControlInputReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.security.PublicKey;

public class demo3 extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener, TextWatcher {

    private TextView textViewtext;
    private CheckBox box;
    private RadioGroup rg;
    private TextView tv;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo3);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        textViewtext = findViewById(R.id.view);
        textViewtext.setBackgroundResource(R.drawable.shape_squ);
        findViewById(R.id.btn3).setBackgroundResource(R.drawable.q1);
        box = findViewById(R.id.box);
        box.setOnCheckedChangeListener(this);
        rg = findViewById(R.id.group);
        rg.setOnCheckedChangeListener(this);
        tv = findViewById(R.id.txt1);
        editText = findViewById(R.id.code);
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btn1){
            textViewtext.setBackgroundResource(R.drawable.shape_squ);
        } else if (v.getId()==R.id.btn2) {
            textViewtext.setBackgroundResource(R.drawable.shape_cycle);
        }
    }

    @Override
    public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
        String format = String.format("你%s，相关协议", isChecked ? "同意了" : "拒绝了");
        box.setText(format);
    }

    @Override
    public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
        if(R.id.group1==checkedId){
            tv.setText("you are a boy");
        }else{
            tv.setText("you are a lady");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            tv.setText("失去焦点");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString();
        if(string.length()==6){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
