package com.square.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.square.config.GameConfig;
import com.square.game.GeneradorSistema;
import com.square.game.GeneradorSonido;
import com.square.game.GeneradorTextura;
import com.square.screen.ConfigScreen;
import com.square.util.Property;

/**
 * Created by Tizon on 15/09/2015.
 */
public class PausaMenu {

    private final GeneradorSistema gen;
    private TextureAtlas mAtlas;
    private ConfigScreen game;
    private Stage menuStage;
    private Label.LabelStyle labelStyle;
    private Label score;
    private Button pause;
    private Button reset;
    private Button exit;
    private Button ads;
    public static boolean started;
    public static int tap;
    private Button sound;

    public PausaMenu(ConfigScreen game, GeneradorSistema gen) {
        this.gen = gen;
        this.game = game;
        menuStage = new Stage(new StretchViewport(game.getWidth(), game.getHeight()));
        labelStyle = new Label.LabelStyle();
        labelStyle.font = GameConfig.bitmapGenerator();
        mAtlas = GeneradorTextura.getButtonAtlas();
        create();
        started = false;
        tap = 0;

    }

    private void create() {
        menuStage.addActor(pauseButtonConfig());
        menuStage.addActor(resetButtonConfig());
        menuStage.addActor(exitButtonConfig());
        menuStage.addActor(adsButtonConfig());
        menuStage.addActor(scoreButtonConfig());
        menuStage.addActor(soundButtonConfig());
        Property p = new Property();
        p.setColor(GameConfig.colorParticulas);
        for (int i = 0; i < 800; i++) {
            game.getGeneradorSistema().crearParticula(pause.getX() + pause.getWidth() / 2, pause.getY() + pause.getHeight() / 2,
                    game.getGeneradorSistema().aumentoRandom(GameConfig.maximoTamanoParticulas),
                    game.getGeneradorSistema().aumentoRandom(GameConfig.maximoTamanoParticulas),
                    p, GameConfig.velocidadNormalParticulas);
        }
        GeneradorSonido.getMusic().pause();
    }

    private Group scoreButtonConfig() {
        score = new Label("0", labelStyle);
        score.setFontScale(GameConfig.wordSize);
        score.setColor(Color.BLACK);
        Table t = new Table();
        t.add(score).top().center();
        score.setText(Puntaje.puntaje + "");
        Group g = new Group();
        g.setPosition(menuStage.getWidth() * 0.5f, menuStage.getHeight() - (score.getHeight() * 2f));
        g.setOrigin(0, 0);
        g.addActor(t);
        return g;
    }

    public Group pauseButtonConfig() {
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("PlayPausaUp"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("PlayPausaDown"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("PlayPausaUp"));
        Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        pause = new Button(btnStyle);
        pause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameConfig.pausa = false;
                game.getParticlesGame().setScreen(game.getParticlesGame().getCuadradoGame());
            }
        });
        pause.setSize(menuStage.getWidth() / 3, menuStage.getWidth() / 3);
        pause.setPosition(menuStage.getWidth() * 0.5f - (pause.getWidth() * 0.5f), menuStage.getHeight() * 0.5f - pause.getHeight() * 0.5f);
        Group g = new Group();
        g.setOrigin(0, 0);
        g.setPosition(0, 0);
        g.addActor(pause);
        return g;
    }

    public Group resetButtonConfig() {
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("ResetUp"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("ResetDown"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("ResetUp"));
        Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        reset = new Button(btnStyle);
        reset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameConfig.pausa = false;
                game.getParticlesGame().getCuadradoGame().getMrCuadrado().setGameover(true);
                game.getParticlesGame().setScreen(game.getParticlesGame().getCuadradoGame());
            }
        });
        reset.setSize(menuStage.getWidth() * 0.25f, menuStage.getWidth() * 0.25f);
        Group g = new Group();
        g.setOrigin(0, 0);
        g.setPosition(pause.getX() * 0.5f - (reset.getWidth() * 0.5f), pause.getY() - reset.getHeight() * 0.5f);
        g.addActor(reset);
        return g;
    }

    public Group soundButtonConfig() {
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("SoundOn"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("SoundOff"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("SoundOn"));
        final Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        final Button.ButtonStyle btnStyleoff = new Button.ButtonStyle(drawableDown, drawableUp, drawableDown);
        sound = new Button(btnStyle);
        sound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (GameConfig.sound) {
                    GeneradorSonido.notifyCrashParticle();
                    GameConfig.sound = false;
                    sound.setStyle(btnStyleoff);
                } else {
                    sound.setStyle(btnStyle);
                    GameConfig.sound = true;
                }
            }
        });
        sound.setSize(menuStage.getWidth() * 0.1f, menuStage.getWidth() * 0.1f);
        sound.setPosition((menuStage.getWidth() - sound.getWidth()) / 2, ads.getY()+(sound.getWidth()/4));
        return sound;
    }

    public Group exitButtonConfig() {
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("HomeUp"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("HomeDown"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("HomeUp"));
        final Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        final Button.ButtonStyle btnStyleoff = new Button.ButtonStyle(drawableDown, drawableUp, drawableDown);
        exit = new Button(btnStyle);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameConfig.pausa = false;
                game.getParticlesGame().getCuadradoGame().getMrCuadrado().setGameover(true);
                game.getParticlesGame().getCuadradoGame().getMainMenu().setStarted(false);
                game.getParticlesGame().setScreen(game.getParticlesGame().getCuadradoGame());
            }
        });
        exit.setSize(menuStage.getWidth() * 0.25f, menuStage.getWidth() * 0.25f);
        exit.setPosition(pause.getX() + pause.getWidth() + pause.getX() * 0.5f - exit.getWidth() * 0.5f, pause.getY() - exit.getHeight() * 0.5f);
        Group g = new Group();
        g.setOrigin(menuStage.getWidth() / 2, 0);
        g.addActor(exit);
        return g;
    }

    public Group adsButtonConfig() {
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("ADS"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("ADSDown"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("ADS"));
        Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        ads = new Button(btnStyle);
        ads.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // GameConfig.pausa=false;
                GeneradorSonido.notifyCrashParticle();
                game.getParticlesGame().getCuadradoGame().getIaBilling().removeAds();
            }
        });
        ads.setSize(menuStage.getWidth() * 0.15f, menuStage.getWidth() * 0.15f);
        ads.setPosition(pause.getX() + (3 * ads.getWidth()), pause.getY() * 0.2f);

        return ads;
    }


    public void update() {
        if (GameConfig.pausa) {
            if (menuStage.getActors().size == 0) {
                create();
            }
            score.setText(Puntaje.puntaje + "");
            menuStage.act(Gdx.graphics.getDeltaTime());
            menuStage.draw();
            //Gdx.app.log("Pausa menu", "game"+game.getParticlesGame().getCuadradoGame().getMrCuadrado().getPosx());
        } else {
            clear();
        }
    }


    public Stage getMenuStage() {
        return menuStage;
    }

    public void setMenuStage(Stage menuStage) {
        this.menuStage = menuStage;
    }

    public Button getPause() {
        return pause;
    }

    public void setPause(ImageButton pause) {
        this.pause = pause;
    }

    public void clear() {
        menuStage.clear();
    }
}
