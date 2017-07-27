package com.avinash.demoapp.activities;

import android.app.Application;
import android.content.Context;

import com.avinash.demoapp.databases.DbHelper;

/**
 * Created by avinash.padalkar on 25/07/2017.
 */

public class MyApplication extends Application {
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = MyApplication.this;
        DbHelper.initDB(mContext);
    }
}