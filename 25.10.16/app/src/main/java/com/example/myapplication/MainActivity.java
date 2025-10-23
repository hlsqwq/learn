package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn2;
    private Button btn1;
    private TextView text;
    private com.example.myapplication.databinding.ActivityMainBinding inflate;

    private String first = "";
    private String second = "";
    private String opt = "";
    private String ans = "";
    private String show_text = "";
    private DecimalFormat df;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        inflate = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        df = new DecimalFormat("#.00");

        for (int i = 0; i < inflate.list.getChildCount(); i++) {
            Button b = (Button) inflate.list.getChildAt(i);
            b.setOnClickListener(this);
        }
    }


    private void show() {
        // todo history
        inflate.text.setText(show_text);
    }

    private void mul() {
        ans = df.format(Double.parseDouble(first) * Double.parseDouble(second));
    }

    private void plus() {
        ans = df.format(Double.parseDouble(first) + Double.parseDouble(second));
    }

    private void div() {
        ans = df.format(Double.parseDouble(first) / Double.parseDouble(second));
    }

    private void sub() {
        ans = df.format(Double.parseDouble(first) - Double.parseDouble(second));
    }

    private void root() {
        ans = df.format(Math.sqrt(Double.parseDouble(first)));
    }

    private void reverse() {
        ans = df.format(1.0 / Double.parseDouble(first));
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        String s = b.getText().toString();
        if (b.getId() == R.id.c) {
            first = "";
            second = "";
            ans = "";
            opt = "";
            show_text = "";
        } else if (b.getId() == R.id.ce) {
            if(show_text.isEmpty()){
                return;
            }
            show_text = show_text.substring(0, show_text.length() - 1);
        } else if (b.getId() == R.id.eq) {
            switch (opt) {
                case "+":
                    plus();
                    break;
                case "-":
                    sub();
                    break;
                case "*":
                    mul();
                    break;
                case "/":
                    div();
                    break;
                case "✓":
                    root();
                    break;
                case "1/x":
                    reverse();
                    break;
            }
            String[] split = show_text.split("=");
            show_text = split[split.length - 1];
            show_text = show_text + "=" + ans;
            first = ans;
            ans = "";
            opt = "";
            second = "";
        } else if (b.getId() == R.id.mul || b.getId() == R.id.sub
                || b.getId() == R.id.plus || b.getId() == R.id.div
                || b.getId() == R.id.root || b.getId() == R.id.reverse) {
            opt = s;
            show_text += s;
        } else {
            if (opt.isEmpty() && show_text.contains("=")) {
                first = "";
                show_text = "";
            }
            if (Objects.equals(opt, "")) {
                first += s;
                if (show_text.equals("0") && !s.equals(".")) {
                    show_text = s;
                } else {
                    show_text += s;
                }
            } else {
                second += s;
                String substring = show_text.substring(show_text.indexOf(opt) + 1);
                if (substring.equals("0") && !s.equals(".")) {
                    char[] charArray = show_text.toCharArray();
                    charArray[charArray.length - 1] = s.charAt(0);
                    show_text = Arrays.toString(charArray).replace("[", "")
                            .replace("]", "")
                            .replace(",", "");
                } else {
                    show_text += s;
                }
            }


        }
        show();
    }

}