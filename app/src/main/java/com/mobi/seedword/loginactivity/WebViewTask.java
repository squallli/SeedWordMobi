package com.mobi.seedword.loginactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

public class WebViewTask extends AsyncTask {
    String sessionCookie;
    CookieManager cookieManager;
    WebView webView;
    String url;
    Context context;

    public WebViewTask(Context context, WebView webView, String url) {
        this.webView = webView;
        this.url = url;
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        CookieSyncManager.createInstance(context);
        cookieManager = CookieManager.getInstance();

        sessionCookie = new PersistentConfig(context).getCookieString();
        if (sessionCookie != null) {
            cookieManager.removeSessionCookie();
        }
        super.onPreExecute();
    }

    protected Boolean doInBackground(Void... param) {
        SystemClock.sleep(1000);
        return false;
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void onPostExecute(Boolean result) {
        if (sessionCookie != null) {
            cookieManager.setCookie("example.com", sessionCookie);
            CookieSyncManager.getInstance().sync();
        }
        webView.loadUrl(url);
    }
}