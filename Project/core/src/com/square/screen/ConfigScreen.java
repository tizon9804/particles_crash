package com.square.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.square.Menus.MainMenu;
import com.square.Menus.PausaMenu;
import com.square.Menus.Puntaje;
import com.square.Menus.ShopMenu;
import com.square.Render.RenderizadorConfig;
import com.square.config.GameConfig;
import com.square.entity.MrCuadrado;
import com.square.entity.Terreno;
import com.square.game.GeneradorSistema;
import com.square.game.GeneradorSonido;
import com.square.game.GeneradorTextura;
import com.square.handler.GestureConfigHandler;
import com.square.handler.InputConfigHandler;


public class ConfigScreen implements Screen {


    public ParticlesGame particlesGame;
    public static OrthographicCamera cam;
    private GeneradorSistema generadorSistema;
    private float height;
    private GestureConfigHandler gestureHandler;
    private RenderizadorConfig rend;
    private InputConfigHandler inputHandler;
    private float width;
    private Terreno terreno;
    private static Puntaje puntaje;
    private MainMenu mainMenu;
    private GeneradorSonido generadorSonido;
    private GeneradorTextura generadorTextura;
    private PausaMenu pausaMenu;
    private ShopMenu shopMenu;

    public ConfigScreen(ParticlesGame particlesGame) {
        this.particlesGame = particlesGame;
        inicializarJuego();
        inicializarCamara();


    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        inicializarObjetos();
        inicializarGeneradores();
        inicializarScreens();
        inicializarHandlers();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        optimizar();
        rend.update();
        pausaMenu.update();
        shopMenu.update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        getGeneradorSistema().getObstaculos().clear();
        getGeneradorSistema().getParticulas().clear();
    }

    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        getGeneradorSistema().getObstaculos().clear();
        getGeneradorSistema().getParticulas().clear();
    }

    @Override
    public void dispose() {
        getGeneradorSistema().getObstaculos().clear();
        getGeneradorSistema().getParticulas().clear();

    }

    //--------------------------------------------------------------------------------------------------------------------------
    //updates
    //--------------------------------------------------------------------------------------------------------------------------

    public void updateSound() {
        generadorSonido.update();
    }

    public void optimizar() {
        getGeneradorSistema().limpiarParticulas();
    }


    //--------------------------------------------------------------------------------------------------------------------------
    //inicializadores
    //--------------------------------------------------------------------------------------------------------------------------


    public void inicializarJuego() {
        //inicializador del juego
        width = GameConfig.width;
        height = GameConfig.height;
        Gdx.app.log("Cuadrado game", "create width " + width + "$$ heigth " + height);
    }

    public void inicializarScreens() {
        rend = new RenderizadorConfig(this);
        pausaMenu = new PausaMenu(this, generadorSistema);
        shopMenu = new ShopMenu(this,generadorSistema);
    }

    public void inicializarCamara() {
        cam = new OrthographicCamera();
        cam.setToOrtho(true, width, height);
        cam.update();
    }

    public void inicializarHandlers() {
        //inicializador del los inputs del juego
        inputHandler = new InputConfigHandler(this);
        gestureHandler = new GestureConfigHandler(this);
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(shopMenu.getShopStage());
        multi.addProcessor(pausaMenu.getMenuStage());
        multi.addProcessor(new GestureDetector(gestureHandler));
        multi.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multi);
        Gdx.input.setCatchBackKey(true);
    }

    public void inicializarObjetos() {
        terreno = new Terreno(width, height);
    }

    public void inicializarGeneradores() {
        generadorSistema = new GeneradorSistema(terreno, null,getParticlesGame().getCuadradoGame());
    }
    //--------------------------------------------------------------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------------------------------------------------------------

    public OrthographicCamera getCam() {
        return cam;
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public GeneradorSistema getGeneradorSistema() {
        return generadorSistema;
    }

    public void setGeneradorSistema(GeneradorSistema generadorSistema) {
        this.generadorSistema = generadorSistema;
    }

    public GestureConfigHandler getGestureHandler() {
        return gestureHandler;
    }

    public void setGestureHandler(GestureConfigHandler gestureHandler) {
        this.gestureHandler = gestureHandler;
    }

    public RenderizadorConfig getRend() {
        return rend;
    }

    public void setRend(RenderizadorConfig rend) {
        this.rend = rend;
    }

    public InputConfigHandler getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(InputConfigHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Terreno getTerreno() {
        return terreno;
    }

    public void setTerreno(Terreno terreno) {
        this.terreno = terreno;
    }

    public com.square.Menus.Puntaje getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(com.square.Menus.Puntaje puntaje) {
        this.puntaje = puntaje;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public ParticlesGame getParticlesGame() {
        return particlesGame;
    }

    public void setParticlesGame(ParticlesGame particlesGame) {
        this.particlesGame = particlesGame;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public GeneradorSonido getGeneradorSonido() {
        return generadorSonido;
    }

    public void setGeneradorSonido(GeneradorSonido generadorSonido) {
        this.generadorSonido = generadorSonido;
    }

    public GeneradorTextura getGeneradorTextura() {
        return generadorTextura;
    }

    public void setGeneradorTextura(GeneradorTextura generadorTextura) {
        this.generadorTextura = generadorTextura;
    }
}
