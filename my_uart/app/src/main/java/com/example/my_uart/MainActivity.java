package com.example.my_uart;

import static cn.wch.uartlib.WCHUARTManager.getInstance;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import cn.wch.uartlib.WCHUARTManager;
import cn.wch.uartlib.callback.IDataCallback;
import cn.wch.uartlib.exception.ChipException;
import cn.wch.uartlib.exception.NoPermissionException;
import cn.wch.uartlib.exception.UartLibException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private WCHUARTManager instance;
    private TextView textView;
    private EditText editText;
    private Button button;
    private UsbDevice usbDevice;
    private static final int num = 5;
    private Spinner spinner;
    private List<UsbDevice> usbDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.read);
        editText = findViewById(R.id.write);
        button = findViewById(R.id.sent);
        button.setVisibility(Button.GONE);
        button.setOnClickListener(this);
        spinner = findViewById(R.id.list);
        spinner.setVisibility(Spinner.VISIBLE);
        instance = getInstance();
        instance.setDebug(true);
        instance.init(this.getApplication());

        findViewById(R.id.search).setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //todo 删除监听 关闭设备 sdk close
    }

    public int getSerialCount(){
        try {
            int serialCount = instance.getSerialCount(usbDevice);
            if(serialCount<0){
                Log.i("hls","获取芯片类型失败");
            }
            return serialCount-1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param num  串口号
     * @param data
     */
    public void write(int num, byte[] data) {
        if(!instance.isConnected(usbDevice)){
            return;
        }
        try {
            int serialCount = getSerialCount();
            if(serialCount<0){
                return;
            }
            instance.asyncWriteData(usbDevice, serialCount, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void read() {
        if(!instance.isConnected(usbDevice)){
            return;
        }
        try {
            instance.registerDataCallback(usbDevice, new IDataCallback() {
                @Override
                public void onData(int i, byte[] bytes, int i1) {
                    if (bytes == null || i1 == 0) {
                        return;
                    }
                    //设置字符编码格式
                    String s = new String(bytes, 0, i1, StandardCharsets.UTF_8);

                    s = "串口" + i + "号,发送的数据:" + s;
                    // 显示数据
                    String finalS = s;
                    runOnUiThread(()->{
                        textView.setText(finalS);
                    });
                    System.out.println(s);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param num 串口号
     */
    public void configureSerialParams(int num) {
        if(!instance.isConnected(usbDevice)){
            return ;
        }
        try {
            int serialCount = getSerialCount();
            if(serialCount<0){
                return;
            }
            if (instance.setSerialParameter(usbDevice, serialCount, 9600, 8, 1, 0, false)) {
                Log.i("hls","配置成功");
            } else {
                Log.i("hls","配置失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void requestPermission(){
        try {
            instance.requestPermission(this,usbDevice);
            Log.i("hls","成功获取权限");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void openUsbDevice() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if(!usbManager.hasPermission(usbDevice)){
            Log.i("hls","没有权限");
            return;
        }
        if (instance.isConnected(usbDevice)) {
            Log.i("hls","设备已打开");
        } else {
            try {
                instance.openDevice(usbDevice);
                Log.i("hls","设备已打开");
            } catch (UartLibException e) {
                throw new RuntimeException(e);
            } catch (NoPermissionException e) {
                throw new RuntimeException(e);
            } catch (ChipException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<UsbDevice> search_device() {
        ArrayList<UsbDevice> usbDevices;
        try {
            usbDevices = instance.enumDevice();
            if (usbDevices.isEmpty()) {
                Toast.makeText(this, "开启设备失败", Toast.LENGTH_SHORT).show();
                return null;
            }
            return usbDevices;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        usbDevice = usbDevices.get(position);
        requestPermission();
        openUsbDevice();
        configureSerialParams(num);
        read();
        runOnUiThread(()->{
            button.setVisibility(Button.VISIBLE);
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if (v.getId() == R.id.search) {
            usbDevices = search_device();
            if (usbDevices == null || usbDevices.isEmpty()) {
                return;
            }
            List<String> collect = usbDevices.stream().map(UsbDevice::getDeviceName).collect(Collectors.toList());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_text, collect);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
            spinner.setOnItemSelectedListener(this);
            runOnUiThread(()->{
                b.setVisibility(Button.GONE);
                spinner.setVisibility(Spinner.VISIBLE);
            });

        } else if (v.getId() == R.id.sent) {
            byte[] bytes = editText.getText().toString().trim().getBytes();
            write(num, bytes);
        }
    }
}