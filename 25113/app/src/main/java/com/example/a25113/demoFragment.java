package com.example.a25113;

import android.media.tv.ad.TvAdView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class demoFragment extends Fragment {


    public static demoFragment newInstance(int position,int img,String name) {
        demoFragment fragment = new demoFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("img", img);
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_demo, container, false);
        Bundle arguments = getArguments();
        if(arguments!=null){
            TextView textView = inflate.findViewById(R.id.tit);
            textView.setText(arguments.getString("name"));
            ImageView imageView = inflate.findViewById(R.id.img);
            imageView.setImageResource(arguments.getInt("img"));
        }
        return inflate;
    }
}