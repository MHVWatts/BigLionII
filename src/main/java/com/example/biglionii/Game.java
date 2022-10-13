package com.example.biglionii;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class Game extends View {
    Context context;
    Bitmap background, lifeImage;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 3;
    Paint scorePaint;
    Paint paint;
    int TEXT_SIZE = 80;
    boolean paused = false;
    Duck duck;
    BigLion bigLion;
    Random random;
    ArrayList<Nuke> enemyNukes;
    ArrayList<Drop> ourDrops;
    Explosion explosion;
    Background background1;
    ArrayList<Explosion> explosions;
    boolean NukeAction = false;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };


    public Game(Context context, int screenX, int screenY) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        enemyNukes = new ArrayList<>();
        ourDrops = new ArrayList<>();
        explosions = new ArrayList<>();
        duck = new Duck(context);
        bigLion = new BigLion(context);
        handler = new Handler();
        background1 = new Background(screenX, screenY, getResources());
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.egg3);
        scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Draw background, points and life
        canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
        canvas.drawText("Pt: " + points, 0, TEXT_SIZE, scorePaint);
        for(int i=life; i>=1; ++i){
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.getWidth() * i, 0, null);
        }
        //Life hits 0, launch GameOver
        if(life == 0){
            paused = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        //Move Big Lion
        bigLion.ex += bigLion.enemyVelocity;
        //If Big Lion collides with right wall, reverse him
        if(bigLion.ex + bigLion.getBigLionWidth() >= screenWidth){
            bigLion.enemyVelocity *= -1;
        }
        //If Big Lion collides with left wall, again reverse him
        if(bigLion.ex <=0){
            bigLion.enemyVelocity *= -1;
        }
        //Until enemyShotAction is false, enemy should fire shots from random travelled distance
        if(NukeAction == false){
            if(bigLion.ex >= 200 + random.nextInt(400)){
                Nuke enemyNuke = new Nuke(context, bigLion.ex + bigLion.getBigLionWidth() / 2, bigLion.ey );
                enemyNukes.add(enemyNuke);
                //enemyShotAction is true so that Big Lion can take a shot at a time
                NukeAction = true;
            }
            if(bigLion.ex >= 400 + random.nextInt(800)){
                Nuke enemyNuke = new Nuke(context, bigLion.ex + bigLion.getBigLionWidth() / 2, bigLion.ey );
                enemyNukes.add(enemyNuke);
                //enemyShotAction is true so that Big Lion can take a shot at a time
                NukeAction = true;
            }
            else{
                Nuke enemyNuke = new Nuke(context, bigLion.ex + bigLion.getBigLionWidth() / 2, bigLion.ey );
                enemyNukes.add(enemyNuke);
                //enemyShotAction is true so that Big Lion can take a shot at a time
                NukeAction = true;
            }
        }
        //Draw the Big Lion
        canvas.drawBitmap(bigLion.getBigLion(), bigLion.ex, bigLion.ey, null);
        //Draw our duck between the left and right edge of the screen
        if(duck.ox > screenWidth - duck.getOurDuckWidth()){
            duck.ox = screenWidth - duck.getOurDuckWidth();
        }else if(duck.ox < 0){
            duck.ox = 0;
        }
        //Draw our duck
        canvas.drawBitmap(duck.getDuck(), duck.ox, duck.oy, null);
        //Draw the nuke downwards our duck, if hit decrase life, remove the nuke from enemyShots ArrayList and show an explosion.
        //Else if, it goes away through the bottom edge of the screen also remove the nuke object from enemyShots.
        //When there is no enemyShots no the screen, change enemyShotAction to false, so that Big Lion can shoot.
        for(int i = 0; i < enemyNukes.size(); ++i){
            enemyNukes.get(i).nukey += 15;
            canvas.drawBitmap(enemyNukes.get(i).getNuke(), enemyNukes.get(i).nukex, enemyNukes.get(i).nukey, null);
            if((enemyNukes.get(i).nukex >= duck.ox)
                    && enemyNukes.get(i).nukex <= duck.ox + duck.getOurDuckWidth()
                    && enemyNukes.get(i).nukey >= duck.oy
                    && enemyNukes.get(i).nukey <= screenHeight){
                life--;
                enemyNukes.remove(i);
                explosion = new Explosion(context, duck.ox, duck.oy);
                explosions.add(explosion);

            }else if(enemyNukes.get(i).nukey >= screenHeight){
                enemyNukes.remove(i);
            }
            if(enemyNukes.size() < 1){
                NukeAction = false;
            }
        }
        //Draw our duck shots towards Big Lion. If the shot hits, increase player's points, remove the shot from ourShots and create a new Explosion object.
        //Else if, the shot goes away through the top edge of the screen also remove the shot object from enemyShots ArrayList.
        for(int i = 0; i < ourDrops.size(); ++i){
            ourDrops.get(i).shy -= 15;
            canvas.drawBitmap(ourDrops.get(i).getDrop(), ourDrops.get(i).shx, ourDrops.get(i).shy, null);
            if((ourDrops.get(i).shx >= bigLion.ex)
                    && ourDrops.get(i).shx <= bigLion.ex + bigLion.getBigLionWidth()
                    && ourDrops.get(i).shy <= bigLion.getBigLionWidth()
                    && ourDrops.get(i).shy >= bigLion.ey){
                points++;
                ourDrops.remove(i);
                explosion = new Explosion(context, bigLion.ex, bigLion.ey);
                explosions.add(explosion);
            }else if(ourDrops.get(i).shy <=0){
                ourDrops.remove(i);
            }
        }
        //Do the explosion
        for(int i=0; i < explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).eX, explosions.get(i).eY, null);
            explosions.get(i).explosionFrame++;
            if(explosions.get(i).explosionFrame > 8){
                explosions.remove(i);
            }
        }
        //If not paused, we’ll call the postDelayed() method on handler object which will cause the
        //run method inside Runnable to be executed after 30 milliseconds, that is the value inside
        //UPDATE_MILLIS.
        if(!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        //When event.getAction() is MotionEvent.ACTION_UP, if ourShots arraylist size < 1,
        //create a new Shot.
        //This way we restrict ourselves of making just one shot at a time, on the screen.
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(ourDrops.size() < 1){
                Drop ourDrop = new Drop(context, duck.ox + duck.getOurDuckWidth() / 2, duck.oy);
                ourDrops.add(ourDrop);
            }
        }
        //When event.getAction() is MotionEvent.ACTION_DOWN, control duck
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            duck.ox = touchX;
        }
        //When event.getAction() is MotionEvent.ACTION_MOVE, control duck
        //along with the touch.
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            duck.ox = touchX;
        }
        //Returning true in an onTouchEvent() tells Android system that you already handled
        //the touch event and no further handling is required.
        return true;
    }
}