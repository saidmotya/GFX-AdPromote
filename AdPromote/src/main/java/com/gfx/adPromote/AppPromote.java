package com.gfx.adPromote;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.gfx.adPromote.ConnectionAd.ConnectionPromote;
import com.gfx.adPromote.ConnectionAd.TaskType;
import com.gfx.adPromote.Interfaces.OnConnectedListener;
import com.gfx.adPromote.Interfaces.OnPromoteAppListener;
import com.gfx.adPromote.Interfaces.OnPromoteAppWithYoutubeListener;
import com.gfx.adPromote.Interfaces.OnPromoteYoutubeListener;

/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class AppPromote {

    public static OnPromoteAppListener onPromoteAppListener;
    public static OnPromoteYoutubeListener onPromoteYoutubeListener;
    public static OnPromoteAppWithYoutubeListener onPromoteAppWithYoutubeListener;


    public static void initializeAppPromote(Context context,String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(context,AdLink, TaskType.AppsPromote);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteAppListener != null){
                        onPromoteAppListener.onInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteAppListener != null){
                        onPromoteAppListener.onInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteAppListener != null){
                onPromoteAppListener.onInitializeFailed("initializePromote failed cause : link is empty.");
            }

        }

   }

    public static void initializeAppPromote(Activity activity, String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(activity,AdLink,TaskType.AppsPromote);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteAppListener != null){
                        onPromoteAppListener.onInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteAppListener != null){
                        onPromoteAppListener.onInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteAppListener != null){
                onPromoteAppListener.onInitializeFailed("initializePromote failed cause : link is empty.");
            }

        }

    }


    public static void initializeYoutubePromote(Context context,String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(context,AdLink,TaskType.YoutubePromote);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteYoutubeListener != null){
                        onPromoteYoutubeListener.onYoutubeInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteYoutubeListener != null){
                        onPromoteYoutubeListener.onYoutubeInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteYoutubeListener != null){
                onPromoteYoutubeListener.onYoutubeInitializeFailed("initializeYoutubePromote failed cause : link is empty.");
            }

        }

    }

    public static void initializeYoutubePromote(Activity activity, String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(activity,AdLink,TaskType.YoutubePromote);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteYoutubeListener != null){
                        onPromoteYoutubeListener.onYoutubeInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteYoutubeListener != null){
                        onPromoteYoutubeListener.onYoutubeInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteYoutubeListener != null){
                onPromoteYoutubeListener.onYoutubeInitializeFailed("initializeYoutubePromote failed cause : link is empty.");
            }

        }

    }


    public static void initializeAppWithYoutube(Context context,String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(context,AdLink,TaskType.AppsPromoteWithYoutube);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteAppWithYoutubeListener != null){
                        onPromoteAppWithYoutubeListener.onAppWithYoutubeInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteAppWithYoutubeListener != null){
                        onPromoteAppWithYoutubeListener.onAppWithYoutubeInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteAppWithYoutubeListener != null){
                onPromoteAppWithYoutubeListener.onAppWithYoutubeInitializeFailed("initializeAppWithYoutube failed cause : link is empty.");
            }

        }

    }

    public static void initializeAppWithYoutube(Activity activity, String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(activity,AdLink,TaskType.AppsPromoteWithYoutube);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteAppWithYoutubeListener != null){
                        onPromoteAppWithYoutubeListener.onAppWithYoutubeInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteAppWithYoutubeListener != null){
                        onPromoteAppWithYoutubeListener.onAppWithYoutubeInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteAppWithYoutubeListener != null){
                onPromoteAppWithYoutubeListener.onAppWithYoutubeInitializeFailed("initializeAppWithYoutube failed cause : link is empty.");
            }

        }

    }


    public static void setOnPromoteListener(OnPromoteAppListener onPromoteAppListener) {
        AppPromote.onPromoteAppListener = onPromoteAppListener;
    }

    public static void setOnPromoteYoutubeListener(OnPromoteYoutubeListener onPromoteYoutubeListener) {
        AppPromote.onPromoteYoutubeListener = onPromoteYoutubeListener;
    }

    public static void setOnPromoteAppWithYoutubeListener(OnPromoteAppWithYoutubeListener onPromoteAppWithYoutubeListener) {
        AppPromote.onPromoteAppWithYoutubeListener = onPromoteAppWithYoutubeListener;
    }
}
