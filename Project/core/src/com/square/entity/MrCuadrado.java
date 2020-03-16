package com.square.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.square.Menus.MainMenu;
import com.square.config.GameConfig;
import com.square.game.Colisionador;
import com.square.game.GeneradorExplosivo;
import com.square.game.GeneradorMonedas;
import com.square.game.GeneradorSistema;
import com.square.game.GeneradorSonido;
import com.square.game.GeneradorTextura;
import com.square.game.Tiempo;
import com.square.screen.CuadradoGame;
import com.square.util.Property;


/**
 * Created by Tizon on 04/07/2015.
 */
public class MrCuadrado extends CuadradoBase {

    private Terreno terreno;
    private float deltax;
    private float deltay;
    private static boolean gameover;
    private long gameoverTime;
    private boolean desAc;
    private int reductor;
    private float lastx;
    private float lasty;


    public MrCuadrado(float x, float y, float width, float height, Terreno terreno) {
        posx = x;
        posy = y;
        lastx = x;
        lasty = y;
        this.width = width;
        this.height = height;
        property = new Property();
        property.setColor(GameConfig.mrCuadradoColor);
        this.terreno = terreno;
        rect = new Rectangle();
        reductor = GameConfig.mrCuadradoReductor;
        gameover = true;
        updateParameters();
        animation = new Animation(GameConfig.mrCuadradoAnimationTime, GeneradorTextura.mrCuadradoDTextures);
    }


    public void update(float x, float y, float dx, float dy) {
        float xt = x-lastx;
        float yt = y-lasty;
        desAc = false;
        if (validate) {
            float tolerancia = GameConfig.mrCuadradowidth / 2;
            float difx = Colisionador.esColisionXTerreno(xt, width, terreno);
            float dify = Colisionador.esColisionYTerreno(yt, height, terreno);
            boolean agrandarx = width < GameConfig.mrCuadradowidth + (tolerancia * 1.5) ? true : false;
            boolean agrandary = height < GameConfig.mrCuadradoheight + (tolerancia * 1.5) ? true : false;
            aplastar(difx, dify, tolerancia, xt, yt, dx, dy, agrandarx, agrandary, terreno,true);
            validateSize();
            //Gdx.app.log("mrcuadrado", "++"+posx);
        }
        updateParameters();


    }

    public void validateSize() {
        if (width > GameConfig.mrCuadradowidth && height > GameConfig.mrCuadradoheight) {
            width = GameConfig.mrCuadradowidth;
            height = GameConfig.mrCuadradoheight;
        }
    }

    public void updateLocalParameters(float xt,float yt){
        if(GameConfig.mrCuadradowidth != GameConfig.mrCuadradowidthActual||GameConfig.mrCuadradoheight!=GameConfig.mrCuadradoheightActual){
            GameConfig.mrCuadradowidth = GameConfig.mrCuadradowidthActual;
            GameConfig.mrCuadradoheight = GameConfig.mrCuadradoheightActual;
            float tolerancia = GameConfig.mrCuadradowidth / 2;
            float difx = Colisionador.esColisionXTerreno(xt, width, terreno);
            float dify = Colisionador.esColisionYTerreno(yt, height, terreno);
            boolean agrandarx = width < GameConfig.mrCuadradowidth + (tolerancia * 1.5) ? true : false;
            boolean agrandary = height < GameConfig.mrCuadradoheight + (tolerancia * 1.5) ? true : false;
            aplastar(difx, dify, tolerancia, xt, yt, 0, 0, agrandarx, agrandary, terreno,false);
            validateSize();
            updateParameters();
        }

    }



    private void aplastar(float difx, float dify, float tolerancia, float x, float y, float dx, float dy, boolean agrandarx, boolean agrandary, Terreno terreno,boolean isHuman) {
        //valida que choque a un lado a la vez
        if (difx == 0 || dify == 0) {
            //ajusta el dedo en el centro
           // Gdx.app.log("mr cuadradopx",posx+"##w"+width+"##h"+height+"##x"+x+"##y"+y);
            if (difx == 0 && isHuman) {
                posx = x - (width / 2);
                deltax = Math.abs(dx);
            }
            //lado izquierdo de la pantalla y se aplasta hasta la tolerancia en x y si en y se puede seguir agrandando
            else if (difx < 0 && width >= tolerancia && agrandary) {
                //ya que hay choque posiciona siempre el bloque en el borde del terreno-- sucede que si mueve el dedo muy rapido el bloque se aplasta antes de tocar el borde
                posx = terreno.getPosx();
                height += -difx;
                width -= -difx / 2;
            }
            //lado derecho de la pantalla
            else if (difx > 0 && width >= tolerancia && agrandary) {
                //posiciona el bloque en el borde del terreno si alcanza la toleranci maxima
                float tempwidth = width-difx / 2;
                posx = width <= tolerancia ? terreno.getPosx() + terreno.width-tempwidth : x - (width / 2);
                height += difx;
                width = tempwidth;
            }


            //mismos pasos pero en y
            if (dify == 0 && isHuman) {
                posy = y - (height / 2);
                deltay = Math.abs(dy);
            } else if (dify < 0 && height >= tolerancia && agrandarx) {
                posy = terreno.getPosy();
                width += -dify;
                height -= -dify / 2;
            } else if (dify > 0 && height >= tolerancia && agrandarx) {
                float tempheight = height - dify / 2;
                posy = height == tolerancia ? terreno.getPosy() + terreno.getHeight() - tempheight : y - (height / 2);
                width += dify;
                height = tempheight;
            }


            // caso no choca con nada genera valores por default
            if (difx == 0 && dify == 0) {
                width = GameConfig.mrCuadradowidth;
                height = GameConfig.mrCuadradoheight;
            }
            if (width <= tolerancia * 1.5 || height <= tolerancia * 1.5) {
                GeneradorSonido.notifySpinIn();
            } else if (width == GameConfig.mrCuadradowidth && height == GameConfig.mrCuadradoheight) {
                GeneradorSonido.notifySpinOut();
            }
        }
        float tolv=tolerancia*1f;
        float tolh=tolerancia*3.2f;
        if(x >= terreno.getPosx()+terreno.getWidth()){
            width = tolv ;
            height = tolh;
            posx = terreno.getPosx()+terreno.getWidth()-(width);
        }
        if(x <= terreno.getPosx()){
            posx = terreno.getPosx();
            width = tolv;
            height = tolh;
        }
        if(y >= terreno.getPosy()+terreno.getHeight()){
            height = tolv;
            width = tolh;
            posy = terreno.getPosy() + terreno.getHeight()-(height);
        }
        if(y <= terreno.getPosy()){
            posy = terreno.getPosy();
            height = tolv;
            width = tolh;
        }

    }

    /*
    /valida que el movimiento inicie dentro del cuadrado
     */
    public void validador(float x, float y) {
        Vector2 vector = new Vector2(x, y);
        Rectangle rect = new Rectangle(posx, posy, width, height);
        //if (rect.contains(x, y)) {
            animation = new Animation(GameConfig.mrCuadradoAnimationTime, GeneradorTextura.mrCuadradoTextures);
            validate = true;
            lastx = x - posx-(width/2);
            lasty = y - posy-(height/2);
        //  } else {
        //    validate = false;
        //}
    }

    public void notifyColission(Obstaculo obs, GeneradorSistema gen) {
        gameover = true;
        MainMenu.started = false;
        gameoverTime = Tiempo.getTiempo();
        GeneradorSonido.notifyCrash();
        GeneradorExplosivo.explotarMrCuadrado(this, (int) (GameConfig.numeroParticulasiniciales), gen);
        CuadradoGame.adI();
    }

    public void animate() {
    }

    public void desAcelerar() {
        animation = new Animation(GameConfig.mrCuadradoAnimationTime, GeneradorTextura.mrCuadradoDTextures);
        desAc = true;
    }

    public float getDeltax() {
        return deltax;
    }

    public void setDeltax(float deltax) {
        this.deltax = deltax;
    }

    public float getDeltay() {
        return deltay;
    }

    public void setDeltay(float deltay) {
        this.deltay = deltay;
    }

    public static boolean isGameover() {
        return gameover;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public long getGameoverTime() {
        return gameoverTime;
    }

    public void setGameoverTime(long gameoverTime) {
        this.gameoverTime = gameoverTime;
    }


}
