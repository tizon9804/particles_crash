package com.square.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Tizon on 15/07/2015.
 */
public class Persistence {


    public static void writeFile(String object, String name) {
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        prefs.putString(name, object);
        prefs.flush();
    }

    public static String readFile(String name) {
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        return prefs.getString(name);
    }
}
