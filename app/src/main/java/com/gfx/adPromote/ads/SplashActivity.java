package com.gfx.adPromote.ads;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.gfx.adPromote.AppPromote;
import com.gfx.adPromote.Interfaces.OnPromoteAppWithYoutubeListener;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        AppPromote.initializeAppWithYoutube(getApplicationContext(),GFX.AppWithYoutubePromo);
        AppPromote.setOnPromoteAppWithYoutubeListener(new OnPromoteAppWithYoutubeListener() {
            @Override
            public void onAppWithYoutubeInitializeSuccessful() {
                GFX.setLog("initializeAppPromote onInitializeSuccessful");
                startActivity(false);
            }

            @Override
            public void onAppWithYoutubeInitializeFailed(String error) {
                GFX.setLog("initializeAppWithYoutube onInitializeFailed : "+error);
                startActivity(true);
            }
        });


        /*
        AppPromote.initializeYoutubePromote(this,GFX.YoutubePromo);
        AppPromote.setOnPromoteYoutubeListener(new OnPromoteYoutubeListener() {
            @Override
            public void onYoutubeInitializeSuccessful() {
                GFX.setLog("initializeYoutube onInitializeSuccessful");
                startActivity(false);
            }

            @Override
            public void onYoutubeInitializeFailed(String error) {
                GFX.setLog("initializeYoutube onInitializeFailed : "+error);
                startActivity(true);
            }
        });

         */

        /*
        AppPromote.initializeAppPromote(this,GFX.AppPromo);
        AppPromote.setOnPromoteListener(new OnPromoteAppListener() {
            @Override
            public void onInitializeSuccessful() {
                GFX.setLog("initializeYoutube onInitializeSuccessful");
                startActivity(false);
            }

            @Override
            public void onInitializeFailed(String error) {
                GFX.setLog("initializeAppPromote onInitializeFailed : "+error);
                startActivity(true);
            }
        });
         */


    }

    private void startActivity(boolean useTime){
        if (useTime){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            },1000*3);
        }else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }


}
