package com.hls.template;

import android.app.Application;
import android.hardware.usb.UsbDevice;
import android.util.Log;

import com.hls.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.wch.uartlib.WCHUARTManager;
import cn.wch.uartlib.callback.IDataCallback;


public class UartConnect extends Connect {


    private final WCHUARTManager instance;
    private final Application application;
    private final CallBack callBack;

    private UsbDevice device;

    public UartConnect(Application application, CallBack callBack) {

        instance = WCHUARTManager.getInstance();
        instance.init(application);
        this.application = application;
        this.callBack = callBack;
    }

    @Override
    public List<Object> getDeviceList() throws Exception {
        ArrayList<UsbDevice> usbDevices = instance.enumDevice();
        Log.i(MainActivity.TAG, String.format("usb获取列表成功:%s", usbDevices));
        return new ArrayList<>(usbDevices);
    }

    @Override
    public void connect(Object o) throws Exception {
        UsbDevice device = (UsbDevice) o;
        if (device == null) {
            return;
        }

        instance.requestPermission(application, device);

        instance.openDevice(device);

        if (!instance.setSerialParameter(device, instance.getSerialCount(device) - 1, 115200, 8, 1, 0, false)) {
            Log.e(MainActivity.TAG, "设置请求参数失败");
        }

        read(device);
        this.device = device;
        Log.i(MainActivity.TAG, String.format("usb:%s,连接成功", device.getDeviceName()));
    }


    /**
     * Register read in callback
     *
     * @param device
     * @throws Exception
     */
    private void read(UsbDevice device) throws Exception {
        if (!instance.isConnected(device)) {
            Log.e(MainActivity.TAG, "设备连接中断");
            return;
        }

        instance.registerDataCallback(device, new IDataCallback() {
            @Override
            public void onData(int i, byte[] bytes, int i1) {
                String s = new String(bytes, 0, i1);
                callBack.read(i, s);
                Log.i(MainActivity.TAG, String.format("usb,读取成功：%s", s));
            }
        });
    }

    /**
     * Obtain the serial port number
     *
     * @return
     * @throws Exception
     */
    private int get_sc() throws Exception {
        int i = instance.getSerialCount(device) - 1;
        if (i < 0) {
            throw new RuntimeException("获取串口数量失败");
        }
        return i;
    }


    /**
     * Write data
     *
     * @param data
     * @throws Exception
     */
    @Override
    public void write(String data) throws Exception {
        if (!instance.isConnected(device)) {
            Log.e(MainActivity.TAG, "设备连接中断");
            return;
        }
        instance.asyncWriteData(device, get_sc(), data.getBytes());
        Log.i(MainActivity.TAG, String.format("usb:%s,data:%s,发送成功", device.getDeviceName(), data));
    }

    @Override
    public String get_device_name() {
        return device.getDeviceName();
    }


}
