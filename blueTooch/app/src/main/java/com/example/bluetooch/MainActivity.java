package com.example.bluetooch;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.companion.CompanionDeviceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_ENABLE_BT = 200;
    public static String NAME = "hls";
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // 注：该UUID为蓝牙串口服务（SPP）的标准UUID，多数蓝牙模块支持
    public static BluetoothAdapter bluetoothAdapter;
    private TextView state;
    private LinearLayout list_layout;
    public static TextView chat;
    public static TextView log;
    private Spinner list_discover;
    private Spinner list_bt;
    private ArrayAdapter<String> adapter_bt;
    private ArrayAdapter<String> adapter_discover;
    private EditText input;
    public static msgHandel msgHandel;
    private List<BluetoothDevice> pair_list;
    private List<BluetoothDevice> no_pair_list;
    static ioThread ioThread;
    private AcceptThread acceptThread;
    private connectThread connectThread;
    private static final int SELECT_DEVICE_REQUEST_CODE = 0;

    private BroadcastReceiver bluetoothReceiver;
    private Button up;
    private Button left;
    private Button right;
    private Button down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        no_pair_list = new ArrayList<>();
        init_ui();
        get_power();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();

        // 获取蓝牙适配器
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            // 设备不支持蓝牙
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }



        //判断设备是否启动
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }





    // 假设已通过扫描获取到目标设备bluetoothDevice
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private void startPairing(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) return;

        // 检查当前配对状态，避免重复发起
        if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            // 发起配对（异步操作，结果通过广播返回）
            boolean isPairingStarted = bluetoothDevice.createBond();
            if (isPairingStarted) {
                log.append("开始配对：" + bluetoothDevice.getName() + "\n");
            } else {
                log.append("配对请求发起失败，请检查权限或设备状态\n");
            }
        } else {
            log.append("设备已配对：" + bluetoothDevice.getName() + "\n");
        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_DEVICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                BluetoothDevice deviceToPair = data.getParcelableExtra(
                        CompanionDeviceManager.EXTRA_DEVICE
                );

                if (deviceToPair != null) {
                    deviceToPair.createBond();
                    // ... Continue interacting with the paired device.
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
    private void discover() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); // 300秒内可发现
        startActivity(discoverableIntent);
        runOnUiThread(() -> {
            Toast.makeText(this, "开始发现", Toast.LENGTH_SHORT).show();
            log.append("开始发现\n");
        });
    }

    private void register() {
        if(bluetoothReceiver!=null){
            return;
        }
        // 广播接收器：监听蓝牙设备发现事件
        bluetoothReceiver = new BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // 发现新设备
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        no_pair_list.add(device);
                        String deviceName = device.getName() != null ? device.getName() : "未知设备";
                        String deviceAddress = device.getAddress(); // 设备MAC地址
                        // 将设备信息添加到列表（如ListView/RecyclerView）
                        String s = "蓝牙设备名称：" + deviceName + "，地址：" + deviceAddress;
                        runOnUiThread(() -> {
                            log.append(s + "\n");
                            adapter_discover.add(deviceName);
                        });
                    }
                } else if (ACTION_DISCOVERY_FINISHED.equals(action)){
                    bluetoothAdapter.startDiscovery();
                }
            }
        };


        // 注册广播接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothReceiver, filter);
        log.append("广播注册成功" + "\n");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private void get_list_bt() {
        register();
        Set<BluetoothDevice> list = bluetoothAdapter.getBondedDevices();
        pair_list = new ArrayList<>();
        pair_list.addAll(list);
        if (list.isEmpty()) {
            runOnUiThread(() -> {
                list_bt.setVisibility(Spinner.GONE);
            });
            return;
        }
        // There are paired devices. Get the name and address of each paired device.
        for (BluetoothDevice device : list) {
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            runOnUiThread(() -> {
                adapter_bt.add(deviceName);
            });
        }
        runOnUiThread(() -> {
            list_bt.setVisibility(Spinner.VISIBLE);
            list_layout.setVisibility(LinearLayout.VISIBLE);
        });


        runOnUiThread(() -> {
            log.append("获取已配对的设备" + "\n");
        });
    }

    private void get_power() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
        };

// 检查并请求权限
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }


    private void init_ui() {
        state = findViewById(R.id.state);
        findViewById(R.id.connect).setOnClickListener(this);
        findViewById(R.id.discover).setOnClickListener(this);

        list_layout = findViewById(R.id.list_layout);
        list_bt = findViewById(R.id.list_bt);
        list_discover = findViewById(R.id.list_discover);
        log = findViewById(R.id.log);
        chat = findViewById(R.id.chat);
        findViewById(R.id.send).setOnClickListener(this);
        input = findViewById(R.id.input);


        adapter_bt = new ArrayAdapter<>(this, R.layout.item);
        adapter_discover = new ArrayAdapter<>(this, R.layout.item);
        list_bt.setAdapter(adapter_bt);
        list_discover.setAdapter(adapter_discover);
        list_bt.setSelection(0);
        list_discover.setSelection(0);
        list_bt.setOnItemSelectedListener(this);
        list_discover.setOnItemSelectedListener(this);


        up = findViewById(R.id.up);
        up.setOnClickListener(this);
        left = findViewById(R.id.left);
        left.setOnClickListener(this);
        right = findViewById(R.id.right);
        right.setOnClickListener(this);
        down = findViewById(R.id.down);
        down.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_SCAN})
    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        if (b.getId() == R.id.connect) {
            adapter_bt.clear();
            adapter_discover.clear();
            get_list_bt();
            // 开始扫描设备（需权限）
            if(bluetoothAdapter.isDiscovering()){
                bluetoothAdapter.cancelDiscovery();
            }else{
                bluetoothAdapter.startDiscovery();
            }
            //注册服务器
            if (acceptThread != null) {
                acceptThread.cancel();
                acceptThread = null;
            }
            acceptThread = new AcceptThread();
            acceptThread.start();
            log.append("完成注册服务器" + "\n");
        } else if (b.getId() == R.id.discover) {
            discover();
        } else if (b.getId() == R.id.send) {
            //todo 发送消息
            byte[] text = (input.getText().toString()+"\r\n").trim().getBytes();
            input.setText("");
            ioThread = AcceptThread.ioThread;
            if (ioThread == null) {
                ioThread = connectThread.ioThread;
            }
            if (ioThread != null) {
                ioThread.write(text);
            }
        }else if(b.getId()== R.id.up){
            byte[] bytes = "f\r\n".getBytes();
            ioThread = AcceptThread.ioThread;
            if (ioThread == null) {
                ioThread = connectThread.ioThread;
            }
            if (ioThread != null) {
                ioThread.write(bytes);
            }
        }else if(b.getId()== R.id.left){
            byte[] bytes = "a\r\n".getBytes();
            ioThread = AcceptThread.ioThread;
            if (ioThread == null) {
                ioThread = connectThread.ioThread;
            }
            if (ioThread != null) {
                ioThread.write(bytes);
            }
        }else if(b.getId()== R.id.right){
            byte[] bytes = "d\r\n".getBytes();
            ioThread = AcceptThread.ioThread;
            if (ioThread == null) {
                ioThread = connectThread.ioThread;
            }
            if (ioThread != null) {
                ioThread.write(bytes);
            }
        }else if(b.getId()== R.id.down){
            byte[] bytes = "s\r\n".getBytes();
            ioThread = AcceptThread.ioThread;
            if (ioThread == null) {
                ioThread = connectThread.ioThread;
            }
            if (ioThread != null) {
                ioThread.write(bytes);
            }
        }
    }

    int temp1 = 0;
    int temp2 = 0;

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int id1 = parent.getId();
        if (parent.getId() == R.id.list_bt) {
            if (temp1 == 0) {
                temp1 = 1;
                return;
            }
            BluetoothDevice bluetoothDevice = pair_list.get(position);
            if (msgHandel != null) {
                msgHandel = null;
            }
            msgHandel = new msgHandel(bluetoothDevice.getName());
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
            connectThread = new connectThread(bluetoothDevice);
            connectThread.start();
        } else if (parent.getId() == R.id.list_discover) {
            if (temp2 == 0) {
                temp2 = 1;
                return;
            }
            BluetoothDevice bluetoothDevice = no_pair_list.get(position);
            startPairing(bluetoothDevice);
            if (msgHandel != null) {
                msgHandel = null;
            }
            msgHandel = new msgHandel(bluetoothDevice.getName());
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
            connectThread connectThread = new connectThread(bluetoothDevice);
            connectThread.start();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
