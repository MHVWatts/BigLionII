package com.example.biglionii;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Drop {
    Bitmap drop;
    Context context;
    int shx, shy;

    public Drop(Context context, int shx, int shy) {
        this.context = context;
        drop = BitmapFactory.decodeResource(context.getResources(), R.drawable.drop);
        this.shx = shx;
        this.shy = shy;
    }
    public Bitmap getDrop(){
        return drop;
    }
    public int getDropWidth() {
        return drop.getWidth();
    }
    public int getDropHeight() {
        return drop.getHeight();
    }
}