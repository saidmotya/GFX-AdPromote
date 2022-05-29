package com.gfx.adPromote.Interfaces;

public interface OnYoutubeBannerListener {
    void onYoutubeBannerAdLoaded();
    void onYoutubeBannerAdClicked();
    void onYoutubeBannerAdFailedToLoad(String error);
}
