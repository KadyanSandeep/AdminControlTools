package com.nativetech.admintools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nativetech.admincontroltools.AnalyticsApplication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AnalyticsApplication.applyAnalytics(this);
        //AnalyticsApplication.addDAUAnalytics(this);
        //AnalyticsApplication.addSessionAnalytics(this);

    }
}
