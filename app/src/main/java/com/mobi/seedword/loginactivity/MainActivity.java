package com.mobi.seedword.loginactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity  {

    private baseFragment fragment;
    private FragmentManager fragmentManager;
    private Button btnScan ;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction;
            fragmentManager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.Activity:
                    btnScan.setVisibility(View.VISIBLE);
                    fragment = new ActivityFragment();
                    fragment.baseUrl = getIntent().getStringExtra("ActivityUrl");
                    break;
                case R.id.Product:
                    btnScan.setVisibility(View.VISIBLE);
                    fragment = new productFragment();
                    break;
                case R.id.Map:
                    btnScan.setVisibility(View.INVISIBLE);
                    WelcomeFragment mapFragment = new WelcomeFragment();
                    mapFragment.Url = "http://www.app.url.tw/app/map.asp";
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_layout,mapFragment).commit();
                    return true;
                case R.id.Question:
                    btnScan.setVisibility(View.INVISIBLE);
                    WelcomeFragment questionFragment = new WelcomeFragment();
                    questionFragment.Url = "http://www.app.url.tw/app/Question.asp";
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_layout,questionFragment).commit();
                    return true;
                case R.id.Logout:
                    finish();
                    return true;
            }

            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_layout,fragment).commit();

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.actionbar,null);
        btnScan = (Button)v.findViewById(R.id.btnScan);

        getSupportActionBar().setCustomView(v);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.my_color));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnScan.setVisibility(View.INVISIBLE);

        if (getIntent().hasExtra("ActivityUrl")) {
            ActivityFragment activityFragment = new ActivityFragment();
            activityFragment.baseUrl = getIntent().getStringExtra("ActivityUrl");
            fragmentManager = getSupportFragmentManager();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_layout, activityFragment).commit();

        } else {
            throw new IllegalArgumentException("Activity cannot find  extras ");
        }



        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.startCamera();
            }
        });

    }
}
