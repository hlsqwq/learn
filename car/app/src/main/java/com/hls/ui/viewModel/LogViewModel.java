package com.hls.ui.viewModel;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hls.ui.adapter.LogAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.Getter;

@Getter
public class LogViewModel extends ViewModel {
    private final MutableLiveData<List<Pair<Integer, String>>> list = new MutableLiveData<>(new ArrayList<>());



    public void add(LogAdapter.Level level, String info){
        String time= new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                .format(new Date());
        String format = String.format("time %s %s %s", time, level, info);
        List<Pair<Integer, String>> list = this.list.getValue();
        if (list != null) {
            list.add(Pair.create(level.ordinal(),info));
        }
        this.list.postValue(list);
    }

    public void addInfo(String info){
        add(LogAdapter.Level.info,info);
    }

    public void addError(String info){
        add(LogAdapter.Level.error,info);
    }

}