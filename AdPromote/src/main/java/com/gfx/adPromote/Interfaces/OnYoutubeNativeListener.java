package com.gfx.adPromote.Interfaces;

public interface OnYoutubeNativeListener {
    void onYoutubeNativeAdLoaded();
    void onYoutubeNativeAdClicked(String type);
    void onYoutubeNativeAdFailedToLoad(String error);
}
