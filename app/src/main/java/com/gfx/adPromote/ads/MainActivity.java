package com.gfx.adPromote.ads;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gfx.adPromote.BannerPromote;
import com.gfx.adPromote.Interfaces.OnAdClosed;
import com.gfx.adPromote.Interfaces.OnBannerListener;
import com.gfx.adPromote.Interfaces.OnInterstitialAdListener;
import com.gfx.adPromote.Interfaces.OnNativeListener;
import com.gfx.adPromote.InterstitialPromote;
import com.gfx.adPromote.InterstitialStyle;
import com.gfx.adPromote.NativePromote;


public class MainActivity extends AppCompatActivity {

    private BannerPromote bannerPromote;
    private InterstitialPromote interstitialPromote;
    private NativePromote nativePromote;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildInterstitial();
        buildBanner();
        buildNative();

        findViewById(R.id.start_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if the interstitial is loaded yet.
                if (interstitialPromote.isAdLoaded()) {

                    interstitialPromote.show(); //call showing interstitial
                    //when user close the interstitial ad, start the next activity.
                    interstitialPromote.setOnAdClosed(new OnAdClosed() {
                        @Override
                        public void onAdClosed() {
                            //your intent
                            startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                            finish();
                        }
                    });
                } else {
                    //your intent : when ad is not ready yet.
                    startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                    finish();
                }

            }
        });



    }

    private void buildInterstitial(){
        interstitialPromote = new InterstitialPromote(MainActivity.this);
        interstitialPromote.setStyle(InterstitialStyle.Advance);
        interstitialPromote.setInstallColor(R.color.my_color); //color of button from resource.
        //interstitialAd.setInstallColor("#E91E63"); //color of button from string.
        interstitialPromote.setTimer(5);//5 second to closed the Ad.
        interstitialPromote.setInstallTitle("Play");
        interstitialPromote.setRadiusButton(10); //corner of button radius.
        interstitialPromote.setOnInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialAdLoaded() {
                GFX.setLog("interstitialAd loaded.");
            }

            @Override
            public void onInterstitialAdClosed() {
                GFX.setLog("interstitialAd closed.");
            }

            @Override
            public void onInterstitialAdClicked() {
                GFX.setLog("interstitialAd clicked.");
            }

            @Override
            public void onInterstitialAdFailedToLoad(String error) {
                GFX.setLog("Interstitial failed to load : " + error);
            }
        });

    }

    private void buildBanner(){
        bannerPromote = findViewById(R.id.banner_view);
        //bannerPromote.setInstallTitle("Play"); //Banner title button
        //bannerPromote.setInstallColor("#FFC107"); //Title button color with param String or Resource color
        //bannerPromote.setDescriptionColor(R.color.my_color_description);//Description Text color with param String or Resource color
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
                GFX.setLog("banner failed to load : " + error);

            }
        });

    }

    private void buildNative(){
        nativePromote = findViewById(R.id.native_ad);
        nativePromote.setButtonColor("#1C7DCA");
        nativePromote.setButtonTitle("Play Now");
        nativePromote.setRadiusButton(10);  //corner of button radius.
        nativePromote.setOnNativeListener(new OnNativeListener() {
            @Override
            public void onNativeAdLoaded() {
                GFX.setLog("Native loaded.");
            }

            @Override
            public void onNativeAdClicked() {
                GFX.setLog("Native clicked.");
            }

            @Override
            public void onNativeAdFailedToLoad(String error) {
                GFX.setLog("Native failed to load  : "+error);
            }
        });
    }


}