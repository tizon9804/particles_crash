package com.square.game;

import com.badlogic.gdx.graphics.Color;
import com.square.config.GameConfig;
import com.square.entity.CuadradoBase;
import com.square.entity.MrCuadrado;
import com.square.util.Property;

/**
 * Created by Tizon on 11/07/2015.
 */
public class GeneradorExplosivo {


    private static Property p = new Property();

    public static void explotar(CuadradoBase cuadrado, int particiones, GeneradorSistema gen) {
        float posx = cuadrado.getPosx();
        float posy = cuadrado.getPosy();
        float width = cuadrado.getWidth();
        double ladop = Math.sqrt(particiones*2);
        float widthp = (float) (width / ladop);
        float heigthp = widthp;
        int velocidad = (int) (ladop * 1.5);
        int j = 0;
        float r = cuadrado.getProperty().getColor().r;
        float g = cuadrado.getProperty().getColor().g;
        float b = cuadrado.getProperty().getColor().b;
        float a = cuadrado.getProperty().getColor().a;
        p.setColor(new Color(r, g, b, a));
        for (int i = 0; i < particiones; i++) {
            if (i == 0) {
                gen.crearParticula(posx, posy, widthp, heigthp, p, velocidad);
            } else if (i % ladop == 0) {
                j++;
            } else {
                float x = (posx + (widthp * (i + 3) % 3));
                float y = (posy + (heigthp * j));
                gen.crearParticula(x, y, gen.aumentoRandom(GameConfig.maximoTamanoParticulas), gen.aumentoRandom(GameConfig.maximoTamanoParticulas), p, velocidad);
            }
        }
    }

    public static void partirObstaculo(CuadradoBase cuadrado, int particiones, GeneradorSistema gen) {
        float posx = cuadrado.getPosx();
        float posy = cuadrado.getPosy();
        float width = cuadrado.getWidth();
        double ladop = Math.sqrt(particiones);
        float widthp = (float) (width / ladop);
        float heigthp = widthp;
        int velocidad = (int) (GameConfig.dificultadGeneradorObstaculos * 0.5);
        int j = 0;
        for (int i = 0; i < particiones; i++) {
            if (i == 0) {
                gen.crearObstaculo(posx, posy, widthp, heigthp, cuadrado.getProperty(), velocidad, true);
            } else {
                float x = (posx + (widthp * (i + 3) % 3));
                float y = (posy + (heigthp * j));
                gen.crearObstaculo(x, y, widthp, heigthp, cuadrado.getProperty(), velocidad, true);
            }
            if (i % ladop == 0) {
                j++;
            }
        }
    }

    public static void explotarMrCuadrado(MrCuadrado mrCuadrado, int particiones, GeneradorSistema gen) {
        float posx = mrCuadrado.getPosx();
        float posy = mrCuadrado.getPosy();
        float width = mrCuadrado.getWidth();
        double ladop = Math.sqrt(particiones);
        float widthp = (float) (width / ladop);
        float heigthp = widthp;
        int velocidad = (int) (ladop);
        int j = 0;
        float r = mrCuadrado.getProperty().getColor().r;
        float g = mrCuadrado.getProperty().getColor().g;
        float b = mrCuadrado.getProperty().getColor().b;
        float a = mrCuadrado.getProperty().getColor().a;
        p.setColor(new Color(r, g, b, a));
        for (int i = 0; i < particiones; i++) {
            if (i == 0) {
                gen.crearParticula(posx, posy, widthp, heigthp, p, velocidad);
            } else if (i % ladop == 0) {
                j++;
            } else {
                float x = (posx + (widthp * (i + 3) % 3));
                float y = (posy + (heigthp * j));
                gen.crearParticula(x, y, widthp, heigthp, p, velocidad);
            }
        }
    }
}
