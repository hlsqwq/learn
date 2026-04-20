package com.hls.template;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cn.wch.uartlib.exception.ChipException;
import cn.wch.uartlib.exception.NoPermissionException;
import cn.wch.uartlib.exception.UartLibException;

public abstract class Connect extends AppCompatActivity {
    abstract public List<Object> getDeviceList() throws Exception;
    abstract public void connect(Object o) throws Exception;
    abstract public void write(String data) throws Exception;

    abstract public String get_device_name();
}
