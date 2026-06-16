package com.example.android_broadcast_send;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        // 接收带 name 参数的广播
        String name = intent.getStringExtra("name");
        if (name != null) {
            Toast.makeText(context, "广播来了：" + name, Toast.LENGTH_SHORT).show();
        }

        // 使用 switch 语句（消除警告）
        switch (action) {
            case "abc1":
                Toast.makeText(context, "广播来了1：", Toast.LENGTH_SHORT).show();
                break;
            case "abc2":
                Toast.makeText(context, "广播来了2：", Toast.LENGTH_SHORT).show();
                break;
            case "abc3":
                // 通知栏显示（需要 POST_NOTIFICATIONS 权限）
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            "channel_id_1",
                            "广播通知渠道",
                            NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("接收广播触发的通知");
                    channel.enableLights(true);
                    channel.enableVibration(true);
                    channel.setLightColor(Color.rgb(99, 99, 99));
                    manager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_1")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("没有网络")
                        .setContentText("网络掉线了")
                        .setAutoCancel(true);
                manager.notify(0x12, builder.build());
                break;
            default:
                break;
        }
    }
}