package com.hls;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class myDialog extends Dialog implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private double val1;
    private double val2;
    private double val3;

    private onDialogClickListener listener;
    private SeekBar s1;
    private TextView st;

    public myDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        s1 = findViewById(R.id.seek);
        s1.setOnSeekBarChangeListener(this);
        st = findViewById(R.id.seek_text);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.8);
            window.setAttributes(params);
        }
    }

    public void addClickListener(onDialogClickListener listener){
        this.listener=listener;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if(seekBar.getId()== R.id.seek){
            String s=progress+"°";
            st.setText(s);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.confirm){
            listener.onConfirm(v);
            dismiss();
        } else if (v.getId()==R.id.cancel) {
            listener.onCancel(v);
            dismiss();
        }
    }
}
