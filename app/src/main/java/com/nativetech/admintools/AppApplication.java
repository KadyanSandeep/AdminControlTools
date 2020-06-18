package com.nativetech.admintools;

import android.app.Application;

import com.nativetech.admincontroltools.AnalyticsApplication;

public class AppApplication extends Application {

    public static final String appRef = "Admin_Control";
    public static String defaultFCMTopic;

    @Override
    public void onCreate() {
        super.onCreate();


        AnalyticsApplication.initAnalytics("Admin_Tools","1:1038685357812:android:9147730de95127e3d980a1",
                "https://content-apps-26f8a.firebaseio.com/",this);

    }



}







