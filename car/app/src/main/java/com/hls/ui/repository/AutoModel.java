package com.hls.ui.repository;

import android.os.Handler;
import android.os.ProfilingTrigger;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;

import com.hls.MainActivity;
import com.hls.template.CallBack;
import com.hls.ui.fragment.AutoFragment;
import com.hls.ui.viewModel.AutoViewModel;
import com.hls.ui.viewModel.LogViewModel;

public class AutoModel {


    public final MediatorLiveData<Object> mediatorLiveData;
    private Handler handler=new Handler();
    private CallBack callBack;

    private BtRepository btRepository=BtRepository.getInstance();
    private Runnable timeOutRunable;

    private int stage=0;
    private int distance=20;

    public interface CallBack{
        void timeOut();
        void defaultEvent();
    }

    public AutoModel(CallBack callBack){
        timeOutRunable=new Runnable() {
            @Override
            public void run() {
                callBack.timeOut();
                startClock();
            }
        };
        mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(btRepository.getDate(), v->{
            stopClock();
            try {
                String[] split = v.split(",");
                double f = Double.parseDouble(split[0].substring(8));
                double b = Double.parseDouble(split[1]);
                double l = Double.parseDouble(split[2]);
                double r = Double.parseDouble(split[3].substring(0, split[3].length() - 1));

                Log.i(MainActivity.TAG,f+"");
                if(stage==2 && f<=distance){
                    //todo
                } else if (stage==1 && f<=distance) {
                    //todo
                } else if (stage == 0 && f <= distance) {
                    stage++;
                    // todo rotate
                    distance=50;
                } else{
                    callBack.defaultEvent();
                }

                startClock();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        startClock();
    }



    void startClock() {
        handler.postDelayed(timeOutRunable,5000);
    }
    void stopClock() {
        handler.removeCallbacks(timeOutRunable);
    }


}
