package com.avinash.demoapp.interfaces;

import org.json.JSONArray;


/**
 * Created by avinash.padalkar on 25/07/2017.
 */
public interface VolleyCallBack {

    public void getResponse(String jsonArray, String tag);

    public void getError(String error);

    public void displayFailedResponse(String failedMessage);

}
