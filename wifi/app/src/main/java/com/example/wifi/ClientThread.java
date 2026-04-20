package com.example.wifi;

import android.os.Handler;
import android.os.Message;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private String serverIp; // 服务器IP
    private int serverPort = 8888; // 服务器端口（与服务器保持一致）
    private String message; // 要发送的消息
    private Handler handler; // 用于更新UI的Handler

    public ClientThread(String serverIp, String message, Handler handler) {
        this.serverIp = serverIp;
        this.message = message;
        this.handler = handler;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            // 连接服务器（IP+端口）
            socket = new Socket(serverIp, serverPort);
            sendLogToUI("已连接到服务器：" + serverIp);

            // 获取输出流（发送消息给服务器）
            BufferedWriter output = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8")
            );
            // 获取输入流（接收服务器回复）
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8")
            );

            // 发送消息（换行符作为结束标记）
            output.write(message + "\n");
            output.flush();
            sendLogToUI("已发送消息：" + message);

            // 接收服务器回复
            String serverReply = input.readLine();
            if (serverReply != null) {
                sendLogToUI("收到服务器回复：" + serverReply);
            }

            // 关闭连接
            output.close();
            input.close();
            socket.close();

        } catch (IOException e) {
            sendLogToUI("客户端异常：" + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 发送日志到UI线程
    private void sendLogToUI(String msg) {
        Message message = handler.obtainMessage(0, msg);
        handler.sendMessage(message);
    }
}