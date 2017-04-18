package com.mobi.seedword.loginactivity;


import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class baseFragment extends Fragment {
    protected ViewGroup mainLayout;
    protected WebView web ;
    protected LinearLayout linner;
    protected QRCodeReaderView qrCodeReaderView;
    protected String baseUrl = "http://www.app.url.tw/app/activity.asp";
    protected int ScreenWidth = 0;
    protected int ScreenHeight = 0;
    void startCamera(){}

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
}
