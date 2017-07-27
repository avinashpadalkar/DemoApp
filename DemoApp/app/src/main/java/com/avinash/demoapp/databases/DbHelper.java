package com.avinash.demoapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.avinash.demoapp.models.ContactsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash.padalkar on 25/07/2017.
 */


public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper sInstance;
    private static final String DB_NAME = "MY_DB";
    private static final String TABLE_NAME = "MY_INFO";
    private String CREATE_TABLE = "create table " + TABLE_NAME + " (name PRIMARY KEY  NOT NULL  UNIQUE, id TEXT, email TEXT, address TEXT, gender TEXT, office TEXT, mobile TEXT, home TEXT)";
    private SQLiteDatabase mDatabase;
    private int openCounter = 0;
    private static final String TAG = DbHelper.class.getSimpleName() + "=== ";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public static synchronized DbHelper getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(TAG +
                    " db not initialized, Please call \"initDB()\" first");
        }
        return sInstance;
    }

    public static synchronized void initDB(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public synchronized boolean insertRecord(ContactsModel.Contacts model) {
        boolean isInserted = false;
        long count = -1;
        mDatabase = writeData();
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("name", model.getName());
            values.put("id" , model.getId());
            values.put("email" , model.getEmail());
            values.put("address" , model.getAddress());
            values.put("gender" , model.getGender());
            values.put("mobile" , model.getPhone().getMobile());
            values.put("home" , model.getPhone().getHome());
            values.put("office" , model.getPhone().getOffice());

                        count = mDatabase.insert(TABLE_NAME, null, values);

            if (count > -1) {
                isInserted = true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            mDatabase.endTransaction();
        } finally {
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            closeDatabase();
        }
        return isInserted;
    }

    public List<ContactsModel.Contacts> getAllrecords() {

        boolean isReadrecords = false;
        List<ContactsModel.Contacts> list = new ArrayList<>();
        mDatabase = readData();
        mDatabase.beginTransaction();
        String selectQuery = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = null;
        try {
            cursor = mDatabase.rawQuery(selectQuery , null);
            if(cursor.moveToFirst()){
                do{
                    ContactsModel.Contacts model = new ContactsModel.Contacts();
                    model.setName(cursor.getString(cursor.getColumnIndex("name")));
                    model.setId(cursor.getString(cursor.getColumnIndex("id")));
                    model.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    model.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    model.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                    ContactsModel.Phone phoneModel = new ContactsModel.Phone();
                    phoneModel.setOffice(cursor.getString(cursor.getColumnIndex("office")));
                    phoneModel.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                    phoneModel.setHome(cursor.getString(cursor.getColumnIndex("home")));
                    model.setPhone(phoneModel);
                    list.add(model);

                }while (cursor.moveToNext());
            }

            if (list.size() > 0) {
                isReadrecords = true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            isReadrecords = false;
            mDatabase.endTransaction();
        } finally {
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            closeDatabase();
        }
        return list;
    }

    public synchronized SQLiteDatabase writeData() {
        openCounter++;
        if (openCounter == 1) {
            Log.i(TAG, "new write open");
            mDatabase = sInstance.getWritableDatabase();
        } else {
            Log.i(TAG, "old write open");
        }
        return mDatabase;
    }

    public synchronized SQLiteDatabase readData() {
        openCounter++;
        if (openCounter == 1) {
            Log.i(TAG, "new read open");
            mDatabase = sInstance.getReadableDatabase();
        } else {
            Log.i(TAG, "old read open");
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        openCounter--;
        if (openCounter == 0) {
            Log.i(TAG, "db closed");
            mDatabase.close();
        } else {
            Log.i(TAG, "db not closed. Open Connections == " + openCounter);
        }
    }


}
