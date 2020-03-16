package com.square.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.square.interfaces.AdsController;
import com.square.interfaces.GooPlayGames;
import com.square.interfaces.IaBilling;
import com.square.screen.ParticlesGame;


public class DesktopLauncher implements AdsController, GooPlayGames, IaBilling {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 800;
        config.width = 480;
        DesktopLauncher desk = new DesktopLauncher();
        new LwjglApplication(new ParticlesGame(desk, desk, desk), config);
    }

    @Override
    public void showInterstitialAd(Runnable then) {

    }

    @Override
    public void showBannerAd() {

    }

    @Override
    public void hideBannerAd() {

    }

    /**
     * Check the status of the user input
     */
    @Override
    public boolean getSignedInGPGS() {
        return false;
    }

    /**
     * Log
     */
    @Override
    public void loginGPGS() {

    }

    @Override
    public void logout() {

    }

    /**
     * Send the result in the high score table
     *
     * @param score
     */
    @Override
    public void submitScoreGPGS(long score) {

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

    }

    @Override
    public void incrementAchievementGPGS(String achievementId, int increment) {

    }

    /**
     * Show Activity records to the table
     */
    @Override
    public void getLeaderboardGPGS() {

    }

    /**
     * Show Activity achievements
     */
    @Override
    public void getAchievementsGPGS() {

    }

    @Override
    public void loadScoreOfLeaderBoard() {

    }

    @Override
    public void submitEvent(String eventId,int increment) {

    }

    @Override
    public String getVersionGame() {
        return null;
    }

    @Override
    public void submitRicherGPGS(long totalMonedas) {

    }

    @Override
    public void loadRicherOfLeaderBoard() {

    }

    @Override
    public void getPositionGPGS() {

    }

    @Override
    public void removeAds() {

    }

    @Override
    public void buyCoins(int value) {

    }

    @Override
    public Array getInventoryPrices() {
        return null;
    }

    @Override
    public void processPurchases() {

    }

    @Override
    public boolean isAdsRemoved() {
        return false;
    }

    @Override
    public void setIsAdsRemoved(boolean val) {

    }
}
