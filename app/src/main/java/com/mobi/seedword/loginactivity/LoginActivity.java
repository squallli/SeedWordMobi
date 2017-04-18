package com.mobi.seedword.loginactivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.solver.SolverVariable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback {

    //String IMEI = "862966029858643";
    EditText txtEmail;
    EditText txtName;
    EditText txtCompany;
    Button btnSubmit;


    String SNID = "";
    String SNTM = "";


    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtCompany = (EditText)findViewById(R.id.txtCompany);
        txtName =(EditText)findViewById(R.id.txtName);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        clsGlobal.mQueue = Volley.newRequestQueue(this);


        SNTM = Util.getDateTimeString("yyyyMMddHHmmss");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtName.getText().toString().equals(""))
                {
                    ShowMessage("Error","Name can not be empty!");
                    return;
                }

                if(txtCompany.getText().toString().equals(""))
                {
                    ShowMessage("Error","Company can not be empty!");
                    return;
                }

                if(!Util.isValidEmail(txtEmail.getText()))
                {
                    ShowMessage("Error","Wrong Email format!");
                    return;
                }

                register();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestCameraPermission();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            SNID = Util.getIMEI(this);
            if(!Util.getSetting(this,"EMAIL").equals(""))
            {
                login();
            }
        } else {
            requestPhoneStatePermission();
        }

    }

    private void login()
    {
        String url = "http://app.seedword.mobi/app/login.asp";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type resultType = new TypeToken<resultObject>() {}.getType();
                resultObject result = gson.fromJson(response,resultType);
                Intent intent = new Intent();
                if(result.getResult().equals("1"))
                {

                    intent.setClass(LoginActivity.this , eventActivity.class);
                    startActivity(intent);
                }
                else
                {
                    intent.setClass(LoginActivity.this , ResetActivity.class);
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("EMAIL", Util.getSetting(LoginActivity.this,"EMAIL"));
                map.put("SNID", SNID);
                map.put("SNTM", SNTM);
                map.put("VF", Util.getVF(LoginActivity.this,SNTM));
                return map;
            }
        };

        clsGlobal.mQueue.add(stringRequest);
    }

    private void register()
    {
        String url = "http://app.seedword.mobi/app/register.asp";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type resultType = new TypeToken<resultObject>() {}.getType();
                resultObject result = gson.fromJson(response,resultType);


                Intent intent = new Intent();
                if(result.getResult().equals("1"))
                {
                    Util.addSetting(LoginActivity.this,"EMAIL",txtEmail.getText().toString());
                    Util.addSetting(LoginActivity.this,"SNID",String.valueOf(SNID));
                    Util.addSetting(LoginActivity.this,"NAME",txtName.getText().toString());
                    Util.addSetting(LoginActivity.this,"COMPANY",txtCompany.getText().toString());
                    //Util.addSetting(LoginActivity.this,"SNTM",String.valueOf(SNTM));

                    intent.setClass(LoginActivity.this , eventActivity.class);
                    startActivity(intent);
                }
                else
                {
                    intent.setClass(LoginActivity.this , ResetActivity.class);
                    intent.putExtra("EMAIL",txtEmail.getText().toString());
                    intent.putExtra("SNID",String.valueOf(SNID));
                    startActivity(intent);
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();
                    Toast.makeText(LoginActivity.this,"系統暫時無法登入!",Toast.LENGTH_LONG).show();
                }
            }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("EMAIL", txtEmail.getText().toString());
                map.put("SNID", String.valueOf(SNID));
                map.put("COMPANY", txtCompany.getText().toString());
                map.put("SNTM", SNTM);
                map.put("NAME", txtName.getText().toString());
                return map;
            }
        };

        clsGlobal.mQueue.add(stringRequest);
    }

    private void ShowMessage(String subject,String Content)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        builder.setMessage(Content)
                .setTitle(subject);

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

        } else {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void requestPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            SNID = Util.getIMEI(this);
        } else {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_PHONE_STATE
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }
}
