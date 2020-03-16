package com.square.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Tizon on 13/07/2015.
 */
public class GameConfig {


    public static final String CONGELANTE = "FREEZER";
    public static final String MULTIPLICADOR = "MULTIPLIER";
    public static final String EXPLOSIVO = "BOOM";
    public static final String CHIQUITOLIN = "MINI";
    public static float width = Gdx.graphics.getWidth();
    public static float height = Gdx.graphics.getHeight();
    public static float area = width * height;
    public static float relativeSide = (float) Math.sqrt(area * porcentaje(2));
    public static float mrCuadradowidthI = relativeSide;
    public static float mrCuadradoheightI = relativeSide;
    public static float mrCuadradowidth = mrCuadradowidthI;
    public static float mrCuadradoheight = mrCuadradoheightI;
    public static float mrCuadradowidthActual = mrCuadradowidthI;
    public static float mrCuadradoheightActual = mrCuadradoheightI;
    public static float mrCuadradoposX = width / 2 - (mrCuadradowidth / 2);
    public static float mrCuadradoposY = height - mrCuadradoheight;
    public static long mrCuadradoGameoverTime = 100;
    public static Color mrCuadradoColor = new Color(0f, 0.67f, 0.88f, 0);
    public static int dificultadinicial = (int) (relativeSide * porcentaje(5));
    public static int dificultadGeneradorObstaculos = dificultadinicial;
    public static long tiempoInicial = 0;
    public static float particulaposX = width / 2;
    public static float particulaposY = height / 2;
    public static int explosionInicialParticulas = (int) (relativeSide * 0.2);
    public static int velocidadNormalParticulas = (int) (relativeSide * 0.025);
    public static int maximoTamanoParticulas = (int) (velocidadNormalParticulas * 1.5f);
    public static Color colorParticulas = new Color(0f, 0f, 0f, 0);
    public static float numeroParticulasiniciales = (int) (relativeSide * 6);
    public static float maximonumeroParticulas = numeroParticulasiniciales * 1.4f;
    public static float obstaculoposX = width / 2;
    public static float obstaculoposY = 10;
    public static long tiempoSalidaObstaculoInicial = dificultadGeneradorObstaculos < 500 ? (long) (600 - Math.pow(dificultadGeneradorObstaculos / 10, 2)) : 100;
    public static long tiempoSalidaObstaculo = tiempoSalidaObstaculoInicial;
    public static long maxtiempoDeVidaObstaculo = (long) (dificultadGeneradorObstaculos * 100);
    public static long maxTiempoPoder = (long) ((dificultadinicial * 100)* 0.30f);
    public static long maxColisionesObstaculo = 10;
    public static float obstaculowidth = (float) (mrCuadradowidth * 1.20);
    public static float obstaculoheight = (float) (mrCuadradoheight * 1.20);
    public static Color colorObstaculos = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 0);
    public static int numeroHijosObstaculo =2;
    public static boolean debug = false;
    public static float wordSize = (float) (relativeSide * 0.025);
    public static float wordSmallSize = (float) (relativeSide * 0.010);
    public static Color colorTerreno = new Color(1f, 1f, 1f, 0);
    public static int mrCuadradoReductor = 10;
    public static boolean sound = true;
    public static float mrCuadradoAnimationTime = (float) porcentaje(5);
    public static boolean pausa = false;
    public static int minTiempoPoder = 5;
    public static int timeLapsePoderCongelante = 0;
    public static int aumentoDificultadInicial = 1;
    public static int aumentoDificultad = aumentoDificultadInicial;
    public static int numObsAparicionPoder = 1;
    public static int minTiempoPoderCongelante = 60*10;
    public static int timeLapseChiquitolin = 60*9;
    public static int probaPoder = 100;
    public static Color colorMoneda= new Color(0,1,1,0);
    public static int velocidadNormalMonedas = 1;
    public static String nameMonedas="monedas";
    public static boolean shop = false;
    public static AssetManager assetManager= new AssetManager();
    public static String namePoderes = "poderesArray";
    public static float chiquitolinSize=1f;
    public static int maxMonedasPorObstaculo = 5;
    public static float  maxtiempoWave =0.01f;
    public static float aumentoOndaObs = 10;
    public static float aumentoOndaMR = 20;
    public static long maxtiempoDeVidaMonedas = (long) (maxtiempoDeVidaObstaculo * 0.6f);
    public static boolean shopMoney = false;


    public static BitmapFont bitmapGenerator() {
        try {
            //FileHandleResolver resolver = new InternalFileHandleResolver();
          //  assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
          //  assetManager.setLoader(BitmapFont.class, ".TTF", new FreetypeFontLoader(resolver));
           // assetManager.load("font.TTF",FreeTypeFontGenerator.class);
          //  assetManager.finishLoading();
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.TTF"));// assetManager.get("font.TTF",FreeTypeFontGenerator.class);
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.genMipMaps = true;
            parameter.minFilter = Texture.TextureFilter.MipMapLinearNearest;
            parameter.magFilter = Texture.TextureFilter.Linear;
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return new BitmapFont();
        }

    }

    public static double porcentaje(double x) {
        return x / 100;
    }


}
