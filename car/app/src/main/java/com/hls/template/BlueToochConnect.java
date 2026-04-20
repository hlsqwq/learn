package com.hls.template;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
import static android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.hls.MainActivity;
import com.hls.ui.viewModel.ActivityViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlueToochConnect extends Connect {

    private static final int REQUEST_ENABLE_BT = 200;
    private static final int REQUEST_DISCOVER_BT = 201;
    private final Application application;
    public final CallBack callBack;
    private final BluetoothAdapter adapter;
    private Activity activity;
    private BlueToochBroadcast receiver;
    private ConnectThread connectThread;
    private BluetoothDevice device;

    private boolean is_pair=false;

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN})
    public BlueToochConnect(Activity activity, Application application,CallBack callBack){
        this.connectThread=null;
        this.activity=activity;
        this.application=application;
        this.callBack=callBack;
        // 获取蓝牙适配器
        BluetoothManager bluetoothManager = application.getSystemService(BluetoothManager.class);
        adapter = bluetoothManager.getAdapter();
        if (adapter == null) {
            // 设备不支持蓝牙
            Log.e(MainActivity.TAG,"设备不支持蓝牙");
            return;
        }

        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
        }

        receiver=new BlueToochBroadcast(adapter);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(ACTION_DISCOVERY_FINISHED);
        filter.addAction(ACTION_BOND_STATE_CHANGED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        activity.registerReceiver(receiver, filter);
        adapter.startDiscovery();
        Log.i(MainActivity.TAG,"开启蓝牙扫描");
    }


    /**
     * 获取已经配对的设备列表
     * @return
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public Set<BluetoothDevice> get_bond_device(){
        if (adapter.isEnabled()){
            Log.i(MainActivity.TAG,"获取配对设备");
            return adapter.getBondedDevices();
        }
        return null;
    }

    @Override
    public List<Object> getDeviceList() {
        return new ArrayList<>(receiver.get_list());
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN})
    private void startPairing(BluetoothDevice bluetoothDevice) throws InterruptedException {
        if (bluetoothDevice == null) return;
        // 检查当前配对状态，避免重复发起
        if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            // 发起配对（异步操作，结果通过广播返回）
            boolean isPairingStarted = bluetoothDevice.createBond();
            if (isPairingStarted) {
                Log.i(MainActivity.TAG,"开始配对：" + bluetoothDevice.getName() + "\n");
                Thread.sleep(5000);
            } else {
                Log.e(MainActivity.TAG,"配对请求发起失败，请检查权限或设备状态\n");
            }
        } else {
            is_pair=true;
            Log.i(MainActivity.TAG,"设备已配对：" + bluetoothDevice.getName() + "\n");
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN})
    @Override
    public void connect(Object o) throws IOException, InterruptedException {
        device = (BluetoothDevice) o;
        startPairing(device);
        if(!is_pair){
            return;
        }
        adapter.cancelDiscovery();
        connectThread = new ConnectThread(device,callBack);
        connectThread.start();
    }



    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_ENABLE_BT){
            if (resultCode==RESULT_OK){
                if(adapter.isEnabled()){
                    Toast.makeText(application.getApplicationContext()
                            ,"成功打开蓝牙",Toast.LENGTH_SHORT).show();
                    Log.i(MainActivity.TAG,"打开蓝牙成功");
                }else{
                    Toast.makeText(application.getApplicationContext()
                            ,"系统错误",Toast.LENGTH_SHORT).show();
                    Log.i(MainActivity.TAG,"蓝牙开启错误");
                }
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(application.getApplicationContext()
                        ,"你拒绝了蓝牙请求",Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.TAG,"你拒绝了蓝牙请求");
            }
        }else if (requestCode==REQUEST_DISCOVER_BT){
            if (resultCode==RESULT_OK){
                Toast.makeText(application.getApplicationContext()
                        ,"已开启发现",Toast.LENGTH_SHORT).show();
                Log.i(MainActivity.TAG,"已开启发现");
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(application.getApplicationContext()
                        ,"你拒绝了开启发现",Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.TAG,"你拒绝了开启发现");
            }
        }
    }


    @Override
    public void write(String data) throws IOException {
        if (connectThread==null){
            return;
        }
        ConnectThread.write(data);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @Override
    public String get_device_name() {
        return device.getName();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.activity.unregisterReceiver(receiver);
    }
}
