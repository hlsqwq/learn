package com.example.bluetooch;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ioThread extends Thread{
    InputStream in;
    OutputStream out;
    msgHandel msgHandel;

    public ioThread(BluetoothSocket socket){
        try {
            msgHandel=MainActivity.msgHandel;
            if(msgHandel==null){
                msgHandel=AcceptThread.msgHandel;
            }
            in = socket.getInputStream();
            out=socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        super.run();
        byte[] buf=new byte[1024];
        while (true){
            try {
                int read = in.read(buf);
                msgHandel.obtainMessage(msgState.MSG_READ,read,0,buf).sendToTarget();
            } catch (IOException e) {
                msgHandel.obtainMessage(msgState.MSG_ERROR,0,0,0).sendToTarget();
                break;
            }
        }

    }

    public void write(byte[]data){
        try {
            out.write(data);
            msgHandel.obtainMessage(msgState.MSG_WRITE,data.length,0,data).sendToTarget();
        } catch (IOException e) {
            msgHandel.obtainMessage(msgState.MSG_ERROR,0,0,0).sendToTarget();
        }
    }


    public void cancel(){
        try {
            if(in!=null){
                in.close();
                in=null;
            }
            if(out!=null){
                out.close();
                out=null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
