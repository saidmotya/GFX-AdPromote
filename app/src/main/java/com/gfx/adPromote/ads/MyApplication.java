package com.gfx.adPromote.ads;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        AppPromote.initializePromote(getApplicationContext(),GFX.AdPromoteLink);
        AppPromote.setOnPromoteListener(new OnPromoteListener() {
            @Override
            public void onInitializeSuccessful() {
                GFX.setLog("AppPromote onInitializeSuccessful");
            }

            @Override
            public void onInitializeFailed(String error) {
                GFX.setLog("AppPromote onInitializeFailed : "+error);
            }
        });
         */

    }


}
