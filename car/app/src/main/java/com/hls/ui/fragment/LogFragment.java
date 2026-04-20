package com.hls.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hls.R;
import com.hls.databinding.FragmentLogBinding;
import com.hls.ui.adapter.LogAdapter;
import com.hls.ui.viewModel.LogViewModel;

import java.util.Objects;


public class LogFragment extends Fragment {

    private FragmentLogBinding binding;
    public static LogViewModel logViewModel;


    public static LogFragment newInstance() {
        return new LogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logViewModel = new ViewModelProvider(requireActivity()).get(LogViewModel.class);
        LogAdapter logAdapter = new LogAdapter(logViewModel);
        binding.log.setAdapter(logAdapter);
        binding.log.setLayoutManager(new LinearLayoutManager(this.getContext()));
        logViewModel.getList().observe(getViewLifecycleOwner(), v->{
            binding.log.scrollToPosition(Objects.requireNonNull(logViewModel.getList().getValue()).size()-1);
        });

    }

}