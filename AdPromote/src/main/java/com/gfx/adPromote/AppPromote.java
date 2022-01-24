package com.gfx.adPromote;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.gfx.adPromote.ConnectionAd.ConnectionPromote;
import com.gfx.adPromote.Interfaces.OnConnectedListener;
import com.gfx.adPromote.Interfaces.OnPromoteListener;
/**
 * Created by SAID MOTYA on 01/01/2022.
 * contact on Facebook : https://web.facebook.com/motya.said
 * This library created specially for SecretGFX group & it free to used.
 */
public class AppPromote {

    public static OnPromoteListener onPromoteListener ;


    public static void initializePromote(Context context,String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(context,AdLink);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteListener != null){
                onPromoteListener.onInitializeFailed("initializePromote failed cause : link is empty.");
            }

        }

   }

    public static void initializePromote(Activity activity, String AdLink){
        if (!TextUtils.isEmpty(AdLink)){

            ConnectionPromote connectionPromote = new ConnectionPromote(activity,AdLink);
            connectionPromote.setOnPromoteConnected(new OnConnectedListener() {
                @Override
                public void onAppConnected() {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeSuccessful();
                    }
                }

                @Override
                public void onAppFailed(String error) {
                    if (onPromoteListener != null){
                        onPromoteListener.onInitializeFailed(error);
                    }
                }
            });
        }else {
            if (onPromoteListener != null){
                onPromoteListener.onInitializeFailed("initializePromote failed cause : link is empty.");
            }

        }

    }


    public static void setOnPromoteListener(OnPromoteListener onPromoteListener) {
        AppPromote.onPromoteListener = onPromoteListener;
    }
}
