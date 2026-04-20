package com.example.bluetooch;

import static com.example.bluetooch.MainActivity.MY_UUID;
import static com.example.bluetooch.MainActivity.NAME;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Objects;
import java.util.UUID;

public class AcceptThread extends Thread {

    private BluetoothServerSocket serverSocket;
    private final BluetoothAdapter bluetoothAdapter;
    public static ioThread ioThread;
    public static msgHandel msgHandel;

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public AcceptThread() {
        try {
            bluetoothAdapter=MainActivity.bluetoothAdapter;
            serverSocket = bluetoothAdapter
                    .listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT})
    @Override
    public void run() {
        super.run();
        BluetoothSocket accept = null;
        while (true) {
            try {
                accept = serverSocket.accept();
                if (accept != null) {
                    BluetoothDevice remoteDevice = accept.getRemoteDevice();
                    String name= Objects.equals(remoteDevice.getName(), "")?"未知设备":remoteDevice.getName();
                    msgHandel = new msgHandel(name);
                    msgHandel.obtainMessage(msgState.MSG,"服务器连接成功").sendToTarget();
                    bluetoothAdapter.cancelDiscovery();
                    ioThread = new ioThread(accept);
                    ioThread.start();
                    serverSocket.close();
                    break;
                }
            }catch (IOException e) {
                Log.e("hls", "线程退出:"+e.getMessage(), e);
                return;
            }

        }
    }

    // Closes the connect socket and causes the thread to finish.
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void cancel() {
        try {
            bluetoothAdapter.cancelDiscovery();
            if(serverSocket!=null){
                serverSocket.close();
                serverSocket=null;
            }
            if(ioThread!=null){
                ioThread.cancel();
                ioThread=null;
            }
        } catch (IOException e) {
            Log.e("hls", "Could not close the connect socket", e);
        }
    }
}
