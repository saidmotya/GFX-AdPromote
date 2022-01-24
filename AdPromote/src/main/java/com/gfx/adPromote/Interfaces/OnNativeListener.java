package com.gfx.adPromote.Interfaces;

public interface OnNativeListener {
    void onNativeAdLoaded();
    void onNativeAdClicked();
    void onNativeAdFailedToLoad(String error);
}
