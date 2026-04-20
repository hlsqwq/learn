package com.hls.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.hls.R;
import com.hls.template.BlueToochConnect;
import com.hls.template.CallBack;
import com.hls.template.Connect;
import com.hls.template.UartConnect;

import java.util.ArrayList;
import java.util.List;

public class SettleDialog extends Dialog implements AdapterView.OnItemClickListener {

    private List<String> str;
    private Activity activity;
    private chooseDialog chooseDialog;

    private Connect connect;

    public SettleDialog(@NonNull Context context) {
        super(context);

        connect = null;
        this.activity = (Activity) context;
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settle_dialog);

        ListView listView = findViewById(R.id.list);
        str = new ArrayList<>();
        str.add("蓝牙");
        str.add("无线");
        str.add("串口");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.item, str);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT})
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Connect blueToochConnect = new BlueToochConnect(activity, activity.getApplication(), new CallBack() {
                    @Override
                    public void read(int count, String data) {

                    }
                });
                if (chooseDialog != null) {

                }
                chooseDialog = new chooseDialog(this.getContext(), blueToochConnect);
                chooseDialog.show();
                this.connect = blueToochConnect;
                break;
            case 1:
                break;
            case 2:
                Connect uartConnect = new UartConnect(activity.getApplication(), new CallBack() {
                    @Override
                    public void read(int count, String data) {

                    }
                });
                chooseDialog = new chooseDialog(this.getContext(), uartConnect);
                chooseDialog.show();
                this.connect = uartConnect;
                break;
        }
        this.dismiss();
    }


    public Connect get_connect() {
        return connect;
    }


}
