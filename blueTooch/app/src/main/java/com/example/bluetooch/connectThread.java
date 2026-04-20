package com.example.bluetooch;

import static com.example.bluetooch.MainActivity.MY_UUID;
import static com.example.bluetooch.MainActivity.log;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresPermission;

import java.io.IOException;

public class connectThread extends Thread{
    private BluetoothSocket socket;
    public static ioThread ioThread;
    private msgHandel msgHandel;


    public connectThread(BluetoothDevice bluetoothDevice){
        try {
            msgHandel=MainActivity.msgHandel;
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    @Override
    public void run() {
        try {
            socket.connect();
        }catch (IOException e) {
            Log.e("hls", "线程退出:"+e.getMessage(), e);
            return;

        }
        ioThread = new ioThread(socket);
        ioThread.start();
        msgHandel.obtainMessage(msgState.MSG,"客户端连接成功").sendToTarget();
    }


    public void cancel(){
        try {
            if(socket != null){
                socket.close();
                socket=null;
            }
            if(ioThread != null){
                ioThread.cancel();
                ioThread=null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
