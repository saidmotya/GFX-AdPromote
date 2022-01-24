package com.gfx.adPromote.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gfx.adPromote.Models.AppModels;

import java.util.ArrayList;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String COLUMN_NAME = "appName";
    private static final String COLUMN_ICON = "appIcon";
    private static final String COLUMN_DOWNLOADS = "appDownload";
    private static final String COLUMN_PACKAGE = "appPackage";
    private static final String COLUMN_PREVIEW = "appPreview";

    private static final String COLUMN_ALl_APPS = "AllApps";
    private static final String DATABASE_NAME = "database.sqlite";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "MotyaAdApps";
    private String CREATE_TABLE;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NAME + " TEXT,"
                + COLUMN_ICON + " TEXT,"
                + COLUMN_DOWNLOADS + " TEXT,"
                + COLUMN_PACKAGE + " TEXT,"
                + COLUMN_PREVIEW + " TEXT,"
                + COLUMN_ALl_APPS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void add(String names, String icon, String downloads, String packageName, String appPreview) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, names);
        contentValues.put(COLUMN_ICON, icon);
        contentValues.put(COLUMN_DOWNLOADS, downloads);
        contentValues.put(COLUMN_PACKAGE, packageName);
        contentValues.put(COLUMN_PREVIEW, appPreview);
        contentValues.put(COLUMN_ALl_APPS, names + icon + downloads + packageName+appPreview);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public Integer delete(String names, String icon, String downloads, String packageName,String appPreview) {
        String[] allColumn = new String[]{names + icon + downloads + packageName+appPreview};
        return getWritableDatabase().delete(TABLE_NAME, COLUMN_ALl_APPS + " = ?",allColumn);
    }

    public int numberOfTable() {
        return (int) DatabaseUtils.queryNumEntries(getWritableDatabase(), TABLE_NAME);
    }

    public ArrayList<AppModels> getAllApps() {
        ArrayList<AppModels> appModels = new ArrayList();
        ArrayList<AppModels> appModelsNulls = new ArrayList();
        boolean isNull = true;
        String select = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = getReadableDatabase().rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range")
                String icons = cursor.getString(cursor.getColumnIndex(COLUMN_ICON));
                @SuppressLint("Range")
                String downloads = cursor.getString(cursor.getColumnIndex(COLUMN_DOWNLOADS));
                @SuppressLint("Range")
                String packageName = cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE));

                @SuppressLint("Range")
                String appPreview = cursor.getString(cursor.getColumnIndex(COLUMN_PREVIEW));

                AppModels apps = new AppModels(name, icons, downloads, packageName,appPreview);
                appModels.add(apps);
                isNull = false;
            } while (cursor.moveToNext());
        }
        return isNull ? appModelsNulls : appModels;
    }

}
