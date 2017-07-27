package com.avinash.demoapp.runtimepermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.avinash.demoapp.R;

import java.util.ArrayList;
/**
 * Created by Avinash.Padalkar on 24/07/2017.
 */

public class RuntimePermissionUtils extends AppCompatActivity {

    public static final String[] CALENDER = {Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR};
    public static final String[] CAMERA = {Manifest.permission.CAMERA};
    public static final String[] CONTACTS = {Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS};
    public static final String[] LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String[] MICROPHONE = {Manifest.permission.RECORD_AUDIO};
    public static final String[] PHONE = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS};
    public static final String[] SENSOR = {Manifest.permission.BODY_SENSORS};
    public static final String[] SMS = {Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS};
    public static final String[] STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] MULTIPLE_PERMISSION = {Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.READ_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int REQUEST_RUNTIME_PERMISSION = 101;
    public static final int REQUESR_SINGLE_RUNTIME_PERMISSION = 102;

    private Context mContext;
    private Activity mActivity;
    private LinearLayout ll_parent;

    public static boolean checkAndroidVersionMarshmallowOrGreater() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissionutils);

        initialization();

    }

    private void initialization() {
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        mContext = RuntimePermissionUtils.this;
        mActivity = RuntimePermissionUtils.this;

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String permission[] = b.getStringArray("permission");
            if (permission.length > 0) {
                checkPermissionAndRequest(mActivity, permission);
            }
        }
    }

    public boolean checkPermissionIsGranted(Activity activity, String[] permissions){
        boolean isPermissionGranted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                isPermissionGranted = false;
                break;
            }
        }
        return isPermissionGranted;
    }

    public void checkPermissionAndRequest(Activity activity, String[] per) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String permission : per) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        if (deniedPermissions.size() > 0) {
            String[] permissions = new String[deniedPermissions.size()];
            permissions = deniedPermissions.toArray(permissions);
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_RUNTIME_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_RUNTIME_PERMISSION) {
            if (permissions.length > 0) {
                ArrayList<String> neverAskPermissions = new ArrayList<>();
                for (int i=0; i<permissions.length; i++) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[i]);
                    if (!showRationale) {
                        boolean isDenied = ContextCompat.checkSelfPermission(mActivity,permissions[i])==PackageManager.PERMISSION_DENIED;
                        if(isDenied) {
                            neverAskPermissions.add(permissions[i]);
                        }
                    }
                }

                Log.e(RuntimePermissionUtils.class.getSimpleName(), "onRequestPermissionsResult");

                if(neverAskPermissions.size() > 0) {
                    showMessageOKCancel();
                }else {
                    finish();
                }
            }
        }

    }

    private void showMessageOKCancel() {

        Snackbar snackbar = Snackbar.make(ll_parent, "You need to allow permission", Snackbar.LENGTH_INDEFINITE)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                        intent.setData(uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        finish();
                        mContext.startActivity(intent);
                    }
                });
        snackbar.show();
    }


}
