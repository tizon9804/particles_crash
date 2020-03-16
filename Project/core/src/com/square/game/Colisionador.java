package com.square.game;

/**
 * Created by Tizon on 08/07/2015.
 */
public class Colisionador {
    /*
    x posicion del objeto centrado
    width tama&ntilde;o del  objeto
    terreno lugar del objeto
   return 0 si no hay colision -x colision primaria arriba o izquierda con su delta de diferencia y +x colision abajo o derecha con su delta de diferencia
     */
    public static float esColisionXTerreno(float x, float width, com.square.entity.CuadradoBase terreno) {
        //la posicion limite del cuadrado en x
        float xl = x - (width / 2);
        float xr = x + (width / 2);
        // Gdx.app.log("input", "colisionxr: " + xr + "## " + (terreno.getWidth()));
        //  Gdx.app.log("input", "colisionxl: " + xl + "## " + (terreno.getPosx()));
        if (xl < terreno.getPosx())
            return (xl - terreno.getPosx());
        if (xr > terreno.getPosx() + terreno.getWidth())
            return (xr - (terreno.getPosx() + terreno.getWidth()));
        return 0;
    }

    /*
    x posicion del objeto centrado
    width tamano del  objeto
    terreno lugar del objeto
    return 0 si no hay colision -x colision primaria arriba o izquierda con su delta de diferencia y +x colision abajo o derecha con su delta de diferencia
     */
    public static float esColisionYTerreno(float y, float height, com.square.entity.CuadradoBase terreno) {
        //la posicion limite del cuadrado en x
        float yu = y - (height / 2);
        float yd = y + (height / 2);
        if (yu < terreno.getPosy())
            return (yu - terreno.getPosy());
        if (yd > terreno.getPosy() + terreno.getHeight())
            return (yd - (terreno.getPosy() + terreno.getHeight()));
        return 0;
    }

    /*
   x posicion del objeto centrado
   width tama�o del  objeto
   terreno lugar del objeto
   retorna el lado que colisiono
    */
    public static Boolean esColisionCuadrado(com.square.entity.CuadradoBase first, com.square.entity.CuadradoBase second) {
        if (first.getRect().overlaps(second.getRect())) {
            //Gdx.app.log("Colisionador", "colisiono: " + second.getPosx() + "## " + second.getPosy());
            return true;
        }
        return false;
    }


}
