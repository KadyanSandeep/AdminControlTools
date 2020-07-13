package com.nativetech.admintools;

import android.app.Application;

import com.nativetech.admincontroltools.AdminToolsApplication;

public class AppApplication extends Application {

    public static final String appRef = "Admin_Control";
    public static String defaultFCMTopic;

    @Override
    public void onCreate() {
        super.onCreate();


        //AdminToolsApplication.initAdminTools("Admin_Tools","app_id","db_url",this);

    }



}







