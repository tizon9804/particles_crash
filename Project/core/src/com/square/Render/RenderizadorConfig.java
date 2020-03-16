package com.square.Render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.square.config.GameConfig;
import com.square.entity.Obstaculo;
import com.square.entity.Terreno;
import com.square.screen.ConfigScreen;

/**
 * Created by Tizon on 04/07/2015.
 */
public class RenderizadorConfig {

    private ConfigScreen game;
    private float stateTime;
    private ShapeRenderer shape;
    private SpriteBatch batch;


    public RenderizadorConfig(ConfigScreen cuadradoGame) {
        game = cuadradoGame;
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(game.getCam().combined);
        shape.setProjectionMatrix(game.getCam().combined);
        stateTime = 0f;
    }

    public void update() {
        game.getCam().update();
        updateTerreno();
        updateObstaculos(game.getGeneradorSistema().getObstaculos(), GameConfig.dificultadGeneradorObstaculos, true);
        updateObstaculos(game.getGeneradorSistema().getParticulas(), GameConfig.velocidadNormalParticulas, false);
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

    public void updateObstaculos(Array<Obstaculo> listObj, int dificultad, boolean notify) {
        Array<Obstaculo> obstaculos = game.getGeneradorSistema().generarMovimiento(listObj, dificultad, notify);
        if (notify) {
            batch.begin();
            for (Obstaculo obs : obstaculos) {
                batch.draw(obs.getTexture(), obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
            }
            batch.end();
        } else {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            for (Obstaculo obs : obstaculos) {
                shape.setColor(obs.getProperty().getColor());
                shape.rect(obs.getPosx(), obs.getPosy(), obs.getWidth(), obs.getHeight());
            }
            shape.end();
        }

    }


}
