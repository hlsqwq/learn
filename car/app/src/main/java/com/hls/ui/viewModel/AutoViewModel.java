package com.hls.ui.viewModel;

import android.os.Handler;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.hls.configuration.BConfig;
import com.hls.template.ConnectThread;
import com.hls.ui.repository.BtRepository;

import java.io.IOException;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class AutoViewModel extends ViewModel {

    private final BtRepository btRepository=BtRepository.getInstance();
    private final Handler handler=new Handler();
    private final MediatorLiveData<Boolean> mediatorLiveData=new MediatorLiveData<>();
    private int stage=0;
    private int distance=20;



//    AutoViewModel(){
//
//        mediatorLiveData.addSource(btRepository.getFlag(),v->{
//            try {
//                btRepository.getFlag().postValue(false);
//                String value = btRepository.getDate().getValue();
//                String[] split = value.split(",");
//                double f = Double.parseDouble(split[0].substring(8));
//                double b = Double.parseDouble(split[1]);
//                double l = Double.parseDouble(split[2]);
//                double r = Double.parseDouble(split[3].substring(0, split[3].length() - 1));
//                switch (stage){
//                    case 0:
//                        if(f>20){
//                            forward();
//                        }else {
//                            stage++;
//                            distance=50;
//                        }
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }





}