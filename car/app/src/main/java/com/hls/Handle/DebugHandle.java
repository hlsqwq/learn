package com.hls.Handle;

import static com.hls.configuration.InfoConfiguration.debug;
import static com.hls.configuration.InfoConfiguration.error;
import static com.hls.configuration.InfoConfiguration.info;
import static com.hls.configuration.InfoConfiguration.task;
import static com.hls.configuration.InfoConfiguration.warn;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.hls.MainActivity;
import com.hls.ui.fragment.LogFragment;

//public class DebugHandle extends Handler{
//
//    private static final DebugHandle handler=new DebugHandle(Looper.getMainLooper());
//
//    private DebugHandle(Looper mainLooper) {
//        super(mainLooper);
//    }
//
//
//    public static void info(String string){
//        handler.obtainMessage(info.ordinal(),string).sendToTarget();
//    }
//
//    public static void error(String string){
//        handler.obtainMessage(error.ordinal(),string).sendToTarget();
//    }
//
//    public static void task(Runnable runnable){
//        handler.obtainMessage(task.ordinal(),runnable).sendToTarget();
//    }
//
//    @Override
//    public void handleMessage(@NonNull Message msg) {
//        if (msg.what==info.ordinal()){
//            String str= msg.obj.toString();
//            LogFragment.logViewModel.addInfo(str);
//        } else if (msg.what==debug.ordinal()) {
//            String str= msg.obj.toString();
//            //todo
//        } else if (msg.what==warn.ordinal()) {
//            String str= msg.obj.toString();
//            //todo
//        }else if (msg.what== error.ordinal()) {
//            String str= msg.obj.toString();
//            LogFragment.logViewModel.addError(str);
//        }else if (msg.what== task.ordinal()){
//            Runnable r= (Runnable) msg.obj;
//            r.run();
//        }
//    }
//}
