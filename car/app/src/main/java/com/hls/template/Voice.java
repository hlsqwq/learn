package com.hls.template;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.hls.MainActivity;
import com.hls.R;
import com.hls.configuration.PackageConfig;

import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechService;
import org.vosk.android.StorageService;

import java.io.IOException;
import java.util.Map;

public class Voice extends Activity implements View.OnClickListener, RecognitionListener {


    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private Model model;
    private Context context;
    private SpeechService speechService;
    private MediaPlayer mediaPlayer;


    public Voice(Activity activity) {
        this.context = activity.getApplicationContext();
//        MainActivity.voice.setOnClickListener(view->recognizeMicrophone());
        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            initModel();
        }
    }

    private void initModel() {
        StorageService.unpack(context, "vosk-model-small-cn-0.22", "model",
                (model) -> {
                    this.model = model;
                },
                (exception) -> Log.e(MainActivity.TAG, "Failed to unpack the model" + exception.getMessage()));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                initModel();
            } else {
                finish();
            }
        }
    }

    private void recognizeMicrophone() {
        if (speechService != null) {
            stopMusic();
//            MainActivity.voice.setText("开始识别");
            speechService.stop();
            speechService = null;
        } else {
//            MainActivity.voice.setText("停止识别");
            try {
                Recognizer rec = new Recognizer(model, 16000.0f);
                speechService = new SpeechService(rec, 16000.0f);
                speechService.startListening(this);
            } catch (IOException e) {
                Log.e(MainActivity.TAG, e.getMessage());

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (speechService != null) {
            speechService.stop();
            speechService.shutdown();
        }
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onPartialResult(String hypothesis) {
    }

    @Override
    public void onResult(String hypothesis) {
        Map map = JSON.parseObject(hypothesis, Map.class);
        String string = String.valueOf(map.get("text"));
        Log.i(MainActivity.TAG, string);
        if (string.contains("你好")) {
//            MainActivity.speech.speakText("有什么吩咐");
        } else if (string.contains("名字")) {
//            MainActivity.speech.speakText("我叫查小狗");
        } else if (string.contains("音乐")) {
            playMusic();
        } else if (string.contains("暂停")) {
            pauseMusic();
        }


        if (MainActivity.settleDialog == null) {
            return;
        }
        Connect connect = MainActivity.settleDialog.get_connect();
        if (connect == null) {
            return;
        }
        try {
            if (string.contains("前进")) {
                connect.write(PackageConfig.FORWARD);
            } else if (string.contains("左")) {
                connect.write(PackageConfig.LEFT);
            } else if (string.contains("右")) {
                connect.write(PackageConfig.RIGHT);
            } else if (string.contains("后")) {
                connect.write(PackageConfig.BACK);
            } else if (string.contains("停止")) {
                connect.write(PackageConfig.STOP);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    private void playMusic() {
        // 先释放旧的播放器，避免重复播放
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        // 初始化MediaPlayer（res/raw资源直接create，无需手动prepare）
        mediaPlayer = MediaPlayer.create(context, R.raw.p1);
//        mediaPlayer.setLooping(true); // 循环播放（不需要就注释掉）
        mediaPlayer.start(); // 开始播放
    }

    // 暂停音乐
    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // 停止音乐
    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // 停止播放
            mediaPlayer.release(); // 释放资源
            mediaPlayer = null; // 置空，避免后续调用报错
        }
    }


    @Override
    public void onFinalResult(String hypothesis) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onTimeout() {

    }
}
