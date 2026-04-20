package com.hls.template;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.hls.MainActivity;

import java.util.Locale;

public class Speech implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    public Speech(Context context){
        tts = new TextToSpeech(context.getApplicationContext(), this);
    }



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 设置TTS语言为中文（中国大陆）
            int result = tts.setLanguage(Locale.CHINESE);
            // 检查语言是否支持
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(MainActivity.TAG,"当前设备不支持中文TTS");
                // 备选方案：切换为英文
                tts.setLanguage(Locale.ENGLISH);
            } else {
                Log.i(MainActivity.TAG,"TTS初始化成功");
            }
            tts.setPitch(3.0f);
            tts.setSpeechRate(1.0f);
        } else {
            // 初始化失败
            Log.e(MainActivity.TAG,"TTS初始化失败");
        }
    }


    /**
     * 播放输入的文本
     */
    public void speakText(String text) {
        if (text.isEmpty()) {
            Log.e(MainActivity.TAG,"请输入要播放的文本");
            return;
        }

        // 核心播放方法
        // 参数1：要播放的文本
        // 参数2：队列模式（QUEUE_FLUSH：清空队列立即播放；QUEUE_ADD：添加到队列末尾）
        // 参数3：播放参数（null为默认）
        // 参数4：播放标识（null为默认）
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

}
