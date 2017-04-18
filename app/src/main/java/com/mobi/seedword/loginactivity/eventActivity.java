package com.mobi.seedword.loginactivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import static com.mobi.seedword.loginactivity.R.id.webview;

public class eventActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, QRCodeReaderView.OnQRCodeReadListener {
    private Button btnScan ;
    private QRCodeReaderView qrCodeReaderView;
    private WebView web ;
    private LinearLayout linner;
    private int  ScreenWidth;
    private int  ScreenHeight;
    CookieManager cookieManager;

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.actionbar,null);
        btnScan = (Button)v.findViewById(R.id.btnScan);

        getSupportActionBar().setCustomView(v);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.my_color));

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linner.getHeight() > 0)
                {
                    linner.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,0));
                }
                else
                {
                    linner.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,ScreenHeight));
                }


            }
        });

        CookieSyncManager.createInstance(this);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }


    }

    @Override protected void onResume() {
        super.onResume();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
    }

    @Override protected void onPause() {
        super.onPause();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.surfaceDestroyed(null);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            initQRCodeReaderView();
        } else {

        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

        } else {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }


    private void initQRCodeReaderView() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight = size.y;

        web = (WebView)findViewById(webview);
        web.setWebViewClient(new WebViewClient());


        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // AppRTC requires third party cookies to work
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(web, true);
        }



        String EMail = Util.getSetting(eventActivity.this,"EMAIL");
        String SNID = Util.getSetting(eventActivity.this,"SNID");
        String SNTM = Util.getDateTimeString("yyyyMMddHHmmss");
        String VF = Util.getVF(eventActivity.this,SNTM);

        web.loadUrl("http://app.seedword.mobi/app/welcome.asp?EMAIL=" + EMail + "&SNID=" + SNID + "&VF=" + VF + "&SNTM=" + SNTM);

        linner = (LinearLayout)findViewById(R.id.linner);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);

        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(eventActivity.this);
        qrCodeReaderView.setBackCamera();

        qrCodeReaderView.setTorchEnabled(false);
        qrCodeReaderView.setQRDecodingEnabled(true);
        linner.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,0));

        qrCodeReaderView.stopCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent intent = new Intent();
        intent.putExtra("ActivityUrl", text);
        intent.setClass(eventActivity.this , MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
