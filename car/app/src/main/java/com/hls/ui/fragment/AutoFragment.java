package com.hls.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hls.MainActivity;
import com.hls.R;
import com.hls.configuration.BConfig;
import com.hls.databinding.FragmentAutoBinding;
import com.hls.template.ConnectThread;
import com.hls.ui.adapter.LogAdapter;
import com.hls.ui.repository.AutoModel;
import com.hls.ui.repository.BtRepository;
import com.hls.ui.viewModel.AutoViewModel;
import com.hls.ui.viewModel.LogViewModel;

import java.io.IOException;

import lombok.SneakyThrows;

public class AutoFragment extends Fragment {

    private AutoViewModel autoViewModel;
    private com.hls.databinding.FragmentAutoBinding binding;
    private final Handler handler=new Handler();

    private final BtRepository btRepository=BtRepository.getInstance();
    private Runnable timeOut;
    private int stage=0;
    private int distance=20;





    public static AutoFragment newInstance() {
        return new AutoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAutoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SneakyThrows
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoViewModel=new ViewModelProvider(this).get(AutoViewModel.class);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(binding.fragment.getId(),new LogFragment())
                .commit();
        LogViewModel logViewModel = new ViewModelProvider(requireActivity()).get(LogViewModel.class);


//        init();

        timeOut = new Runnable() {
            @Override
            public void run() {
                check();
                startClock();
            }
        };
        btRepository.getDate().observe(getViewLifecycleOwner(),v->{
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
                    forward();
                }
                startClock();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        startClock();
    }

    private void init() {
        for (int i = 0; i < 3; i++) {
            try {
                ConnectThread.write(BConfig.FORWARD);
                ConnectThread.write(BConfig.BACK);
                ConnectThread.write(BConfig.STOP);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void stop() throws IOException, InterruptedException {
        ConnectThread.write(BConfig.STOP);
    }
    public void forward() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectThread.write(BConfig.FORWARD);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        },100);
    }


    public void check() {
        try {
            ConnectThread.write(BConfig.distance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    void startClock() {
        handler.postDelayed(timeOut,5000);
    }
    void stopClock() {
        handler.removeCallbacks(timeOut);
    }


}

