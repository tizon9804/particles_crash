package com.square.game;

import com.square.config.GameConfig;

/**
 * Created by Tizon on 11/07/2015.
 */
public class Tiempo {

    private static long tiempo;
    private static long aumento;
    private static long timelapse;

    public Tiempo() {
        tiempo = 0;
        aumento = 1;
        timelapse = 0;
    }

    public static void update() {
        if (timelapse <= 0){
            aumento = 1;
            GameConfig.tiempoSalidaObstaculo = GameConfig.tiempoSalidaObstaculoInicial;
        }
        else timelapse--;
        tiempo += aumento;
    }

    public static long getTimelapse() {
        return timelapse;
    }

    public static void setTimelapse(long timelapse) {
        Tiempo.timelapse = timelapse;
    }

    public static long getTiempo() {
        return tiempo;
    }

    public static void setTiempo(long temp) {
        tiempo = temp;
    }

    public static long getAumento() {
        return aumento;
    }

    public static void setAumento(int aumento, long time) {
        Tiempo.aumento = aumento;
        Tiempo.timelapse = time;
    }
}
