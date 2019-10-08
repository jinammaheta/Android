package com.example.smc1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;

public class WebViewClientimpl extends WebViewClient {

    private Activity activity = null;
    private Context c;
    String dest;
    public WebViewClientimpl(WebViewexample m) {
        activity=m;
        dest=activity.getIntent().getStringExtra("dest");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if(URLUtil.isNetworkUrl(url)) {
            return false;
        }
        else
        {
            System.out.println("in ");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+dest));
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
        }
        return true;
    }

}
