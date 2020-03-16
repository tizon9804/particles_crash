package com.square.interfaces;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by Tizon on 31/12/2015.
 */
public interface IaBilling {

    // (arbitrary) request code for the purchase flow
    public String SKU_REMOVE_ADS = "remove_particles_ads";
    public String COIN_550 = "coin_550";
    public String COIN_2000 = "coin_2000";
    public String COIN_7000 = "coin_7000";
    public String COIN_26000 = "coin_26000";
    public String COIN_140000 = "coin_140000";
    public String COIN_800000 = "coin_800000";
    public String[] purchases = new String[]{SKU_REMOVE_ADS, COIN_550, COIN_2000, COIN_7000, COIN_26000, COIN_140000, COIN_800000};
    static final int RC_REQUEST = 10001;

    public void removeAds();

    public void buyCoins(String value);

    public Array getInventoryPrices();

    public void processPurchases();

    public boolean isAdsRemoved();

    public void setIsAdsRemoved(boolean val);

    public ArrayList getPurchasedList();


}
