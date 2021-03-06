package com.square.handler;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.square.config.GameConfig;
import com.square.screen.ConfigScreen;
import com.square.util.Property;

/**
 * Created by Tizon on 04/07/2015.
 */
public class GestureConfigHandler implements GestureDetector.GestureListener {


    private ConfigScreen configScreen;

    public GestureConfigHandler(ConfigScreen cuadradoGame) {
        configScreen = cuadradoGame;
    }

    /**
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @see InputProcessor#touchDown(int, int, int, int)
     */
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Property p = new Property();
        p.setColor(GameConfig.colorParticulas);
        for (int i = 0; i < 200; i++) {
            configScreen.getGeneradorSistema().crearParticula(x, y,
                    configScreen.getGeneradorSistema().aumentoRandom(GameConfig.maximoTamanoParticulas),
                    configScreen.getGeneradorSistema().aumentoRandom(GameConfig.maximoTamanoParticulas),
                    p, GameConfig.velocidadNormalParticulas);
        }
        return false;
    }

    /**
     * Called when a tap occured. A tap happens if a touch went down on the screen and was lifted again without moving outside
     * of the tap square. The tap square is a rectangular area around the initial touch position as specified on construction
     * time of the {@link GestureDetector}.
     *
     * @param x
     * @param y
     * @param count  the number of taps.
     * @param button
     */
    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        //temporal
        com.square.util.Property p = new com.square.util.Property();
        p.setColor(new Color(0.7f, 0.5f, 0, 0));
        configScreen.getGeneradorSistema().crearObstaculo(0, 0, 250, 250, p, 20, true);
        return false;
    }

    /**
     * Called when the user dragged a finger over the screen and lifted it. Reports the last known velocity of the finger in
     * pixels per second.
     *
     * @param velocityX velocity on x in seconds
     * @param velocityY velocity on y in seconds
     * @param button
     */
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (velocityX > 0) {
            // configScreen.getParticlesGame().setScreen(configScreen.getParticlesGame().getCuadradoGame());
        }
        return false;
    }

    /**
     * Called when the user drags a finger over the screen.
     *
     * @param x
     * @param y
     * @param deltaX the difference in pixels to the last drag event on x.
     * @param deltaY the difference in pixels to the last drag event on y.
     */
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Property p = new Property();
        p.setColor(GameConfig.colorParticulas);
        for (int i = 0; i < 200; i++) {
            configScreen.getGeneradorSistema().crearParticula(x, y,
                    configScreen.getGeneradorSistema().aumentoRandom(GameConfig.maximoTamanoParticulas),
                    configScreen.getGeneradorSistema().aumentoRandom(GameConfig.maximoTamanoParticulas),
                    p, GameConfig.velocidadNormalParticulas);
        }
        return true;
    }

    /**
     * Called when no longer panning.
     *
     * @param x
     * @param y
     * @param pointer
     * @param button
     */
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    /**
     * Called when the user performs a pinch zoom gesture. The original distance is the distance in pixels when the gesture
     * started.
     *
     * @param initialDistance distance between fingers when the gesture started.
     * @param distance        current distance between fingers.
     */
    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    /**
     * Called when a user performs a pinch zoom gesture. Reports the initial positions of the two involved fingers and their
     * current positions.
     *
     * @param initialPointer1
     * @param initialPointer2
     * @param pointer1
     * @param pointer2
     */
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    /**
     * Called when no longer pinching.
     */
    @Override
    public void pinchStop() {

    }
}
