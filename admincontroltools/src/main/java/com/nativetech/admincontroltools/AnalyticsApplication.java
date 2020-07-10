package com.nativetech.admincontroltools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AnalyticsApplication extends Application {

    public static SharedPreferences MainSharedPreferences;
    public static String ALERT_BOX_STATUS,ALERT_BOX_TITLE,ALERT_BOX_MESSAGE,ALERT_BOX_URL;
    public static String ALERT_BOX_POSITIVE_BUTTON,ALERT_BOX_NEGATIVE_BUTTON,ALERT_BOX_NEUTRAL_BUTTON;

    public static String RATE_BOX_STATUS,RATE_BOX_TITLE,RATE_BOX_MESSAGE,RATE_BOX_URL;
    public static String RATE_BOX_POSITIVE_BUTTON,RATE_BOX_NEGATIVE_BUTTON,RATE_BOX_NEUTRAL_BUTTON;

    public static FirebaseDatabase masterDatabase;
    public static String appRef = "Not_Specified";
    public static String defaultFCMTopic;
    public static ArrayList<String> appRefArrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        MainSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        masterDatabase = FirebaseDatabase.getInstance();
        defaultFCMTopic = getApplicationContext().getPackageName();

    }

    public static void initAdminTools(String appDefaultRef, String applicationId, String databaseUrl, Context context) {
        appRef = appDefaultRef;
        //FirebaseDatabase masterDatabase = null;
        try {
            boolean hasBeenInitialized = false;
            FirebaseApp firebaseApp;
            List<FirebaseApp> appList = FirebaseApp.getApps(context);
            for (FirebaseApp app : appList) {
                if (app.getName().equals("master")) {
                    hasBeenInitialized = true;
                }
            }
            FirebaseOptions options = new FirebaseOptions.Builder()
                    //.setApplicationId("1:277387363818:android:896cdf25319dcd83") //old Required for Analytics.
                    //.setApplicationId("1:1038685357812:android:9147730de95127e3d980a1") //new Required for Analytics.
                    .setApplicationId(applicationId) //new Required for Analytics.
                    .setApiKey("qwerty") // Required for Auth.
                    //.setDatabaseUrl("https://content-apps-ca.firebaseio.com/") // old Required for RTDB.
                    //.setDatabaseUrl("https://content-apps-26f8a.firebaseio.com/") // new Required for RTDB.
                    .setDatabaseUrl(databaseUrl) // new Required for RTDB.
                    .build();

            if (!hasBeenInitialized)
                firebaseApp = FirebaseApp.initializeApp(context, options, "master");
            else
                firebaseApp = FirebaseApp.getInstance("master");
            masterDatabase = FirebaseDatabase.getInstance(firebaseApp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return masterDatabase;
    }

    public static void applyAdminTools(Context context){

        applyAnalytics(context);
        checkUpdates(masterDatabase,"",context);

        MainSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int launchTime = MainSharedPreferences.getInt("launchTime", 0);
        launchTime++;
        SharedPreferences.Editor editor1 = MainSharedPreferences.edit();
        editor1.putInt("launchTime", launchTime);
        editor1.apply();

        try {
            if (MainSharedPreferences.getInt("launchTime", 1) > 1) {
                showRatingBox(context);
            }
            showAlertBox(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void applyAnalytics(Context context) {
        // First time Open Code on install
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            try {

                Calendar calendar = Calendar.getInstance();
                DateFormat idDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String today_date = idDateFormat.format(calendar.getTime());

                final DatabaseReference masterTotalRef = masterDatabase.getReference("Analytics").child(appRef)
                        .child("Total_Downloads");
                masterTotalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            try {
                                masterTotalRef.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        if (mutableData.getValue() != null) {
                                            int value = mutableData.getValue(Integer.class);
                                            value++;
                                            mutableData.setValue(value);
                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            masterTotalRef.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                final DatabaseReference masterDayWiseRef = masterDatabase.getReference("Analytics").child(appRef)
                        .child("Day_Wise").child(today_date).child("First_Open");
                masterDayWiseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            try {
                                masterDayWiseRef.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        if (mutableData.getValue() != null) {
                                            int value = mutableData.getValue(Integer.class);
                                            value++;
                                            mutableData.setValue(value);
                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            masterDayWiseRef.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                if (BuildConfig.DEBUG){
                    setAppId(context);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        addDAUAnalytics(context);

    }

    public static void addDAUAnalytics(Context context) {
        Calendar calendar = Calendar.getInstance();
        DateFormat idDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String today_date = idDateFormat.format(calendar.getTime());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.getString("lastOpenDate", "No_Date").equals(today_date)) {
            // run your one time code
            try {

                final DatabaseReference masterDayWiseRef = masterDatabase.getReference("Analytics").child(appRef)
                        .child("Day_Wise").child(today_date).child("DAU");
                masterDayWiseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            try {
                                masterDayWiseRef.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        if (mutableData.getValue() != null) {
                                            int value = mutableData.getValue(Integer.class);
                                            value++;
                                            mutableData.setValue(value);
                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            masterDayWiseRef.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("lastOpenDate", today_date);
            editor.commit();
        }


        addSessionAnalytics(context);


    }

    public static void addSessionAnalytics(Context context){

        /*DatabaseReference analyticsRef = masterDatabase.getReference().child("Analytics");
        analyticsRef.keepSynced(true);
        analyticsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    appRefArrayList.clear();
                    appRefArrayList.add("Select a App");
                    for (DataSnapshot devicesSnapshot : dataSnapshot.getChildren()) {
                        appRefArrayList.add(devicesSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/

        Calendar calendar = Calendar.getInstance();
        DateFormat idDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String today_date = idDateFormat.format(calendar.getTime());

        final DatabaseReference masterDayWiseRef = masterDatabase.getReference("Analytics").child(appRef)
                .child("Day_Wise").child(today_date).child("Sessions");
        masterDayWiseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        masterDayWiseRef.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                if (mutableData.getValue() != null) {
                                    int value = mutableData.getValue(Integer.class);
                                    value++;
                                    mutableData.setValue(value);
                                }
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    masterDayWiseRef.setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private static void checkUpdates(FirebaseDatabase database, String rootRef, Context context) {
        //Implement force updater via firebase realtime database
        final String[] ForceRequired = new String[1];
        final String[] ForceUpdateStatus = new String[1];
        DatabaseReference forceUpdater_ref = database.getReference(rootRef).child(appRef).child("ForceUpdater");
        forceUpdater_ref.keepSynced(true);
        forceUpdater_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ForceRequired[0] = dataSnapshot.child("ForceRequired").getValue(String.class);
                    ForceUpdateStatus[0] = dataSnapshot.child("ForceUpdateStatus").getValue(String.class);
                    String NewVersion = dataSnapshot.child("ForceUpdate").getValue(String.class);
                    String CurrentVersion = BuildConfig.VERSION_NAME;
                    try {
                        if (NewVersion != null) {

                            if (!TextUtils.equals(CurrentVersion, NewVersion) && !TextUtils.equals(ForceRequired[0], "true") && TextUtils.equals(ForceUpdateStatus[0], "on")) {
                                updateDialog();
                            }
                            if (!TextUtils.equals(CurrentVersion, NewVersion) && TextUtils.equals(ForceRequired[0], "true") && TextUtils.equals(ForceUpdateStatus[0], "on")) {
                                ForceUpdateDialog();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            private void updateDialog() {
                AlertDialog dialog =  new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogWhite))
                        .setTitle("New version available")
                        .setCancelable(false)
                        .setMessage("Please, Update your app to new version for new features ")
                        .setPositiveButton("Update Now",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {

                                        try {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("market://details?id=" + context.getPackageName())));
                                        } catch (android.content.ActivityNotFoundException e) {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                                        }

                                    }
                                }).setNegativeButton("No, thanks",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                }).create();
                dialog.show();
            }

            private void ForceUpdateDialog() {
                AlertDialog dialog =  new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogWhite))
                        .setTitle("Update Needed")
                        .setCancelable(false)
                        .setMessage("Please, Update your app to new version. This version of " + context.getApplicationInfo().loadLabel(context.getPackageManager()) + " become obsolete.")
                        .setPositiveButton("Update Now",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        ((Activity)context).finish();
                                        try {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("market://details?id=" + context.getPackageName())));
                                        } catch (android.content.ActivityNotFoundException e) {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                                        }

                                    }
                                }).create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void showRatingBox(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference AlertBox_ref = masterDatabase.getReference(appRef).child("DashBoard").child("Rate_Box");
        AlertBox_ref.keepSynced(true);
        AlertBox_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        RATE_BOX_TITLE = dataSnapshot.child("Rate_Box_Title").getValue(String.class);
                        RATE_BOX_MESSAGE = dataSnapshot.child("Rate_Box_Message").getValue(String.class);
                        RATE_BOX_URL = dataSnapshot.child("Rate_Box_URL").getValue(String.class);
                        RATE_BOX_POSITIVE_BUTTON = dataSnapshot.child("Rate_Box_Positive_Button").getValue(String.class);
                        RATE_BOX_NEUTRAL_BUTTON = dataSnapshot.child("Rate_Box_Neutral_Button").getValue(String.class);
                        RATE_BOX_NEGATIVE_BUTTON = dataSnapshot.child("Rate_Box_Negative_Button").getValue(String.class);
                        RATE_BOX_STATUS = dataSnapshot.child("Rate_Box_Status").getValue(String.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (RATE_BOX_STATUS != null && MainSharedPreferences.getBoolean("isShowRateBoxAgain", true)) {

                            if (TextUtils.equals(RATE_BOX_STATUS, "on")) {
                                AlertBox();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            private void AlertBox() {
                AlertDialog dialog =  new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogWhite))
                        .setTitle(RATE_BOX_TITLE)
                        .setCancelable(false)
                        .setMessage(RATE_BOX_MESSAGE)
                        .setPositiveButton(RATE_BOX_POSITIVE_BUTTON,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        try {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("market://details?id=" + context.getPackageName())));
                                        } catch (android.content.ActivityNotFoundException e1) {
                                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                                        }

                                    }
                                })
                        .setNeutralButton(RATE_BOX_NEUTRAL_BUTTON, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //MainSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = MainSharedPreferences.edit();
                                editor.putBoolean("isShowRateBoxAgain", false);
                                editor.apply();
                            }
                        }).setNegativeButton(RATE_BOX_NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(),"The read failed: " + databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showAlertBox(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference AlertBox_ref = masterDatabase.getReference(appRef).child("DashBoard").child("Alert_Box");
        AlertBox_ref.keepSynced(true);
        AlertBox_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        ALERT_BOX_TITLE = dataSnapshot.child("Alert_Box_Title").getValue(String.class);
                        ALERT_BOX_MESSAGE = dataSnapshot.child("Alert_Box_Message").getValue(String.class);
                        ALERT_BOX_URL = dataSnapshot.child("Alert_Box_URL").getValue(String.class);
                        ALERT_BOX_POSITIVE_BUTTON = dataSnapshot.child("Alert_Box_Positive_Button").getValue(String.class);
                        ALERT_BOX_NEUTRAL_BUTTON = dataSnapshot.child("Alert_Box_Neutral_Button").getValue(String.class);
                        ALERT_BOX_NEGATIVE_BUTTON = dataSnapshot.child("Alert_Box_Negative_Button").getValue(String.class);
                        ALERT_BOX_STATUS = dataSnapshot.child("Alert_Box_Status").getValue(String.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (ALERT_BOX_STATUS != null && MainSharedPreferences.getBoolean("isShowAlertBoxAgain", true)) {

                            if (TextUtils.equals(ALERT_BOX_STATUS, "on")) {
                                AlertBox();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            private void AlertBox() {
                AlertDialog dialog =  new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogWhite))
                        .setTitle(ALERT_BOX_TITLE)
                        .setCancelable(false)
                        .setMessage(ALERT_BOX_MESSAGE)
                        .setPositiveButton(ALERT_BOX_POSITIVE_BUTTON,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        try {
                                            if (ALERT_BOX_URL!=null) {
                                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ALERT_BOX_URL)));
                                            }
                                        } catch (android.content.ActivityNotFoundException e) {
                                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(RATE_BOX_URL)));
                                            dialog.dismiss();
                                        }

                                    }
                                })
                        .setNeutralButton(ALERT_BOX_NEUTRAL_BUTTON, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedPreferences MainSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = MainSharedPreferences.edit();
                                editor.putBoolean("isShowAlertBoxAgain", false);
                                editor.apply();
                            }
                        }).setNegativeButton(ALERT_BOX_NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(),"The read failed: " + databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void setAppId(Context context){
        //set appId;
        String packageName = context.getPackageName();
        final DatabaseReference masterAppRef = masterDatabase.getReference("Analytics").child(appRef)
                .child("appId");
        masterAppRef.setValue(packageName);
    }



}







