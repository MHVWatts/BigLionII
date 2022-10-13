package com.example.biglionii;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class BigLion {
    Context context;
    Bitmap bigLion;
    int ex, ey;
    int enemyVelocity;
    Random random;

    public BigLion(Context context) {
        this.context = context;
        bigLion = BitmapFactory.decodeResource(context.getResources(), R.drawable.biglion);
        random = new Random();
        ex = 200 + random.nextInt(400);
        ey = 0;
        enemyVelocity = 14 + random.nextInt(10);
    }

    public Bitmap getBigLion(){
        return bigLion;
    }

    int getBigLionWidth(){
        return bigLion.getWidth();
    }

    int getEnemySpaceshipHeight(){
        return bigLion.getHeight();
    }
}