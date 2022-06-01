package com.gfx.adPromote.Config;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class AppsConfig {

    public static String appPromote = "AppsPromote";
    public static String appName = "name";
    public static String appIcons = "icon";
    public static String appShortDescription = "shortDescription";
    public static String appPackage = "packageName";
    public static String appPreview = "preview";
    public static String screenShot = "screenShot";

    public static String youtubePromote = "YoutubePromote";
    public static String youtubeTitle = "title";
    public static String youtubeIcon = "icon";
    public static String youtubePreview = "preview";
    public static String youtubePreviewSmall = "preview-small";
    public static String youtubeWatch = "watch";
    public static String youtubeChannel = "channel";
    public static String youtubeDescription = "Description";


    public static String[] downloadCount = new String[]{
            "100 K",
            "1 Million",
            "5 Million",
            "500 K",
            "10 Million",
            "10 K",
            "50 Million",
            "50 K"};

    public static float[] ratingCount = new float[]{
            4.0f,
            4.3f,
            4.5f,
            4.7f,
            4.8f,
            4.9f,
            5.0f,
    };

    public static void openAdLink(Context context, String link) {

        if (!TextUtils.isEmpty(link) && link != null) {

            if (link.startsWith("http")) {
                //open in internet app :
                openOnInternet(context, link);
            } else {
                // open app with his package name in google play store.
                openOnGooglePlayStore(context, link);
            }

        } else {
            setToast(context, "Failed to get Ad link.");
        }
    }

    public static void openOnGooglePlayStore(Context context, String packageName) {
        try {
            String GGMarket = "market://details?id=";
            Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri.parse(GGMarket + packageName));
            intentMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentMarket.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                intentMarket.setPackage("com.android.vending");
            } catch (Exception v) {
                v.printStackTrace();
            }
            context.startActivity(intentMarket);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            String GG_APPS = "https://play.google.com/store/apps/details?id=";
            Intent intentStore = new Intent(Intent.ACTION_VIEW, Uri.parse(GG_APPS + packageName));
            intentStore.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentStore.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                intentStore.setPackage("com.android.vending");
            } catch (Exception v) {
                v.printStackTrace();
            }
            if (isIntentAvailable(context, intentStore)) {
                context.startActivity(intentStore);
            } else {
                setToast(context, "Failed to open Ad.");
            }

        }
    }

    public static void openOnInternet(Context context, String link) {
        try {
            Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intentMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentMarket.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentMarket);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            setToast(context, "Failed to open Ad.");

        }
    }


    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void youtubeSubscribeChannel(Context context, String channelId) {
        try {
            Uri channel = Uri.parse("https://www.youtube.com/" + channelId + "?sub_confirmation=1");
            Intent intent = new Intent(Intent.ACTION_VIEW, channel);
            intent.setPackage(youtubePackage(context.getApplicationContext()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {
            setToast(context, "Failed to open Channel.");
        }

    }

    public static void youtubeWatchVideo(Context context, String videoLink) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
            intent.setPackage(youtubePackage(context.getApplicationContext()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intentE = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
                intentE.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentE.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentE);
            } catch (Exception ee) {
                setToast(context, "Failed to open Channel.");
            }

        }

    }

    public static String youtubePackage(Context context) {
        PackageManager manager;
        if (isLeanback(manager = context.getPackageManager())) {
            return "com.google.android.youtube.tv";
        } else {
            return isAndroidTv(manager) ? "com.google.android.youtube.googletv" : "com.google.android.youtube";
        }
    }

    private static boolean isLeanback(PackageManager packageManager) {
        return packageManager.hasSystemFeature("android.software.leanback");
    }

    private static boolean isAndroidTv(PackageManager packageManager) {
        return packageManager.hasSystemFeature("com.google.android.tv");
    }


    public static void setLog(String log) {
        Log.d("adPromote", log);
    }

    public static void setToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }


}
