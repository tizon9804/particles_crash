package com.square.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.square.Menus.MainMenu;
import com.square.config.GameConfig;

/**
 * Created by Tizon on 14/08/2015.
 */
public class GeneradorSonido {

    private final static String MUSIC = "electro.mp3";
    private final static String CRASH = "wbs.wav";
    private final static String CRASHPARTICLE = "slime.wav";
    private final static String SPIN = "wdsl.wav";
    private final static String COLLISION = "woosh.wav";
    private final static String PICKUPCOIN = "pickupcoin.wav";
    private final static String POWERUPCHIC = "powerupchiquitolin.wav";
    private final static String POWERUP = "powerup.wav";
    private static boolean crashParticle;
    private boolean isPlaying;
    private static boolean collision;
    private static boolean crash;
    private static boolean spin;
    private static boolean pickup;
    private static boolean pwrup;
    private static boolean pwrupchic;
    private static boolean spinIn;
    private static Music music;
    private Sound soundSpin;
    private Sound soundCrash;
    private Sound soundCollision;
    private Sound soundCrashParticle;
    private Sound soundPickupCoin;
    private Sound soundPwrUp;
    private Sound soundPwrUpChic;

    public GeneradorSonido(){
        GameConfig.assetManager.load(CRASH,Sound.class);
        GameConfig.assetManager.load(CRASHPARTICLE,Sound.class);
        GameConfig.assetManager.load(SPIN,Sound.class);
        GameConfig.assetManager.load(COLLISION,Sound.class);
        GameConfig.assetManager.load(PICKUPCOIN,Sound.class);
        GameConfig.assetManager.load(POWERUP,Sound.class);
        GameConfig.assetManager.load(POWERUPCHIC, Sound.class);
        GameConfig.assetManager.load(MUSIC, Sound.class);
        music = Gdx.audio.newMusic(Gdx.files.internal(MUSIC));
    }
    public void cargarSonidos() {
        crash = false;
        collision = false;
        spin = false;
        spinIn = false;
        crashParticle = false;
        soundCrash = GameConfig.assetManager.get(CRASH,Sound.class);
        soundCrashParticle = GameConfig.assetManager.get(CRASHPARTICLE,Sound.class);
        soundSpin = GameConfig.assetManager.get(SPIN,Sound.class);
        soundCollision = GameConfig.assetManager.get(COLLISION,Sound.class);
        soundPickupCoin = GameConfig.assetManager.get(PICKUPCOIN,Sound.class);
        soundPwrUp =  GameConfig.assetManager.get(POWERUP,Sound.class);
        soundPwrUpChic =  GameConfig.assetManager.get(POWERUPCHIC, Sound.class);
    }

    public static void notifyCrash() {
        crash = true;
    }

    public static void notifyCrashParticle() {
        crashParticle = true;
    }

    public static void notifyColision() {
        collision = true;
    }

    public static void notifyPickUpCoin(){
        pickup = true;
    }
    public static void notifyPowerUp(){
        pwrup = true;
    }
    public static void notifyPowerUPChic(){
        pwrupchic = true;
    }

    public static void notifySpinIn() {
        if (!spin) {
            spin = true;
            spinIn = true;
        }
    }

    public static void notifySpinOut() {
        spin = false;
    }

    public void update() {
        if (GameConfig.sound) {
            if (crash) {
                crash = false;
                soundCrash.play(2.0f);
            }
            if (crashParticle) {
                crashParticle = false;
                soundCrashParticle.play(0.2f);
            }
            if (spinIn) {
                spinIn = false;
                soundSpin.play(1.0f);
            }
            if (collision) {
                collision = false;
                soundCollision.play(1.0f);
            }
            if(pickup){
                pickup = false;
                soundPickupCoin.play(0.1f);
            }
            if(pwrup){
                pwrup = false;
                soundPwrUp.play(0.2f);
            }
            if(pwrupchic){
                pwrupchic = false;
                soundPwrUpChic.play(0.2f);
            }
            if(MainMenu.started && !music.isPlaying()&& !GameConfig.pausa) {
                   music.setLooping(true);
                   music.play();
            }
            if(!MainMenu.started && music.isPlaying() ){
                music.stop();
            }
        }
    }

    public static boolean isCrashParticle() {
        return crashParticle;
    }

    public static void setCrashParticle(boolean crashParticle) {
        GeneradorSonido.crashParticle = crashParticle;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public static boolean isCollision() {
        return collision;
    }

    public static void setCollision(boolean collision) {
        GeneradorSonido.collision = collision;
    }

    public static boolean isCrash() {
        return crash;
    }

    public static void setCrash(boolean crash) {
        GeneradorSonido.crash = crash;
    }

    public static boolean isSpin() {
        return spin;
    }

    public static void setSpin(boolean spin) {
        GeneradorSonido.spin = spin;
    }

    public static boolean isPickup() {
        return pickup;
    }

    public static void setPickup(boolean pickup) {
        GeneradorSonido.pickup = pickup;
    }

    public static boolean isPwrup() {
        return pwrup;
    }

    public static void setPwrup(boolean pwrup) {
        GeneradorSonido.pwrup = pwrup;
    }

    public static boolean isPwrupchic() {
        return pwrupchic;
    }

    public static void setPwrupchic(boolean pwrupchic) {
        GeneradorSonido.pwrupchic = pwrupchic;
    }

    public static boolean isSpinIn() {
        return spinIn;
    }

    public static void setSpinIn(boolean spinIn) {
        GeneradorSonido.spinIn = spinIn;
    }

    public static Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Sound getSoundSpin() {
        return soundSpin;
    }

    public void setSoundSpin(Sound soundSpin) {
        this.soundSpin = soundSpin;
    }

    public Sound getSoundCrash() {
        return soundCrash;
    }

    public void setSoundCrash(Sound soundCrash) {
        this.soundCrash = soundCrash;
    }

    public Sound getSoundCollision() {
        return soundCollision;
    }

    public void setSoundCollision(Sound soundCollision) {
        this.soundCollision = soundCollision;
    }

    public Sound getSoundCrashParticle() {
        return soundCrashParticle;
    }

    public void setSoundCrashParticle(Sound soundCrashParticle) {
        this.soundCrashParticle = soundCrashParticle;
    }

    public Sound getSoundPickupCoin() {
        return soundPickupCoin;
    }

    public void setSoundPickupCoin(Sound soundPickupCoin) {
        this.soundPickupCoin = soundPickupCoin;
    }

    public Sound getSoundPwrUp() {
        return soundPwrUp;
    }

    public void setSoundPwrUp(Sound soundPwrUp) {
        this.soundPwrUp = soundPwrUp;
    }

    public Sound getSoundPwrUpChic() {
        return soundPwrUpChic;
    }

    public void setSoundPwrUpChic(Sound soundPwrUpChic) {
        this.soundPwrUpChic = soundPwrUpChic;
    }
}
