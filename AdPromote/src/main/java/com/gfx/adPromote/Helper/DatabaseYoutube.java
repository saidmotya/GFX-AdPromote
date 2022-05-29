package com.gfx.adPromote.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gfx.adPromote.Models.YoutubeModels;

import java.util.ArrayList;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class DatabaseYoutube extends SQLiteOpenHelper {


    private static final String COLUMN_TITLE = "youtube_title";
    private static final String COLUMN_ICON = "youtube_icon";
    private static final String COLUMN_PREVIEW = "youtube_preview";
    private static final String COLUMN_WATCH_VIDEO = "youtube_video";
    private static final String COLUMN_CHANNEL_ID = "youtube_channel";

    private static final String COLUMN_ALl_YOUTUBE = "AllYoutube";
    private static final String DATABASE_NAME = "databaseYoutube.sqlite";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "MotyaAdYoutube";
    private String CREATE_TABLE;


    public DatabaseYoutube(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_ICON + " TEXT,"
                + COLUMN_PREVIEW + " TEXT,"
                + COLUMN_WATCH_VIDEO + " TEXT,"
                + COLUMN_CHANNEL_ID + " TEXT,"
                + COLUMN_ALl_YOUTUBE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void addYoutubeItem(String title, String icon, String preview, String watch, String id) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_ICON, icon);
        contentValues.put(COLUMN_PREVIEW, preview);
        contentValues.put(COLUMN_WATCH_VIDEO, watch);
        contentValues.put(COLUMN_CHANNEL_ID, id);
        contentValues.put(COLUMN_ALl_YOUTUBE, title + icon + preview + watch + id);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public Integer deleteYoutubeItem(String title, String icon, String preview, String watch, String id) {
        String[] allColumn = new String[]{title + icon + preview + watch + id};
        return getWritableDatabase().delete(TABLE_NAME, COLUMN_ALl_YOUTUBE + " = ?", allColumn);
    }

    public int numberOfYoutubeTable() {
        return (int) DatabaseUtils.queryNumEntries(getWritableDatabase(), TABLE_NAME);
    }

    public ArrayList<YoutubeModels> getAllYoutubeList() {
        ArrayList<YoutubeModels> youtubeModels = new ArrayList();
        ArrayList<YoutubeModels> youtubeModelsNulls = new ArrayList();
        boolean isNull = true;
        String select = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = getReadableDatabase().rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range")
                String icons = cursor.getString(cursor.getColumnIndex(COLUMN_ICON));
                @SuppressLint("Range")
                String preview = cursor.getString(cursor.getColumnIndex(COLUMN_PREVIEW));
                @SuppressLint("Range")
                String watch = cursor.getString(cursor.getColumnIndex(COLUMN_WATCH_VIDEO));
                @SuppressLint("Range")
                String channelId = cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_ID));
                YoutubeModels youtubeData = new YoutubeModels(title, icons, preview, watch, channelId);
                youtubeModels.add(youtubeData);
                isNull = false;
            } while (cursor.moveToNext());
        }
        return isNull ? youtubeModelsNulls : youtubeModels;
    }

}
