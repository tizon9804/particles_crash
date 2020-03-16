package com.square.entity;

import com.badlogic.gdx.math.Rectangle;
import com.square.config.GameConfig;
import com.square.game.GeneradorTextura;

/**
 * Created by Tizon on 04/07/2015.
 */
public class Terreno extends CuadradoBase {

    public Terreno(float width, float height) {
        posx = 0;
        posy = 0;
        this.width = width;
        this.height = height;
        property = new com.square.util.Property();
        rect = new Rectangle();
        property.setColor(GameConfig.colorTerreno);
        texture = GeneradorTextura.terrenoTexture;
    }
}
