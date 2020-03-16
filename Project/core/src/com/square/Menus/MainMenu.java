package com.square.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.square.persistence.Persistence;
import com.square.screen.CuadradoGame;
import com.square.util.Property;

/**
 * Created by Tizon on 14/07/2015.
 */
public class MainMenu {

    private final GeneradorSistema gen;
    private TextureAtlas mAtlas;
    private CuadradoGame game;
    private Stage menuStage;
    private Label.LabelStyle labelStyle;
    private Image logo;
    private Image scoreT;
    private Label record;
    private Label score;
    private Label money;
    private Label version;
    private Button play;
    private Button addMoney;
    private Button config;
    private Button sound;
    private Button ads;
    private Button recordT;
    private Button achievement;
    private Button ranking;
    public static boolean started;
    public static int tap;
    public boolean showLeaderBoard;
    private Property p;
    private String puntaje;
    private String verifierAds;
    private Group moneyGroup;
    private Group versionGroup;
    private Group recordGroup;
    private Group achievementGroup;
    private Group playGroup;
    private Group rankingGroup;
    private Group logoGroup;
    private Group shopGroup;
    private Group adsGroup;

    public MainMenu(CuadradoGame game, GeneradorSistema gen) {
        this.gen = gen;
        this.game = game;
        menuStage = new Stage(new StretchViewport(game.getWidth(), game.getHeight()));
        labelStyle = new Label.LabelStyle();
        labelStyle.font = GameConfig.bitmapGenerator();
        mAtlas = GeneradorTextura.getButtonAtlas();
        create();
        started = false;
        tap = 0;
        p = new Property();


    }

    private void create() {

        //menuStage.addActor(configButtonConfig());
       menuStage.addActor(logoConfig());
       menuStage.addActor(playButtonConfig());
       menuStage.addActor(shopButtonConfig());
       menuStage.addActor(adsButtonConfig());
       menuStage.addActor(recordButtonConfig());
       menuStage.addActor(achievementButtonConfig());
       menuStage.addActor(rankingButtonConfig());
       menuStage.addActor(moneyConfig());
       menuStage.addActor(versionLabelConfig());
       game.getGeneradorMonedas().saveCoins();
       money.setText(game.getGeneradorMonedas().getTotalMonedas() + " ");
       game.getGooPlayGames().getPositionGPGS();
       if (Puntaje.puntaje == Puntaje.record && Puntaje.puntaje != 0)
            game.getGooPlayGames().submitScoreGPGS(Puntaje.record);
       else
            game.getGooPlayGames().submitScoreGPGS(Puntaje.puntaje);
       showBanner();

    }

    public void update() {
        calcularRecord();
        if (menuStage.getActors().size == 0) {
            create();
            Gdx.app.log("MAIN MENU", "Initialize MENU");
            game.getPuntaje().unlockAchievements();
        }
        menuStage.act(Gdx.graphics.getDeltaTime());
        menuStage.draw();
    }

    private void calcularRecord() {
        try {
            puntaje = Puntaje.puntaje == 0 ? "" : Puntaje.puntaje + "";
            score.setText(puntaje + "");
            record.setText(Puntaje.record + "");
            p.setColor(GameConfig.mrCuadradoColor);
            if (Puntaje.puntaje == Puntaje.record && Puntaje.puntaje != 0) {
                game.getGooPlayGames().submitScoreGPGS(Puntaje.record);
                for (int i = 0; i < 12; i++) {
                    gen.crearParticula(menuStage.getWidth() * 0.8f, menuStage.getHeight() * 0.7f,
                            gen.aumentoRandom(GameConfig.maximoTamanoParticulas),
                            gen.aumentoRandom(GameConfig.maximoTamanoParticulas),
                            p, GameConfig.velocidadNormalParticulas);
                }
                if (showLeaderBoard) {
                    //submit score in google play games
                    game.getGooPlayGames().submitScoreGPGS(Puntaje.puntaje);
                    game.getGooPlayGames().submitRicherGPGS(game.getGeneradorMonedas().getTotalMonedas());
                    showLeaderBoard = false;
                }
            }

        } catch (Exception e) {
            //Gdx.app.log("mainMenu","new record");
        }
    }

    private Actor moneyConfig(){
        if(moneyGroup==null) {
            TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("CoinAddUp"));
            TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("CoinAddDown"));
            TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("CoinAddUp"));
            Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
            money = new Label(game.getGeneradorMonedas().getTotalMonedas() + " ", labelStyle);
            money.setFontScale(GameConfig.wordSize / 2);
            money.setColor(Color.BLACK);
            addMoney = new Button(btnStyle);
            addMoney.setPosition(menuStage.getWidth() * 0.87f, menuStage.getHeight() - (menuStage.getWidth() * 0.07f));
            addMoney.setSize(menuStage.getWidth() * 0.1f, (menuStage.getWidth() * 0.1f * addMoney.getHeight()) / addMoney.getWidth());
            addMoney.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameConfig.shop = true;
                    GameConfig.pausa = false;
                    GameConfig.shopMoney = true;
                    game.getParticlesGame().setScreen(game.getParticlesGame().getConfig());
                    GeneradorSonido.notifyCrashParticle();

                }
            });
            Table t = new Table();
            t.setPosition(addMoney.getX() - addMoney.getWidth() * 0.6f, addMoney.getY() + (addMoney.getHeight() / 2));
            t.add(money);
            moneyGroup = new Group();
            moneyGroup.setPosition(0, 0);
            moneyGroup.setOrigin(0, 0);
            moneyGroup.addActor(addMoney);
            moneyGroup.addActor(t);
        }
        return moneyGroup;
    }

    private Actor versionLabelConfig() {
        if(versionGroup==null) {
            version = new Label(game.getGooPlayGames().getVersionGame(), labelStyle);
            version.setFontScale(GameConfig.wordSmallSize);
            version.setColor(Color.BLACK);
            versionGroup = new Group();
            versionGroup.setPosition(menuStage.getWidth() * 0.01f, menuStage.getHeight() - (1.5f * version.getHeight()));
            versionGroup.setOrigin(menuStage.getWidth() / 2, 0);
            versionGroup.addActor(version);
        }
        return versionGroup;
    }

   /**
     * region of record green
     *
     * @return
     */
    private Group recordButtonConfig() {
        if(recordGroup==null) {
            TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("Highscore"));
            TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("HighscoreDown"));
            TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("Highscore"));
            Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
            recordT = new Button(btnStyle);
            recordT.setPosition(0, 0);
            recordT.setSize(menuStage.getWidth() * 0.2f, (menuStage.getWidth() * 0.2f * recordT.getHeight()) / recordT.getWidth());
            recordT.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.square.android");
                }
            });
            record = new Label("0", labelStyle);
            record.setFontScale(GameConfig.wordSize);
            Table t = new Table();
            t.setPosition(recordT.getX(), recordT.getY());
            t.setWidth(recordT.getWidth());
            t.setHeight(recordT.getHeight() / 2);
            t.add(record).center();
            recordGroup = new Group();
            recordGroup.setPosition(menuStage.getWidth() / 2 + recordT.getWidth(), play.getY() * 0.6f);
            recordGroup.setOrigin(0, 0);
            recordGroup.addActor(recordT);
            recordGroup.addActor(t);
        }

        return recordGroup;
    }

    /**
     * region of record green
     *
     * @return
     */
    private Group achievementButtonConfig() {
        if(achievementGroup==null) {
            TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("LogrosUp"));
            TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("LogrosDown"));
            TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("LogrosUp"));
            Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
            achievement = new Button(btnStyle);
            achievement.setPosition(0, 0);
            achievement.setSize(menuStage.getWidth() * 0.2f, (menuStage.getWidth() * 0.2f * achievement.getHeight()) / achievement.getWidth());
            achievement.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GeneradorSonido.notifyCrashParticle();
                    game.getGooPlayGames().getAchievementsGPGS();
                }
            });
            achievementGroup = new Group();
            achievementGroup.setPosition(menuStage.getWidth() / 2 - (2 * achievement.getWidth()), play.getY() * 0.6f);
            achievementGroup.setOrigin(0, 0);
            achievementGroup.addActor(achievement);
        }
        return achievementGroup;
    }

    /**
     * region of record green
     *
     * @return
     */
    private Group rankingButtonConfig() {
        if(rankingGroup==null) {
            TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("RankingUp"));
            TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("RankingDown"));
            TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("RankingUp"));
            Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
            ranking = new Button(btnStyle);
            ranking.setPosition(0, 0);
            ranking.setSize(menuStage.getWidth() * 0.2f, (menuStage.getWidth() * 0.2f * ranking.getHeight()) / ranking.getWidth());
            ranking.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GeneradorSonido.notifyCrashParticle();
                    game.getGooPlayGames().submitRicherGPGS(game.getGeneradorMonedas().getTotalMonedas());
                    game.getGooPlayGames().submitScoreGPGS(Puntaje.record);
                    game.getGooPlayGames().getLeaderboardGPGS();
                }
            });
            rankingGroup = new Group();
            rankingGroup.setPosition((menuStage.getWidth() - achievement.getWidth()) / 2, play.getY() * 0.6f);
            rankingGroup.setOrigin(0, 0);
            rankingGroup.addActor(ranking);
        }
           
        return rankingGroup;
    }

    /**
     * Logo region
     *
     * @return
     */
    private Group logoConfig() {
        if(logoGroup == null) {
            TextureRegionDrawable logotext = new TextureRegionDrawable(mAtlas.findRegion("Logo"));
            logo = new Image(logotext);
            logo.setSize(menuStage.getWidth() * 0.9f, (menuStage.getWidth() * 0.9f * logo.getHeight()) / logo.getWidth());
            logo.setPosition((menuStage.getWidth() - logo.getWidth()) * 0.5f, menuStage.getHeight() * 0.6f);
            logoGroup = new Group();
            logoGroup.setPosition(0, 0);
            logoGroup.setOrigin(0, 0);
            logoGroup.addActor(logo);
        }
        return logoGroup;
    }

    public Group playButtonConfig() {
            TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("PlayUp"));
            TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("PlayDown"));
            TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("PlayUp"));
            if (Puntaje.puntaje > 0) {
                drawableUp = new TextureRegionDrawable(mAtlas.findRegion("PlayScoreUp"));
                drawableDown = new TextureRegionDrawable(mAtlas.findRegion("PlayScoreDown"));
                drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("PlayScoreUp"));
            }
            Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
            play = new Button(btnStyle);
            play.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    started = true;
                    game.getPuntaje().restaurarPuntaje();
                    game.getGeneradorMonedas().restaurarMonedas();
                    game.getGooPlayGames().submitRicherGPGS(game.getGeneradorMonedas().getTotalMonedas());
                    game.getGooPlayGames().submitScoreGPGS(Puntaje.record);
                    GeneradorSonido.notifyCrashParticle();
                }
            });
            play.setSize(menuStage.getWidth() * 0.5f, (menuStage.getWidth() * 0.5f * play.getHeight()) / play.getWidth());
            play.setPosition((menuStage.getWidth() - play.getWidth()) / 2, logo.getY() - play.getHeight() * 1.5f);
            score = new Label("", labelStyle);
            score.setColor(Color.BLACK);
            Table t = new Table();
            t.setPosition(play.getX(), play.getY());
            t.setWidth(play.getWidth());
            t.setHeight(play.getHeight());
            t.add(score).center().padLeft(play.getWidth() / 3).padBottom(play.getHeight() * 0.05f);
            score.setFontScale(GameConfig.wordSize);
            playGroup= new Group();
            playGroup.setOrigin(menuStage.getWidth() / 2, 0);
            playGroup.setPosition(0, 0);
            playGroup.addActor(t);
            playGroup.addActor(play);
        return playGroup;
    }

    public Group configButtonConfig() {
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("GearUp"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("GearDown"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("GearUp"));
        Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
        config = new Button(btnStyle);
        config.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GeneradorSonido.notifyCrashParticle();
                game.getParticlesGame().setScreen(game.getParticlesGame().getConfig());
            }
        });
        config.setSize(menuStage.getWidth() * 0.15f, menuStage.getWidth() * 0.15f);
        Group g = new Group();
        g.setOrigin(0, 0);
        g.setPosition(play.getX() / 2, play.getY() * 0.4f - config.getHeight() / 2);
        g.addActor(config);
        return g;
    }

    public Group shopButtonConfig() {
        if(shopGroup == null) {
            TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion("ShopUp"));
            TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion("ShopDown"));
            TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("ShopUp"));
            final Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
            //final Button.ButtonStyle btnStyleoff = new Button.ButtonStyle(drawableDown, drawableUp, drawableDown);
            sound = new Button(btnStyle);
            sound.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!MainMenu.started) {
                        GameConfig.shop = true;
                        GameConfig.pausa = false;
                        //Gdx.app.log("puntaje", "mrcuadrado"+game.getMrCuadrado().getPosx());
                        game.getParticlesGame().setScreen(game.getParticlesGame().getConfig());
                        GeneradorSonido.notifyCrashParticle();
                    }
                }
            });
            sound.setSize(menuStage.getWidth() * 0.15f, menuStage.getWidth() * 0.15f);
            sound.setPosition(menuStage.getWidth() / 2 - 3 * sound.getWidth() / 2, play.getY() * 0.4f - sound.getHeight() / 2);
            shopGroup = new Group();
            shopGroup.setOrigin(menuStage.getWidth() / 2, 0);
            shopGroup.addActor(sound);
        }
        return shopGroup;
    }

    public Group adsButtonConfig() {
        if(adsGroup == null) {
            TextureRegionDrawable drawableUp = null;
            TextureRegionDrawable drawableDown = null;
            TextureRegionDrawable drawableChecked = null;
            if (game.getIaBilling().isAdsRemoved()) {
                drawableUp = new TextureRegionDrawable(mAtlas.findRegion("ADSOwned"));
                drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("ADSOwned"));
            } else {
                drawableUp = new TextureRegionDrawable(mAtlas.findRegion("ADS"));
                drawableChecked = new TextureRegionDrawable(mAtlas.findRegion("ADS"));
            }
            drawableDown = new TextureRegionDrawable(mAtlas.findRegion("ADSDown"));

            Button.ButtonStyle btnStyle = new Button.ButtonStyle(drawableUp, drawableDown, drawableChecked);
            ads = new Button(btnStyle);
            ads.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GeneradorSonido.notifyCrashParticle();
                    game.getIaBilling().removeAds();
                }
            });
            ads.setSize(menuStage.getWidth() * 0.15f, menuStage.getWidth() * 0.15f);
            adsGroup = new Group();
            adsGroup.setOrigin(0, 0);
            adsGroup.setPosition(menuStage.getWidth() / 2 + ads.getWidth() / 2, play.getY() * 0.4f - ads.getHeight() / 2);
            adsGroup.addActor(ads);
        }
        return adsGroup;
    }




    private void showBanner() {
        verifierAds = Persistence.readFile("remove_particle_ads");
        // verify in local if no internet connection has
        if (!verifierAds.isEmpty()) {
            if (verifierAds.equals("jwcww789j25647cjhcce/c54aefa/v5daujhbvcgfsts")) {
                game.getIaBilling().setIsAdsRemoved(true);
            }
        }
        // verify if remove ads is purchased
        if (!game.getIaBilling().isAdsRemoved()) {
            game.showBanner();
            Persistence.writeFile("jwcww789j25646cjhcce/c54aefa/v5daujhbvcgfsts", "remove_particles_ads");
        } else {
            // write in local to non  internet connections times
            Persistence.writeFile("jwcww789j25647cjhcce/c54aefa/v5daujhbvcgfsts", "remove_particles_ads");
        }
    }



    public Label getMoney() {
        return money;
    }

    public void setMoney(Label money) {
        this.money = money;
    }

    public Label getVersion() {
        return version;
    }

    public void setVersion(Label version) {
        this.version = version;
    }

    public Button getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(Button addMoney) {
        this.addMoney = addMoney;
    }

    public Button getAchievement() {
        return achievement;
    }

    public void setAchievement(Button achievement) {
        this.achievement = achievement;
    }

    public Button getRanking() {
        return ranking;
    }

    public void setRanking(Button ranking) {
        this.ranking = ranking;
    }

    public boolean isShowLeaderBoard() {
        return showLeaderBoard;
    }

    public void setShowLeaderBoard(boolean showLeaderBoard) {
        this.showLeaderBoard = showLeaderBoard;
    }

    public Property getP() {
        return p;
    }

    public void setP(Property p) {
        this.p = p;
    }

    public Stage getMenuStage() {
        return menuStage;
    }

    public void setMenuStage(Stage menuStage) {
        this.menuStage = menuStage;
    }

    public Button getPlay() {
        return play;
    }

    public void setPlay(ImageButton play) {
        this.play = play;
    }

    public void clear() {
        menuStage.clear();
    }

    public GeneradorSistema getGen() {
        return gen;
    }

    public TextureAtlas getmAtlas() {
        return mAtlas;
    }

    public void setmAtlas(TextureAtlas mAtlas) {
        this.mAtlas = mAtlas;
    }

    public CuadradoGame getGame() {
        return game;
    }

    public void setGame(CuadradoGame game) {
        this.game = game;
    }

    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(Label.LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public Button getRecordT() {
        return recordT;
    }

    public void setRecordT(Button recordT) {
        this.recordT = recordT;
    }

    public Image getScoreT() {
        return scoreT;
    }

    public void setScoreT(Image scoreT) {
        this.scoreT = scoreT;
    }

    public Label getRecord() {
        return record;
    }

    public void setRecord(Label record) {
        this.record = record;
    }

    public Label getScore() {
        return score;
    }

    public void setScore(Label score) {
        this.score = score;
    }

    public void setPlay(Button play) {
        this.play = play;
    }

    public Button getConfig() {
        return config;
    }

    public void setConfig(Button config) {
        this.config = config;
    }

    public Button getSound() {
        return sound;
    }

    public void setSound(Button sound) {
        this.sound = sound;
    }

    public Button getAds() {
        return ads;
    }

    public void setAds(Button ads) {
        this.ads = ads;
    }

    public static boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        MainMenu.started = started;
    }

    public static int getTap() {
        return tap;
    }

    public static void setTap(int tap) {
        MainMenu.tap = tap;
    }
}
