package com.example.smc1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewexample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView =(WebView)findViewById(R.id.web1);

        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClientimpl(WebViewexample.this));
        webSettings.setJavaScriptEnabled(true);
        SharedPreferences sd=getSharedPreferences("session",0);
        SharedPreferences.Editor ed=sd.edit();
        ed.putString("dest",getIntent().getStringExtra("dest"));
        ed.commit();
        webView.loadUrl("http://maps.google.com/maps?saddr="+getSharedPreferences("session",0).getString("src",null)+"&daddr="+getIntent().getStringExtra("dest"));
    }
}
