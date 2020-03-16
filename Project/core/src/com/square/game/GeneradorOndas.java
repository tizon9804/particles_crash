package com.square.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.square.config.GameConfig;
import com.square.entity.CirculoBase;
import com.square.util.Property;

/**
 * Created by Tizon on 02/02/2016.
 */
public class GeneradorOndas {


    protected Array<CirculoBase> waves;
    protected Pool<CirculoBase> inventario;

    public GeneradorOndas(){
        waves = new Array();
        inventario = new Pool<CirculoBase>() {
            @Override
            protected CirculoBase newObject() {
                return new CirculoBase();
            }
        };
    }


    public void createWave(float posx, float posy,Property p,float aumento){
            CirculoBase circle = inventario.obtain();
            circle.init(posx, posy, p,aumento);
            waves.add(circle);


    }

    public void generarMovimiento(){
        for (CirculoBase circle:waves){
            circle.update();
            if(circle.getProperty().getColor().a<=0){
                waves.removeValue(circle,true);
                inventario.free(circle);
            }
        }
    }

    public Array<CirculoBase> getWaves() {
        return waves;
    }

    public void setWaves(Array<CirculoBase> waves) {
        this.waves = waves;
    }

    public void limpiargenerdador() {
        waves.clear();
        inventario.clear();
    }
}
