package com.example.biglionii;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Duck {
    Context context;
    Bitmap duck;
    int ox, oy;
    Random random;

    public Duck(Context context) {
        this.context = context;
        duck = BitmapFactory.decodeResource(context.getResources(), R.drawable.duckl);
        random = new Random();
        ox = random.nextInt(Game.screenWidth);
        oy = Game.screenHeight - duck.getHeight()*2;
    }

    public Bitmap getDuck(){
        return duck;
    }

    int getOurDuckWidth(){
        return duck.getWidth();
    }
}