package com.hls.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hls.R;
import com.hls.ui.viewModel.HomeViewModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import ai.onnxruntime.OnnxJavaType;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.extensions.OrtxPackage;
import lombok.SneakyThrows;
import lombok.val;


public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private com.hls.databinding.FragmentHomeBinding binding;



    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @SneakyThrows
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
        sessionOptions.registerCustomOpLibrary(OrtxPackage.getLibraryPath());
        OrtEnvironment environment = OrtEnvironment.getEnvironment();
        OrtSession session = environment.createSession(readModel(), sessionOptions);

        InputStream open = this.requireContext().getAssets().open("1.png");
        Bitmap deal = deal(open, session, environment);
        binding.image.setImageBitmap(deal);
    }

    @SneakyThrows
    private Bitmap deal(InputStream open, OrtSession session, OrtEnvironment environment) {

        byte[] rawImageBytes = getByte(open);
        ByteBuffer wrap = ByteBuffer.wrap(rawImageBytes);
        long[] array = {rawImageBytes.length};
        OnnxTensor tensor = OnnxTensor.createTensor(environment, wrap, array, OnnxJavaType.UINT8);
        HashMap<String, OnnxTensor> map = new HashMap<>();
        map.put("image",tensor);
        OrtSession.Result run = session.run(map);
        byte[] value = (byte[]) run.get(0).getValue();
        byte[] value1 = (byte[]) run.get(1).getValue();
        System.out.println(Arrays.toString(value1));
        return BitmapFactory.decodeByteArray(value, 0, value.length);
    }


    @SneakyThrows
    private byte[] readModel() {
        InputStream inputStream = this.requireContext().getResources()
                .openRawResource(R.raw.yolov8n_with_pre_post_processing);
        return getByte(inputStream);
    }

    @SneakyThrows
    private byte[] getByte(InputStream inputStream) {
        byte[] buf=new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len=0;
        while ((len=inputStream.read(buf))!=-1){
            byteArrayOutputStream.write(buf,0,len);
        }
        return byteArrayOutputStream.toByteArray();
    }


}