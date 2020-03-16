package com.square.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.square.config.GameConfig;
import com.square.interfaces.AdsController;
import com.square.interfaces.GooPlayGames;
import com.square.interfaces.IaBilling;


/**
 * Created by Tizon on 10/12/2015.
 */
public class WelcomeScreen implements Screen {

    private IaBilling iaBilling;
    private AdsController adsController;
    private GooPlayGames gooPlayGames;
    private CuadradoGame cuadradoGame;
    private ConfigScreen config;
    private ParticlesGame particlesGame;
    private float welh;
    private float width;
    private float height;
    private OrthographicCamera cam;
    private SpriteBatch batch;
    private static String WELCOME = "Welcome.png";
    public static Texture welcomeTexture;
    private int tiempo;
    private float timeAnimate;
    private ShapeRenderer shape;
    private float progress;


    public WelcomeScreen(AdsController adsController, GooPlayGames gooPlayGames, IaBilling iaBilling, ParticlesGame particlesGame, CuadradoGame cuadradoGame, ConfigScreen config) {
        inicializarJuego();
        inicializarCamara();
        welcomeTexture = new Texture(Gdx.files.internal(WELCOME));
        welh = (width * welcomeTexture.getHeight()) / (welcomeTexture.getWidth());
        this.adsController = adsController;
        this.gooPlayGames = gooPlayGames;
        this.iaBilling = iaBilling;
        this.cuadradoGame = cuadradoGame;
        this.config = config;
        this.particlesGame = particlesGame;
        tiempo = 0;
        timeAnimate = 0;
        progress = 0;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        animateWelcome();
        animateProgressBar();
        cam.update();
        if (timeAnimate >= 1) {
            if (cuadradoGame != null) {
                // asset manager.update, si ya cargaron las texturas en ram
                if (progress >= 100 && GameConfig.assetManager.update()) {
                    particlesGame.setScreen(cuadradoGame);
                }
                progress++;
            } else {
                progress = 50;
                initializeGame();
            }
        }

    }

    private void animateProgressBar() {
        float percent = (progress * width) / 100;
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0.8f, 1f, 0);
        shape.rect(0, (height / 2) - (welh * 0.9f), percent, height * 0.01f);
        shape.end();
    }

    private void animateWelcome() {
        if (timeAnimate < 1) {
            timeAnimate += 0.01f;
            progress += 0.5;
        } else {
            timeAnimate = 1;
        }
        batch.enableBlending();
        batch.begin();
        batch.setColor(1, 1, 1, timeAnimate);
        batch.draw(welcomeTexture,
                width/2-(width * 0.85f)/2,
                (height / 2) - (welh / 2),
                width * 0.85f, welh * 0.85f);

        batch.end();
    }

    private void initializeGame() {
        Gdx.app.log("ParticleGame", "starting CuadradoGame");
        cuadradoGame = new CuadradoGame(adsController, gooPlayGames, iaBilling, particlesGame);
        Gdx.app.log("ParticleGame", "starting Config screen");
        config = new ConfigScreen(particlesGame);
        Gdx.app.log("ParticleGame", "screen CuadradoGame");
        particlesGame.setCuadradoGame(cuadradoGame);
        particlesGame.setConfig(config);

    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }

    public void inicializarJuego() {
        //inicializador del juego
        width = GameConfig.width;
        height = GameConfig.height;
        Gdx.app.log("Welcome Screen", "create width " + width + "$$ heigth " + height);
    }

    public void inicializarCamara() {
        cam = new OrthographicCamera();
        cam.setToOrtho(false, width, height);
        cam.update();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        shape = new ShapeRenderer();
        shape.setProjectionMatrix(cam.combined);
    }


    public AdsController getAdsController() {
        return adsController;
    }

    public void setAdsController(AdsController adsController) {
        this.adsController = adsController;
    }

    public GooPlayGames getGooPlayGames() {
        return gooPlayGames;
    }

    public void setGooPlayGames(GooPlayGames gooPlayGames) {
        this.gooPlayGames = gooPlayGames;
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

    public ParticlesGame getParticlesGame() {
        return particlesGame;
    }

    public void setParticlesGame(ParticlesGame particlesGame) {
        this.particlesGame = particlesGame;
    }

    public float getWelh() {
        return welh;
    }

    public void setWelh(float welh) {
        this.welh = welh;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public static String getWELCOME() {
        return WELCOME;
    }

    public static void setWELCOME(String WELCOME) {
        WelcomeScreen.WELCOME = WELCOME;
    }

    public static Texture getWelcomeTexture() {
        return welcomeTexture;
    }

    public static void setWelcomeTexture(Texture welcomeTexture) {
        WelcomeScreen.welcomeTexture = welcomeTexture;
    }
}
