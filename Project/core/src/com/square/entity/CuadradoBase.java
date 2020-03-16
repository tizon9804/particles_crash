package com.square.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.square.util.Property;

/**
 * Created by Tizon on 11/07/2015.
 */
public class CuadradoBase {

    protected float posx;
    protected float posy;
    protected float width;
    protected float height;
    protected Rectangle rect;
    protected Vector2[][] lados;
    protected boolean validate;
    protected Property property;
    protected long colisiones;
    protected long vida;
    protected TextureRegion texture;
    protected Animation animation;
    protected float state;

    public CuadradoBase() {
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void updateParameters() {
        rect.set(posx, posy, width, height);
    }

    public void updateColision() {
        colisiones++;
    }

    public void updateVida() {
        vida++;
    }

    public float getPosx() {
        return posx;
    }

    public void setPosx(float posx) {
        this.posx = posx;
    }

    public float getPosy() {
        return posy;
    }

    public void setPosy(float posy) {
        this.posy = posy;
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

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public Vector2[][] getLados() {
        return lados;
    }

    public void setLados(Vector2[][] lados) {
        this.lados = lados;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public long getColisiones() {
        return colisiones;
    }

    public float getState() {
        state+= Gdx.graphics.getDeltaTime()*0.5f;
        return state;
    }

    public void setColisiones(long colisiones) {
        this.colisiones = colisiones;
    }

    public long getVida() {
        return vida;
    }

    public void setVida(long vida) {
        this.vida = vida;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void setState(float state) {
       this.state = this.state==0?state:this.state;
    }
}
