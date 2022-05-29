package com.gfx.adPromote.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class PromotePrefs {

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor prefEditor;

    private final String DOWNLOAD_COUNTER = "DOWNLOAD_COUNTER_";
    private final String RATE_COUNTER = "RATE_COUNTER_";
    private final String RATE_PEOPLE = "RATE_PEOPLE";

    private final String VIEWS_COUNTER = "VIEWS_COUNTER_";
    private final String LIKE_COUNTER = "LIKE_COUNTER_";

    @SuppressLint("CommitPrefEdits")
    public PromotePrefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefEditor = prefs.edit();
    }


    public void setDownloadCounter(int position,String downloads) {
        prefEditor.putString(DOWNLOAD_COUNTER+position, downloads);
        prefEditor.apply();
    }

    public String getDownloadCounter(int position) {
        return prefs.getString(DOWNLOAD_COUNTER+position, "");
    }

    public void setRateCounter(int position,float rating) {
        prefEditor.putFloat(RATE_COUNTER+position, rating);
        prefEditor.apply();
    }

    public float getRateCounter(int position) {
        return prefs.getFloat(RATE_COUNTER+position, 4.8f);
    }


    public void setRatePeople(int position,String peopleRate) {
        prefEditor.putString(RATE_PEOPLE+position, peopleRate);
        prefEditor.apply();
    }

    public String getRatePeople(int position) {
        return prefs.getString(RATE_PEOPLE+position, "");
    }

    public void setViewsCounter(int position,String downloads) {
        prefEditor.putString(VIEWS_COUNTER +position, downloads);
        prefEditor.apply();
    }

    public String getViewsCounter(int position) {
        return prefs.getString(VIEWS_COUNTER +position, "");
    }

    public void setLikeCounter(int position,String likes) {
        prefEditor.putString(LIKE_COUNTER + position, likes);
        prefEditor.apply();
    }

    public String getLikeCounter(int position) {
        return prefs.getString(LIKE_COUNTER +position, "12K");
    }




}
