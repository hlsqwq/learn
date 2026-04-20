package com.example.bluetooch;

import static com.example.bluetooch.MainActivity.chat;
import static com.example.bluetooch.MainActivity.log;
import static com.example.bluetooch.msgState.MSG;
import static com.example.bluetooch.msgState.MSG_ERROR;
import static com.example.bluetooch.msgState.MSG_READ;
import static com.example.bluetooch.msgState.MSG_WRITE;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class msgHandel extends Handler {

    private final String name;

    public msgHandel(String name) {
        super(Looper.getMainLooper());
        this.name=name;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case MSG_READ:
                byte[] data = (byte[]) msg.obj;
                String s = new String(data, 0, msg.arg1);
                String str=name+":"+s+"\n";
                chat.append(str);
                break;
            case MSG_WRITE:
                byte[] data1 = (byte[]) msg.obj;
                String s1 = new String(data1, 0, msg.arg1);
                String str1="ME"+":"+s1+"\n";
                chat.append(str1);
                break;
            case MSG_ERROR:
                log.append("连接中断，或意外错误\n");
                break;
            case MSG:
                log.append(msg.obj+"\n");
                break;
        }

    }
}


