package com.gfx.adPromote.Interfaces;

public interface OnBannerListener {
    void onBannerAdLoaded();
    void onBannerAdClicked();
    void onBannerAdFailedToLoad(String error);
}
