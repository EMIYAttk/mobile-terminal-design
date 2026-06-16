package com.example.wechat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SmsActivity extends AppCompatActivity {

    private EditText etPhone, etMsg;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        etPhone = findViewById(R.id.et_phone);
        etMsg = findViewById(R.id.et_msg);
        btnSend = findViewById(R.id.btn_send);

        // 请求权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }

        btnSend.setOnClickListener(v -> sendSms());
    }

    private void sendSms() {
        String phone = etPhone.getText().toString().trim();
        String message = etMsg.getText().toString().trim();

        if (phone.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "请填写完整电话号码和短信内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有发送短信权限，请手动开启", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            // 发送短信（不使用广播接收器，避免兼容性问题）
            if (message.length() > 70) {
                ArrayList<String> parts = smsManager.divideMessage(message);
                for (String part : parts) {
                    smsManager.sendTextMessage(phone, null, part, null, null);
                }
            } else {
                smsManager.sendTextMessage(phone, null, message, null, null);
            }
            // 直接显示成功（用户能看到）
            Toast.makeText(this, "短信已发送", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "发送失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "短信权限已获取", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "短信权限被拒绝，无法发送短信", Toast.LENGTH_SHORT).show();
            }
        }
    }
}