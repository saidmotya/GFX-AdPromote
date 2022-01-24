package com.gfx.adPromote.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gfx.adPromote.Models.AppScreen;

import java.util.ArrayList;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class DataScreen extends SQLiteOpenHelper {


    private static final String COLUMN_INDEX = "screen_index";
    private static final String COLUMN_LINK = "screen_link";

    private static final String COLUMN_ALl_APPS = "AllApps";
    private static final String DATABASE_NAME = "ScreenDatabase.sqlite";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "MotyaScreen";
    private String CREATE_TABLE;


    public DataScreen(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_INDEX + " TEXT,"
                + COLUMN_LINK + " TEXT,"
                + COLUMN_ALl_APPS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void add(int index, String screen) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_INDEX, index);
        contentValues.put(COLUMN_LINK, screen);
        contentValues.put(COLUMN_ALl_APPS, index + screen);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public Integer delete(String names, String icon, String downloads, String packageName, String appPreview) {
        String[] allColumn = new String[]{names + icon + downloads + packageName + appPreview};
        return getWritableDatabase().delete(TABLE_NAME, COLUMN_ALl_APPS + " = ?", allColumn);
    }

    public int numberOfTable() {
        return (int) DatabaseUtils.queryNumEntries(getWritableDatabase(), TABLE_NAME);
    }

    public ArrayList<AppScreen> getAllScreen() {
        ArrayList<AppScreen> screenList = new ArrayList();
        ArrayList<AppScreen> appScreenNulls = new ArrayList();
        boolean isNull = true;
        String select = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = getReadableDatabase().rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range")
                int index = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INDEX)));
                @SuppressLint("Range")
                String screen = cursor.getString(cursor.getColumnIndex(COLUMN_LINK));
                AppScreen appScreen = new AppScreen(index,screen);
                screenList.add(appScreen);
                isNull = false;
            } while (cursor.moveToNext());
        }
        return isNull ? appScreenNulls : screenList;
    }

}
