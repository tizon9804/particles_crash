package com.square.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.square.config.GameConfig;
import com.square.game.GeneradorTextura;
import com.square.util.Property;

/**
 * Created by Tizon on 12/01/2016.
 */
public class Poder {

    protected Property property;
    protected int timelapse;
    protected int level;
    protected String type;
    protected int price;

    public Poder(){

    }

    public Poder(String type, int level, int timelapse, int price) {
        this.type = type;
        this.level = level;
        this.timelapse = timelapse;
        this.price = price;
    }

    public void purchaseLevel(){
        level++;
        price *= level;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public int getTimelapse() {
        return timelapse;
    }

    public void setTimelapse(int timelapse) {
        this.timelapse = timelapse;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
