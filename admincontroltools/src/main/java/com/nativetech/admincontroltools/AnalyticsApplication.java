package com.nativetech.admincontroltools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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


    public static FirebaseDatabase masterDatabase;

    public static final String appRef = "Admin_Control";
    public static String defaultFCMTopic;

    public static ArrayList<String> appRefArrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase masterDatabase = FirebaseDatabase.getInstance();
        defaultFCMTopic = getApplicationContext().getPackageName();

        // uncomment below line it for testing ads, updateBox, ratingBox
        // comment before go to production
        // firebaseDatabase = connectWithMasterControl();

    }

    public static FirebaseDatabase initAnalytics(String applicationId, String databaseUrl, Context context) {
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

        return masterDatabase;
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


            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }


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



}







