package com.square.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.square.config.GameConfig;
import com.square.entity.Poder;
import com.square.game.GeneradorMonedas;
import com.square.game.GeneradorPoderes;
import com.square.game.GeneradorSistema;
import com.square.game.GeneradorSonido;
import com.square.game.GeneradorTextura;
import com.square.interfaces.IaBilling;
import com.square.persistence.Persistence;
import com.square.screen.ConfigScreen;

import java.util.ArrayList;

/**
 * Created by Tizon on 14/01/2016.
 */
public class ShopMenu {

    private static final String COINS_PURCHASED = "3JHDGDILIKLÑ1121";
    private Table tableModel;
    private Label.LabelStyle labelStyle;
    private ScrollPane scrollPane;
    private Stage shopStage;
    List<String> list;
    private ConfigScreen game;
    private TextureAtlas mAtlas;
    private Image title;
    private Button sound;
    private Label money;
    private Image addMoney;
    private GeneradorPoderes generadorPoderes;
    private GeneradorMonedas generadorMonedas;
    private Json jsonUtil = new Json();


    public ShopMenu(ConfigScreen game, GeneradorSistema gen){
        this.game = game;
        shopStage = new Stage(new StretchViewport(game.getWidth(), game.getHeight()));
        labelStyle = new Label.LabelStyle();
        labelStyle.font = GameConfig.bitmapGenerator();
        tableModel = new Table();
        mAtlas = GeneradorTextura.getButtonAtlas();
        create();
        verifyPurchased();
    }

    private void agregarDecoracion() {
        Table deco = new Table();
        title = new Image(mAtlas.findRegion("UpgradeTitle"));
        float width = shopStage.getWidth()*0.5f;
        title.setSize(width, (width * title.getHeight()) / title.getWidth());
        title.setPosition((shopStage.getWidth() - title.getWidth()) / 2, shopStage.getHeight() * 0.87f);
        shopStage.addActor(title);
        soundButtonConfig();
    }

    public void soundButtonConfig() {
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
        sound.setSize(shopStage.getWidth() * 0.1f, shopStage.getWidth() * 0.1f);
        sound.setPosition((shopStage.getWidth() - sound.getWidth()) / 2, title.getY() * 0.93f);
        shopStage.addActor(sound);
    }

    private void moneyConfig(){
        generadorMonedas = game.getParticlesGame().getCuadradoGame().getGeneradorMonedas();
        money = new Label(generadorMonedas.getTotalMonedas()+"", labelStyle);
        money.setFontScale(GameConfig.wordSize/2);
        money.setColor(Color.BLACK);
        addMoney = new Image(GeneradorTextura.getCoinTextures().get(0));
        addMoney.setPosition(shopStage.getWidth()*0.90f, shopStage.getHeight() - (shopStage.getWidth() * 0.08f));
        addMoney.setSize(shopStage.getWidth() * 0.05f, (shopStage.getWidth() * 0.05f * addMoney.getHeight()) / addMoney.getWidth());
        Table t = new Table();
        t.setPosition(addMoney.getX()-addMoney.getWidth(),addMoney.getY()+(addMoney.getHeight()/2));
        t.add(money);
        Group g = new Group();
        g.setPosition(0, 0);
        g.setOrigin(0, 0);
        g.addActor(addMoney);
        g.addActor(t);
        shopStage.addActor(g);
    }

    public void agregarPoderes(){
        generadorPoderes = game.getParticlesGame().getCuadradoGame().getGeneradorPoderes();
        int nummejoras = 0;
        for(Poder poder:generadorPoderes.getPoderes()){
            TextureRegion texture =game.getParticlesGame().getCuadradoGame().getGeneradorPoderes().darTexturaTipo(poder.getType());
            createModel(poder.getType(),texture, poder.getLevel(), poder.getPrice(),"Purchase",true,"0");
            tableModel.row();
        }
    }

    public void agregarInventarioMonedas(){

        try {
            Array<String> list = game.getParticlesGame().getCuadradoGame().getIaBilling().getInventoryPrices();
           // Gdx.app.log("shop menu","#####################"+list.get(0));
            for (String dao : list) {
                String coins = dao.split(":")[1].split("_")[1];
                String tempprice=dao.split(":")[0];
                tempprice=tempprice.substring(2).replace(".","");
                int price = Integer.parseInt(tempprice);
                createModel(coins + " Coins", GeneradorTextura.getCoinTextures().get(1), 1, price, "RealPurchase", false,dao.split(":")[1]);
                tableModel.row();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createModel(final String type, TextureRegion texture, final int level, final int price, String nameButton, final boolean hasBarra, final String extra_iabilling) {
        Table model = new Table();
        Table column = new Table();
        Image img = new Image(texture);
        Label name = new Label(type,labelStyle);
        name.setFontScale(GameConfig.wordSize);
        final Image barra = new Image(mAtlas.findRegions("BarraPoder").get(level - 1));
        TextureRegionDrawable drawableUp = new TextureRegionDrawable(mAtlas.findRegion(nameButton+"Up"));
        TextureRegionDrawable drawableDown = new TextureRegionDrawable(mAtlas.findRegion(nameButton+"Down"));
        TextureRegionDrawable drawableChecked = new TextureRegionDrawable(mAtlas.findRegion(nameButton+"Up"));
        model.setBackground(new TextureRegionDrawable(mAtlas.findRegion("Layout")));
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(drawableUp, drawableDown, drawableChecked,labelStyle.font);
        String nameprice="";
        if(level >= mAtlas.findRegions("BarraPoder").size)
            nameprice = "OK";
        else
            nameprice = price + "";
        final TextButton purchase = new TextButton(nameprice,btnStyle);
        purchase.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               buyInShop(hasBarra,type,purchase,barra,price,extra_iabilling);
            }
        });
        purchase.getLabel().setFontScale(GameConfig.wordSize);
        float padgen = shopStage.getWidth() * 0.2f;
        float widthsmall = shopStage.getWidth() * 0.2f;
        float widthbig = shopStage.getWidth() * 0.5f;
        model.add(img).center().padRight(padgen).size(widthsmall);
        column.add(name).center();
        if(hasBarra) {
            column.row();
            column.add(barra).width(widthbig).height((widthbig * barra.getHeight()) / (barra.getWidth()));
        }
        model.add(column);
        model.add(purchase).right().center().padLeft(padgen).width(widthbig).height((widthbig * purchase.getHeight()) / (purchase.getWidth()));
        tableModel.add(model).size(shopStage.getWidth()*1.8f,shopStage.getHeight()*0.30f).padBottom(shopStage.getHeight() * 0.07f);
    }

    public void update() {
        if (GameConfig.shop) {
            if (shopStage.getActors().size == 0) {
                create();
            }
            shopStage.act(Gdx.graphics.getDeltaTime());
            shopStage.draw();
        } else {
            clear();
        }
    }

    public void buyInShop(boolean hasBarra,String type,TextButton purchase,Image barra,int price,String extra_iabilling){
        // it means that is to upgrade a performance
        if(hasBarra){
            for(Poder poder:generadorPoderes.getPoderes()){
                if(poder.getType().equals(type)) {
                    //Gdx.app.log("shop",poder.getLevel()+"");
                    if(poder.getLevel()>= mAtlas.findRegions("BarraPoder").size){
                        purchase.setText("OK");
                        break;
                    }
                    if (generadorMonedas.getTotalMonedas() >= poder.getPrice()){
                        generadorMonedas.setTotalMonedas(generadorMonedas.getTotalMonedas() - poder.getPrice());
                        generadorMonedas.setSpend(generadorMonedas.getSpend()+poder.getPrice());
                        poder.purchaseLevel();
                        generadorPoderes.savePoderes(generadorPoderes.getPoderes());
                        if(poder.getLevel()>= mAtlas.findRegions("BarraPoder").size)
                            purchase.setText("OK");
                        else
                            purchase.setText(poder.getPrice() + "");
                        barra.setDrawable(new SpriteDrawable(new Sprite(mAtlas.findRegions("BarraPoder").get(poder.getLevel() - 1))));
                        GeneradorMonedas.updateAmountCoins(generadorPoderes.getPoderes());
                        //todo sound when purchases
                    }
                    else{
                        //todo dialog to purchase with reAL MONEY
                    }
                    break;
                }
            }
        }
        else{
            game.getParticlesGame().getCuadradoGame().getIaBilling().buyCoins(extra_iabilling);
            verifyPurchased();
        }
        money.setText(generadorMonedas.getTotalMonedas() + "");
        game.getParticlesGame().getCuadradoGame().getMainMenu().getMoney().setText(generadorMonedas.getTotalMonedas() + "");
        generadorMonedas.saveCoins();
    }

    private void verifyPurchased() {
        ArrayList<Integer> pur = game.getParticlesGame().getCuadradoGame().getIaBilling().getPurchasedList();
        ArrayList<Integer> pur2=pur;
        String coins_purchased= Persistence.readFile(COINS_PURCHASED);
        if (!coins_purchased.isEmpty()) {
            pur2 = jsonUtil.fromJson(ArrayList.class, coins_purchased);
        }
        for(int i=1;i<pur.size();i++){
                if(pur.get(i)==1 && pur2.get(i)==1){
                    generadorMonedas.setTotalMonedas(generadorMonedas.getTotalMonedas() + Integer.parseInt(IaBilling.purchases[i].replace("coin_","")));
                    pur.set(i,2);
                    Persistence.writeFile(jsonUtil.toJson(pur,ArrayList.class),COINS_PURCHASED);
                }
            }

    }

    public void clear() {
        shopStage.clear();
    }

    private void create() {
        moneyConfig();
        agregarDecoracion();
        if(GameConfig.shopMoney) {
            agregarInventarioMonedas();
            agregarPoderes();
        }
        else{
            agregarPoderes();
            agregarInventarioMonedas();
        }
        scrollPane = new ScrollPane(tableModel);
        scrollPane.setBounds(0, 0, game.getWidth() * 1.8f, game.getHeight() * 1.45f);
        scrollPane.setSmoothScrolling(true);
        scrollPane.setPosition(game.getWidth() / 2 - scrollPane.getWidth() / 4,
                game.getHeight() / 2 - scrollPane.getHeight() * 0.29f);
        scrollPane.setTransform(true);
        scrollPane.setScale(0.5f);
        shopStage.addActor(scrollPane);
        GameConfig.shopMoney = false;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public Stage getShopStage() {
        return shopStage;
    }

    public void setShopStage(Stage shopStage) {
        this.shopStage = shopStage;
    }
}
