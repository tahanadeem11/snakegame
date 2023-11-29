package com.example.snakegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    // list of snake points / snake length
    private final List<SnakePoints> snakePointsList = new ArrayList<>();
    private SurfaceView surfaceView;
    private TextView scoreTV;
    // surface holder to draw snake surface's canvas
    private SurfaceHolder surfaceHolder;

    // snake moving position. values must be right, left, top, bottom.
    // by default snake move to right.
    private String movingPosition = "right";

    //score
    private int score = 0;

    // snake size / point size
    // you can change this value to make bigger size snake
    private static final int pointSize = 28;

    // default snake tale
    private  static final int defaultTalePoints = 3;

    // snake color
    private static final int snakeColor = Color.YELLOW;

    // snake moving speed. value must be lie between 1 - 1000
    private static final int snakeMovingSpeed = 800;

    // random point position cordinates on the surfaceView
    private int positionX, positionY;

    // timer to move snake / change snake position after a specific time (snakeMovingSpeed)
    private Timer timer;

    // canvas to draw snake and show on surface View
    private Canvas canvas = null;

    // point color / single point color of a snake
    private Paint pointColor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView= findViewById(R.id.surfaceView);
        scoreTV= findViewById(R.id.scoreTV);

        // getting image button from xml file
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);

        // adding callback to surfaceView
        surfaceView.getHolder().addCallback(this);
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if previous moving position is not bottom. snake can't move.
                // for example if snake moving to bottom then snake can't directly start moving to top
                // snake must take right of left first then top
                if(!movingPosition.equals("bottom")){
                    movingPosition = "top";
                }

            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("right")){
                    movingPosition = "left";
                }

            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("left")){
                    movingPosition = "right";
                }
            }
        });

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("top")){
                    movingPosition = "bottom";
                }

            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // when surface is created then get surfaceHolder from it and assign to surfaceHolder
        this.surfaceHolder = surfaceHolder;

        // init data for snake / surfaceview
        init();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
    private void init(){

        // clear snake points / snake length
        snakePointsList.clear();

        // set default score as 0
        scoreTV.setText("0");

        // make score 0
        score = 0;

        // setting default moving position
        movingPosition = "right";

        // default snake starting position on the screen
        int startPositionX = (pointSize)* defaultTalePoints;

        // making snake default length / points
        for(int i=0; i< defaultTalePoints; i++){

            // adding points to snake's tale
            SnakePoints snakePoints = new SnakePoints(startPositionX, pointSize);
            snakePointsList.add(snakePoints);

            // increasing value for next point as snake's tale
            startPositionX = startPositionX - (pointSize * 2);

        }

        // add random points on the screen to be added by snake
        addPoint();

        // start moving snake / start game
        movesnake();

    }

    private void addPoint(){

        // getting surfaceView width and height to add point on the surface to be eaten by the snake
        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        // check randomXPosition is even or odd value. We need need only even number
        if((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;
        }

        // check randomYPosition is even or odd value. We need need only even number
        if((randomYPosition % 2) != 0) {
            randomYPosition = randomYPosition +1;
        }
       positionX = (pointSize * randomXPosition) + pointSize;
        positionY =  (pointSize * randomYPosition) + pointSize;
    }

    private Paint movesnake(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // getting head position
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();

                // check if snake eaten a point
                if(headPositionX == positionX && positionY == headPositionY){

                    // grow snake after eaten point
                    growSnake();

                    // add another snake random point on the screen
                    addPoint();
                }

                // check of which snake side moving
                switch (movingPosition){
                    case "right":
                        // moving snake head's to right
                        // other points follow snake head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;

                    case "left":
                        // moving snake head's to left
                        // other points follow snake head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;

                    case "top":
                        // moving snake head's to top
                        // other points follow snake head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY - (pointSize * 2));
                        break;

                    case "bottom":
                        // moving snake head's to bottom
                        // other points follow snake head point to move the snake
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                }

                // check if game over. whether snake touch edges or snake itself
                if(checkGameOver(headPositionX, headPositionY)){

                    // stop this timer / stop moving snake
                    timer.purge();
                    timer.cancel();

                    // show game over dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this);
                    builder.setMessage("Your Score ="+score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // restart game / re- init data
                            init();
                        }
                    });

                    // timer run in background so we need to show dialog on main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();

                        }
                    });
                }

                else{

                    // lock canvas on surfaceHolder to draw on it
                    canvas = surfaceHolder.lockCanvas();

                    // clear canvas with white color
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    // change snake head's position. other snake points will follow snake's head
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createPointColor());

                    // draw random point circle on the surface to be eaten by the snake
                    canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                    // other points following snake head's position 0 is head of snake
                    for(int i = 1; i < snakePointsList.size(); i++){

                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();

                        // move point across the head
                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(), pointSize, createPointColor());

                        // change head position
                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                }

                    // unlock canvas to draw on surfaceView
                    surfaceHolder.unlockCanvasAndPost(canvas);
            }
        };  1000- snakeMovingSpeed, 1000- snakeMovingSpeed);
    }

    private void growSnake(){

            // create new snake point
            SnakePoints snakePoints = new SnakePoints(0,0);

            // add point to the snake tale
            snakePointsList.add(snakePoints);

            // increase score
            score++;

            // setting score to text view
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTV.setText(String.valueOf(score));
            }
        });

        }
    private boolean checkGameOver(int headPositionX, int headPositionY){
        boolean gameOver = false;

        // check if snake head touches edges
        if(snakePointsList.get(0).getPositionX() < 0 ||
        snakePointsList.get(0).getPositionY() < 0 ||
        snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() ||
        snakePointsList.get(0).getPositionY() >= surfaceView.getHeight())
        {
         gameOver = true;
        }
        else{

            //check if snake head touches snake itself
            for(int i = 1; i < snakePointsList.size(); i++){

                if(headPositionX == snakePointsList.get(i).getPositionX() &&
                headPositionY == snakePointsList.get(i).getPositionY()){
                    gameOver = true;
                    break;
                }
            }
        }

        return gameOver;
    }

    private Paint createPointColor(){

        // check color is not defined before
        if(pointColor == null){
            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true); //smoothness
             }
            return pointColor;
        }

    }

}