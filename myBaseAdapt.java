package com.example.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.TextViewKt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class myBaseAdapt extends BaseAdapter {

    private List<item>list;
    private Context context;


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        class temp{
            ImageView img;
            TextView tit;
            TextView content;
        }
        if(convertView==null){
            convertView=View.inflate(context,R.layout.item,null);
            temp temp = new temp();
            temp.img = convertView.findViewById(R.id.img);
            temp.tit = convertView.findViewById(R.id.tit);
            temp.content = convertView.findViewById(R.id.content);
            convertView.setTag(temp);
        }

        temp tag = (temp) convertView.getTag();
        tag.img.setImageResource(list.get(position).getImg());
        tag.content.setText(list.get(position).getContent());
        tag.tit.setText(list.get(position).getTit());
        return convertView;
    }


    public void add(item item){
        if(item!=null){
            list.add(item);
            notifyDataSetChanged();
        }
    }
}
