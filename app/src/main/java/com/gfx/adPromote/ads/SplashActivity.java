package com.gfx.adPromote.ads;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.gfx.adPromote.AppPromote;
import com.gfx.adPromote.Interfaces.OnPromoteListener;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        AppPromote.initializePromote(this,GFX.AdPromoteLink);
        AppPromote.setOnPromoteListener(new OnPromoteListener() {
            @Override
            public void onInitializeSuccessful() {
                GFX.setLog("AppPromote onInitializeSuccessful");
                startActivity(false);
            }

            @Override
            public void onInitializeFailed(String error) {
                GFX.setLog("AppPromote onInitializeFailed : "+error);
                startActivity(true);
            }
        });




    }

    private void startActivity(boolean useTime){
        if (useTime){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            },1000*3);
        }else {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

    }


}
