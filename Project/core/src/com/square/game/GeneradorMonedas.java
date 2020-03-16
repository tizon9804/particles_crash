package com.square.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
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
public class GeneradorMonedas extends GeneradorSistema {

    private long totalMonedas;
    private long monedas;
    private Json json;
    private long spend;

    public GeneradorMonedas(Terreno terreno, MrCuadrado mrCuadrado, CuadradoGame game) {
        super(terreno, mrCuadrado, game);
        monedas = 0;
        spend = 0;
        totalMonedas = 0;
        json = new Json();
        game.getGooPlayGames().loadRicherOfLeaderBoard();
        String val = Persistence.readFile(GameConfig.nameMonedas);
        if (!val.isEmpty()) {
            totalMonedas=Integer.parseInt(val);
        } else {
            totalMonedas = 0;
        }
    }
    public void restaurarMonedas() {
        String sp = Persistence.readFile("spend");
        if(!sp.isEmpty()) spend = Long.parseLong(sp);
        String val = Persistence.readFile(GameConfig.nameMonedas);
        //todo
        String cloudRecordSaved = Persistence.readFile("cloudtodo"+GameConfig.nameMonedas);
        long cloudRecord = 0;
        if (!cloudRecordSaved.isEmpty()) {
            try {
                cloudRecord = Long.parseLong(cloudRecordSaved);
            } catch (Exception e) {
                Gdx.app.log("Puntaje richer", "something was wrong, trying to fix");
                Persistence.writeFile(json.toJson(0), GameConfig.nameMonedas);
            }
        }
        if (!val.isEmpty()) {
            totalMonedas=Integer.parseInt(val);
        } else {
            totalMonedas = 0;
        }
        // obtain the record in cloud
        Gdx.app.log("Puntaje richer en la nube", cloudRecord + "!!");
        if (cloudRecord > totalMonedas) {
            totalMonedas = cloudRecord;
            Persistence.writeFile(json.toJson(totalMonedas), GameConfig.nameMonedas);
        } else if (cloudRecord < totalMonedas) {
            game.getGooPlayGames().submitRicherGPGS(totalMonedas);
        }
    }


    public void crearMoneda(float x, float y, float width, float height, Property property, int velocidad, boolean hijo) {
        int direccionx = (int) (aumentoRandom(velocidad) % 2);
        int direcciony = (int) (aumentoRandom(velocidad) % 2);
        direccionx = direccionx == 0 ? -1 : 1;
        direcciony = direcciony == 0 ? -1 : 1;
        Obstaculo obs =obstaculosPool.obtain();
        obs.init(x, y, width, height, aumentoRandom(velocidad) * direccionx, aumentoRandom(velocidad) * direcciony, property, hijo);
        obs.setAnimation(new Animation(GameConfig.mrCuadradoAnimationTime,GeneradorTextura.getCoinTextures()));
        obstaculos.add(obs);
    }

    protected void crearEstelaParticulas(Obstaculo obs, int dif, boolean notify) {

    }

    public void generarMonedas(Obstaculo obs) {
        if (!mrCuadrado.isGameover()) {
            int value=Math.abs(random.nextInt(GameConfig.maxMonedasPorObstaculo));
            p.setColor(GameConfig.colorMoneda);
            float h = 0;
            float v = 0;
            for(int i=0;i<value;i++) {
                h =  GameConfig.mrCuadradowidthI *0.25f;
                v =  GameConfig.mrCuadradowidthI *0.25f;
                if (value % 2 == 0) {
                    v = v *(i*1.5f);
                    h= 0;
                } else {
                    h = h *(i*1.5f);
                    v= 0;
                }
                crearMoneda(obs.getPosx()+h, obs.getPosy()+v, GameConfig.mrCuadradowidthI / 3, GameConfig.mrCuadradoheightI / 3, p, GameConfig.velocidadNormalMonedas, true);
            }
        }
    }

    public Array generarMovimiento(Array<Obstaculo> listObj, int dif, boolean notify) {
        float colisionx = 0;
        float colisiony = 0;
        boolean colisionc;
        float x = 0;
        float y = 0;
        listaExplotar.clear();
        for (Obstaculo obs : listObj) {
            // a futuro generalizar
            //colisionx = Colisionador.esColisionXTerreno(obx, obs.getWidth(), terreno);
            //colisiony = Colisionador.esColisionYTerreno(oby, obs.getHeight(), terreno);
            colisionc = mrCuadrado != null ? Colisionador.esColisionCuadrado(obs, mrCuadrado) : false;
            updateVida(notify, obs);
            updateColision(colisionx, colisiony, colisionc, obs, notify, dif);
            updateExplosion(notify, obs);
            x = obs.getPosx();
            y = obs.getPosy();
            obs.update(x, y);
        }
        explotarObjetos();
        return listObj;
    }
    @Override
    public void limpiarGenerador() {
        super.limpiarGenerador();
        monedas = 0;
    }

    protected void updateExplosion(boolean notify, Obstaculo obs) {
        if (notify && (obs.getColisiones() >= GameConfig.maxColisionesObstaculo || obs.getVida() >= GameConfig.maxtiempoDeVidaMonedas)) {
            listaExplotar.add(obs);
        }
    }


    protected void explotarObjetos() {
        for (Obstaculo obs : listaExplotar) {
            if (obs.isHijo()) {
                obstaculos.removeValue(obs, false);
                obstaculosPool.free(obs);
            } else {
                obstaculos.removeValue(obs, false);
                obstaculosPool.free(obs);
            }
        }
    }

    protected void updateVida(boolean notify, Obstaculo obs) {
        if (notify) {
            obs.updateVida();
        }

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
                    obs.setColisiones(GameConfig.maxColisionesObstaculo);
                    //obtain new money
                    monedas++;
                    totalMonedas++;
                    GeneradorSonido.notifyPickUpCoin();
                }
                obs.setAumentoX((obs.getAumentoX() * ((mrCuadrado.getDeltax() / 100) + 1)) * -1);
                obs.setAumentoY((obs.getAumentoY() * ((mrCuadrado.getDeltay() / 100) + 1)) * -1);
            }
        }
    }

    public void saveCoins(){
        Persistence.writeFile(json.toJson(totalMonedas), GameConfig.nameMonedas);
    }

    public long getTotalMonedas() {
        return totalMonedas;
    }

    public void setTotalMonedas(long totalMonedas) {
        this.totalMonedas = totalMonedas;
        Persistence.writeFile(json.toJson(totalMonedas), GameConfig.nameMonedas);

    }

    public static void updateAmountCoins(Array<Poder> poderes){
        int nummejoras = 0;
        for(Poder poder:poderes) {
            nummejoras += (poder.getLevel() - 1);
        }
        GeneradorPoderes.numMejoras = nummejoras;
        Gdx.app.log("agregar poderes#","#######"+ nummejoras);
        GameConfig.maxMonedasPorObstaculo = 5 + nummejoras;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    public long getSpend() {
        return spend;
    }

    public void setSpend(long spend) {
        this.spend = spend;
        Persistence.writeFile(json.toJson(this.spend),"spend");
    }

    public long getMonedas() {
        return monedas;
    }

    public void setMonedas(long monedas) {
        this.monedas = monedas;
    }
}
