package com.square.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.square.config.GameConfig;

import java.util.Random;

/**
 * Created by Tizon on 14/08/2015.
 */
public class GeneradorTextura {


    private static String SOMBRA = "SombraObs";
    private static String CONGELANTESHADOW = "Congelante";
    private static String ATLAS = "packmr.pack";
    private static String BUTTON = "packbutton.pack";
    private static String MRCUADRADO = "MrCuadrado";
    private static String MRCUADRADOD = "MrCuadradoD";
    private static String TERRENO = "Terreno";
    private static String OBSTACULO = "Obstaculo";
    private static String PODER = "Poder";
    private static String COIN = "Coin3d";
    private static TextureAtlas squareAtlas;
    private static TextureAtlas buttonAtlas;
    public static Array<TextureAtlas.AtlasRegion> mrCuadradoTextures;
    public static Array<TextureAtlas.AtlasRegion> mrCuadradoDTextures;
    public static TextureRegion terrenoTexture;
    public static Array<TextureAtlas.AtlasRegion> obstaculoTextures;
    public static Array<TextureAtlas.AtlasRegion> poderTextures;
    public static Array<TextureAtlas.AtlasRegion> coinTextures;
    public static TextureRegion congelante;
    public static TextureRegion sombra;

    public GeneradorTextura(){
       GameConfig.assetManager.load(ATLAS,TextureAtlas.class);
       GameConfig.assetManager.load(BUTTON,TextureAtlas.class);
    }

    public void cargarTexturas() {
        squareAtlas = GameConfig.assetManager.get(ATLAS,TextureAtlas.class);
        buttonAtlas = GameConfig.assetManager.get(BUTTON,TextureAtlas.class);
        mrCuadradoTextures = squareAtlas.findRegions(MRCUADRADO);
        mrCuadradoDTextures = squareAtlas.findRegions(MRCUADRADOD);
        terrenoTexture = squareAtlas.findRegion(TERRENO);
        obstaculoTextures = squareAtlas.findRegions(OBSTACULO);
        poderTextures = squareAtlas.findRegions(PODER);
        coinTextures = squareAtlas.findRegions(COIN);
        congelante =  squareAtlas.findRegion(CONGELANTESHADOW);
        sombra = squareAtlas.findRegion(SOMBRA);
    }

    public static TextureRegion getObstaculoRND() {
        Random n = new Random();
        int rand = (int) (Math.random() * 100 % obstaculoTextures.size);
        //Gdx.app.log("texture",obstaculoTextures.size+"$$"+rand);
        return obstaculoTextures.get(rand);
    }

    public static TextureRegion getCongelante() {
        return congelante;
    }

    public static void setCongelante(TextureRegion congelante) {
        GeneradorTextura.congelante = congelante;
    }

    public static String getCOIN() {
        return COIN;
    }

    public static void setCOIN(String COIN) {
        GeneradorTextura.COIN = COIN;
    }

    public static Array<TextureAtlas.AtlasRegion> getCoinTextures() {
        return coinTextures;
    }

    public static void setCoinTextures(Array<TextureAtlas.AtlasRegion> coinTextures) {
        GeneradorTextura.coinTextures = coinTextures;
    }

    public static String getPODER() {
        return PODER;
    }

    public static void setPODER(String PODER) {
        GeneradorTextura.PODER = PODER;
    }

    public static Array<TextureAtlas.AtlasRegion> getPoderTextures() {
        return poderTextures;
    }

    public static void setPoderTextures(Array<TextureAtlas.AtlasRegion> poderTextures) {
        GeneradorTextura.poderTextures = poderTextures;
    }

    public static String getATLAS() {
        return ATLAS;
    }

    public static void setATLAS(String ATLAS) {
        GeneradorTextura.ATLAS = ATLAS;
    }

    public static String getBUTTON() {
        return BUTTON;
    }

    public static void setBUTTON(String BUTTON) {
        GeneradorTextura.BUTTON = BUTTON;
    }

    public static String getMRCUADRADO() {
        return MRCUADRADO;
    }

    public static void setMRCUADRADO(String MRCUADRADO) {
        GeneradorTextura.MRCUADRADO = MRCUADRADO;
    }

    public static String getMRCUADRADOD() {
        return MRCUADRADOD;
    }

    public static void setMRCUADRADOD(String MRCUADRADOD) {
        GeneradorTextura.MRCUADRADOD = MRCUADRADOD;
    }

    public static Array<TextureAtlas.AtlasRegion> getMrCuadradoTextures() {
        return mrCuadradoTextures;
    }

    public static void setMrCuadradoTextures(Array<TextureAtlas.AtlasRegion> mrCuadradoTextures) {
        GeneradorTextura.mrCuadradoTextures = mrCuadradoTextures;
    }

    public static Array<TextureAtlas.AtlasRegion> getMrCuadradoDTextures() {
        return mrCuadradoDTextures;
    }

    public static void setMrCuadradoDTextures(Array<TextureAtlas.AtlasRegion> mrCuadradoDTextures) {
        GeneradorTextura.mrCuadradoDTextures = mrCuadradoDTextures;
    }

    public static String getTERRENO() {
        return TERRENO;
    }

    public static void setTERRENO(String TERRENO) {
        GeneradorTextura.TERRENO = TERRENO;
    }

    public static String getOBSTACULO() {
        return OBSTACULO;
    }

    public static void setOBSTACULO(String OBSTACULO) {
        GeneradorTextura.OBSTACULO = OBSTACULO;
    }

    public static TextureAtlas getSquareAtlas() {
        return squareAtlas;
    }

    public void setSquareAtlas(TextureAtlas squareAtlas) {
        this.squareAtlas = squareAtlas;
    }

    public static TextureAtlas getButtonAtlas() {
        return buttonAtlas;
    }

    public void setButtonAtlas(TextureAtlas buttonAtlas) {
        this.buttonAtlas = buttonAtlas;
    }

    public static Array<TextureAtlas.AtlasRegion> getMrCuadradoTexture() {
        return mrCuadradoTextures;
    }

    public static void setMrCuadradoTexture(Array<TextureAtlas.AtlasRegion> mrCuadradoTexture) {
        GeneradorTextura.mrCuadradoTextures = mrCuadradoTexture;
    }

    public static TextureRegion getTerrenoTexture() {
        return terrenoTexture;
    }

    public static void setTerrenoTexture(TextureRegion terrenoTexture) {
        GeneradorTextura.terrenoTexture = terrenoTexture;
    }

    public static Array<TextureAtlas.AtlasRegion> getObstaculoTextures() {
        return obstaculoTextures;
    }

    public static void setObstaculoTextures(Array<TextureAtlas.AtlasRegion> obstaculoTextures) {
        GeneradorTextura.obstaculoTextures = obstaculoTextures;
    }


}
