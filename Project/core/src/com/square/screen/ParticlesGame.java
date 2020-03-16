package com.square.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.square.interfaces.AdsController;
import com.square.interfaces.GooPlayGames;
import com.square.interfaces.IaBilling;

/**
 * Created by Tizon on 28/08/2015.
 */
public class ParticlesGame extends Game {

    private final AdsController adsController;
    private final GooPlayGames gooPlayGames;
    private final IaBilling iaBilling;
    private com.square.screen.CuadradoGame cuadradoGame;
    private ConfigScreen config;
    private WelcomeScreen welcomeScreen;

    public ParticlesGame(AdsController adsController, GooPlayGames gooplayGames, IaBilling iaBilling) {
        this.adsController = adsController;
        this.gooPlayGames = gooplayGames;
        this.iaBilling = iaBilling;
    }

    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public void create() {
        Gdx.app.log("ParticleGame", "starting Welcome");
        welcomeScreen = new WelcomeScreen(adsController, gooPlayGames, iaBilling, this, cuadradoGame, config);
        Gdx.app.log("ParticleGame", "screen Welcome");
        setScreen(welcomeScreen);
    }

    public CuadradoGame getCuadradoGame() {
        return cuadradoGame;
    }

    public void setCuadradoGame(CuadradoGame cuadradoGame) {
        this.cuadradoGame = cuadradoGame;
    }

    public ConfigScreen getConfig() {
        return config;
    }

    public void setConfig(ConfigScreen config) {
        this.config = config;
    }

    public AdsController getAdsController() {
        return adsController;
    }

    public GooPlayGames getGooPlayGames() {
        return gooPlayGames;
    }

    public WelcomeScreen getWelcomeScreen() {
        return welcomeScreen;
    }

    public void setWelcomeScreen(WelcomeScreen welcomeScreen) {
        this.welcomeScreen = welcomeScreen;
    }
}
