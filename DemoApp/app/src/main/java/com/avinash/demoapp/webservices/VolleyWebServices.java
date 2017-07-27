package com.avinash.demoapp.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.avinash.demoapp.R;
import com.avinash.demoapp.apputils.Utils;
import com.avinash.demoapp.interfaces.VolleyCallBack;
import com.avinash.demoapp.interfaces.VolleyStringCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import static com.avinash.demoapp.webservices.VolleySingleton.getInstance;


/**
 * Created by avinash.padalkar on 25/07/2017.
 */

public class VolleyWebServices {
    VolleyStringCallBack stringListner;
    private Context context;
    private String url_validate = "&=*+-_.,:!?()/~%";
    private VolleySingleton requestQueue;
    private VolleyCallBack listner;

    public VolleyStringCallBack getStringListner() {
        return stringListner;
    }

    public void setStringListner(VolleyStringCallBack stringListner) {
        this.stringListner = stringListner;
    }


    public VolleyCallBack getListner() {
        return listner;
    }

    public void setListner(VolleyCallBack listner) {
        this.listner = listner;
    }

    public void MyJsonObjectRequestGet(final Context mContext,
                                       final CoordinatorLayout cdLayout,
                                       final String methodUrl,
                                       final String methodName) {

        if (Utils.isInternetAvailable(mContext)) {
            this.context = mContext;

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl, url_validate);
//            final String final_url = methodUrl;
            Log.d("Request====", final_url);
            requestQueue = getInstance(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    final_url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject completeObject) {
                            dialog.dismiss();

                            try {

                                Log.d("Response===" + methodName, "" + completeObject.toString());
                                JSONArray response_jsonArray = completeObject.getJSONArray("contacts");
                                listner.getResponse(completeObject.toString(), methodName);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                listner.getError("");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            // TODO snackbar
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            Snackbar snackbar = Snackbar.make(cdLayout, volleyError.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            listner.getError(volleyError.getMessage());

                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //requestQueue.add(jsonObjectRequest);
            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, "R.string.no_internet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }
}
/*


    public void MyJsonString(final Context mContext,
                             final CoordinatorLayout cdLayout,
                             final String methodUrl,
                             final String methodName) {

        if (Utils.isInternetAvailable(mContext)) {
            this.context = mContext;
            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl, url_validate);

            //requestQueue = Volley.newRequestQueue(mContext);
            requestQueue = getInstance(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    final_url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject completeObject) {
                            dialog.dismiss();

                            try {
                                Log.d("Request====", final_url);
                                Log.d("Response===" + methodName, "" + completeObject.toString());
                                */
/*JSONObject response_jsonObject = completeObject.getJSONObject(context.getString(R.string.response));
                                String status = response_jsonObject.getString(context.getString(R.string.status));
                                if (status.equalsIgnoreCase(context.getString(R.string.succeed))) {
                                    String responseToReturn = response_jsonObject.getString(context.getString(R.string.data));
                                    stringListner.getResponseString(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase(context.getString(R.string.failed))) {
                                    Log.d("Response===" + methodName, "" + completeObject.toString());
                                    stringListner.getErrorString(response_jsonObject.getString(context.getString(R.string.data)));
                                    Snackbar snackbar = Snackbar.make(cdLayout, response_jsonObject.getString(context.getString(R.string.data)), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }*//*


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            // TODO snackbar
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            String errorMsg = null;
                            try {
                                errorMsg = volleyError.getCause().getMessage().toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                                errorMsg = "Please Check Internet Connection.";
                            }
                            if (errorMsg.isEmpty() || errorMsg.equalsIgnoreCase(null)) {
                                errorMsg = "Please Check Internet Connection.";
                            }
                            Snackbar snackbar = Snackbar.make(cdLayout, errorMsg, Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            stringListner.getErrorString(volleyError.getMessage());
 */
/*try
                        {
                            ErrorMsg.showErrorDialg(context, new String(volleyError.networkResponse.data));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
*//*

                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //requestQueue.add(jsonObjectRequest);
            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, "R.string.no_internet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    public void MyJsonObjPostRequest(final Context mContext,
                                     final CoordinatorLayout cdLayout,
                                     final String methodUrl,
                                     final String methodName, final byte[] fileData) {

        if (Utils.isInternetAvailable(mContext)) {
            this.context = mContext;

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl, url_validate);

            //requestQueue = Volley.newRequestQueue(mContext);
            requestQueue = getInstance(mContext);
            VolleyMultipartRequest jsonObjectRequest = new VolleyMultipartRequest(
                    Request.Method.POST,
                    final_url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse completeObject) {
                            dialog.dismiss();
                            String resultResponse = new String(completeObject.data);
                            Log.e("Response message", "" + resultResponse);
                            try {
                                Log.d("Request====", final_url);
                                Log.d("Response===" + methodName, "" + completeObject.toString());

                                JSONObject response = new JSONObject(resultResponse);
                                JSONObject response_jsonObject = response.getJSONObject("response");
                                String status = response_jsonObject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String responseToReturn = response_jsonObject.getString("data");
                                    stringListner.getResponseString(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase("failed")) {
                                    Log.d("Response===" + methodName, "" + completeObject.toString());
                                    stringListner.getErrorString(response_jsonObject.getString("data"));
                                    Snackbar snackbar = Snackbar.make(cdLayout, response_jsonObject.getString("data"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            // TODO snackbar
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            Snackbar snackbar = Snackbar.make(cdLayout, volleyError.getCause().getMessage().toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            stringListner.getErrorString(volleyError.getMessage());
                        }
                    }) {

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("ImageByte", new VolleyMultipartRequest.DataPart("choose_report.pdf", fileData, "doc/pdf"));

                    return params;
                }

            };


            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, "R.string.No_internet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }


    */
/*
    public void MyJsonStringPost(final Context mContext,
                             final CoordinatorLayout cdLayout,
                             final String methodUrl,
                             final String methodName) {

        if (Internet.isAvailable(mContext)) {
            this.context = mContext;

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl,url_validate);

            //requestQueue = Volley.newRequestQueue(mContext);
            requestQueue = getInstance(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    final_url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject completeObject) {
                            dialog.dismiss();

                            try {
                                Log.d("Request====", final_url);
                                Log.d("Response===" + methodName, "" + completeObject.toString());
                                JSONObject response_jsonObject = completeObject.getJSONObject("response");
                                String status = response_jsonObject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String responseToReturn = response_jsonObject.getString("data");

                                    stringListner.getStringResponse(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase("failed")) {
                                    Log.d("Response===" + methodName, "" + completeObject.toString());
                                    stringListner.getError(response_jsonObject.getString("data"));
                                    Snackbar snackbar = Snackbar.make(cdLayout, response_jsonObject.getString("data"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            // TODO snackbar
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            Snackbar snackbar = Snackbar.make(cdLayout, volleyError.getCause().getMessage().toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            stringListner.getError(volleyError.getMessage());
 try
                        {
                            ErrorMsg.showErrorDialg(context, new String(volleyError.networkResponse.data));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //requestQueue.add(jsonObjectRequest);
            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, R.string.No_internet, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    public void MyJsonObjRequest(final Context mContext,
                             final CoordinatorLayout cdLayout,
                             final String methodUrl,
                             final String methodName) {

        if (Internet.isAvailable(mContext)) {
            this.context = mContext;

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl,url_validate);

            //requestQueue = Volley.newRequestQueue(mContext);
            requestQueue = getInstance(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    final_url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject completeObject) {
                            dialog.dismiss();

                            try {
                                Log.d("Request====", final_url);
                                Log.d("Response===" + methodName, "" + completeObject.toString());
                                JSONObject response_jsonObject = completeObject.getJSONObject("response");
                                String status = response_jsonObject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    JSONObject responseToReturn = response_jsonObject.getJSONObject("data");
                                    jsonObjListner.getJsonObjResponse(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase("failed")) {
                                    Log.d("Response===" + methodName, "" + completeObject.toString());
                                    jsonObjListner.getError(response_jsonObject.getString("data"));
                                    Snackbar snackbar = Snackbar.make(cdLayout, response_jsonObject.getString("data"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            // TODO snackbar
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            Snackbar snackbar = Snackbar.make(cdLayout, volleyError.getCause().getMessage().toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            jsonObjListner.getError(volleyError.getMessage());
 try
                        {
                            ErrorMsg.showErrorDialg(context, new String(volleyError.networkResponse.data));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //requestQueue.add(jsonObjectRequest);
            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, R.string.No_internet, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }



    public void MyJsonObjPostRequest(final Context mContext,
                                 final CoordinatorLayout cdLayout,
                                 final String methodUrl,
                                 final String methodName,final byte[] fileData) {

        if (Util.isInternetAvailable(mContext)) {
            this.context = mContext;

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl,url_validate);

            //requestQueue = Volley.newRequestQueue(mContext);
            requestQueue = getInstance(mContext);
            VolleyMultipartRequest jsonObjectRequest = new VolleyMultipartRequest(
                    Request.Method.POST,
                    final_url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse completeObject) {
                            dialog.dismiss();
                            String resultResponse = new String(completeObject.data);
                            Log.e("Response message", "" + resultResponse);
                            try {
                                Log.d("Request====", final_url);
                                Log.d("Response===" + methodName, "" + completeObject.toString());

                                JSONObject response = new JSONObject(resultResponse);
                                JSONObject response_jsonObject = response.getJSONObject("response");
                                String status = response_jsonObject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String responseToReturn = response_jsonObject.getString("data");
                                    stringListner.getStringResponse(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase("failed")) {
                                    Log.d("Response===" + methodName, "" + completeObject.toString());
                                    stringListner.getError(response_jsonObject.getString("data"));
                                    Snackbar snackbar = Snackbar.make(cdLayout, response_jsonObject.getString("data"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            // TODO snackbar
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            Snackbar snackbar = Snackbar.make(cdLayout, volleyError.getCause().getMessage().toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            stringListner.getError(volleyError.getMessage());
                        }
                    }) {

                  @Override
                    protected Map<String, DataPart> getByteData () {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("FileData", new VolleyMultipartRequest.DataPart("choose_report.pdf", fileData, "doc/pdf"));

                    return params;
                }

            };


            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, R.string.No_internet, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }


    public void MyJsonObjNoFilePostRequest(final Context mContext,
                                     final CoordinatorLayout cdLayout,
                                     final String methodUrl,
                                     final String methodName) {

        if (Internet.isAvailable(mContext)) {
            this.context = mContext;

            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();

            final String final_url = Uri.encode(methodUrl,url_validate);

            //requestQueue = Volley.newRequestQueue(mContext);
            requestQueue = getInstance(mContext);
            VolleyMultipartRequest jsonObjectRequest = new VolleyMultipartRequest(
                    Request.Method.POST,
                    final_url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse completeObject) {
                            dialog.dismiss();
                            String resultResponse = new String(completeObject.data);
                            Log.e("Response message", "" + resultResponse);
                            try {
                                Log.d("Request====", final_url);
                                Log.d("Response===" + methodName, "" + completeObject.toString());

                                JSONObject response = new JSONObject(resultResponse);
                                JSONObject response_jsonObject = response.getJSONObject("response");
                                String status = response_jsonObject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String responseToReturn = response_jsonObject.getString("data");
                                    stringListner.getStringResponse(responseToReturn, methodName);
                                } else if (status.equalsIgnoreCase("failed")) {
                                    Log.d("Response===" + methodName, "" + completeObject.toString());
                                    stringListner.getError(response_jsonObject.getString("data"));
                                    Snackbar snackbar = Snackbar.make(cdLayout, response_jsonObject.getString("data"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.dismiss();
                            // TODO snackbar
                            Log.d("Request====", final_url);
                            Log.d("WebServices===", "inside Response.ErrorListener()===");
                            Snackbar snackbar = Snackbar.make(cdLayout, volleyError.getCause().getMessage().toString(), Snackbar.LENGTH_INDEFINITE).setAction("Retry!", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyJsonObjectRequestGet(mContext, cdLayout, methodUrl, methodName);
                                }
                            });
                            snackbar.show();
                            stringListner.getError(volleyError.getMessage());
                        }
                    });


            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.addToRequestQueue(jsonObjectRequest);
        } else {
            Snackbar snackbar = Snackbar.make(cdLayout, R.string.No_internet, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

*//*


}
*/
