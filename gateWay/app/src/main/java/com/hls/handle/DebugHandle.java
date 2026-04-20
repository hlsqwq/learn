package com.hls.handle;


import static com.hls.enumeration.meg.ERROR;
import static com.hls.enumeration.meg.LOG;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

public class DebugHandle extends Handler {

    static DebugHandle debugHandle;
    private DebugHandle(Looper mainLooper){

    }

    public static DebugHandle getInstance(){
        if(debugHandle==null){
            debugHandle= new DebugHandle(Looper.getMainLooper());
        }
        return debugHandle;
    }



    @Override
    public void handleMessage(@NonNull Message msg) {
        int type=msg.what;
        if(type==LOG.ordinal()){

        } else if (type==ERROR.ordinal()) {

        }
    }
}
