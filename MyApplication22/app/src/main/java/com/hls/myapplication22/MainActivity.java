package com.hls.myapplication22;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Arrays;

import ai.onnxruntime.*;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private OrtEnvironment env;
    private OrtSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText = findViewById(R.id.result_text);

        // 初始化 ONNX Runtime
        try {
            initOnnxRuntime();
            // 执行推理
            float[] outputScores = runInference();
            // 显示结果
            resultText.setText("Inference Output: " + Arrays.toString(outputScores));
        } catch (Exception e) {
            e.printStackTrace();
            resultText.setText("Error: " + e.getMessage());
        }
    }

    private void initOnnxRuntime() throws OrtException {
        // 创建 ORT 环境
        env = OrtEnvironment.getEnvironment();
        // 构建 SessionOptions
        OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();
        // 可选: 使用 CPU 或 NNAPI 等加速，如果需要，可启用如下:
        // sessionOptions.addNnapi();

        // 从assets加载模型
        try {
            InputStream modelStream = getAssets().open("model.onnx");
            byte[] modelBytes = new byte[modelStream.available()];
            modelStream.read(modelBytes);
            session = env.createSession(modelBytes, sessionOptions);
        } catch (IOException ioException) {
            throw new RuntimeException("Failed to load model from assets", ioException);
        }
    }

    private float[] runInference() throws OrtException {
        // 准备输入张量
        // 假设输入大小 [1, 3, 224, 224]，数据类型 float32
        float[] inputData = new float[1 * 3 * 224 * 224];
        // 这里示例: 全部填充随机值 or 0.5f
        // 实际中可来自图像预处理
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = 0.5f;
        }

        // ONNX Runtime需要将Java数组包装成OnnxTensor
        long[] inputShape = new long[]{1, 3, 224, 224};
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, inputData, inputShape);

        // 准备输入名 (与导出时的 input_names 对应)
        String inputName = session.getInputNames().iterator().next();

        // 运行会话
        OrtSession.Result result = session.run(Collections.singletonMap(inputName, inputTensor));

        // 假设输出名为 "output"，或者取 getOutputNames() 的第一个
        String outputName = session.getOutputNames().iterator().next();
        float[][] outputRaw = (float[][]) result.get(0).getValue();

        // 此时 outputRaw 可能为 [1, num_classes]，示例中只返回数组
        float[] outputScores = outputRaw[0];

        // 释放资源
        inputTensor.close();
        result.close();
        return outputScores;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭 Session 和 Env，避免内存泄漏
        if (session != null) {
            try {
                session.close();
            } catch (OrtException e) {
                e.printStackTrace();
            }
        }
        if (env != null) {
            try {
                env.close();
            } catch (OrtException e) {
                e.printStackTrace();
            }
        }
    }
}