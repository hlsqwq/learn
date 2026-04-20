package com.hls.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.health.connect.datatypes.Device;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.hls.R;
import com.hls.template.BlueToochConnect;
import com.hls.template.Connect;
import com.hls.template.DeviceAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class chooseDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener {

    private final Connect connect;
    private DeviceAdapter pair_adapter;
    private DeviceAdapter scan_adapter;

    public chooseDialog(@NonNull Context context, Connect connect) {
        super(context);
        this.connect = connect;
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_dialog);

        List<Object> pair = new ArrayList<>();
        List<Object> scan = new ArrayList<>();

        pair_adapter = new DeviceAdapter(this.getContext(), R.layout.item, pair);
        scan_adapter = new DeviceAdapter(this.getContext(), R.layout.item, scan);

        ListView listView = findViewById(R.id.pair_list);
        listView.setAdapter(pair_adapter);
        listView.setOnItemClickListener(this);

        ListView listView1 = findViewById(R.id.scan_list);
        listView1.setAdapter(scan_adapter);
        listView1.setOnItemClickListener(this);
        LinearLayout layout = findViewById(R.id.pair);


        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(this);

        if (connect instanceof BlueToochConnect) {
            layout.setVisibility(LinearLayout.VISIBLE);
            BlueToochConnect b = (BlueToochConnect) connect;
            pair_adapter.setList(new ArrayList<>(b.get_bond_device()));
            pair_adapter.notifyDataSetChanged();
        } else {
            layout.setVisibility(LinearLayout.GONE);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.pair_list) {
            Object item = pair_adapter.getItem(position);
            new Thread(() -> {
                try {
                    connect.connect(item);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
            this.dismiss();
        } else if (parent.getId() == R.id.scan_list) {
            Object item = scan_adapter.getItem(position);
            new Thread(() -> {
                try {
                    connect.connect(item);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
            this.dismiss();
        }
    }




    @Override
    public void onClick(View v) {
        List<Object> deviceList = null;
        try {
            deviceList = connect.getDeviceList();
            if (deviceList == null || deviceList.isEmpty()) {
                return;
            }
            scan_adapter.getList().clear();
            scan_adapter.setList(deviceList);
            scan_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
