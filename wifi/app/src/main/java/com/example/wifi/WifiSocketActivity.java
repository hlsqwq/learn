package com.example.wifi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WifiSocketActivity extends AppCompatActivity {
    private TextView tvLocalIp;
    private TextView tvLog;
    private EditText etServerIp;
    private EditText etMessage;
    private Button btnStartServer;
    private Button btnStopServer;
    private Button btnSend;

    private ServerThread serverThread;
    private Handler uiHandler; // 用于接收子线程消息并更新UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_socket);

        // 初始化控件
        initViews();

        // 初始化UI Handler（绑定主线程）
        uiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String log = (String) msg.obj;
                tvLog.append(log + "\n"); // 更新日志到UI
            }
        };

        // 显示本机IP（用于其他设备连接）
        String localIp = NetworkUtils.getLocalIpAddress(this);
        tvLocalIp.setText("本机IP：" + localIp);

        // 启动服务器按钮
        btnStartServer.setOnClickListener(v -> {
            if (serverThread == null || !serverThread.isAlive()) {
                serverThread = new ServerThread(uiHandler);
                serverThread.start(); // 启动服务器线程
            } else {
                tvLog.append("服务器已在运行中\n");
            }
        });

        // 停止服务器按钮
        btnStopServer.setOnClickListener(v -> {
            if (serverThread != null && serverThread.isAlive()) {
                serverThread.stopServer();
                serverThread = null;
            } else {
                tvLog.append("服务器未运行\n");
            }
        });

        // 发送消息按钮（客户端功能）
        btnSend.setOnClickListener(v -> {
            String ip = etServerIp.getText().toString().trim();
            String msg = etMessage.getText().toString().trim();
            if (ip.isEmpty() || msg.isEmpty()) {
                tvLog.append("请输入服务器IP和消息\n");
                return;
            }
            // 启动客户端线程发送消息
            new ClientThread(ip, msg, uiHandler).start();
        });
    }

    private void initViews() {
        tvLocalIp = findViewById(R.id.tv_local_ip);
        tvLog = findViewById(R.id.tv_log);
        etServerIp = findViewById(R.id.et_server_ip);
        etMessage = findViewById(R.id.et_message);
        btnStartServer = findViewById(R.id.btn_start_server);
        btnStopServer = findViewById(R.id.btn_stop_server);
        btnSend = findViewById(R.id.btn_send);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁时停止服务器
        if (serverThread != null) {
            serverThread.stopServer();
        }
    }
}