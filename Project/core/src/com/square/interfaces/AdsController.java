package com.square.interfaces;

public interface AdsController {

    public void showInterstitialAd(Runnable then);

    public void showBannerAd();

    public void hideBannerAd();

}
