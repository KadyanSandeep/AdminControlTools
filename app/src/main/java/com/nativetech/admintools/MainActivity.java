package com.nativetech.admintools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nativetech.admincontroltools.AdminToolsApplication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AdminToolsApplication.applyAdminTools(BuildConfig.DEBUG,BuildConfig.VERSION_NAME,this);

    }
}
