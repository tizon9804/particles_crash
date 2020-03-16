package com.square.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.square.config.GameConfig;
import com.square.entity.MrCuadrado;
import com.square.game.GeneradorPoderes;
import com.square.game.GeneradorSonido;
import com.square.game.GeneradorTextura;
import com.square.game.Tiempo;
import com.square.persistence.Persistence;
import com.square.screen.CuadradoGame;


/**
 * Created by Tizon on 11/07/2015.
 */
public class Puntaje {

    private CuadradoGame game;
    private Label fps;
    private Label labelPuntaje;
    private Label.LabelStyle labelStyle;
    private Table tablepuntaje;
    private Stage stage;
    public static long puntaje;
    public static long record;
    public Button pausa;
    private String idAchievement;
    private Label money;
    private Button addMoney;
    private Json json;


    public Puntaje(CuadradoGame game) {
        puntaje = 0;
        game.getGooPlayGames().loadScoreOfLeaderBoard();
        stage = new Stage();
        this.game = game;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = GameConfig.bitmapGenerator();
        labelPuntaje = new Label("0", labelStyle);
        labelPuntaje.setColor(Color.BLACK);
        labelPuntaje.setFontScale(GameConfig.wordSize);
        tablepuntaje = new Table();
        tablepuntaje.setFillParent(true);
        tablepuntaje.setDebug(GameConfig.debug);
        tablepuntaje.center().top();
        tablepuntaje.add(labelPuntaje).expandX().center();
       // fps = new Label("0", labelStyle);
        //fps.setColor(Color.GREEN);
        //fps.setFontScale(GameConfig.wordSize);
        stage.addActor(pausaButtonConfig());
        stage.addActor(tablepuntaje);
        stage.addActor(moneyConfig());
        json = new Json();
        tablepuntaje.add(fps).right().top();
        String recordSaved = Persistence.readFile("best");
        if (!recordSaved.isEmpty()) {
            record = Long.parseLong(recordSaved);
        } else {
            record = 0;
        }
    }

    public void restaurarPuntaje(){
        String recordSaved = Persistence.readFile("best");
        String cloudRecordSaved = Persistence.readFile("cloudbest");
        long cloudRecord = 0;
        if (!cloudRecordSaved.isEmpty()) {
            try {
                cloudRecord = Long.parseLong(cloudRecordSaved);
            } catch (Exception e) {
                Gdx.app.log("Puntaje", "something was wrong, trying to fix");
                Json json = new Json();
                Persistence.writeFile(json.toJson(0), "cloudbest");
            }
        }
        if (!recordSaved.isEmpty()) {
            record = Long.parseLong(recordSaved);
        } else {
            record = 0;
        }
        // obtain the record in cloud
        Gdx.app.log("Puntaje en la nube", cloudRecord + "!!");
        if (cloudRecord > record) {
            record = cloudRecord;
            Json json = new Json();
            Persistence.writeFile(json.toJson(record), "best");
        } else if (cloudRecord < record) {
            game.getGooPlayGames().submitScoreGPGS(record);
        }
    }

    private Actor moneyConfig(){
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(GeneradorTextura.getCoinTextures().get(0));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(GeneradorTextura.getCoinTextures().get(0));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(GeneradorTextura.getCoinTextures().get(0));
        Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        money = new Label(game.getGeneradorMonedas().getMonedas()+"", labelStyle);
        money.setFontScale(GameConfig.wordSize / 2);
        money.setColor(Color.BLACK);
        addMoney = new Button(btnStyle);
        addMoney.setPosition(stage.getWidth() * 0.1f, stage.getHeight() - (stage.getWidth() * 0.07f));
        addMoney.setSize(stage.getWidth() * 0.04f, (stage.getWidth() * 0.04f * addMoney.getHeight()) / addMoney.getWidth());
        addMoney.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        Table t = new Table();
        t.setPosition(addMoney.getX() - addMoney.getWidth(), addMoney.getY() + (addMoney.getHeight() / 2));
        t.add(money);
        Group g = new Group();
        g.setPosition(0, 0);
        g.setOrigin(0, 0);
        g.addActor(addMoney);
        g.addActor(t);
        return g;
    }

    private Actor pausaButtonConfig() {
        TextureAtlas mAtlas = GeneradorTextura.getButtonAtlas();
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("PauseUp"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("PauseDown"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("PauseUp"));
        Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        pausa = new Button(btnStyle);
        pausa.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (MainMenu.started) {
                    GameConfig.pausa = true;
                    GameConfig.shop = false;
                    //Gdx.app.log("puntaje", "mrcuadrado"+game.getMrCuadrado().getPosx());
                    game.getParticlesGame().setScreen(game.getParticlesGame().getConfig());
                    GeneradorSonido.notifyCrashParticle();
                }

            }
        });
        pausa.setSize(game.getWidth() * 0.05f, game.getHeight() * 0.05f);
        Group g = new Group();
        g.setPosition(stage.getWidth() - (pausa.getWidth() * 1.2f), stage.getHeight() - (pausa.getHeight() * 1.2f));
        g.addActor(pausa);
        return g;
    }

    public void update() {
        if (!MrCuadrado.isGameover()) {
            calcularPuntaje();
            if (Tiempo.getAumento() > 1) {
                labelPuntaje.setColor(Color.ORANGE);
                labelPuntaje.setText(puntaje + " x" + Tiempo.getAumento());
            } else {
                labelPuntaje.setColor(Color.BLACK);
                labelPuntaje.setText(puntaje + "");
            }

           // fps.setText(Gdx.graphics.getFramesPerSecond()+"");
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    /**
     * Actualiza el puntaje con relacion al tiempo y actualiza el record si puntaje lo sobrepasa
     */
    public void calcularPuntaje() {
        if (!MrCuadrado.isGameover()) {
            puntaje = Tiempo.getTiempo() / 10;
            money.setText(game.getGeneradorMonedas().getMonedas()+"");
            if (puntaje > record) {
                Json json = new Json();
                record = puntaje;
                Persistence.writeFile(json.toJson(record), "best");
                game.getMainMenu().showLeaderBoard = true;
            }
        }
    }

    public void unlockAchievements() {
        //maximun improvement
        String increment = Persistence.readFile("achievement_15");
        int i = 0;
        if(!increment.isEmpty())i = Integer.parseInt(increment);
        idAchievement = game.getGeneradorPoderes().getMaxMejoras() >= 1 && i==0 ? "achievement_15" : "no";
        if(idAchievement.equals("achievement_15")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorPoderes().getMaxMejoras() >= 2 && i==1 ? "achievement_15" : "no";
        if(idAchievement.equals("achievement_15")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorPoderes().getMaxMejoras() >= 3 && i==2 ? "achievement_15" : "no";
        if(idAchievement.equals("achievement_15")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorPoderes().getMaxMejoras() >= 4 && i==3 ? "achievement_15" : "no";
        if(idAchievement.equals("achievement_15")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorPoderes().getMaxMejoras() >= 5 && i==4 ? "achievement_15" : "no";
        if(idAchievement.equals("achievement_15")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorPoderes().getMaxMejoras() >= 6 && i==5 ? "achievement_15" : "no";
        if(idAchievement.equals("achievement_15")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        // score 100 without coins
        idAchievement = puntaje >= 1000 && game.getGeneradorMonedas().getMonedas()==0 ? "achievement_14" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // score 1000 without powers
        idAchievement = puntaje >= 1000 && game.getGeneradorPoderes().getUsedPower()==0 ? "achievement_13" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // spend in total 3000
        increment = Persistence.readFile("achievement_12");
        i = 0;
        if(!increment.isEmpty())i = Integer.parseInt(increment);
        idAchievement = game.getGeneradorMonedas().getSpend() >= 1000 && i==0 ? "achievement_12" : "no";
        if(idAchievement.equals("achievement_12")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorMonedas().getSpend() >= 2000 && i==1 ? "achievement_12" : "no";
        if(idAchievement.equals("achievement_12")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorMonedas().getSpend() >= 3000 && i==2 ? "achievement_12" : "no";
        if(idAchievement.equals("achievement_12")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        idAchievement = game.getGeneradorMonedas().getMonedas() >= 500 ? "achievement_11" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // score 500 with out use power
        if(game.getGeneradorPoderes().getUsedPower()==0) {
            increment = Persistence.readFile("achievement_10");
            i = 0;
            if(!increment.isEmpty())i = Integer.parseInt(increment);
            idAchievement = puntaje >= 500 && i==4 ? "achievement_10" : "no";
            if(idAchievement.equals("achievement_10")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
            idAchievement = puntaje >= 400 && i==3 ? "achievement_10" : "no";
            if(idAchievement.equals("achievement_10")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
            idAchievement = puntaje >= 300 && i==2 ? "achievement_10" : "no";
            if(idAchievement.equals("achievement_10")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
            idAchievement = puntaje >= 200 && i==1 ? "achievement_10" : "no";
            if(idAchievement.equals("achievement_10")){i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
            idAchievement = puntaje >= 100 && i==0 ? "achievement_10" : "no";
            if(idAchievement.equals("achievement_10")){
                i++;game.getGooPlayGames().incrementAchievementGPGS(idAchievement, 1);}
        }
        // save 5000 without spend it
        idAchievement = game.getGeneradorMonedas().getTotalMonedas()>=5000 ? "achievement_9" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // first purchase
        idAchievement = GeneradorPoderes.numMejoras > 0 ? "achievement_8" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // score 1000
        idAchievement = record >= 1000 ? "achievement_5" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // score 500
        idAchievement = record >= 500 ? "achievement_4" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // score 400
        idAchievement = record >= 400 ? "achievement_3" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // score 300
        idAchievement = record >= 100 ? "achievement_2" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
        // score 200
        idAchievement = record >= 20 ? "achievement_1" : "no";
        game.getGooPlayGames().unlockAchievementGPGS(idAchievement);
    }

    public void restart() {
        puntaje = 0;
    }

    public long getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(long puntaje) {
        this.puntaje = puntaje;
    }

    public Label getLabelPuntaje() {
        return labelPuntaje;
    }

    public void setLabelPuntaje(Label labelPuntaje) {
        this.labelPuntaje = labelPuntaje;
    }

    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(Label.LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
