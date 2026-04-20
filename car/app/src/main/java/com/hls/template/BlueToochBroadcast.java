package com.hls.template;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import com.hls.MainActivity;

import java.util.HashSet;
import java.util.Set;

public class BlueToochBroadcast extends BroadcastReceiver {

    private Set<BluetoothDevice> list;
    private BluetoothAdapter adapter;
    private boolean is_restart;


    public BlueToochBroadcast(BluetoothAdapter adapter) {
        list = new HashSet<>();
        this.is_restart = false;
        this.adapter = adapter;
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT})
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            list.add(device);
        } else if (ACTION_DISCOVERY_FINISHED.equals(action)) {
            if (is_restart) {
                adapter.startDiscovery();
            }
        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            // 解析广播中的设备和状态
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            int previousBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

            // 判定配对成功
            if (bondState == BluetoothDevice.BOND_BONDED) {
                Log.i(MainActivity.TAG,"配对成功");
            }
            // 判定配对失败（从“配对中”变为“未配对”）
            else if (bondState == BluetoothDevice.BOND_NONE && previousBondState == BluetoothDevice.BOND_BONDING) {
                Log.e(MainActivity.TAG,"配对失败！请检查PIN码是否正确");
            }

            // 配对中（可选：提示用户正在处理）
            else if (bondState == BluetoothDevice.BOND_BONDING) {
                Log.i(MainActivity.TAG,"正在配对中，请输入PIN码...");
            }
        }
    }


    public Set<BluetoothDevice> get_list() {
        return this.list;
    }

}
