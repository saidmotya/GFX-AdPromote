package com.gfx.adPromote.ads;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gfx.adPromote.Interfaces.OnAdClosed;
import com.gfx.adPromote.Interfaces.OnYoutubeInterstitialListener;
import com.gfx.adPromote.InterstitialYoutube;
import com.gfx.adPromote.YoutubeStyle;


public class SecondActivity extends AppCompatActivity {

    private InterstitialYoutube interstitialYoutube;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        interstitialYoutube = new InterstitialYoutube(this, YoutubeStyle.Normal);
        //interstitialYoutube.setTimer(10);
        interstitialYoutube.setRadiusButtonOpenAd(5);
        interstitialYoutube.setDescriptionTitle("Hello world ! this a short description of what i'm talking about");
        //interstitialYoutube.setOpenAdColor("#FF9800");
        interstitialYoutube.setOnYoutubeInterstitialListener(new OnYoutubeInterstitialListener() {
            @Override
            public void onYoutubeInterstitialLoaded() {
                GFX.setLog("interstitialYoutube loaded.");
            }

            @Override
            public void onYoutubeInterstitialClosed() {
                GFX.setLog("interstitialYoutube closed.");
            }

            @Override
            public void onYoutubeInterstitialClicked(String type) {
                GFX.setLog("interstitialYoutube clicked with type : " + type);
            }

            @Override
            public void onYoutubeInterstitialFailedToLoad(String error) {
                GFX.setLog("interstitialYoutube failed to loading  : " + error);

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interstitialYoutube.show();
                interstitialYoutube.setOnAdClosed(new OnAdClosed() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });


            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}