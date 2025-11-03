package com.example.a25113;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class newMyAdapter extends PagerAdapter {

    List<View> list;

    public newMyAdapter(Context context, List<item>list){
        this.list=new ArrayList<>();
        int count=0;
        for (item item : list) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.demo, null);
            ImageView imageView = inflate.findViewById(R.id.img);
            RadioGroup radioGroup = inflate.findViewById(R.id.group);
            Button button = inflate.findViewById(R.id.start);

            for(int i=0;i<list.size();i++){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setPadding(10,10,10,10);
                if(count==i){
                    radioButton.setChecked(true);
                }
                radioGroup.addView(radioButton);
            }
            imageView.setImageResource(item.getImg());
            if(count==list.size()-1){
                button.setVisibility(ViewGroup.VISIBLE);
            }
            this.list.add(inflate);
            count++;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(list.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageView = list.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
