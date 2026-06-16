package com.example.bilibili;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {  // 类名改为大写（可选）

    private ListView listView;
    private WebView webView;
    private TextView titleText;
    private ProgressBar progressBar;

    private String[] upNames = {
            "小约翰", "燕三嘤嘤嘤", "麻薯波比呀", "精罗伯爵", "我真没想重生啊"
    };

    private String[] bvids = {
            "BV1kHLU6jE2Q",
            "BV1Dw4m1a7yk",
            "BV1NoVm62EFd",
            "BV1yTVr6dETL",
            "BV1j5cQzZEot"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        webView = findViewById(R.id.web_view);
        titleText = findViewById(R.id.title_text);
        progressBar = findViewById(R.id.progress_bar);

        Button btnBack = findViewById(R.id.btn_back);
        Button btnWeb = findViewById(R.id.btn_web);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, txt.class);
            startActivity(intent);
        });

        btnWeb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.neuq.edu.cn"));
            startActivity(intent);
        });

        listView.setAdapter(new UpAdapter());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            titleText.setText("正在播放：" + upNames[position]);
            loadBilibiliVideo(bvids[position]);
            listView.setItemChecked(position, true);
        });

        setupWebView();          // 配置 WebView（现在包含 Client 了）
        titleText.setText("正在播放：" + upNames[0]);
        loadBilibiliVideo(bvids[0]);
    }

    private void loadBilibiliVideo(String bvid) {
        progressBar.setVisibility(View.VISIBLE);
        String html =
                "<!DOCTYPE html><html><head>" +
                        "<meta name='viewport' content='width=device-width,initial-scale=1.0'>" +
                        "<style>" +
                        "*{margin:0;padding:0}" +
                        "body{background:#000;display:flex;justify-content:center;align-items:center;height:100vh}" +
                        "iframe{width:100%;height:100%;border:none}" +
                        "</style></head><body>" +
                        "<iframe src='https://player.bilibili.com/player.html?bvid=" + bvid +
                        "&page=1&autoplay=1&high_quality=1' allowfullscreen></iframe>" +
                        "</body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    // 修复后的 setupWebView 方法（包含 WebViewClient 和 WebChromeClient）
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // 添加 WebViewClient：处理页面加载完成及错误
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "加载失败：" + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 让 WebView 内部处理链接，而不是跳转浏览器
                view.loadUrl(url);
                return true;
            }
        });

        // 添加 WebChromeClient：监听加载进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }

    // 自定义 Adapter
    class UpAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return upNames.length;
        }

        @Override
        public Object getItem(int p) {
            return upNames[p];
        }

        @Override
        public long getItemId(int p) {
            return p;
        }

        @Override
        public View getView(int p, View v, ViewGroup parent) {
            if (v == null) {
                v = LayoutInflater.from(MainActivity.this)
                        .inflate(android.R.layout.simple_list_item_activated_1, parent, false);
            }
            TextView tv = v.findViewById(android.R.id.text1);
            tv.setText(upNames[p]);
            tv.setTextSize(14);
            tv.setPadding(30, 20, 30, 20);
            return v;
        }
    }
}