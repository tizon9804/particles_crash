package com.square.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.square.config.GameConfig;
import com.square.util.Property;

/**
 * Created by Tizon on 02/02/2016.
 */
public class CirculoBase implements Pool.Poolable {

    protected float posx;
    protected float posy;
    protected float rad;
    protected Property property;
    protected TextureRegion texture;
    protected Animation animation;
    private float aumento;

    public CirculoBase(){
        this.posx = 0;
        this.posy = 0;
        this.rad = 0;
        this.property = new Property();
        this.property.setColor(new Color(Color.BLUE));
    }

    public void init(float posx, float posy, Property p,float aumento) {
        this.posx = posx;
        this.posy = posy;
        this.rad = 0;
        this.property.getColor().r = p.getColor().r;
        this.property.getColor().g = p.getColor().g;
        this.property.getColor().b = p.getColor().b;
        this.aumento = aumento;
        property.getColor().a=0.8f;
    }

    public void update() {
        rad+=aumento;
        property.getColor().a=property.getColor().a>0?property.getColor().a-GameConfig.maxtiempoWave:0;
    }

    public float getRad() {
        return rad;
    }

    public void setRad(float rad) {
        this.rad = rad;
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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }


    /**
     * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
     */
    @Override
    public void reset() {
        this.posx = 0;
        this.posy = 0;
        this.rad = 0;
        this.property = new Property();
        this.property.setColor(new Color(Color.BLUE));
    }
}
