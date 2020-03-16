package com.square.interfaces;

public interface GooPlayGames {
    /**
     * Check the status of the user input
     */
    public boolean getSignedInGPGS();

    /**
     * Log
     */
    public void loginGPGS();

    public void logout();

    /**
     * Send the result in the high score table
     */
    public void submitScoreGPGS(long score);

    /**
     * Unlock achievement
     * <p/>
     * param AchievementId
     * ID achievements. Taken from the file games-ids.xml
     */
    public void unlockAchievementGPGS(String achievementId);

    public void incrementAchievementGPGS(String achievementId, final int increment);

    /**
     * Show Activity records to the table
     */
    public void getLeaderboardGPGS();

    /**
     * Show Activity achievements
     */
    public void getAchievementsGPGS();

    public void loadScoreOfLeaderBoard();

    public void submitEvent(String eventId, int increment);

    public String getVersionGame();

    public void submitRicherGPGS(long totalMonedas);

    public void loadRicherOfLeaderBoard();

    public void getPositionGPGS();
}