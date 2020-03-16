package com.square.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.square.Menus.MainMenu;
import com.square.Menus.Puntaje;
import com.square.config.GameConfig;
import com.square.entity.MrCuadrado;
import com.square.entity.Terreno;
import com.square.game.GeneradorMonedas;
import com.square.game.GeneradorOndas;
import com.square.game.GeneradorPoderes;
import com.square.game.GeneradorSistema;
import com.square.game.GeneradorSonido;
import com.square.game.GeneradorTextura;
import com.square.game.Tiempo;
import com.square.handler.GestureHandler;
import com.square.handler.InputHandler;
import com.square.interfaces.AdsController;
import com.square.interfaces.GooPlayGames;
import com.square.interfaces.IaBilling;
import com.square.util.Property;

import java.util.Calendar;

import static com.square.config.GameConfig.colorObstaculos;
import static com.square.config.GameConfig.colorParticulas;
import static com.square.config.GameConfig.dificultadGeneradorObstaculos;
import static com.square.config.GameConfig.maximoTamanoParticulas;
import static com.square.config.GameConfig.mrCuadradoColor;
import static com.square.config.GameConfig.mrCuadradoGameoverTime;
import static com.square.config.GameConfig.mrCuadradoposX;
import static com.square.config.GameConfig.mrCuadradoposY;
import static com.square.config.GameConfig.numeroParticulasiniciales;
import static com.square.config.GameConfig.obstaculoheight;
import static com.square.config.GameConfig.obstaculoposX;
import static com.square.config.GameConfig.obstaculoposY;
import static com.square.config.GameConfig.obstaculowidth;
import static com.square.config.GameConfig.particulaposX;
import static com.square.config.GameConfig.particulaposY;
import static com.square.config.GameConfig.tiempoInicial;


public class CuadradoGame implements Screen {


    private static com.square.interfaces.AdsController adsController;
    private static boolean showAd;
    private static int timesPlayed;
    private final GooPlayGames gooPlayGames;
    private final IaBilling iaBilling;
    private Calendar mainTime;
    public ParticlesGame particlesGame;
    private MrCuadrado mrCuadrado;
    public static OrthographicCamera cam;
    private GeneradorSistema generadorSistema;
    private float height;
    private GestureHandler gestureHandler;
    private com.square.Render.Renderizador rend;
    private InputHandler inputHandler;
    private float width;
    private Terreno terreno;
    private Tiempo tiempo;
    private static Puntaje puntaje;
    private MainMenu mainMenu;
    private GeneradorSonido generadorSonido;
    private GeneradorTextura generadorTextura;
    private GeneradorPoderes generadorPoderes;
    private GeneradorMonedas generadorMonedas;
    private GeneradorOndas generadorOndas;


    public CuadradoGame(AdsController adsController, GooPlayGames gooPlayGames, IaBilling iaBilling, ParticlesGame particlesGame) {
        this.gooPlayGames = gooPlayGames;
        this.adsController = adsController;
        this.iaBilling = iaBilling;
        this.particlesGame = particlesGame;
        inicializarJuego();
        inicializarGeneradores();
        inicializarCamara();
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        mainTime = Calendar.getInstance();
        Gdx.app.log("Cuadrado game", "show method");
        if(terreno==null) {
            cargarElementos();
            inicializarObjetos();
            inicializarGeneradoresPost();
            inicializarScreens();
        }
        inicializarHandlers();
    }

    private void cargarElementos() {
        generadorSonido.cargarSonidos();
        generadorTextura.cargarTexturas();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if (!GameConfig.pausa) {
            updateAds();
            updateLevel();
            optimizar();
            rend.update();
            updateSound();
            updateGameOver();
            tiempo.update();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        Calendar fin = Calendar.getInstance();
        int diference= (int) (fin.getTimeInMillis()- mainTime.getTimeInMillis());
        diference/=1000;
        getGooPlayGames().submitEvent("event_time_played",diference);
    }

    @Override
    public void resume() {
        mainTime = Calendar.getInstance();
        if(!MrCuadrado.isGameover()){
            GameConfig.pausa = true;
            GameConfig.shop = false;
            particlesGame.setScreen(particlesGame.getConfig());
        }

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        Calendar fin = Calendar.getInstance();
        int diference= (int) (fin.getTimeInMillis()- mainTime.getTimeInMillis());
        diference/=1000;
        getGooPlayGames().submitEvent("event_time_played",diference);
    }

    @Override
    public void dispose() {
        getGeneradorSistema().getObstaculos().clear();
        getGeneradorSistema().getParticulas().clear();
        getGeneradorPoderes().getParticulas().clear();
        getGeneradorPoderes().getObstaculos().clear();
        getGeneradorPoderes().getPoderesObs().clear();
        getGeneradorMonedas().getObstaculos().clear();
        getGeneradorMonedas().getParticulas().clear();
        Calendar fin = Calendar.getInstance();
        int diference= (int) (fin.getTimeInMillis()- mainTime.getTimeInMillis());
        diference/=1000;
        getGooPlayGames().submitEvent("event_time_played",diference);
        mrCuadrado = null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    //updates
    //--------------------------------------------------------------------------------------------------------------------------

    public void updateSound() {
        generadorSonido.update();
    }

    public void updateGameOver() {
        if (!mainMenu.started && !showAd) {
            mainMenu.update();
        } else if (mainMenu.started) {
            mainMenu.clear();
            empezarJuego();
        }
    }

    public void updateAds() {
        if (!iaBilling.isAdsRemoved()) {
            showIntersticial(mrCuadrado.getGameoverTime());
        } else {
            showAd = false;
        }
    }

    public void updateLevel() {
        getGeneradorSistema().aumentarNivel();
        getGeneradorPoderes().aumentarNivel();
    }

    public void optimizar() {
        getGeneradorSistema().limpiarParticulas();
        getGeneradorPoderes().limpiarParticulas();
        getGeneradorMonedas().limpiarParticulas();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    //nuevo juego
    //--------------------------------------------------------------------------------------------------------------------------

    private void empezarJuego() {
        if (mrCuadrado.isGameover()) {
            particlesGame.setCuadradoGame(this);
            showAd = false;
            hideBanner();
            Tiempo.setTiempo(tiempoInicial);
            Tiempo.setAumento(1, 0);
            dificultadGeneradorObstaculos = GameConfig.dificultadinicial;
            puntaje.restart();
            getGooPlayGames().submitEvent("event_games_played", 1);
            getGooPlayGames().submitEvent("event_money_obtained", (int) generadorMonedas.getMonedas());
            mrCuadrado.setGameover(false);
            mrCuadrado.setPosx(mrCuadradoposX);
            mrCuadrado.setPosy(mrCuadradoposY);
            mrCuadrado.setWidth(GameConfig.mrCuadradowidthI);
            mrCuadrado.setHeight(GameConfig.mrCuadradoheightI);
            mrCuadrado.getProperty().setColor(mrCuadradoColor);
            mrCuadrado.updateParameters();
            getGeneradorSistema().setMrCuadrado(mrCuadrado);
            getGeneradorSistema().limpiarGenerador();
            getGeneradorPoderes().limpiarGenerador();
            getGeneradorMonedas().limpiarGenerador();
            getGeneradorOndas().limpiargenerdador();
            GameConfig.timeLapsePoderCongelante = 0;
            GameConfig.timeLapseChiquitolin = 0;
            //creal el juego
            Property p = new Property();
            p.setColor(colorParticulas);
            Property pobs = new Property();
            pobs.setColor(colorObstaculos);
            //crea las particulas
            for (long i = 0; i < numeroParticulasiniciales; i++) {
                getGeneradorSistema().crearParticula(particulaposX,
                        particulaposY,
                        ((int) getGeneradorSistema().aumentoRandom(maximoTamanoParticulas)),
                        ((int) getGeneradorSistema().aumentoRandom(maximoTamanoParticulas)), p,
                        GameConfig.explosionInicialParticulas);
            }
            getGeneradorSistema().crearObstaculo(obstaculoposX, obstaculoposY, obstaculowidth, obstaculoheight, pobs, dificultadGeneradorObstaculos, false);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    //ads
    //--------------------------------------------------------------------------------------------------------------------------


    public static void adI() {
        showAd = true;
    }

    public static void showIntersticial(long gameoverTime) {
        if (showAd) {
            if (puntaje.getPuntaje() >= Puntaje.record / 2 || timesPlayed >= 5) {
                if (Tiempo.getTiempo() >= gameoverTime + GameConfig.mrCuadradoGameoverTime) {
                    showAd = false;
                    timesPlayed = 0;
                    adsController.showInterstitialAd(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Interstitial app closed");
                        }
                    });
                }
            } else {
                showAd = false;
            }
            timesPlayed++;
        }
    }

    public void showBanner() {
        adsController.showBannerAd();
    }

    public void hideBanner() {
        adsController.hideBannerAd();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    //inicializadores
    //--------------------------------------------------------------------------------------------------------------------------


    public void inicializarJuego() {
        //inicializador del juego
        gooPlayGames.loginGPGS();
        showAd = false;
        width = GameConfig.width;
        height = GameConfig.height;
        Gdx.app.log("Cuadrado game", "create width " + width + "$$ heigth " + height);
        timesPlayed = 0;
    }

    public void inicializarScreens() {
        puntaje = new Puntaje(this);
        mainMenu = new MainMenu(this, generadorSistema);
        rend = new com.square.Render.Renderizador(this);
        tiempo = new Tiempo();
    }

    public void inicializarCamara() {
        cam = new OrthographicCamera();
        cam.setToOrtho(true, width, height);
        cam.update();
    }

    public void inicializarHandlers() {
        //inicializador del los inputs del juego
        inputHandler = new InputHandler(this);
        gestureHandler = new GestureHandler(this);
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(inputHandler);
        multi.addProcessor(new GestureDetector(gestureHandler));
        multi.addProcessor(puntaje.getStage());
        multi.addProcessor(mainMenu.getMenuStage());
        Gdx.input.setInputProcessor(multi);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    public void inicializarObjetos() {
        terreno = new Terreno(width, height);
        mrCuadrado = new MrCuadrado(mrCuadradoposX, mrCuadradoposY, GameConfig.mrCuadradowidth, GameConfig.mrCuadradoheight, terreno);
        mrCuadrado.setGameoverTime(-mrCuadradoGameoverTime);
    }

    public void inicializarGeneradores() {
        generadorTextura = new GeneradorTextura();
        generadorSonido = new GeneradorSonido();
    }

    public void inicializarGeneradoresPost() {
        generadorOndas = new GeneradorOndas();
        generadorSistema = new GeneradorSistema(terreno, mrCuadrado,this);
        generadorPoderes = new GeneradorPoderes(terreno, mrCuadrado, this);
        generadorMonedas = new GeneradorMonedas(terreno,mrCuadrado,this);
    }
    //--------------------------------------------------------------------------------------------------------------------------
    //getters and setters
    //--------------------------------------------------------------------------------------------------------------------------


    public GeneradorOndas getGeneradorOndas() {
        return generadorOndas;
    }

    public void setGeneradorOndas(GeneradorOndas generadorOndas) {
        this.generadorOndas = generadorOndas;
    }

    public GeneradorMonedas getGeneradorMonedas() {
        return generadorMonedas;
    }

    public void setGeneradorMonedas(GeneradorMonedas generadorMonedas) {
        this.generadorMonedas = generadorMonedas;
    }

    public GeneradorPoderes getGeneradorPoderes() {
        return generadorPoderes;
    }

    public void setGeneradorPoderes(GeneradorPoderes generadorPoderes) {
        this.generadorPoderes = generadorPoderes;
    }

    public IaBilling getIaBilling() {
        return iaBilling;
    }

    public MrCuadrado getMrCuadrado() {
        return mrCuadrado;
    }

    public void setMrCuadrado(MrCuadrado mrCuadrado) {
        this.mrCuadrado = mrCuadrado;
    }

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

    public GestureHandler getGestureHandler() {
        return gestureHandler;
    }

    public void setGestureHandler(GestureHandler gestureHandler) {
        this.gestureHandler = gestureHandler;
    }

    public com.square.Render.Renderizador getRend() {
        return rend;
    }

    public void setRend(com.square.Render.Renderizador rend) {
        this.rend = rend;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(InputHandler inputHandler) {
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

    public static AdsController getAdsController() {
        return adsController;
    }

    public static void setAdsController(AdsController adsController) {
        CuadradoGame.adsController = adsController;
    }

    public static boolean isShowAd() {
        return showAd;
    }

    public static void setShowAd(boolean showAd) {
        CuadradoGame.showAd = showAd;
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

    public Tiempo getTiempo() {
        return tiempo;
    }

    public void setTiempo(Tiempo tiempo) {
        this.tiempo = tiempo;
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

    public GooPlayGames getGooPlayGames() {
        return gooPlayGames;
    }
}
