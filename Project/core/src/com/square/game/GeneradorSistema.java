package com.square.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.square.config.GameConfig;
import com.square.entity.MrCuadrado;
import com.square.entity.Obstaculo;
import com.square.entity.Terreno;
import com.square.screen.CuadradoGame;
import com.square.util.Property;

import java.util.Random;

/**
 * Created by Tizon on 04/07/2015.
 */
public class GeneradorSistema {

    private static final Color NEGRO = new Color(0f, 0f, 0f, 0);;
    private static final Color BLANCO = new Color(0.9f, 0.9f, 0.9f, 0);
    private static final Color COLOR1OBS =new Color(1f, 0.6f, 0, 0); ;
    private static final Color COLOR2OBS =new Color(0, 0.2f, 0.8f, 0) ;
    private static final Color COLOR3OBS = new Color(0, 0.6f, 1f, 0);

    protected Random random;
    protected CuadradoGame game;
    protected Sound sound;
    protected Array<Obstaculo> listaExplotar;
    protected MrCuadrado mrCuadrado;
    protected Terreno terreno;
    protected Array<Obstaculo> obstaculos;
    protected Array<Obstaculo> particulas;
    protected int numHijos;
    protected int numPartHijos;
    protected long maxColisiones;
    protected long deltaTiempo;
    protected Property generalproperty;
    protected int selecol;
    protected Property p;
    protected final Pool<Obstaculo> obstaculosPool = new Pool<Obstaculo>() {
        @Override
        protected Obstaculo newObject() {
            return new Obstaculo();
        }
    };


    public GeneradorSistema(Terreno terreno, MrCuadrado mrCuadrado,CuadradoGame game) {
        this.game=game;
        obstaculos = new Array();
        particulas = new Array();
        deltaTiempo = 0;
        this.mrCuadrado = mrCuadrado;
        this.terreno = terreno;
        generalproperty = new Property();
        generalproperty.setColor(GameConfig.colorObstaculos);
        maxColisiones = GameConfig.maxtiempoDeVidaObstaculo;
        numHijos = GameConfig.numeroHijosObstaculo;
        numPartHijos = (int) (GameConfig.numeroParticulasiniciales/2);
        listaExplotar = new Array();
        selecol = 0;
        random = new Random();
        p = new Property();
    }

    public void crearObstaculo(float x, float y, float width, float height, Property property, int velocidad, boolean hijo) {
        int direccionx = (int) (aumentoRandom(velocidad) % 2);
        int direcciony = (int) (aumentoRandom(velocidad) % 2);
        direccionx = direccionx == 0 ? -1 : 1;
        direcciony = direcciony == 0 ? -1 : 1;
        float rndvelocidad = velocidad/2;
        if (obstaculos.size>2){
           rndvelocidad= aumentoRandom(velocidad);
        }
        rndvelocidad = rndvelocidad <= 0.5f ? aumentoRandom(velocidad*2) : rndvelocidad;//en caso que la velocidad inicial sea muy pequeña, se aleatoriza
        Obstaculo obs = obstaculosPool.obtain();
        obs.init(x, y, width, height, rndvelocidad * direccionx, rndvelocidad * direcciony, property, hijo);
        obs.setTexture(GeneradorTextura.getObstaculoRND());
        obstaculos.add(obs);
    }

    public void crearParticula(float x, float y, float width, float height, Property property, int velocidad) {
        int direccionx = (int) (aumentoRandom(velocidad) % 2);
        int direcciony = (int) (aumentoRandom(velocidad) % 2);
        direccionx = direccionx == 0 ? -1 : 1;
        direcciony = direcciony == 0 ? -1 : 1;
        Obstaculo obs = obstaculosPool.obtain();
        obs.init(x, y, width, height, aumentoRandom(velocidad) * direccionx, aumentoRandom(velocidad) * direcciony, property, false);
        particulas.add(obs);
    }

    public void limpiarGenerador() {
        deltaTiempo = Tiempo.getTiempo() + 600;
        getParticulas().clear();
        getObstaculos().clear();
        obstaculosPool.clear();
    }

    /*
    metodo llamado desde otra clase
     */
    public Array generarMovimiento(Array<Obstaculo> listObj, int dif, boolean notify) {
        float obx = 0;
        float oby = 0;
        float colisionx = 0;
        float colisiony = 0;
        boolean colisionc;
        float x = 0;
        float y = 0;
        listaExplotar.clear();
        for (Obstaculo obs : listObj) {
            obx = obs.getPosx() + (obs.getWidth() / 2);
            oby = obs.getPosy() + (obs.getHeight() / 2);
            // a futuro generalizar
            colisionx = Colisionador.esColisionXTerreno(obx, obs.getWidth(), terreno);
            colisiony = Colisionador.esColisionYTerreno(oby, obs.getHeight(), terreno);
            colisionc = mrCuadrado != null ? Colisionador.esColisionCuadrado(obs, mrCuadrado) : false;
            updateColor(notify, obs);
            updateColision(colisionx, colisiony, colisionc, obs, notify, dif);
            updateExplosion(notify, obs);
            x = obs.getPosx() + obs.getAumentoX();
            y = obs.getPosy() + obs.getAumentoY();
            obs.update(x, y);
        }
        explotarObjetos();
        if(obstaculos.size>7){
            int i = 1;
            for(Obstaculo obs:obstaculos){
                obs.setColisiones(GameConfig.maxColisionesObstaculo);
                if(i<=0)break;
                i--;
            }
        }
        game.getGeneradorPoderes().updatePoderes();
        return listObj;
    }

    protected void updateExplosion(boolean notify, Obstaculo obs) {
        if (notify && (obs.getColisiones() >= GameConfig.maxColisionesObstaculo || obs.getVida() >= GameConfig.maxtiempoDeVidaObstaculo)) {
            listaExplotar.add(obs);
        }
    }

    protected void updateColor(boolean notify, Obstaculo obs) {
        if (notify) {
            obs.updateVida();
        }
    }

    protected void updateColision(float colisionx, float colisiony, boolean colisionc, Obstaculo obs, boolean notify, int dif) {
        //normalizar
        float rndvelocidad = notify?dif:aumentoRandom(dif);
        if (colisionx != 0) {
            colisionx = colisionx > 0 ? -1 : 1;
            if (obstaculos.size>2){
                rndvelocidad= aumentoRandom(dif);
            }
            obs.setAumentoX(rndvelocidad * colisionx);
            if (notify) {
                obs.updateColision();
                GeneradorSonido.notifyColision();
            }

        }
        if (colisiony != 0) {
            colisiony = colisiony > 0 ? -1 : 1;
            rndvelocidad = notify?dif:aumentoRandom(dif);
            if (obstaculos.size>=2){
                rndvelocidad= aumentoRandom(dif);
            }
            obs.setAumentoY(rndvelocidad * colisiony);
            if (notify) {
                obs.updateColision();
                GeneradorSonido.notifyColision();
            }
        }
        if (colisionc) {

            if (mrCuadrado!=null && !mrCuadrado.isGameover()) {
                if (notify) {
                    mrCuadrado.notifyColission(obs, this);
                    game.getGeneradorOndas().createWave(mrCuadrado.getPosx(),mrCuadrado.getPosy(),mrCuadrado.getProperty(),GameConfig.aumentoOndaMR);
                    //Gdx.app.log("input", "colisiono " + mrCuadrado.getPosy() + "$$" + mrCuadrado.getPosx());
                }
                obs.setAumentoX((obs.getAumentoX() * ((mrCuadrado.getDeltax() / 100) + 1)) * -1);
                obs.setAumentoY((obs.getAumentoY() * ((mrCuadrado.getDeltay() / 100) + 1)) * -1);
            }
        }
    }

    protected void explotarObjetos() {
        for (Obstaculo obs : listaExplotar) {
            if (obs.isHijo()) {
                GeneradorSonido.notifyCrashParticle();
                GeneradorExplosivo.explotar(obs, numPartHijos, this);
                if(game!=null) {
                    game.getGeneradorMonedas().generarMonedas(obs);
                    game.getGeneradorOndas().createWave(obs.getPosx(), obs.getPosy(), obs.getProperty(), GameConfig.aumentoOndaObs);
                }
                obstaculos.removeValue(obs, false);
                obstaculosPool.free(obs);
            } else {
                GeneradorExplosivo.partirObstaculo(obs, numHijos, this);
                obstaculos.removeValue(obs, false);
                obstaculosPool.free(obs);

            }
        }
        //System.gc();
    }


   public void limpiarParticulas() {
        if (particulas.size >= GameConfig.maximonumeroParticulas) {
            //
            // Gdx.app.log("generadors", "limpiando particulas...");
            int borrar = (int) (particulas.size - GameConfig.numeroParticulasiniciales) / 3;
            for(int i = 0;i<borrar;i++){
                Obstaculo obs = particulas.get(i);
                particulas.removeValue(obs,false);
                obstaculosPool.free(obs);
            }


        }
    }

    public void aumentarNivel() {
        if ((Tiempo.getTiempo() >= deltaTiempo||obstaculos.size == 0) && mrCuadrado != null && !mrCuadrado.isGameover()) {
            //creal el juego
            p.setColor(GameConfig.colorObstaculos);
            float x = (terreno.getWidth() - mrCuadrado.getPosx()) < terreno.getWidth() / 2 ? 0 : terreno.getWidth() - GameConfig.obstaculowidth;
            float y = (terreno.getHeight() - mrCuadrado.getPosy()) < terreno.getHeight() / 2 ? 0 : terreno.getHeight() - GameConfig.obstaculoheight;
            int velocidad =  GameConfig.dificultadGeneradorObstaculos >= 30?30:GameConfig.dificultadGeneradorObstaculos;
            crearObstaculo(x, y, GameConfig.obstaculowidth, GameConfig.obstaculoheight, p,velocidad, false);
            Gdx.app.log("generadorsistema nivel dificultad", GameConfig.tiempoSalidaObstaculo + "##" + GameConfig.dificultadGeneradorObstaculos);
            GameConfig.dificultadGeneradorObstaculos += GameConfig.aumentoDificultad;
            numHijos =  GameConfig.dificultadGeneradorObstaculos < 20 ? 4 : 2;
            deltaTiempo = Tiempo.getTiempo() + GameConfig.tiempoSalidaObstaculo;
            selecol++;
            if (selecol > 3) selecol = 0;
            switch (selecol) {
                case 0:
                    GameConfig.colorObstaculos = COLOR1OBS;
                    break;
                case 1:
                    GameConfig.colorObstaculos = COLOR2OBS;
                    break;
                case 2:
                    GameConfig.colorObstaculos = COLOR3OBS;
                    break;
            }
        }


    }

    public CuadradoGame getGame() {
        return game;
    }

    public void setGame(CuadradoGame game) {
        this.game = game;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public Array<Obstaculo> getListaExplotar() {
        return listaExplotar;
    }

    public void setListaExplotar(Array<Obstaculo> listaExplotar) {
        this.listaExplotar = listaExplotar;
    }

    public long getDeltaTiempo() {
        return deltaTiempo;
    }

    public void setDeltaTiempo(long deltaTiempo) {
        this.deltaTiempo = deltaTiempo;
    }

    public Property getGeneralproperty() {
        return generalproperty;
    }

    public void setGeneralproperty(Property generalproperty) {
        this.generalproperty = generalproperty;
    }

    public int getSelecol() {
        return selecol;
    }

    public void setSelecol(int selecol) {
        this.selecol = selecol;
    }

    public float aumentoRandom(int dif) {
        return (float) Math.random() * 1000 % dif;
    }

    public Array<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public void setObstaculos(Array<Obstaculo> obstaculos) {
        this.obstaculos = obstaculos;
    }

    public Array<Obstaculo> getParticulas() {
        return particulas;
    }

    public void setParticulas(Array<Obstaculo> particulas) {
        this.particulas = particulas;
    }

    public MrCuadrado getMrCuadrado() {
        return mrCuadrado;
    }

    public void setMrCuadrado(MrCuadrado mrCuadrado) {
        this.mrCuadrado = mrCuadrado;
    }

    public Terreno getTerreno() {
        return terreno;
    }

    public void setTerreno(Terreno terreno) {
        this.terreno = terreno;
    }

    public int getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(int numHijos) {
        this.numHijos = numHijos;
    }

    public int getNumPartHijos() {
        return numPartHijos;
    }

    public void setNumPartHijos(int numPartHijos) {
        this.numPartHijos = numPartHijos;
    }

    public long getMaxColisiones() {
        return maxColisiones;
    }

    public void setMaxColisiones(long maxColisiones) {
        this.maxColisiones = maxColisiones;
    }


}
