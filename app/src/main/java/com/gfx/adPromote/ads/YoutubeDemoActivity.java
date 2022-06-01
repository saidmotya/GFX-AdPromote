package com.gfx.adPromote.ads;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gfx.adPromote.Interfaces.OnAdClosed;
import com.gfx.adPromote.Interfaces.OnYoutubeInterstitialListener;
import com.gfx.adPromote.InterstitialYoutube;
import com.gfx.adPromote.YoutubeStyle;


public class YoutubeDemoActivity extends AppCompatActivity {

    private InterstitialYoutube interstitialYoutube1;
    private InterstitialYoutube interstitialYoutube2;
    
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_demo);

        interstitialYoutube1 = new InterstitialYoutube(this, YoutubeStyle.Normal);
        interstitialYoutube1.setTimer(5);
        interstitialYoutube1.setRadiusButtonOpenAd(5);
        interstitialYoutube1.setOnYoutubeInterstitialListener(new OnYoutubeInterstitialListener() {
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


        interstitialYoutube2 = new InterstitialYoutube(this, YoutubeStyle.Advance);
        interstitialYoutube2.setTimer(5);
        interstitialYoutube2.setRadiusButtonOpenAd(5);
        interstitialYoutube2.setOnYoutubeInterstitialListener(new OnYoutubeInterstitialListener() {
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



        findViewById(R.id.interstitialYoutube1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialYoutube1.show();
                interstitialYoutube1.setOnAdClosed(new OnAdClosed() {
                    @Override
                    public void onAdClosed() {
                        Toast.makeText(getApplicationContext(),"Interstitial Style 1 Closed !",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        findViewById(R.id.interstitialYoutube2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialYoutube2.show();
                interstitialYoutube2.setOnAdClosed(new OnAdClosed() {
                    @Override
                    public void onAdClosed() {
                        Toast.makeText(getApplicationContext(),"Interstitial Style 2 Closed !",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

}