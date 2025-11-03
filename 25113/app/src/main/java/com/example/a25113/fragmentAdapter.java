package com.example.a25113;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class fragmentAdapter extends FragmentPagerAdapter {
    private final List<item> list;

    public fragmentAdapter(@NonNull FragmentManager fm, List<item>list) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.list=list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return demoFragment.newInstance(position,list.get(position).getImg(),list.get(position).getName());
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
