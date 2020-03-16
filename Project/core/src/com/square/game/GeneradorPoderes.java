package com.square.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.square.config.GameConfig;
import com.square.entity.MrCuadrado;
import com.square.entity.Obstaculo;
import com.square.entity.Poder;
import com.square.entity.Terreno;
import com.square.persistence.Persistence;
import com.square.screen.CuadradoGame;
import com.square.util.Property;

import java.util.Random;

/**
 * Created by Tizon on 12/01/2016.
 */
public class GeneradorPoderes extends GeneradorSistema {


    public static int numMejoras;
    protected Array<Poder> poderes;
    protected Array poderesObs;
    protected Poder congelante;
    protected Poder multiplicador;
    protected Poder boom;
    protected Poder chiquitolin;
    private Json json;
    private int usedPower;
    private int maxMejoras;

    public GeneradorPoderes(Terreno terreno, MrCuadrado mrCuadrado, CuadradoGame game) {
        super(terreno, mrCuadrado, game);
        //poderes
        poderesObs = new Array();
        numMejoras = 0;
        crearPoderes();


    }

    public void crearPoderes() {
        String tmppoderes = Persistence.readFile(GameConfig.namePoderes);
        json = new Json();
       // Gdx.app.log("poderes", tmppoderes.isEmpty() + "##\n" + tmppoderes);
        if(!tmppoderes.isEmpty()){
            poderes = json.fromJson(Array.class,tmppoderes);
        }
        else {
            poderes = new Array();
            congelante = new Poder(GameConfig.CONGELANTE, 1, GameConfig.minTiempoPoderCongelante,100);
            multiplicador = new Poder(GameConfig.MULTIPLICADOR, 1, GameConfig.minTiempoPoder,300);
            boom = new Poder(GameConfig.EXPLOSIVO, 1, GameConfig.minTiempoPoder,100);
            chiquitolin = new Poder(GameConfig.CHIQUITOLIN, 1, GameConfig.timeLapseChiquitolin,50);
            poderes.add(multiplicador);
            poderes.add(congelante);
            poderes.add(boom);
            poderes.add(chiquitolin);
            Persistence.writeFile(json.toJson(poderes),GameConfig.namePoderes);
        }

        GeneradorMonedas.updateAmountCoins(poderes);
    }

    public void savePoderes(Array poderes){
        Persistence.writeFile(json.toJson(poderes),GameConfig.namePoderes);
    }

    public TextureRegion darTexturaTipo(String type){
        if (type.equals(GameConfig.EXPLOSIVO)) {
            return GeneradorTextura.getPoderTextures().get(0);
        }
        else if(type.equals(GameConfig.CONGELANTE)){
            return GeneradorTextura.getPoderTextures().get(1);
        }
        else if(type.equals(GameConfig.CHIQUITOLIN)){
            return GeneradorTextura.getPoderTextures().get(2);
        }
        else if(type.equals(GameConfig.MULTIPLICADOR)){
           return GeneradorTextura.getPoderTextures().get(3);
        }
        return GeneradorTextura.terrenoTexture;
    }

    public void crearPoder(float x, float y, float width, float height, Property property, int velocidad, boolean hijo) {
        int direccionx = (int) (aumentoRandom(velocidad) % 2);
        int direcciony = (int) (aumentoRandom(velocidad) % 2);
        direccionx = direccionx == 0 ? -1 : 1;
        direcciony = direcciony == 0 ? -1 : 1;
        Poder poder = (Poder) poderes.random();
        Obstaculo obs =obstaculosPool.obtain();
        obs.init(x, y, width, height, aumentoRandom(velocidad) * direccionx, aumentoRandom(velocidad) * direcciony, property, hijo);
        obs.setTexture(darTexturaTipo(poder.getType()));
        obstaculos.add(obs);
        poderesObs.add(poder);
    }

    public void aumentarNivel() {
        if (Tiempo.getTiempo() >= deltaTiempo && !mrCuadrado.isGameover() && game.getGeneradorSistema().getObstaculos().size >= GameConfig.numObsAparicionPoder) {
            int value=Math.abs(random.nextInt(100));
            //Gdx.app.log("poderes",value+":");
            if (obstaculos.size < 1 && value<=GameConfig.probaPoder) {
                p.setColor(GameConfig.colorParticulas);
                float x = (terreno.getWidth() - mrCuadrado.getPosx()) < terreno.getWidth() / 2 ? aumentoRandom((int) terreno.getWidth()/2 ) : terreno.getWidth() - aumentoRandom((int) terreno.getWidth()/2 ) ;
                float y = (terreno.getHeight() - mrCuadrado.getPosy()) < terreno.getHeight() / 2 ? aumentoRandom((int) terreno.getHeight()/2 ) : terreno.getHeight() -aumentoRandom((int) terreno.getHeight()/2 );
                crearPoder(x, y, GameConfig.mrCuadradowidthI / 2, GameConfig.mrCuadradoheightI / 2, p, GameConfig.velocidadNormalParticulas, true);
                GameConfig.probaPoder=GameConfig.probaPoder<=20?20:GameConfig.probaPoder--;
            }
            deltaTiempo = (long) (Tiempo.getTiempo() + GameConfig.tiempoSalidaObstaculo);
        }
    }

    public Array generarMovimiento(Array<Obstaculo> listObj, int dif, boolean notify) {
        float obx = 0;
        float oby = 0;
        float colisionx = 0;
        float colisiony = 0;
        boolean colisionc;
        float x = 0;
        float y = 0;
        listaExplotar.clear();
        if(listObj.size==0){
            particulas.clear();
        }
        for (Obstaculo obs : listObj) {
            crearEstelaParticulas(obs, dif, notify);
            obx = obs.getPosx() + (obs.getWidth() / 2);
            oby = obs.getPosy() + (obs.getHeight() / 2);
            // a futuro generalizar
            colisionx = Colisionador.esColisionXTerreno(obx, obs.getWidth(), terreno);
            colisiony = Colisionador.esColisionYTerreno(oby, obs.getHeight(), terreno);
            colisionc = mrCuadrado != null ? Colisionador.esColisionCuadrado(obs, mrCuadrado) : false;
            updateVida(notify, obs);
            updateColision(colisionx, colisiony, colisionc, obs, notify, dif);
            updateExplosion(notify, obs);
            x = obs.getPosx() + obs.getAumentoX();
            y = obs.getPosy() + obs.getAumentoY();
            obs.update(x, y);
        }
        explotarObjetos();
        return listObj;
    }

    protected void updateVida(boolean notify, Obstaculo obs) {
        if (notify) {
            obs.updateVida();
        }
    }

    public void updatePoderes() {
        if(MrCuadrado.isGameover()){
            GameConfig.mrCuadradoheight = GameConfig.mrCuadradoheightI;
            GameConfig.mrCuadradowidth = GameConfig.mrCuadradowidthI;
            mrCuadrado.setWidth(GameConfig.mrCuadradowidth);
            mrCuadrado.setHeight(GameConfig.mrCuadradoheight);
        }
        else if (GameConfig.timeLapseChiquitolin > 0) {
            //Gdx.app.log("chiquitolin#########",GameConfig.timeLapseChiquitolin+"########"+GameConfig.chiquitolinSize);
            GameConfig.timeLapseChiquitolin--;
            GameConfig.mrCuadradoheightActual = GameConfig.mrCuadradoheightActual<=GameConfig.mrCuadradoheightI*GameConfig.chiquitolinSize?GameConfig.mrCuadradoheightI*GameConfig.chiquitolinSize:GameConfig.mrCuadradoheightActual-2;
            GameConfig.mrCuadradowidthActual = GameConfig.mrCuadradowidthActual<=GameConfig.mrCuadradowidthI*GameConfig.chiquitolinSize?GameConfig.mrCuadradowidthI*GameConfig.chiquitolinSize:GameConfig.mrCuadradowidthActual-2;
        }
        else{
            GameConfig.mrCuadradoheightActual = GameConfig.mrCuadradoheightActual>=GameConfig.mrCuadradoheightI?GameConfig.mrCuadradoheightI:GameConfig.mrCuadradoheightActual+1;
            GameConfig.mrCuadradowidthActual = GameConfig.mrCuadradowidthActual>=GameConfig.mrCuadradowidthI?GameConfig.mrCuadradowidthI:GameConfig.mrCuadradowidthActual+1;
        }
        mrCuadrado.updateLocalParameters(mrCuadrado.getPosx(),mrCuadrado.getPosy());
        if(GameConfig.timeLapsePoderCongelante>0){
           // GameConfig.dificultadGeneradorObstaculos += GameConfig.aumentoDificultad;
            GameConfig.timeLapsePoderCongelante--;
        }
        else {
            GameConfig.timeLapsePoderCongelante = 0;
            GameConfig.aumentoDificultad = GameConfig.aumentoDificultadInicial;
        }
        if(Tiempo.getAumento()== GameConfig.aumentoDificultadInicial && (deltaTiempo-Tiempo.getTiempo()>= GameConfig.tiempoSalidaObstaculo * 3)){
            deltaTiempo = Tiempo.getTiempo() + GameConfig.tiempoSalidaObstaculo;
        }
    }

    public void limpiarParticulas() {
        if (particulas.size >= GameConfig.maximonumeroParticulas / 10) {
            int borrar = (int) (particulas.size - GameConfig.numeroParticulasiniciales / 10) / 3;
            particulas.removeRange(0, borrar);
        }
    }

    protected void crearEstelaParticulas(Obstaculo obs, int dif, boolean notify) {
        if (notify) {
            if (particulas.size < GameConfig.maximonumeroParticulas / 10) {
                crearParticula(obs.getPosx() + obs.getWidth() / 2, obs.getPosy() + obs.getHeight() / 2, aumentoRandom(GameConfig.maximoTamanoParticulas), aumentoRandom(GameConfig.maximoTamanoParticulas), mrCuadrado.getProperty(), dif);
            } else {
                for (Obstaculo part : particulas) {
                    part.setPosx(obs.getPosx());
                    part.setPosy(obs.getPosy());
                }
            }
        }
    }

    protected void updateExplosion(boolean notify, Obstaculo obs) {
        if (notify && (obs.getVida() >= GameConfig.maxTiempoPoder)) {
            listaExplotar.add(obs);
        }
    }

    protected void updateColor(boolean notify, Obstaculo obs) {
        if (notify) {
            obs.updateVida();
        } else {
            obs.getProperty().setColor(changeColor(obs));
        }

    }
    public void limpiarGenerador() {
        super.limpiarGenerador();
        poderesObs.clear();
        usedPower = 0;
    }

    protected void explotarObjetos() {
        for (Obstaculo obs : listaExplotar) {
            if (obs.isHijo()) {
                poderesObs.removeIndex(obstaculos.indexOf(obs, false));
                obstaculos.removeValue(obs, false);
                obstaculosPool.free(obs);
            } else {
                GeneradorExplosivo.partirObstaculo(obs, numHijos, this);
                obstaculos.removeValue(obs, false);
                obstaculosPool.free(obs);

            }
        }
    }

    protected Color changeColor(Obstaculo obs) {
        return mrCuadrado.getProperty().getColor();
    }

    protected void updateColision(float colisionx, float colisiony, boolean colisionc, Obstaculo obs, boolean notify, int dif) {
        //normalizar
        if (colisionx != 0) {
            colisionx = colisionx > 0 ? -1 : 1;
            obs.setAumentoX(aumentoRandom(dif) * colisionx);
            if (notify) {
                obs.updateColision();
                GeneradorSonido.notifyColision();
            }

        }
        if (colisiony != 0) {
            colisiony = colisiony > 0 ? -1 : 1;
            obs.setAumentoY(aumentoRandom(dif) * colisiony);
            if (notify) {
                obs.updateColision();
                GeneradorSonido.notifyColision();
            }
        }
        if (colisionc) {
            if (!mrCuadrado.isGameover()) {
                if (notify) {
                    obs.setVida(GameConfig.maxTiempoPoder);
                    usedPower++;
                    ejecutarPoder(obs);
                }
                obs.setAumentoX((obs.getAumentoX() * ((mrCuadrado.getDeltax() / 100) + 1)) * -1);
                obs.setAumentoY((obs.getAumentoY() * ((mrCuadrado.getDeltay() / 100) + 1)) * -1);
            }
        }
    }

    private void ejecutarPoder(Obstaculo obs) {
        Poder poder = (Poder) poderesObs.get(obstaculos.indexOf(obs, false));
        //explota todos los obstaculos asignandole sus maximas colisiones posibles
        if (poder.getType().equals(GameConfig.EXPLOSIVO)) {
            int i = poder.getLevel()+2;
            for (Obstaculo obsta : game.getGeneradorSistema().obstaculos) {
                if(i>0){
                obsta.setColisiones(GameConfig.maxColisionesObstaculo);
                obsta.setHijo(true);
                }
                i--;
            }
            GeneradorSonido.notifyPowerUp();
        }
        //aumenta dependiendo de la mejora del poder la suma del tiempo influenciando el puntaje
        else if (poder.getType().equals(GameConfig.MULTIPLICADOR)) {
            Tiempo.setAumento(poder.getLevel() * 2, poder.getTimelapse() * 100);
            GameConfig.tiempoSalidaObstaculo *= poder.getLevel() * 2;
            deltaTiempo *= poder.getLevel() * 2;
            GeneradorSonido.notifyPowerUp();
        }
        //disminuye la velocidad de los cuadrados, disminuye el aumento de la dificultad y disminuye en dependiendo de la mejora la dificultad
        else if (poder.getType().equals(GameConfig.CONGELANTE)) {
            GameConfig.timeLapsePoderCongelante = poder.getTimelapse();
            for (Obstaculo obsta : game.getGeneradorSistema().obstaculos) {
                obsta.setAumentoX(obsta.getAumentoX() / (poder.getLevel() + 1));
                obsta.setAumentoY(obsta.getAumentoY() / (poder.getLevel() + 1));
            }
            GeneradorSonido.notifyPowerUp();
        }
        // dismuniye el tamaño del mrcuadrado y de los obstaculos dependiendo de la mejora del poder
        else if (poder.getType().equals(GameConfig.CHIQUITOLIN)) {
            GameConfig.timeLapseChiquitolin = poder.getTimelapse() * poder.getLevel();
            GameConfig.chiquitolinSize = 1-((poder.getLevel()+2)*0.1f);
            GeneradorSonido.notifyPowerUPChic();
        }
    }

    public int getUsedPower() {
        return usedPower;
    }

    public void setUsedPower(int usedPower) {
        this.usedPower = usedPower;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Array<Poder> getPoderes() {
        return poderes;
    }

    public void setPoderes(Array poderes) {
        this.poderes = poderes;
    }

    public Array getPoderesObs() {
        return poderesObs;
    }

    public void setPoderesObs(Array poderesObs) {
        this.poderesObs = poderesObs;
    }

    public Poder getCongelante() {
        return congelante;
    }

    public void setCongelante(Poder congelante) {
        this.congelante = congelante;
    }

    public Poder getMultiplicador() {
        return multiplicador;
    }

    public void setMultiplicador(Poder multiplicador) {
        this.multiplicador = multiplicador;
    }

    public Poder getBoom() {
        return boom;
    }

    public int getMaxMejoras() {
        int mayor = 0;
       for (Poder poder:poderes){
           if(mayor < poder.getLevel()-1)
               mayor = poder.getLevel()-1;
       }
        return mayor;
    }

    public void setBoom(Poder boom) {
        this.boom = boom;
    }

    public Poder getChiquitolin() {
        return chiquitolin;
    }

    public void setChiquitolin(Poder chiquitolin) {
        this.chiquitolin = chiquitolin;
    }


}
