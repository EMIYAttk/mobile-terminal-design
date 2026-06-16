package com.example.bilibili;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class txt extends AppCompatActivity {

    TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt); // 关键！

        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(getTxtFromAssets("text.txt"));
    }

    private String getTxtFromAssets(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
        } catch (Exception e) {
            return "读取失败：" + e.getMessage();
        }
        return sb.toString();
    }
}