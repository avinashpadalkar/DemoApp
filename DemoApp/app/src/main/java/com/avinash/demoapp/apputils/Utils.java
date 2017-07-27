package com.avinash.demoapp.apputils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Avinash.Padalkar on 24/07/2017.
 */

public class Utils {

    public static boolean isInternetAvailable(Context mContext) {
        if (haveNetworkConnection(mContext)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean haveNetworkConnection(Context contxt) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) contxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    return haveConnectedWifi;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    return haveConnectedMobile;
                }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
