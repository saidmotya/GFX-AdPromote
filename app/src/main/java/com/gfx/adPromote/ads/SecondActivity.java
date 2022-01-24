package com.gfx.adPromote.ads;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gfx.adPromote.BannerPromote;
import com.gfx.adPromote.Interfaces.OnBannerListener;
import com.gfx.adPromote.InterstitialPromote;


public class SecondActivity extends AppCompatActivity {

    private BannerPromote bannerPromote;

    private InterstitialPromote interstitialPromote;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),MainActivity.class));
               finish();

            }
        });


        bannerPromote = findViewById(R.id.banner_view);
        bannerPromote.setOnBannerListener(new OnBannerListener() {
            @Override
            public void onBannerAdLoaded() {
                GFX.setLog("banner loaded.");
            }

            @Override
            public void onBannerAdClicked() {
                GFX.setLog("banner clicked.");

            }

            @Override
            public void onBannerAdFailedToLoad(String error) {
                GFX.setLog("banner failed to load : "+error);

            }
        });



    }


}