package com.mobi.seedword.loginactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;



public class ActivityFragment extends baseFragment implements  ActivityCompat.OnRequestPermissionsResultCallback,QRCodeReaderView.OnQRCodeReadListener{


    public ActivityFragment() {
        baseUrl = "";
    }
    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_activity, container, false);
        mainLayout = (ViewGroup)v.findViewById(R.id.main_layout);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }
        return v;
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override public void onClick(View view) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else {
            Snackbar.make(mainLayout, "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (qrCodeReaderView != null) {
            qrCodeReaderView.surfaceDestroyed(null);
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        linner.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
        qrCodeReaderView.stopCamera();
        web.loadUrl(baseUrl + "?TKID=" + text);
    }


    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    public void initQRCodeReaderView() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight = size.y;

        View content = getActivity().getLayoutInflater().inflate(R.layout.content_decoder, mainLayout, true);

        web = (WebView)content.findViewById(R.id.webview);
        web.setWebViewClient(mWebViewClient);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            // AppRTC requires third party cookies to work
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(web, true);
        }
        web.loadUrl(this.baseUrl);

        linner = (LinearLayout)content.findViewById(R.id.linner);

        qrCodeReaderView = (QRCodeReaderView) content.findViewById(R.id.qrdecoderview);

        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();

        qrCodeReaderView.setTorchEnabled(false);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.startCamera();

        linner.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,0));

    }

    public void startCamera()
    {
        linner.setLayoutParams(new RelativeLayout.LayoutParams(ScreenWidth,ScreenHeight));
        qrCodeReaderView.startCamera();
    }
}
