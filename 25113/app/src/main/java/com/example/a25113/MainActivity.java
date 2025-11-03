package com.example.a25113;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private List<item> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
//        ImageView imageView = new ImageView(this);
//        ImageView imageView2 = new ImageView(this);
//        ImageView imageView3 = new ImageView(this);
//        imageView.setImageResource(R.drawable.a1);
//        imageView2.setImageResource(R.drawable.a2);
//        imageView3.setImageResource(R.drawable.a3);
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT
//                , ViewGroup.LayoutParams.WRAP_CONTENT));
//        imageView2.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT
//                , ViewGroup.LayoutParams.WRAP_CONTENT));
//        imageView3.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT
//                , ViewGroup.LayoutParams.WRAP_CONTENT));
        list.add(new item(R.drawable.a1,"1"));
        list.add(new item(R.drawable.a2,"2"));
        list.add(new item(R.drawable.a3,"3"));



        ViewPager view = findViewById(R.id.page);
//        myAdapter myAdapter = new myAdapter(list);
//        newMyAdapter newMyAdapter = new newMyAdapter(this, list);
        fragmentAdapter fragmentAdapter = new fragmentAdapter(getSupportFragmentManager(), list);
        view.setAdapter(fragmentAdapter);
        view.addOnPageChangeListener(this);
//        view.setCurrentItem(2);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Toast.makeText(this,list.get(position).getName(),LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}