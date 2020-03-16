package com.square.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.vending.billing.utilPay.IabHelper;
import com.android.vending.billing.utilPay.IabResult;
import com.android.vending.billing.utilPay.Inventory;
import com.android.vending.billing.utilPay.Purchase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.GameHelper;
import com.square.config.GameConfig;
import com.square.interfaces.AdsController;
import com.square.interfaces.GooPlayGames;
import com.square.interfaces.IaBilling;
import com.square.persistence.Persistence;
import com.square.screen.ParticlesGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class AndroidLauncher extends AndroidApplication implements AdsController, GooPlayGames, IaBilling {


    private final static int REQUEST_CODE_UNUSED = 9002;

    private InterstitialAd interstitialAd;
    private AdView bannerAd;
    /**
     * Conection to google play games
     */

    private GameHelper gameHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App I ndexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;
    private String version;
    private IabHelper mHelper;
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener;

    private AdRequest.Builder builder;
    private AdRequest ad;
    private Json json;
    private Array<Object> listPrices;
    public ArrayList<Integer> purchased;


    // Assistant to work with gaming services


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        version = getString(R.string.version);
        purchased = new ArrayList();
        json =  new Json();
        setupAds();
        initializeGamePlay();
        initializeInappBilling();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // Create a gameView and a bannerAd AdView
        View gameView = initializeForView(new ParticlesGame(this, this, this), config);
        // Define the layout
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(bannerAd, params);
        setContentView(layout);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initializeInappBilling() {
        String base64EncodedPublicKey = getString(R.string.app_64id);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Gdx.app.log("IAB", "problem to setting up in-app billing:" + result);
                }
                Gdx.app.log("IAB", "Billing success:" + result);
                processPurchases();
            }
        });
        // Callback for when a purchase is finished
        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                if (purchase == null) return;
                Gdx.app.log("IAB", "Purchase finished: " + result + ", purchase: " + purchase);

                // if we were disposed of in the meantime, quit.
                if (mHelper == null) return;

                if (result.isFailure()) {
                    //complain("Error purchasing: " + result);
                    //setWaitScreen(false);
                    return;
                }

                Gdx.app.log("IAB", "Purchase successful.");
                for(int i=0;i<purchases.length;i++) {
                    if (purchase.getSku().equals(purchases[i])) {
                        // bought the premium upgrade!
                        Gdx.app.log("IAB", "Purchase is premium upgrade. Congratulating user.");

                        // Do what you want here maybe call your game to do some update
                        //
                        // Maybe set a flag to indicate that ads shouldn't show anymore
                        purchased.set(i,1);
                    }
                }
            }
        };

        // Listener that's called when we finish querying the items and subscriptions we own
        mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                Gdx.app.log("IAB", "Query inventory finished.");
                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Is it a failure?
                if (result.isFailure()) {
                    // handle failure here
                    return;
                }
                try {
                    Gdx.app.log("IAB", "waiting: " + inventory.toString());
                    listPrices = new Array<>();
                    String price = inventory.getSkuDetails(COIN_550).getPrice();
                    listPrices.add(price + ":" + COIN_550);
                    price = inventory.getSkuDetails(COIN_2000).getPrice();
                    listPrices.add(price + ":" + COIN_2000);
                    price = inventory.getSkuDetails(COIN_7000).getPrice();
                    listPrices.add(price + ":" + COIN_7000);
                    price = inventory.getSkuDetails(COIN_26000).getPrice();
                    listPrices.add(price + ":" + COIN_26000);
                    price = inventory.getSkuDetails(COIN_140000).getPrice();
                    listPrices.add(price + ":" + COIN_140000);
                    price = inventory.getSkuDetails(COIN_800000).getPrice();
                    listPrices.add(price + ":" + COIN_800000);
                    Gdx.app.log("IAB", "loading prices: " + listPrices.size);
                }
                catch (Exception e){
                    Gdx.app.log("IAB", "#########################error loading prices: " + listPrices.size);
                }
                // Do we have the premium upgrade?

                for(int i=0;i<purchases.length;i++){
                    purchased.add(inventory.getPurchase(purchases[i])!= null?1:0);
                }
                Gdx.app.log("IAB", "remove add purchased is: " + purchases[0]);
            }
        };
    }

    private void initializeGamePlay() {
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            /**
             * Called when sign-in fails. As a result, a "Sign-In" button can be
             * shown to the user; when that button is clicked, call
             *
             * @link{GamesHelper#beginUserInitiatedSignIn . Note that not all calls
             * to this method mean an
             * error; it may be a result
             * of the fact that automatic
             * sign-in could not proceed
             * because user interaction
             * was required (consent
             * dialogs). So
             * implementations of this
             * method should NOT display
             * an error message unless a
             * call to @link{GamesHelper#
             * hasSignInError} indicates
             * that an error indeed
             * occurred.
             */
            @Override
            public void onSignInFailed() {

            }

            /**
             * Called when sign-in succeeds.
             */
            @Override
            public void onSignInSucceeded() {

            }
        };
        gameHelper.setup(gameHelperListener);
    }

    public void setupAds() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.intersticial_unit_id));
        builder = new AdRequest.Builder();
        ad = builder.build();
        interstitialAd.loadAd(ad);
        bannerAd = new AdView(this);
        bannerAd.setVisibility(View.INVISIBLE);
        bannerAd.setBackgroundColor(Color.WHITE); // white
        bannerAd.setAdUnitId(getString(R.string.banner_menu_unit_id));
        bannerAd.setAdSize(AdSize.SMART_BANNER);
        bannerAd.loadAd(ad);
    }

    @Override
    public void showInterstitialAd(final Runnable then) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (then != null) {
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Gdx.app.postRunnable(then);
                            AdRequest ad = builder.build();
                            interstitialAd.loadAd(ad);
                        }
                    });
                }
                interstitialAd.show();
            }
        });
    }

    @Override
    public void showBannerAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideBannerAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Check the status of the user input
     */
    @Override
    public boolean getSignedInGPGS() {
        return gameHelper.isSignedIn();
    }

    /**
     * Log
     */
    @Override
    public void loginGPGS() {
        if (isNetworkConnected()) {
            try {
                Gdx.app.log("trying to log in", "Android Launcher");
                runOnUiThread(new Runnable() {
                    //@Override
                    public void run() {
                        gameHelper.beginUserInitiatedSignIn();
                    }
                });
            } catch (Exception e) {
                Gdx.app.log("AndroidLauncher", "Log in failed: " + e.getMessage() + ".");
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    @Override
    public void logout() {
        try {
            runOnUiThread(new Runnable() {
                //@Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("AndroidLauncher", "Log out failed: " + e.getMessage() + ".");
        }
    }


    /**
     * Send the result in the high score table
     *
     * @param score
     */
    @Override
    public void submitScoreGPGS(long score) {
        if (getSignedInGPGS()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
        } else {
            loginGPGS();
        }
    }

    @Override
    public void loadScoreOfLeaderBoard() {
        try {
            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(gameHelper.getApiClient(), getString(R.string.leaderboard_id), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
                @Override
                public void onResult(final Leaderboards.LoadPlayerScoreResult scoreResult) {
                    if (isScoreResultValid(scoreResult)) {
                        // here you can get the score like this
                        long points = scoreResult.getScore().getRawScore();
                        Gdx.app.log("loadScoreOfLeaderBoard", points + "#");
                        Json json = new Json();
                        Persistence.writeFile(json.toJson(points), "cloudbest");
                    }
                }
            });
        } catch (Exception e) {
            Gdx.app.log("Error to connect", "load score");
        }
    }

    @Override
    public void submitEvent(String eventId,int increment) {
        // increment the event counter
        int resID = getResources().getIdentifier(eventId,
                "string", getPackageName());
        final String id = getString(resID);
        Gdx.app.log("AndroidLauncher", "Event.." + eventId + "");
        Games.Events.increment(gameHelper.getApiClient(), id, increment);
    }

    @Override
    public String getVersionGame() {
        return version;
    }

    @Override
    public void submitRicherGPGS(long totalMonedas) {
        if (getSignedInGPGS()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.leaderboard_richer_id), totalMonedas);
        } else {
            loginGPGS();
        }
    }

    @Override
    public void loadRicherOfLeaderBoard() {
        try {
            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(gameHelper.getApiClient(), getString(R.string.leaderboard_richer_id), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
                @Override
                public void onResult(final Leaderboards.LoadPlayerScoreResult scoreResult) {
                    if (isScoreResultValid(scoreResult)) {
                        // here you can get the score like this
                        long points = scoreResult.getScore().getRawScore();
                        Gdx.app.log("loadRicherOfLeaderBoard", points + "#");
                        Json json = new Json();
                        Persistence.writeFile(json.toJson(points), "cloud"+GameConfig.nameMonedas);
                    }
                }
            });
        } catch (Exception e) {
            Gdx.app.log("Error to connect richer ", "load score richer");
        }
    }

    private boolean isScoreResultValid(final Leaderboards.LoadPlayerScoreResult scoreResult) {

        return scoreResult != null && GamesStatusCodes.STATUS_OK == scoreResult.getStatus().getStatusCode() && scoreResult.getScore() != null;
    }

    /**
     * Unlock achievement
     * <p/>
     * param AchievementId
     * ID achievements. Taken from the file games-ids.xml
     *
     * @param achievementId
     */
    @Override
    public void unlockAchievementGPGS(String achievementId) {
        try {
            // Open achievement with ID achievementId
            if (!achievementId.equals("no")) {
                int resID = getResources().getIdentifier(achievementId,
                        "string", getPackageName());
                final String id = getString(resID);
                Games.Achievements.load(gameHelper.getApiClient(), false).setResultCallback(new ResultCallback<Achievements.LoadAchievementsResult>() {
                    @Override
                    public void onResult(Achievements.LoadAchievementsResult loadAchievementsResult) {
                        Achievement ach;
                        AchievementBuffer aBuffer = loadAchievementsResult.getAchievements();
                        Iterator<Achievement> aIterator = aBuffer.iterator();
                        while (aIterator.hasNext()) {
                            ach = aIterator.next();
                            //Gdx.app.log("AndroidLauncher", id + "$$" + ach.getAchievementId() + "##" + ach.getState());
                            if (id.equals(ach.getAchievementId())) {
                                if (ach.getState() == Achievement.STATE_REVEALED) {
                                    Gdx.app.log("AndroidLauncher", "Unlocking.." + ach.getAchievementId() + "");
                                    Games.Achievements.unlock(gameHelper.getApiClient(), id);
                                }
                                if (ach.getState() == Achievement.STATE_HIDDEN) {
                                    Gdx.app.log("AndroidLauncher", "Unlocking hidden.." + ach.getAchievementId() + "");
                                    Games.Achievements.unlock(gameHelper.getApiClient(), id);
                                }
                                aBuffer.close();
                                break;
                            }
                        }
                    }
                });

            }
        } catch (Exception e) {
            Gdx.app.log("Error to connect", "unlock achievment");
        }

    }

    /**
     * Unlock achievement
     * <p/>
     * param AchievementId
     * ID achievements. Taken from the file games-ids.xml
     *
     * @param achievementId
     */
    @Override
    public void incrementAchievementGPGS(final String achievementId, final int increment) {
        try {
            // Open achievement with ID achievementId
            if (!achievementId.equals("no")) {
                int resID = getResources().getIdentifier(achievementId,
                        "string", getPackageName());
                final String id = getString(resID);
                Games.Achievements.load(gameHelper.getApiClient(), false).setResultCallback(new ResultCallback<Achievements.LoadAchievementsResult>() {
                    @Override
                    public void onResult(Achievements.LoadAchievementsResult loadAchievementsResult) {
                        Achievement ach;
                        AchievementBuffer aBuffer = loadAchievementsResult.getAchievements();
                        Iterator<Achievement> aIterator = aBuffer.iterator();
                        while (aIterator.hasNext()) {
                            ach = aIterator.next();
                            //Gdx.app.log("AndroidLauncher", id + "$$" + ach.getAchievementId() + "##" + ach.getState());
                            if (id.equals(ach.getAchievementId())) {
                                if (ach.getType() == Achievement.TYPE_INCREMENTAL) {
                                    Gdx.app.log("AndroidLauncher", "Unlocking.." + ach.getAchievementId() + "");
                                    Games.Achievements.increment(gameHelper.getApiClient(), id, increment);
                                    String increment = Persistence.readFile(achievementId);
                                    int i = 0;
                                    if(!increment.isEmpty())i = Integer.parseInt(increment);
                                    Persistence.writeFile(json.toJson(i+1), achievementId);
                                }
                                aBuffer.close();
                                break;
                            }
                        }
                    }
                });

            }
        } catch (Exception e) {
            Gdx.app.log("Error to connect", "unlock achievment");
        }

    }

    /**
     * Show Activity records to the table
     */
    @Override
    public void getLeaderboardGPGS() {
        //Activity // call for all registered tables records. as
        //    Activity // can call for a specific table
        try {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.getApiClient()), REQUEST_CODE_UNUSED);
        } catch (Exception e) {
            Gdx.app.log("Error to connect", "leaderboard");
        }
    }

    @Override
    public void getPositionGPGS() {
        //Activity // call for all registered tables records. as
        //    Activity // can call for a specific table
        try {
            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(gameHelper.getApiClient(), getString(R.string.leaderboard_id), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
                @Override
                public void onResult(final Leaderboards.LoadPlayerScoreResult scoreResult) {
                    if (isScoreResultValid(scoreResult)) {
                        // here you can get the score like this
                        long points = scoreResult.getScore().getRank();
                        Gdx.app.log("your ranking", points + "#");
                        if(points<=1)unlockAchievementGPGS("achievement_6");
                        if(points<=2)unlockAchievementGPGS("achievement_7");
                    }
                }
            });
        } catch (Exception e) {
            Gdx.app.log("Error to connect", "leaderboard");
        }
    }

    /**
     * Show Activity achievements
     */
    @Override
    public void getAchievementsGPGS() {
        // Call Activity achievements
        try {
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
        } catch (Exception e) {
            Gdx.app.log("Error to connect", "achievements");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        gameHelper.onStart(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AndroidLauncher Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.square.android/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AndroidLauncher Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.square.android/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        gameHelper.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    @Override
    public void removeAds() {
        try {
            mHelper.launchPurchaseFlow(this, SKU_REMOVE_ADS, RC_REQUEST, mPurchaseFinishedListener, "HANDLE_PAYLOADS");
        } catch (Exception e) {
            Gdx.app.log("Error to launch purchase", "remove ads" + e.getMessage());
        }
    }

    @Override
    public void buyCoins(String value) {
        try {
            mHelper.launchPurchaseFlow(this, value, RC_REQUEST, mPurchaseFinishedListener, "HANDLE_PAYLOADS");
        } catch (Exception e) {
            Gdx.app.log("Error to launch purchase", "buy coins" + e.getMessage());
        }
    }

    @Override
    public Array getInventoryPrices() {
        return listPrices;
    }

    @Override
    public void processPurchases() {
        try {
            String[] moreSkus = {COIN_550, COIN_2000, COIN_7000, COIN_26000, COIN_140000, COIN_800000};
            mHelper.queryInventoryAsync(true, Arrays.asList(moreSkus), mGotInventoryListener);
        }
        catch (Exception e){
            Gdx.app.log("Error to launch purchase", "buy coins" + e.getMessage());
        }
    }

    @Override
    public boolean isAdsRemoved() {
        if(purchased.size()==0){
            purchased.set(0,0);
        }
        return  (purchased.get(0)==1);
    }

    @Override
    public void setIsAdsRemoved(boolean val) {
        if(val)
        purchased.set(0,1);
        else
        purchased.set(0,0);

    }

    @Override
    public ArrayList getPurchasedList() {
        return purchased;
    }


}



