package com.hls.ui.repository;

import android.Manifest;
import android.app.Activity;
import android.health.connect.datatypes.Device;

import androidx.annotation.RequiresPermission;
import androidx.lifecycle.MutableLiveData;

import com.hls.template.BlueToochConnect;
import com.hls.template.CallBack;
import com.hls.template.Connect;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
public class BtRepository {

    private static volatile BtRepository btRepository;
    private MutableLiveData<String>date=new MutableLiveData<>();
    private BtRepository(){}

    public static BtRepository getInstance(){
        if(btRepository==null){
            synchronized (BtRepository.class){
                if(btRepository==null) {
                    btRepository=new BtRepository();
                }
            }
        }
        return btRepository;
    }






}
