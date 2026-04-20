package com.example.wifi;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private Handler handler; // 用于更新UI的Handler

    // 构造方法：传入Handler（绑定主线程）
    public ServerThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            // 监听8888端口（端口范围1024-65535，避免系统占用）
            serverSocket = new ServerSocket(8888);
            isRunning = true;
            sendLogToUI("服务器启动成功，端口：8888");

            // 循环等待客户端连接（阻塞操作，需在子线程）
            while (isRunning) {
                Socket clientSocket = serverSocket.accept(); // 阻塞，直到有客户端连接
                sendLogToUI("客户端已连接：" + clientSocket.getInetAddress().getHostAddress());

                // 处理客户端消息（单独开线程，避免阻塞主线程监听）
                new ClientHandlerThread(clientSocket, handler).start();
            }

        } catch (IOException e) {
            if (!isRunning) {
                sendLogToUI("服务器已停止");
            } else {
                sendLogToUI("服务器异常：" + e.getMessage());
            }
        }
    }

    // 停止服务器
    public void stopServer() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close(); // 关闭ServerSocket，会触发accept()抛出异常，退出循环
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 发送日志到UI线程
    private void sendLogToUI(String msg) {
        Message message = handler.obtainMessage(0, msg);
        handler.sendMessage(message);
    }

    // 处理单个客户端的线程（避免阻塞服务器监听）
    private static class ClientHandlerThread extends Thread {
        private Socket clientSocket;
        private Handler handler;

        public ClientHandlerThread(Socket socket, Handler handler) {
            this.clientSocket = socket;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                // 获取输入流（客户端发的消息）
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream(), "UTF-8")
                );
                // 获取输出流（向客户端回复）
                BufferedWriter output = new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8")
                );

                // 读取客户端消息（阻塞，直到收到消息）
                String clientMsg = input.readLine();
                if (clientMsg != null) {
                    sendLogToUI("收到消息：" + clientMsg);

                    // 向客户端回复消息
                    String reply = "服务器已收到：" + clientMsg;
                    output.write(reply + "\n"); // 换行符作为消息结束标记
                    output.flush();
                    sendLogToUI("已回复：" + reply);
                }

                // 关闭连接（短连接，一次通讯后断开）
                input.close();
                output.close();
                clientSocket.close();
                sendLogToUI("客户端连接已关闭");

            } catch (IOException e) {
                sendLogToUI("客户端处理异常：" + e.getMessage());
            }
        }

        private void sendLogToUI(String msg) {
            Message message = handler.obtainMessage(0, msg);
            handler.sendMessage(message);
        }
    }
}