package com.gfx.adPromote.ConnectionAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import com.gfx.adPromote.BuildConfig;
import com.gfx.adPromote.Config.AppsConfig;
import com.gfx.adPromote.Helper.DataScreen;
import com.gfx.adPromote.Helper.DatabaseHelper;
import com.gfx.adPromote.Helper.PromotePrefs;
import com.gfx.adPromote.Interfaces.OnConnectedListener;
import com.gfx.adPromote.Models.AppModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class ConnectionPromote {

    protected String TAG = "connectionPromote";
    private final String ConnectionAdOff = "AdPromoteCache.json";

    private Activity activity ;
    private Context context;
    private final String adLink;
    private URL url;

    private final DatabaseHelper databaseHelper ;
    private final DataScreen dataScreen ;
    private final PromotePrefs promotePrefs;
    private OnConnectedListener onConnectedListener;

    public ConnectionPromote(Context context, String adLink) {
        this.context = context;
        this.adLink = adLink;
        databaseHelper = new DatabaseHelper(context);
        dataScreen = new DataScreen(context);
        promotePrefs = new PromotePrefs(context);
        new setAppPromotion().execute();

    }

    public ConnectionPromote(Activity activity, String adLink) {
        this.activity = activity;
        this.adLink = adLink;
        this.context = activity.getApplicationContext();
        databaseHelper = new DatabaseHelper(activity.getApplicationContext());
        dataScreen = new DataScreen(activity.getApplicationContext());
        promotePrefs = new PromotePrefs(activity.getApplicationContext());
        setAppPromotionThread();

    }


    //Execute connection in Android 30 or Higher : this method need Activity.
    private void setAppPromotionThread(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String task = doInBackgroundTask();
                if (BuildConfig.DEBUG) {
                    setLog("Thread Connecting Running...");
                }
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        setConnection(task,true);
                        if (BuildConfig.DEBUG) {
                            setLog("Thread Finished with : "+task);

                        }
                    }
                });
            }
        }).start();
    }

    //Deprecated on Android 30 or Higher
    @SuppressLint("StaticFieldLeak")
    private class setAppPromotion extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
           return doInBackgroundTask();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setConnection(s,false);

        }
    }

    protected void setConnection(String values,boolean isThread) {

        try {

            JSONObject jsonAdObject = new JSONObject(values);
            JSONArray jsonApps = jsonAdObject.getJSONArray(AppsConfig.appPromote);

            for (int j = 0; j < jsonApps.length(); j++) {

                JSONObject object = jsonApps.getJSONObject(j);

                String name = object.getString(AppsConfig.appName);
                String icons = object.getString(AppsConfig.appIcons);
                String downloads = object.getString(AppsConfig.appShortDescription);
                String packageName = object.getString(AppsConfig.appPackage);
                String appPreview = object.getString(AppsConfig.appPreview);

                //check if is app Already exist :
                if (!isAppsAdded(name, icons, downloads, packageName,appPreview)){
                    //added the values to data base :
                    databaseHelper.add(name, icons, downloads, packageName,appPreview);
                    //generate the numbers of downloads outside database :
                    generateCounter(j);

                    //get the screenshot from json array :
                    JSONArray screenArray = object.getJSONArray(AppsConfig.screenShot);

                    String screenLink = "";
                    for (int i = 0; i < screenArray.length(); ++i) {
                        String screen = screenArray.getString(i);
                        screenLink = screenLink + screen + ",";
                    }
                    dataScreen.add(j,screenLink);


                }else {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "this items is already added before.");
                    }
                }
            }

            if (onConnectedListener != null) {
                onConnectedListener.onAppConnected();
            }

        } catch (JSONException e) {
            if (onConnectedListener != null) {
                onConnectedListener.onAppFailed(e.getMessage());
            }

            //reconnect again :
            try {
                if (checkConnection()) {


                    try {
                        if (BuildConfig.DEBUG) {
                            setLog("reconnecting in 5 second !");
                        }
                        new CountDownTimer(1000 * 5, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if (BuildConfig.DEBUG) {
                                    setLog(""+millisUntilFinished/1000);
                                }
                            }

                            @Override
                            public void onFinish() {
                                if (BuildConfig.DEBUG) {
                                    setLog("Execute Connection...");
                                }
                                if (isThread){
                                    setAppPromotionThread();
                                }else {
                                    new setAppPromotion().execute();
                                }
                            }
                        }.start();
                    } catch (Exception countError) {
                        countError.printStackTrace();
                    }

                } else {
                    if (onConnectedListener != null) {
                        onConnectedListener.onAppFailed("Connecting stopped = user turn-off the connection.");
                    }
                }

            } catch (Exception e2) {
                if (onConnectedListener != null) {
                    onConnectedListener.onAppFailed(e2.getMessage());
                }
            }

        }

    }


    private void generateCounter(int position){
        if (promotePrefs != null){
            promotePrefs.setDownloadCounter(position,generateDownloads());
            promotePrefs.setRateCounter(position,generateRating());
            promotePrefs.setRatePeople(position,generatePeople());
        }
    }

    private String generateDownloads() {
        try {
            Random random = new Random();
            int index = random.nextInt(AppsConfig.downloadCount.length);
            return AppsConfig.downloadCount[index];
        } catch (Exception e) {
            return AppsConfig.downloadCount[0];
        }
    }
    private float generateRating() {
        try {
            Random random = new Random();
            int index = random.nextInt(AppsConfig.ratingCount.length);
            return AppsConfig.ratingCount[index];
        } catch (Exception e) {
            return AppsConfig.ratingCount[0];
        }
    }

    private String generatePeople() {
        try {
            Random random = new Random();
            int index = random.nextInt(10);
            return index+" thousand";
        } catch (Exception e) {
            return "6 thousand";
        }
    }

    private boolean isAppsAdded(String names, String icon, String downloads, String packageName,String appPreview) {
        boolean result = false;
        ArrayList<AppModels> models = databaseHelper.getAllApps();
        if (models != null) {
            for (int i = 0; i < models.size(); i++) {
                if ((models.get(i)).getAllModels().equals(names + icon + downloads + packageName+appPreview)) {
                    result = true;
                }
            }
        }
        return result;
    }

    private void setLog(String log){
        Log.d(TAG, log);
    }

    private String doInBackgroundTask(){

        File file = new File(context.getFilesDir().getPath() + "/" + ConnectionAdOff);
        if (checkConnection()) {

            try {
                url = new URL(adLink);
            } catch (MalformedURLException e) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "URL Malformed cause : " + e.getMessage());
                }
            }

            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");

            } catch (IOException e1) {
                return e1.toString();
            }

            try {

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    return buffToString(new InputStreamReader(inputStream), true);

                } else {

                    if (file.exists()) {
                        return buffToString(new FileReader(file), false);
                    }

                }
            } catch (IOException e2) {
                return e2.toString();
            } finally {
                connection.disconnect();
            }

        } else {

            try {
                return buffToString(new FileReader(file), false);
            } catch (IOException e) {
                return e.toString();
            }
        }
        return "";
    }

    private String buffToString(Reader ourReader, boolean save) {
        try {
            BufferedReader reader = new BufferedReader(ourReader);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            if (save) {
                if (!result.toString().equals(null)) {
                    writeJsonToFile(result.toString());
                }
            }

            return (result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private void writeJsonToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(ConnectionAdOff, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkConnection() {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }

            if (activeNetworkInfo.isConnectedOrConnecting()) {

                return true;
            }
        }
        return false;
    }

    public void setOnPromoteConnected(OnConnectedListener onConnectedListener) {
        this.onConnectedListener = onConnectedListener;
    }

}
