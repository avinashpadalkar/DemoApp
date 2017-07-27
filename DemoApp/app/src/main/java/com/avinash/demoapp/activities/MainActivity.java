package com.avinash.demoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avinash.demoapp.R;
import com.avinash.demoapp.adapters.MainAdapter;
import com.avinash.demoapp.databases.DbHelper;
import com.avinash.demoapp.interfaces.ClickListener;
import com.avinash.demoapp.interfaces.VolleyCallBack;
import com.avinash.demoapp.listeners.RecyclerTouchListener;
import com.avinash.demoapp.models.ContactsModel;
import com.avinash.demoapp.runtimepermissions.RuntimePermissionUtils;
import com.avinash.demoapp.webservices.VolleyWebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VolleyCallBack, SearchView.OnQueryTextListener {


    private SearchView searchView;
    private Context mContext;
    private RecyclerView recyclerView;
    private DbHelper mHelper;
    private RuntimePermissionUtils runtimePermissionUtils;
    private VolleyWebServices services;
    private ContactsModel contactsModel;
    List<ContactsModel> gsonResponseList;
    List<ContactsModel.Contacts> mainList = new ArrayList<>();
    List<ContactsModel.Contacts> filteredList = new ArrayList<>();
    private Gson gson;
    private GsonBuilder gsonBuilder;
    private MainAdapter adapter;
    private TextView tvNoDataFound;
    private Button btnWebservice;

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialization();

        btnWebservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RuntimePermissionUtils.checkAndroidVersionMarshmallowOrGreater()) {
                    if (runtimePermissionUtils.checkPermissionIsGranted(MainActivity.this, RuntimePermissionUtils.STORAGE)) {
                        // Permission is Granted.
                        Toast.makeText(mContext, "Permission is Granted", Toast.LENGTH_SHORT).show();
                        makeWebserviceRequest();
                    } else {
                        Intent intent = new Intent(mContext, RuntimePermissionUtils.class);
                        intent.putExtra("permission", RuntimePermissionUtils.STORAGE);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(mContext, "Your Phone is below Android M", Toast.LENGTH_SHORT).show();
                    makeWebserviceRequest();
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void intialization() {
        mContext = this;
        mHelper = DbHelper.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnWebservice = (Button) findViewById(R.id.btn_webservice);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        tvNoDataFound = (TextView) findViewById(R.id.tv_no_data_found);
        adapter = new MainAdapter();
        runtimePermissionUtils = new RuntimePermissionUtils();
        services = new VolleyWebServices();

        gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        gsonResponseList = new ArrayList<ContactsModel>();
        gsonResponseList.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void makeWebserviceRequest() {
        String url = "https://api.androidhive.info/contacts/";
        services.MyJsonObjectRequestGet(mContext, null, url, "contacts");
        services.setListner(this);
    }

    @Override
    public void getResponse(String jsonArray, String tag) {
        Log.e("TAG", "" + jsonArray);
        if (tag.equalsIgnoreCase("contacts")) {

            gsonResponseList.clear();
            gsonResponseList.add((ContactsModel) gson.fromJson(jsonArray, ContactsModel.class));
            mainList.clear();
            mainList = gsonResponseList.get(0).getContacts();

//            contactsModel = gson.fromJson(response , ContactsModel.class);
            Log.e("TAG", "contactsModel = " + gsonResponseList);
           /* if (gsonResponseList.size() > 0) {
                adapter = new MainAdapter(mContext, gsonResponseList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }*/

            addToDB(mainList);
            setToAdapter();

        }
    }

    private void setToAdapter() {
        List<ContactsModel.Contacts> list = mHelper.getAllrecords();
        adapter = new MainAdapter(mContext, list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void addToDB(List<ContactsModel.Contacts> list) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                boolean isInsert = mHelper.insertRecord(list.get(i));
                if (isInsert) {
                    Log.e("TAG", "Insert==" + list.get(i).getName());
                } else {
                    Log.e("TAG", "Insert Not==" + list.get(i).getName());
                }
            }
        } else {
            Toast.makeText(mContext, "No Data Found!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getError(String error) {
        Log.e("TAG", "" + error);
    }

    @Override
    public void displayFailedResponse(String failedMessage) {

        Log.e("TAG", "" + failedMessage);
    }

    @Override
    protected void onPause() {
        try {
            searchView.clearFocus();
            searchView.setQuery("", false);
            searchView.setIconified(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_bar, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        item.collapseActionView();
        searchView.setIconified(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        filteredList.clear();
        filteredList = filter(mainList, query);
        if (filteredList.size() > 0) {

//            tvNoDataFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            MainAdapter adapter = new MainAdapter(mContext, filteredList);
//            adapter.setUrnList(filteredModelList);
            recyclerView.setAdapter(adapter);

            /*rv_urn_list.setAdapter(null);
            rvOfflineUrnList.setVisibility(View.VISIBLE);
            tvOfflineUrnNoData.setVisibility(View.GONE);


            OfflineUrnListAdapter offlineUrnListAdapter = new OfflineUrnListAdapter(this, filteredModelList);
            rvOfflineUrnList.setAdapter(offlineUrnListAdapter);
            offlineUrnListAdapter.notifyDataSetChanged();*/
            return true;
        } else {

/*            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText("No Match Found!!");*/
//            tvNoDataFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<ContactsModel.Contacts> filter(List<ContactsModel.Contacts> models, String query) {
        query = query.toLowerCase();

        final ArrayList<ContactsModel.Contacts> filteredModelList = new ArrayList<>();
        for (ContactsModel.Contacts model : models) {
            final String urnNo = model.getName().toLowerCase();
            final String regNo = model.getId().toLowerCase();
            final String patientName = model.getEmail().toLowerCase();
            if (urnNo.contains(query) || regNo.contains(query) || patientName.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
