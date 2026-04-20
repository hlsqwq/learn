package com.hls.template;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.hls.MainActivity;
import com.hls.ui.viewModel.ActivityViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ConnectThread extends Thread {

    private BluetoothSocket socket;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private InputStream inputStream;
    private static OutputStream outputStream;
    private final CallBack callBack;

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public ConnectThread(BluetoothDevice bluetoothDevice, CallBack callBack) throws IOException {
        this.callBack=callBack;
        socket=bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
        try {
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            Log.i(MainActivity.TAG,"蓝牙连接成功");
        } catch (IOException connectException) {
            connectException.printStackTrace();
            Log.e(MainActivity.TAG,"连接错误");
            socket.close();
        }
    }



    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void run() {
        byte[] bytes = new byte[1024];
        while (true){
            try {
                if(inputStream==null){
                    continue;
                }
                int read = inputStream.read(bytes);
                Log.i(MainActivity.TAG,"蓝牙收到数据");
                callBack.read(-1,new String(bytes,0,read));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void write(String data) throws IOException {
        if (outputStream==null){
            return;
        }
        outputStream.write(data.getBytes(StandardCharsets.US_ASCII));
        Log.i(MainActivity.TAG,"发送成功:"+data);
    }
}