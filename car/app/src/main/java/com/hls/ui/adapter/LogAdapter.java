package com.hls.ui.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.hls.databinding.FragmentLogBinding;
import com.hls.databinding.ItemLogBinding;
import com.hls.ui.viewModel.LogViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private final LogViewModel logViewModel;
    public enum Level {
        info,
        debug,
        warning,
        error
    }

    private static final int []colors={0xFF333333,0xFF2196F3,0xFFFF9800,0xFFF44336};

    public LogAdapter(LogViewModel logViewModel){
        this.logViewModel=logViewModel;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLogBinding binding = ItemLogBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        List<Pair<Integer, String>> value = logViewModel.getList().getValue();
        if (value==null){
            return;
        }
        Pair<Integer, String> item = value.get(position);
        holder.binding.text.setText(item.second);
        holder.binding.text.setTextColor(colors[item.first]);
    }


    @Override
    public int getItemCount() {
        return Objects.requireNonNull(logViewModel.getList().getValue()).size();
    }



    public static class LogViewHolder extends RecyclerView.ViewHolder {

        public ItemLogBinding binding;
        public LogViewHolder(ItemLogBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
