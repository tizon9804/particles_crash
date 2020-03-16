package com.square.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.square.config.GameConfig;
import com.square.entity.CirculoBase;
import com.square.entity.MrCuadrado;
import com.square.entity.Obstaculo;
import com.square.entity.Terreno;
import com.square.game.GeneradorTextura;
import com.square.screen.CuadradoGame;

/**
 * Created by Tizon on 04/07/2015.
 */
public class Renderizador {

    private CuadradoGame game;
    public static float stateTime;
    private ShapeRenderer shape;
    private SpriteBatch batch;


    public Renderizador(CuadradoGame cuadradoGame) {
        game = cuadradoGame;
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(game.getCam().combined);
        shape.setProjectionMatrix(game.getCam().combined);
        stateTime = 0f;
    }

    public void update() {
        // no usar System.gc() reduce los fps
        game.getCam().update();//ok
        updateTerreno();//ok
        game.getPuntaje().update();//ok
        updateWaves();
        updateObstaculos(game.getGeneradorSistema().getParticulas(), GameConfig.velocidadNormalParticulas, false);
        updateObstaculos(game.getGeneradorSistema().getObstaculos(), GameConfig.dificultadGeneradorObstaculos, true);
        updatePoderes(game.getGeneradorPoderes().getObstaculos(), GameConfig.velocidadNormalParticulas, true);
        updatePoderes(game.getGeneradorPoderes().getParticulas(), GameConfig.velocidadNormalParticulas, false);
        updateMonedas(game.getGeneradorMonedas().getObstaculos(), GameConfig.velocidadNormalMonedas, true);
        updateMonedas(game.getGeneradorMonedas().getParticulas(), GameConfig.velocidadNormalMonedas, false);
        updateCuadrado();
    }

    private void updateWaves() {
        game.getGeneradorOndas().generarMovimiento();
        Array<CirculoBase> waves = game.getGeneradorOndas().getWaves();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (CirculoBase circle : waves) {
            shape.setColor(circle.getProperty().getColor());
            shape.circle(circle.getPosx(), circle.getPosy(), circle.getRad());
        }
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void updateMonedas(Array<Obstaculo> monedas, int velocidadNormalParticulas, boolean notify) {

        Array<Obstaculo> listMonedas = game.getGeneradorMonedas().generarMovimiento(monedas, velocidadNormalParticulas, notify);
        if (notify) {
            TextureRegion f;
            float width;
            float height;
            batch.begin();
            int i = 0;
            for (Obstaculo obs : listMonedas) {
                obs.setState(stateTime + (i*0.05f));
                f = obs.getAnimation().getKeyFrame(obs.getState(), true);
                width = (obs.getHeight() * f.getRegionWidth()) / (f.getRegionHeight());
                height = obs.getHeight();
                batch.draw(f, obs.getPosx() - (width / 2), obs.getPosy(),width,height);
                i++;
            }
            batch.end();
        } else {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            for (Obstaculo obs : listMonedas) {
                shape.setColor(obs.getProperty().getColor());
                shape.rect(obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
            }
            shape.end();
        }

    }

    private void updatePoderes(Array<Obstaculo> listObj, int dificultad, boolean notify) {
        Array<Obstaculo> poderes = game.getGeneradorPoderes().generarMovimiento(listObj, dificultad, notify);
        if (notify) {
            batch.begin();
            for (Obstaculo obs : poderes) {
                batch.draw(obs.getTexture(), obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
            }
            batch.end();
        } else {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            for (Obstaculo obs : poderes) {
                shape.setColor(obs.getProperty().getColor());
                shape.rect(obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
            }
            shape.end();
        }
    }

    private void updateTerreno() {
        Terreno terr = game.getTerreno();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(terr.getProperty().getColor());
        shape.rect(terr.getPosx(), terr.getPosy(), terr.getWidth(), terr.getHeight());
        shape.end();
        batch.begin();
        batch.draw(terr.getTexture(), terr.getPosx(), terr.getPosy(), terr.getWidth(), terr.getHeight());
        batch.end();
    }

    public void updateCuadrado() {
        MrCuadrado square = game.getMrCuadrado();
        square.animate();
        stateTime += Gdx.graphics.getDeltaTime();
        if (!square.isGameover()) {
            batch.begin();
            batch.draw(GeneradorTextura.sombra, square.getPosx(), square.getPosy(), square.getWidth() * 1.1f, square.getHeight() * 1.1f);
            batch.draw(square.getAnimation().getKeyFrame(stateTime, true), square.getPosx(), square.getPosy(), square.getWidth(), square.getHeight());
            batch.end();
        }
    }

    public void updateObstaculos(Array<Obstaculo> listObj, int dificultad, boolean notify) {
        Array<Obstaculo> obstaculos = game.getGeneradorSistema().generarMovimiento(listObj, dificultad, notify);

        if (notify) {
            batch.begin();
            for (Obstaculo obs : obstaculos) {
                batch.draw(GeneradorTextura.sombra, obs.getPosx(), obs.getPosy(), obs.getWidth()*1.1f, obs.getHeight()*1.1f);
                batch.draw(obs.getTexture(), obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
                if(GameConfig.timeLapsePoderCongelante > 0){
                    batch.draw(GeneradorTextura.getCongelante(), obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
                }
            }
            batch.end();
        } else {
            //Gdx.app.log("generadors", obstaculos.size+"##");
            shape.begin(ShapeRenderer.ShapeType.Filled);
            for (Obstaculo obs : obstaculos) {
                shape.setColor(obs.getProperty().getColor());
                shape.rect(obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
            }
            shape.end();
        }

    }


}
