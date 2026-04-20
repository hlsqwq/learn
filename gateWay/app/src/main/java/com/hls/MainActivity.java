package com.hls;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hls.configuration.Mqttconfig;
import com.hls.po.Sensor;
import com.hls.template.MqttTemplate;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private MqttTemplate mqttTemplate;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String deviceName="JDY-33-BLE";
    private OutputStream outputStream;
    private InputStream inputStream;


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String server="tcp://10.128.254.45:1883";
        String username="hls";
        String passwd="1234";
        String clientId="android";

        Mqttconfig mqttconfig = new Mqttconfig(server, clientId, "", "");
        try {
            mqttTemplate = new MqttTemplate(mqttconfig);
        } catch (MqttException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter adapter = bluetoothManager.getAdapter();

        for (BluetoothDevice device : adapter.getBondedDevices()) {
            Log.i("hls",device.getName());
            if (device.getName().equals(deviceName)) {
                try {
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    socket.connect();
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }




        while (true){

            byte []buf=new byte[1024];
            JSONObject data=null;
            try {
                int read = inputStream.read(buf);
                String str=new String(buf,0,read);
                data = JSON.parseObject(str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(data==null){
                continue;
            }

            Sensor sensor = new Sensor();
            sensor.setHumidity(Double.parseDouble(data.get("humi").toString()));
            sensor.setTemperature(Double.parseDouble(data.get("temp").toString()));
            sensor.setIllumination(get_num(100,1000));
            Calendar instance = Calendar.getInstance();
            @SuppressLint("DefaultLocale") String format = String.format("%04d-%02d-%02d %02d:%02d:%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1
                    , instance.get(Calendar.DAY_OF_MONTH), instance.get(Calendar.HOUR_OF_DAY)
                    , instance.get(Calendar.MINUTE), instance.get(Calendar.SECOND));
            sensor.setCreateTime(format);
            byte[] bytes= JSON.toJSONBytes(sensor);
            try {
                mqttTemplate.send("demo",bytes);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }

    }




    public double get_num(double min,double max){
        return (Math.random()*(max-min))+min;
    }

}