package com.mobi.seedword.loginactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.mobi.seedword.loginactivity.R.id.webview;

public class ResetActivity extends AppCompatActivity {
    private WebView web ;
    CookieManager cookieManager;
    Button btnReset;
    String SNID = "";
    String EMAIL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        CookieSyncManager.createInstance(this);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        Intent intent = this.getIntent();
        EMAIL = intent.getStringExtra("EMAIL");
        SNID = Util.getIMEI(this);

        web = (WebView)findViewById(webview);
        web.setWebViewClient(new WebViewClient());


        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // AppRTC requires third party cookies to work
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(web, true);
        }

        web.loadUrl("http://app.seedword.mobi/app/askreset.asp");

        btnReset = (Button)findViewById(R.id.btnReset);


        btnReset.setTag("reset");
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog pDialog = new ProgressDialog(ResetActivity.this);
                pDialog.setMessage("Waiting");
                pDialog.show();

                StringRequest stringRequest;
                if(btnReset.getTag().toString().equals("reset"))
                {
                    String SNTM = Util.getDateTimeString("yyyyMMddHHmmss");
                    String url = "http://app.seedword.mobi/app/VFemail.asp";
                    String postData = "EMAIL=" + EMAIL + "&SNID=" + SNID + "&SNTM="  + SNTM + "&VF=" + Util.getVF(ResetActivity.this,SNTM,SNID,EMAIL);
                    web.postUrl(url,postData.getBytes());

                    btnReset.setText("Login again");
                    btnReset.setTag("loginagain");
                    pDialog.hide();
                }
                else
                {
                    stringRequest = new StringRequest(Request.Method.POST, "http://app.seedword.mobi/app/login.asp", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Type resultType = new TypeToken<resultObject>() {}.getType();
                            resultObject result = gson.fromJson(response,resultType);

                            if(result.getResult().equals("1"))
                            {
                                Util.addSetting(ResetActivity.this,"EMAIL",EMAIL);
                                Util.addSetting(ResetActivity.this,"SNID",SNID);
                                Intent intent = new Intent();
                                intent.setClass(ResetActivity.this , eventActivity.class);
                                startActivity(intent);
                            }
                            pDialog.hide();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.hide();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            String SNTM = Util.getDateTimeString("yyyyMMddHHmmss");
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("EMAIL", EMAIL);
                            map.put("SNID", SNID);
                            map.put("SNTM", SNTM);
                            map.put("VF", Util.getVF(ResetActivity.this,SNTM,SNID,EMAIL));
                            return map;
                        }
                    };

                    clsGlobal.mQueue.add(stringRequest);
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
