package com.gfx.adPromote.Interfaces;

public interface OnYoutubeInterstitialListener {
    void onYoutubeInterstitialLoaded();
    void onYoutubeInterstitialClosed();
    void onYoutubeInterstitialClicked(String type);
    void onYoutubeInterstitialFailedToLoad(String error);
}
