package com.avinash.demoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.avinash.demoapp.R;
import com.avinash.demoapp.databases.DbHelper;
import com.avinash.demoapp.runtimepermissions.RuntimePermissionUtils;

import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private RuntimePermissionUtils runtimePermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        runtimePermissionUtils = new RuntimePermissionUtils();

    }

    private void checkAndroidVersionAndRequest() {
        if (RuntimePermissionUtils.checkAndroidVersionMarshmallowOrGreater()) {
            if(runtimePermissionUtils.checkPermissionIsGranted(SplashActivity.this, RuntimePermissionUtils.STORAGE)){
                // Permission is Granted.
                Toast.makeText(mContext, "Permission is Granted", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }else {
                Intent intent = new Intent(mContext, RuntimePermissionUtils.class);
                intent.putExtra("permission", RuntimePermissionUtils.STORAGE);
                startActivity(intent);
            }
        } else {
            Toast.makeText(mContext, "Your Phone is below Android M", Toast.LENGTH_SHORT).show();
            startMainActivity();
        }
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                intent.setFlags(FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(intent);
            }
        },3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndroidVersionAndRequest();
    }
}
