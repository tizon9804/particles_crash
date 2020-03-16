package com.square.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.square.util.Property;

/**
 * Created by Tizon on 04/07/2015.
 */
public class Obstaculo extends CuadradoBase implements Pool.Poolable {

    private float aumentoX;
    private float aumentoY;
    private boolean hijo;

    public Obstaculo(){

    }

    public void init(float x, float y, float width, float height, float aumentoX, float aumentoY, Property property, boolean hijo) {
        posx = x;
        posy = y;
        this.aumentoX = aumentoX;
        this.aumentoY = aumentoY;
        this.width = width;
        this.height = height;
        this.property = property;
        this.hijo = hijo;
        validate = true;
        colisiones = 0;
        rect = new Rectangle();
        vida = 0;
        state = 0f;
        updateParameters();

    }

    public void update(float x, float y) {
        if (validate) {
            posx = x;
            posy = y;
            updateParameters();
        }
    }

    public float getAumentoX() {
        return aumentoX;
    }

    public void setAumentoX(float aumentoX) {
        this.aumentoX = aumentoX;
    }

    public float getAumentoY() {
        return aumentoY;
    }

    public void setAumentoY(float aumentoY) {
        this.aumentoY = aumentoY;
    }

    public boolean isHijo() {
        return hijo;
    }

    public void setHijo(boolean hijo) {
        this.hijo = hijo;
    }


    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        posx = 0;
        posy = 0;
        this.aumentoX = 0;
        this.aumentoY = 0;
        this.width = 0;
        this.height = 0;
        this.property = null;
        this.hijo = false;
        validate = true;
        colisiones = 0;
        rect = null;
        state = 0f;
    }
}
