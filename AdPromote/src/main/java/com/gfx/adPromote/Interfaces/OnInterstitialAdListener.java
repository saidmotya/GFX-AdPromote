package com.gfx.adPromote.Interfaces;

public interface OnInterstitialAdListener {
    void onInterstitialAdLoaded();
    void onInterstitialAdClosed();
    void onInterstitialAdClicked();
    void onInterstitialAdFailedToLoad(String error);
}
