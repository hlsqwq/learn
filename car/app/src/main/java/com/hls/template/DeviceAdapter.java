package com.hls.template;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresPermission;

import com.hls.R;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<Object>list;


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

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private String get_info(Object o){
        if(o instanceof BluetoothDevice){
            BluetoothDevice d= (BluetoothDevice) o;
            return String.format("name:%s,address:%s"
                    ,d.getName(),d.getAddress());
        }else{
            UsbDevice d= (UsbDevice) o;
            return  String.format("id:%s,address:%s"
                    ,d.getDeviceId(),d.getDeviceName());
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=LayoutInflater.from(context).inflate(resource, parent, false);
            convertView.setTag(convertView.findViewById(R.id.text));
        }

        TextView textView = (TextView) convertView.getTag();
        String info = get_info(list.get(position));
        textView.setText(info);
        return convertView;
    }
}
