package com.example.biglionii;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    Bitmap explosion[] = new Bitmap[9];
    int explosionFrame;
    int eX, eY;

    public Explosion(Context context, int eX, int eY) {
        explosion[0] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.explosion);

        explosionFrame = 0;
        this.eX = eX;
        this.eY = eY;
    }

    public Bitmap getExplosion(int explosionFrame){
        return explosion[explosionFrame];
    }
}