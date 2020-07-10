package com.nativetech.admintools;

import android.app.Application;

public class AppApplication extends Application {

    public static final String appRef = "Admin_Control";
    public static String defaultFCMTopic;

    @Override
    public void onCreate() {
        super.onCreate();


        //AdminToolsApplication.initAnalytics("Admin_Tools","","",this);

    }



}







