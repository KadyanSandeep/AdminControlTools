package com.nativetech.admintools;

import android.app.Application;

import com.nativetech.admincontroltools.AnalyticsApplication;

public class AppApplication extends Application {

    public static final String appRef = "Admin_Control";
    public static String defaultFCMTopic;

    @Override
    public void onCreate() {
        super.onCreate();


        AnalyticsApplication.initAnalytics("","",this);

    }



}







