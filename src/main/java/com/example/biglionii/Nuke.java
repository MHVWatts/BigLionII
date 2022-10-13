package com.example.biglionii;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Nuke {
    Bitmap nuke;
    Context context;
    int nukex, nukey;

    public Nuke(Context context, int nukex, int nukey) {
        this.context = context;
        nuke = BitmapFactory.decodeResource(context.getResources(), R.drawable.nuke);
        this.nukex = nukex;
        this.nukey = nukey;
    }
    public Bitmap getNuke(){
        return nuke;
    }
    public int getShotWidth() {
        return nuke.getWidth();
    }
    public int getShotHeight() {
        return nuke.getHeight();
    }
}