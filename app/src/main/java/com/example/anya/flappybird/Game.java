package com.example.anya.flappybird;

/**
 * Created by anya on 7/7/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.*;
import java.lang.*;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Game extends View implements Runnable {

    Paint fontSettings = new Paint();
    String consoleText;

    Paint mPaint = new Paint();

    //COLOURS
    //text colour
    final int textColour = 0xffffff00;
    //background colour
    final int backgroundColour = 0xff5db4dd;
    //0xff4ec0ca
    //0xff4ec0ee
    //0xff48b2dd
    //0xff48aadd
    //0xff48addd

    //controls
    final int scrollspeed = 5;
    final int scrollspeed2 = 1;

    int spacing; //space btwn pipes (individual)

    int x; //limiter for pipe Y coordinates

    //GRAPHICS
    //scrolling scene
    Bitmap scene;
    Rect s1;
    Rect s2;
    Rect s3;
    int swidth;
    int sheight;
    int s1X;
    int s2X;
    int s3X;
    int sY;

    //pipes
    Bitmap pipe;
    Bitmap pipe2;
    Rect p1;
    Rect p2;
    Rect p3;
    Rect pA;
    Rect pB;
    Rect pC;
    int pwidth;
    int pheight;
    int p1X;
    int p2X;
    int p3X;
    int p1Y;
    int p2Y;
    int p3Y;
    int pspacing;
    int nextpipe; //the next pipe that will be passed (for keeping score)

    //ground
    Bitmap ground;
    Rect g1;
    Rect g2;
    Rect g3;
    Rect g4;
    Rect g5;
    Rect g6;
    Rect g7;
    int gwidth;
    int gheight;
    int g1X;
    int g2X;
    int g3X;
    int g4X;
    int g5X;
    int g6X;
    int g7X;
    int gY;

    //bird sprite
    Bitmap birds; //yellow bird strip
    Rect b;
    Rect subB; //switch sprites
    int bheight;
    int bwidth;
    int spriteNum; //0 to 2
    int counter;
    int bX;
    int bY;
    float realY; //holds the actual y coordinate for the bird
    int angle; //does this need to be a float
    double linVelocity;
    double linAccel; //linear acceleration
    int termVelocity;
    int onTapLinVel;
    boolean upBob = true; //for bob motion at beginning
    int countBob = 0; //counter for bobbing motion

    //start screen
    Bitmap prompter;
    Rect prompt;
    int promptheight;
    int promptwidth;
    int promptX;
    int promptY;

    //score and high score
    int score;
    Bitmap onesMap;
    Bitmap tensMap;
    Bitmap hundsMap;
    Rect sOnesRect;
    Rect sTensRect;
    Rect sHundsRect;
    Rect onesRect;
    Rect tensRect;
    Rect hundsRect;
    int digitHeight;
    int digitWidth;
    int onesDigit;
    int tensDigit;
    int hundsDigit;
    int scoreX;
    int scoreY;

    //gameover and get ready
    Bitmap getReadyMap;
    Rect getReadyRect;
    int bannerHeight;
    int bannerWidth;
    int bannerX;
    int bannerY;
    Bitmap gameOverMap;
    Rect gameOverRect;


    //title
    Bitmap title;
    Rect titleRect;
    int titleheight;
    int titlewidth;
    int titleX;
    int titleY;

    //play button
    Bitmap play;
    Rect playRect;
    int playheight;
    int playwidth;
    int playX;
    int playY;

    //rankings button
    Bitmap rank;
    Rect rankRect;
    int rankheight;
    int rankwidth;
    int rankX;
    int rankY;

    //menu btn on gameover
    Bitmap menu;
    Rect menuRect;
    int menuheight;
    int menuwidth;
    int menuX;
    int menuY;

    //ok btn on gameover
    Bitmap ok;
    Rect okRect;
    //uses height and width from menu btn
    int okX;
    int okY;

    //scoreview
    Bitmap scoreView;
    Rect scoreViewRect;
    int scoreViewHeight;
    int scoreViewWidth;
    int scoreViewX;
    int scoreViewY;

    //medals
    Bitmap bronze;
    Bitmap silver;
    Bitmap gold;
    Bitmap platinum;
    Rect medalRect;
    int medalWidth;
    int medalHeight;
    int medalX;
    int medalY;
    int highscore; //user's highest score is fetched from internal storage
    Bitmap newMap;
    Rect newRect;
    int newHeight;
    int newWidth;
    int newX;
    int newY;
    Bitmap highOnesMap;
    Bitmap highTensMap;
    Bitmap highHundsMap;
    Rect highOnesSrc;
    Rect highTensSrc;
    Rect highHundsSrc;
    Rect highOnesDest;
    Rect highTensDest;
    Rect highHundsDest;
    int highscoreX;
    int highscoreY;
    int highOnesDigit;
    int highTensDigit;
    int highHundsDigit;
    

    //score when game is over
    int finalScoreX;
    int finalScoreY;

    Paint fadePaint; //use for fading in


    boolean wait; //used to add a few seconds pause
    boolean fetched; //used to get high score only once
    boolean newHighScore;

    double SCALE;

    final String FILE_NAME = "highscore.txt";



    //generic
    Rect button; //for btn pressed

    int SCREENHEIGHT, SCREENWIDTH;



    enum GameState {
        STARTSCREEN, WAITFORTOUCH, PLAY, GAMEOVER, DISPLAYRANKINGS
    }

    //initialize enum gameState
    GameState currentState = GameState.STARTSCREEN;


    Point touchPoint = new Point();
    boolean isTouchDown = false;

    public Game(Context context) {
        super(context);
        this.Initialize(context);
    }

    //INITIALIZE LIVES HERE
    protected boolean Initialize(Context context) {
        fetched = false;
        //reset score
        score = 0;
        onesDigit = 0;
        tensDigit = 0;
        hundsDigit = 0;

        //get screen info
        SCREENWIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        SCREENHEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

        SCALE = SCREENWIDTH / 1300.00;
        linAccel = 1.15 * SCALE; //FIND AND EDIT

        termVelocity = (int)(50*SCALE); //orig 50
        onTapLinVel = (int)(-25*SCALE); //orig 20

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;

        //scoring
        onesMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.numstring, o);
        tensMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.numstring, o);
        hundsMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.numstring, o);
        digitHeight = (onesMap.getHeight());
        digitWidth = (onesMap.getWidth() / 10);
        scoreX = SCREENWIDTH / 2;
        scoreY = SCREENHEIGHT / 20;
        onesRect = new Rect((onesDigit * digitWidth), 0, (onesDigit * digitWidth) + digitWidth, digitHeight);
        tensRect = new Rect((tensDigit * digitWidth), 0, (tensDigit * digitWidth) + digitWidth, digitHeight);
        hundsRect = new Rect((hundsDigit * digitWidth), 0, (hundsDigit * digitWidth) + digitWidth, digitHeight);
        sOnesRect = new Rect(scoreX, scoreY, scoreX + (int)(SCALE*digitWidth), scoreY + (int)(SCALE*digitHeight));
        sTensRect = new Rect(scoreX, scoreY, scoreX + (int)(SCALE*digitWidth), scoreY + (int)(SCALE*digitHeight));
        sHundsRect = new Rect(scoreX, scoreY, scoreX + (int)(SCALE*digitWidth), scoreY + (int)(SCALE*digitHeight));


        //menu and ok buttons
        menu = BitmapFactory.decodeResource(context.getResources(), R.drawable.menubtn, o);
        menuheight = (int)(SCALE*menu.getHeight());
        menuwidth = (int)(SCALE*menu.getWidth());
        menuX = (SCREENWIDTH / 2) - menuwidth - (int)(60 * SCALE);
        menuY = (SCREENHEIGHT - (int)(450*SCALE));
        menuRect = new Rect(menuX, menuY, menuX + menuwidth, menuY + menuheight);

        ok = BitmapFactory.decodeResource(context.getResources(), R.drawable.okbtn, o);
        okX = (SCREENWIDTH / 2) + (int)(60 * SCALE);
        okY = menuY;
        okRect = new Rect(okX, okY, okX + menuwidth, okY + menuheight);

        //banner (gameover and getready)
        getReadyMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.getreadybanner, o);
        bannerHeight = (int)(SCALE*getReadyMap.getHeight());
        bannerWidth = (int)(SCALE*getReadyMap.getWidth());
        bannerX = ((SCREENWIDTH - bannerWidth) / 2);
        bannerY = scoreY + (int)(SCALE * digitHeight) + (int)(SCALE* 250);
        getReadyRect = new Rect(bannerX, bannerY, bannerX + bannerWidth, bannerY + bannerHeight);
        gameOverMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameoverbanner, o);
        gameOverRect = new Rect(bannerX, bannerY, bannerX + bannerWidth, bannerY + bannerHeight);

        //scoreView
        scoreView = BitmapFactory.decodeResource(context.getResources(), R.drawable.scoreview, o);
        scoreViewHeight = (int)(SCALE*scoreView.getHeight());
        scoreViewWidth = (int)(SCALE*scoreView.getWidth());
        scoreViewX = (SCREENWIDTH - scoreViewWidth) / 2;
        scoreViewY = bannerY + bannerHeight + (int)(100 * SCALE); //starts far off screen and will come in gradually
        scoreViewRect = new Rect(scoreViewX, scoreViewY, scoreViewX + scoreViewWidth, scoreViewY + scoreViewHeight);
        fadePaint = new Paint();
        fadePaint.setAlpha(0);

        //medals
        bronze = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_bronze, o);
        silver = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_silver, o);
        gold = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_gold, o);
        platinum = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_platinum, o);
        medalWidth = (int)(SCALE*bronze.getWidth());
        medalHeight = (int)(SCALE*bronze.getHeight());
        medalX = scoreViewX + (int)(SCALE* 130);
        medalY = scoreViewY + (int)(SCALE* 210);
        medalRect = new Rect(medalX, medalY, medalX + medalWidth, medalY + medalHeight);
        finalScoreX = scoreViewX + scoreViewWidth - (int)(100*SCALE) - (int)(SCALE*(digitWidth/2));
        finalScoreY = (scoreViewY) + (scoreViewHeight / 2) - (int)(SCALE*(digitHeight / 2)) - (int)(20*SCALE);

        newMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.newtext, o);
        newHeight = (int)(SCALE*newMap.getHeight());
        newWidth = (int)(SCALE*newMap.getWidth());
        newX = scoreViewX + (scoreViewWidth / 2) + (int)(100 * SCALE);
        newY = scoreViewY + (scoreViewHeight / 2) + (int)(SCALE * 10);
        newRect = new Rect(newX, newY, newX + newWidth, newY + newHeight);

        highOnesMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.numstring, o);
        highTensMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.numstring, o);
        highHundsMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.numstring, o);
        highscoreX = finalScoreX;
        highscoreY = scoreViewY + scoreViewHeight - (int)(SCALE*digitHeight) - (int)(10 * SCALE);
        highOnesSrc = new Rect((highOnesDigit * (digitWidth)), 0, (highOnesDigit + 1) * (digitWidth), (digitHeight));
        highTensSrc = new Rect((highTensDigit * digitWidth), 0, ((highTensDigit + 1) * digitWidth), digitHeight);
        highHundsSrc = new Rect((highHundsDigit * digitWidth), 0, (highHundsDigit + 1) * digitWidth, digitHeight);
        highOnesDest = new Rect(highscoreX, highscoreY, highscoreX + (int)(SCALE*(digitWidth / 2)), highscoreY + (int)(SCALE*(digitHeight / 2)));
        highTensDest = new Rect(highscoreX - (int)(SCALE*(digitWidth / 2)), highscoreY, highscoreX, highscoreY + (int)(SCALE*(digitHeight / 2)));
        highHundsDest = new Rect(highscoreX - (int)(SCALE*digitWidth), highscoreY, highscoreX - (int)(SCALE*(digitWidth / 2)), highscoreY + (int)(SCALE*(digitHeight / 2)));

        //ground
        ground = BitmapFactory.decodeResource(context.getResources(), R.drawable.ground, o);
        gwidth = (int)(SCALE*ground.getWidth());
        gheight = (int)(SCALE*ground.getHeight());
        g1X = 0;
        g2X = g1X + gwidth;
        g3X = g2X + gwidth;
        g4X = g3X + gwidth;
        g5X = g4X + gwidth;
        g6X = g5X + gwidth;
        g7X = g6X + gwidth;
        gY = SCREENHEIGHT - gheight;
        g1 = new Rect(g1X, gY, g1X + gwidth, gY + gheight);
        g2 = new Rect(g2X, gY, g2X + gwidth, gY + gheight);
        g3 = new Rect(g3X, gY, g3X + gwidth, gY + gheight);
        g4 = new Rect(g4X, gY, g4X + gwidth, gY + gheight);
        g5 = new Rect(g5X, gY, g5X + gwidth, gY + gheight);
        g6 = new Rect(g6X, gY, g6X + gwidth, gY + gheight);
        g7 = new Rect(g7X, gY, g7X + gwidth, gY + gheight);

        //scene
        scene = BitmapFactory.decodeResource(context.getResources(), R.drawable.scene, o);
        //scene2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.scene, o);
        swidth = (int)(SCALE*scene.getWidth());
        sheight = (int)(SCALE*scene.getHeight());
        s1X = 0;
        s2X = s1X + swidth;
        s3X = s2X + swidth;
        sY = SCREENHEIGHT - (sheight + gheight);
        s1 = new Rect(s1X, sY, s1X + swidth, sY + sheight);
        s2 = new Rect(s2X, sY, s2X + swidth, sY + sheight);
        s3 = new Rect(s3X, sY, s3X + swidth, sY + sheight);

        //pipes
        nextpipe = 1;
        pipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipes, o);
        pipe2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bottom, o);

        //get parameters
        pwidth = (int)(SCALE*pipe.getWidth());
        pheight = (int)(SCALE*pipe.getHeight());
        x = (pheight / 5);
        pspacing = (int)(pwidth * 1.75);
        spacing = (int)(0.3*SCREENHEIGHT);

        //set coordinates
        p1X = pspacing + SCREENWIDTH;
        p2X = p1X + pwidth + pspacing;
        p3X = p2X + pwidth + pspacing;
        p1Y = getY(spacing, pheight, x, SCREENHEIGHT);
        p2Y = getY(spacing, pheight, x, SCREENHEIGHT);
        p3Y = getY(spacing, pheight, x, SCREENHEIGHT);


        //create rectangles
        p1 = new Rect(p1X, p1Y, p1X + pwidth, p1Y + pheight);
        p2 = new Rect(p2X, p2Y, p2X + pwidth, p2Y + pheight);
        p3 = new Rect(p3X, p3Y, p3X + pwidth, p3Y + pheight);
        pA = new Rect(p1X, p1.bottom + spacing, p1X + pwidth, p1.bottom + pheight + spacing);
        pB = new Rect(p2X, p2.bottom + spacing, p2X + pwidth, p2.bottom + pheight + spacing);
        pC = new Rect(p3X, p3.bottom + spacing, p3X + pwidth, p3.bottom + pheight + spacing);


       //BIRD (playable sprite)
        birds = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowbirds, o);

        //get parameters
        bwidth = birds.getWidth();
        bheight = birds.getHeight();

        spriteNum = 0;
        counter = 0;
        angle = 0;


        //title
        title = BitmapFactory.decodeResource(context.getResources(), R.drawable.titletxt, o);
        titleheight = (int)(SCALE*title.getHeight());
        titlewidth = (int)(SCALE*title.getWidth());
        titleX = (SCREENWIDTH / 2) - ((titlewidth + (bwidth / 3) + (int)(30*SCALE)) / 2);
        titleY = bY + ((titleheight - bheight) / 2); //title needs to bob with bY
        titleRect = new Rect(titleX, titleY, titleX + titlewidth, titleY + titleheight);

        //bird w/ more params (rel to title)
        bX = titleX + titlewidth + (int)(30* SCALE);
        bY = SCREENHEIGHT / 3;
        b = new Rect(bX, bY, bX + (int)((bwidth / 3)), bY + (int)(bheight));
        subB = new Rect(spriteNum * (bwidth / 3), 0, (spriteNum * (bwidth / 3)) + (bwidth / 3), bheight);

        //play button
        play = BitmapFactory.decodeResource(context.getResources(), R.drawable.playbtn, o);
        playheight = (int)(SCALE*play.getHeight());
        playwidth = (int)(SCALE*play.getWidth());
        playX = (SCREENWIDTH / 2) - playwidth - (int)(60*SCALE);
        playY = gY - playheight;
        playRect = new Rect(playX, playY, playX + playwidth, playY + playheight);

        //rankings button
        rank = BitmapFactory.decodeResource(context.getResources(), R.drawable.rankbtn, o);
        rankheight = (int)(SCALE*rank.getHeight());
        rankwidth = (int)(SCALE*rank.getWidth());
        rankX = (SCREENWIDTH / 2) + (int)(60*SCALE);
        rankY = gY - rankheight;
        rankRect = new Rect(rankX, rankY, rankX + rankwidth, rankY + rankheight);

        //BEGIN SCREEN
        prompter = BitmapFactory.decodeResource(context.getResources(), R.drawable.beginprompt, o);

        //get parameters
        promptwidth = (int)(SCALE*prompter.getWidth());
        promptheight = (int)(SCALE*prompter.getHeight());

        promptX = (SCREENWIDTH / 4) + (bwidth/6);
        promptY = (SCREENHEIGHT / 2) - bheight;

        prompt = new Rect(promptX, promptY, promptX + promptwidth, promptY + promptheight);



        touchPoint = new Point();


        return true;
    }

    public void Reset() {
        //re-initialize things that need to be initialized again
        //reset booleans

        Initialize(getContext());
        //bird sprite's x
        if (currentState == GameState.WAITFORTOUCH) {
            bX = SCREENWIDTH / 3;
        } /*else if (currentState == GameState.STARTSCREEN) {
            bX = titleX + titlewidth + (int)(30*SCALE);
        } */

        //bird sprite's y
        if (currentState == GameState.WAITFORTOUCH) {
            bY = SCREENHEIGHT / 2;
        } /*else if (currentState == GameState.STARTSCREEN) {
            bY = SCREENHEIGHT / 3;
        } */

       /* //bird sprite's velocity
        linVelocity = 0;

        //score
        score = 0;
        onesDigit = 0;
        tensDigit = 0;
        hundsDigit = 0;


        //reset pipes
        nextpipe = 1;
        p1X = pspacing + SCREENWIDTH;
        p2X = p1X + pwidth + pspacing;
        p3X = p2X + pwidth + pspacing;
        p1Y = getY(spacing, pheight, x, SCREENHEIGHT);
        p2Y = getY(spacing, pheight, x, SCREENHEIGHT);
        p3Y = getY(spacing, pheight, x, SCREENHEIGHT); */

    }


    @Override
    //use this to get the size of the view ("this") which is not known during initialization
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        this.Reset(); //should only be called after playingArea is reset
    }


   public int getY(int spacing, int pheight, int x, int SCREENHEIGHT) {
        int returnInt = 0;
        Random rand = new Random();
       do {
           returnInt = - rand.nextInt(SCREENHEIGHT);
       } while ((pheight + returnInt) < x || ((pheight + returnInt) + spacing + x + gheight) > SCREENHEIGHT);


       return returnInt;
    }


    //put game mechanics here.  Update() will continuously be called by run() until game paused
    protected void Update() {

        switch(currentState)
        {
            case STARTSCREEN:
                //beginning screen w/ buttons
                //pause code at beginning to allow time for Reset() to run
                if (wait) {
                    sleepThread(100);
                    wait = false;
                }
                sleepThread(10);
                backgroundScroll();
                getFlappyY();
                if (pressedbtn(playRect)) {
                    touchPoint.set(0, 0);
                    currentState = GameState.WAITFORTOUCH;
                    isTouchDown = false;
                    bX = SCREENWIDTH / 4;
                    bY = SCREENHEIGHT / 2;
                }

                if (pressedbtn(rankRect)) {
                    isTouchDown = false;
                    currentState = GameState.DISPLAYRANKINGS;
                }

                break;
            case WAITFORTOUCH:
                //waiting for user to begin tapping
                //pause code at beginning to allow time for Reset() to run
                if (wait) {
                    sleepThread(100);
                    wait = false;
                }
                sleepThread(10);
                backgroundScroll();
                getFlappyY();
                if (isTouchDown) {
                    getStats();
                    touchPoint.set(0, 0);
                    isTouchDown = false;
                    currentState = GameState.PLAY;
                }
                break;
            case PLAY:
                //scroll code
                backgroundScroll();
                pScroll();
                getScore();
                getStats();

                if(collision()) {
                    //falls to ground
                    fallDown();
                    currentState = GameState.GAMEOVER;
                    isTouchDown = false;
                    touchPoint.set(0, 0);
                }

                if (touchGround()) {
                    //game over
                    sleepThread(20);
                    currentState = GameState.GAMEOVER;
                    isTouchDown = false;
                    touchPoint.set(0, 0);
                }

                sleepThread(10);
                break;
            case GAMEOVER:
                if (pressedbtn(menuRect)) {
                    wait = true;
                    currentState = GameState.STARTSCREEN;
                    Reset();
                    touchPoint.set(0, 0);
                    isTouchDown = false;
                }
                sleepThread(10);
                if (pressedbtn(okRect)) {
                    wait = true;
                    currentState = GameState.WAITFORTOUCH;
                    Reset();
                    touchPoint.set(0, 0);
                    isTouchDown = false;
                }
                //game over
                //show high score
                break;
            case DISPLAYRANKINGS:
                touchPoint.set(0, 0);
                if (isTouchDown) {
                    isTouchDown = false;
                    touchPoint.set(0, 0);
                    currentState = GameState.STARTSCREEN;
                }
                //display rankings screen
                //what does this even look like?
                break;
        }

    }


    protected boolean pressedbtn (Rect button) {
        return (button.contains(touchPoint.x, touchPoint.y));
    }


    public void sleepThread(int timePeriod) {
        //sleeps thread for given time period
        try {
            Thread.sleep((long)timePeriod);
        } catch (InterruptedException e) {
            //caught
        }
    }


    public void fallDown() {
        //flappy falls
        sleepThread(50);

        //fall
        while (!touchGround()) {
            //fall
            sleepThread(10);

            /*
            realY += linVelocity;
            bY = (int)realY;
            if (linVelocity + linAccel > termVelocity) {
                linVelocity = termVelocity;
            } else {
                linVelocity += linAccel;
            }
            */

            bY += linVelocity;
            if (linVelocity + linAccel > termVelocity) {
                linVelocity = termVelocity;
            } else {
                linVelocity += linAccel;
            }
        }
        sleepThread(500);
    }

    public boolean touchGround() {
        //check if it touches the ground
        return (bY + bheight >= gY + (int)(SCALE*20));
    }

    public void getFlappyY() {

        if (currentState == GameState.WAITFORTOUCH) {
                if (upBob & countBob == 0) {
                    bY ++;
                    if (bY >= (SCREENHEIGHT / 2) + 20) { //has gone up 50px from original
                        upBob = false; //make it go down
                        countBob = 0; //start counter for pauseBob
                    }
                } else if (countBob == 0){
                    bY --;
                    if (bY <= (SCREENHEIGHT / 2) - 20) { //gone down 50px from original
                        upBob = true; //make it go up
                        countBob = 0; //start counter for pauseBob
                    }
                } else {
                    countBob++;
                    if (countBob == 7) {
                        countBob = 0;
                    }
                }
            } else if (currentState == GameState.STARTSCREEN) {
            if (upBob & countBob == 0) {
                bY ++;
                if (bY >= (SCREENHEIGHT / 3) + 20) { //has gone up 50px from original
                    upBob = false; //make it go down
                    countBob = 0; //start counter for pauseBob
                }
            } else if (countBob == 0){
                bY --;
                if (bY <= (SCREENHEIGHT / 3) - 20) { //gone down 50px from original
                    upBob = true; //make it go up
                    countBob = 0; //start counter for pauseBob
                }
            } else {
                countBob++;
                if (countBob == 7) {
                    countBob = 0;
                }
            }
        }


    }


    public boolean collision() {
        //check if it collided
        Rect r1 = new Rect(b);
        return (r1.intersect(p1) | r1.intersect(pA) | r1.intersect(p2) | r1.intersect(pB) | r1.intersect(p3) | r1.intersect(pC));
    }


    public void pScroll() {
        //SCREEN SCROLLS
        p1X -= scrollspeed;
        p2X -= scrollspeed;
        p3X -= scrollspeed;
        //wrap and generate new Y coordinates
        if (p1X + pwidth < 0) {
            p1X = p3X + pwidth + pspacing;
            p1Y = getY(spacing, pheight, x, SCREENHEIGHT);
        } else if (p2X + pwidth < 0) {
            p2X = p1X + pwidth + pspacing;
            p2Y = getY(spacing, pheight, x, SCREENHEIGHT);
        } else if (p3X + pwidth < 0) {
            p3X = p2X + pwidth + pspacing;
            p3Y = getY(spacing, pheight, x, SCREENHEIGHT);
        }

    }


    public void backgroundScroll() {
        //scene scroll
        s1X -= scrollspeed2;
        s2X -= scrollspeed2;
        s3X -= scrollspeed2;
        //wrap
        if (s1X + swidth < 0) {
            s1X = s3X + swidth;
        } else if (s2X + swidth < 0) {
            s2X = s1X + swidth;
        } else if (s3X + swidth < 0) {
            s3X = s2X + swidth;
        }

        //ground scroll
        g1X -= scrollspeed;
        g2X -= scrollspeed;
        g3X -= scrollspeed;
        g4X -= scrollspeed;
        g5X -= scrollspeed;
        g6X -= scrollspeed;
        g7X -= scrollspeed;
        //wrap
        if (g1X  + gwidth < 0) {
            g1X = g7X + gwidth;
        } else if (g2X + gwidth < 0) {
            g2X = g1X + gwidth;
        } else if (g3X + gwidth < 0) {
            g3X = g2X + gwidth;
        } else if (g4X + gwidth < 0) {
            g4X = g3X + gwidth;
        } else if (g5X + gwidth < 0) {
            g5X = g4X + gwidth;
        } else if (g6X + gwidth < 0) {
            g6X = g5X + gwidth;
        } else if (g7X + gwidth < 0) {
            g7X = g6X + gwidth;
        }

        //bird animation
        counter ++;
        if (counter == 10) {
            spriteNum += 1;
            if (spriteNum == 3) {
                spriteNum = 0;
            }
                counter = 0;
        }

    }


    public void getScore() {
        int i;
        if (nextpipe == 1) {
            i = p1X;
        } else if (nextpipe == 2) {
            i = p2X;
        } else {
            i = p3X;
        }

        //get score
        if (bX >= i) {
            score++;
            nextpipe++;
            if (nextpipe == 4) {
                nextpipe = 1;
            }

            onesDigit++;
            if (onesDigit == 10) {
                onesDigit = 0;
                tensDigit++;
                if (tensDigit == 10) {
                    tensDigit = 0;
                    hundsDigit++;
                }
            }
        }


    }


    private Rect contains(int x, int y) {
        return null;
    }


    protected void Shutdown() {

    }


    public void getStats() {
        //get speed and angle of flappy
        //flappy needs an acceleration!
        //flappy should get an initial upward (negative) velocity when tapped
        if (isTouchDown) {
            linVelocity = onTapLinVel;
            isTouchDown = false;
        }

        if (bY + (bheight / 2) + linVelocity >= 0) {
            bY += linVelocity;
        } else {
            bY = -(bheight / 2);
            linVelocity = 0;
        }

        if (linVelocity + linAccel > termVelocity) {
            linVelocity = termVelocity;
        } else {
            linVelocity += linAccel;
        }

    }

    public void writeHighScore() {
        try {
           // OutputStreamWriter out = new OutputStreamWriter(Context.openFileOutput(FILE_NAME, MODE_PRIVATE));
            OutputStreamWriter out = new OutputStreamWriter(getContext().openFileOutput(FILE_NAME, MODE_PRIVATE));
            out.write(highscore);
            out.close();

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public int readHighScore () {
        int num = 0;

        try {
            InputStream inputStream = getContext().openFileInput(FILE_NAME);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //String str;
                //StringBuilder strBuf = new StringBuilder();

                num = bufferedReader.read();
                /*while((num = bufferedReader.read()) != null) {
                    strBuf.append(str);
                } */

                inputStream.close();

                //num = Integer.parseInt(strBuf.toString());

            }
        } catch (Exception e) {
            //caught
        }
        return num;
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        int touchY = (int)event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isTouchDown = true;
            touchPoint.set(touchX, touchY); //cache this value
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

        }

        if (event.getAction() == MotionEvent.ACTION_UP) {

        }

        if (event.getAction() == MotionEvent.ACTION_CANCEL) {

        }


        return true;
    }



    //drawing

    /* onDraw is called once by system, invalidate() calls onDraw(Canvas canvas)
     */

    @Override
    protected void onDraw(Canvas canvas) {
        if(wait) { //postpone drawing until wait is false
            sleepThread(200);
            wait = false;
        }

        //BACKGROUND COLOUR
        mPaint.setColor(backgroundColour);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, SCREENWIDTH, SCREENHEIGHT, mPaint);


        //SCENE (BACKDROP)
        s1.set(s1X, sY, s1X + swidth, sY + sheight);
        canvas.drawBitmap(scene, null, s1, null);
        s2.set(s2X, sY, s2X + swidth, sY + sheight);
        canvas.drawBitmap(scene, null, s2, null);
        s3.set(s3X, sY, s3X + swidth, sY + sheight);
        canvas.drawBitmap(scene, null, s3, null);


        //PIPES
        if (currentState == GameState.PLAY | currentState == GameState.GAMEOVER) { //draw only when playing
            //draw top pipes
            p1.set(p1X, p1Y, p1X + pwidth, p1Y + pheight);
            canvas.drawBitmap(pipe, null, p1, null);
            p2.set(p2X, p2Y, p2X + pwidth, p2Y + pheight);
            canvas.drawBitmap(pipe, null, p2, null);
            p3.set(p3X, p3Y, p3X + pwidth, p3Y + pheight);
            canvas.drawBitmap(pipe, null, p3, null);

            //draw bottom pipes
            pA.set(p1X, p1.bottom + spacing, p1X + pwidth, p1.bottom + spacing + pheight);
            canvas.drawBitmap(pipe2, null, pA, null);
            pB.set(p2X, p2.bottom + spacing, p2X + pwidth, p2.bottom + spacing + pheight);
            canvas.drawBitmap(pipe2, null, pB, null);
            pC.set(p3X, p3.bottom + spacing, p3X + pwidth, p3.bottom + spacing + pheight);
            canvas.drawBitmap(pipe2, null, pC, null);
        }

        //GET READY BANNER
        if (currentState == GameState.WAITFORTOUCH) {
            canvas.drawBitmap(getReadyMap, null, getReadyRect, null);
        }

        //GAME OVER BANNER
        if (currentState == GameState.GAMEOVER) {
            if (fadePaint.getAlpha() < 250) {
                fadePaint.setAlpha(fadePaint.getAlpha() + 5);
            }

            canvas.drawBitmap(gameOverMap, null, gameOverRect, fadePaint);
            canvas.drawBitmap(ok, null, okRect, null);
            canvas.drawBitmap(menu, null, menuRect, null);
            canvas.drawBitmap(scoreView, null, scoreViewRect, null);
            //score = 5; //use this to test the score and medal displays
            if (score >= 40) {
                canvas.drawBitmap(platinum, null, medalRect, null);
            } else if (score >= 30) {
                canvas.drawBitmap(gold, null, medalRect, null);
            } else if (score >= 20) {
                canvas.drawBitmap(silver, null, medalRect, null);
            } else if (score >= 10) {
                canvas.drawBitmap(bronze, null, medalRect, null);
            }

            Paint alphaPaint = new Paint();
            alphaPaint.setAlpha(195); //make slightly transparent

             //draw score
            sOnesRect.set(finalScoreX, finalScoreY, finalScoreX + (int)(SCALE*(digitWidth / 2)), finalScoreY + (int)(SCALE*(digitHeight / 2)));
            sTensRect.set(finalScoreX - (int)(SCALE*(digitWidth / 2)), finalScoreY, finalScoreX, finalScoreY + (int)(SCALE*(digitHeight / 2)));
            sHundsRect.set(finalScoreX - (int)(SCALE*digitWidth), finalScoreY, finalScoreX - (int)(SCALE*(digitWidth / 2)), finalScoreY + (int)(SCALE*(digitHeight / 2)));
            canvas.drawBitmap(onesMap, onesRect, sOnesRect, alphaPaint);
            if (score >= 10) {
                canvas.drawBitmap(tensMap, tensRect, sTensRect, alphaPaint);
            }
            if (score >= 100) {
                canvas.drawBitmap(hundsMap, hundsRect, sHundsRect, alphaPaint);
            }

            if (!fetched) {

                highscore = readHighScore();
                newHighScore = (score > highscore);
                if (newHighScore) {
                    highscore = score;
                    //write new high score to file
                    writeHighScore();
                }


                //get each digit
                highOnesDigit = highscore % 10;
                if (highscore >= 10) {
                    highTensDigit = ((highscore % 100) - highOnesDigit) / 10;
                }
                if (highscore >= 100) {
                    highHundsDigit = ((highscore - (10 * highTensDigit)) - highOnesDigit) / 100;
                }

                highOnesSrc.set((highOnesDigit * (digitWidth)), 0, (highOnesDigit + 1) * (digitWidth), (digitHeight));
                highTensSrc.set((highTensDigit * digitWidth), 0, (highTensDigit + 1) * digitWidth, digitHeight);
                highHundsSrc.set((highHundsDigit * digitWidth), 0, (highHundsDigit + 1) * digitWidth, digitHeight);
                highOnesDest.set(highscoreX, highscoreY, highscoreX + (int)(SCALE*(digitWidth / 2)), highscoreY + (int)(SCALE*(digitHeight / 2)));
                highTensDest.set(highscoreX - (int)(SCALE*(digitWidth / 2)), highscoreY, highscoreX, highscoreY + (int)(SCALE*(digitHeight / 2)));
                highHundsDest.set(highscoreX - (int)(SCALE*digitWidth), highscoreY, highscoreX - (int)(SCALE*(digitWidth / 2)), highscoreY + (int)(SCALE*(digitHeight / 2)));

                fetched = true;
            }


            canvas.drawBitmap(highOnesMap, highOnesSrc, highOnesDest, alphaPaint);
            if (highscore >= 10) {
                canvas.drawBitmap(highTensMap, highTensSrc, highTensDest, alphaPaint);
            }
            if (highscore >= 100) {
                canvas.drawBitmap(highHundsMap, highHundsSrc, highHundsDest, alphaPaint);
            }

            if (newHighScore) {
                canvas.drawBitmap(newMap, null, newRect, null);
            }

            //show high score
            //will need secondary set of bitmaps


        }

        //draw ground
        g1.set(g1X, gY, g1X + gwidth, gY + gheight);
        canvas.drawBitmap(ground, null, g1, null);
        g2.set(g2X, gY, g2X + gwidth, gY + gheight);
        canvas.drawBitmap(ground, null, g2, null);
        g3.set(g3X, gY, g3X + gwidth, gY + gheight);
        canvas.drawBitmap(ground, null, g3, null);
        g4.set(g4X, gY, g4X + gwidth, gY + gheight);
        canvas.drawBitmap(ground, null, g4, null);
        g5.set(g5X, gY, g5X + gwidth, gY + gheight);
        canvas.drawBitmap(ground, null, g5, null);
        g6.set(g6X, gY, g6X + gwidth, gY + gheight);
        canvas.drawBitmap(ground, null, g6, null);
        g7.set(g7X, gY, g7X + gwidth, gY + gheight);
        canvas.drawBitmap(ground, null, g7, null);


        if (currentState == GameState.STARTSCREEN) {
            //TITLE
            titleY = bY - ((titleheight - bheight) / 2); //title needs to bob with bY
            titleRect.set(titleX, titleY, titleX + titlewidth, titleY + titleheight);
            canvas.drawBitmap(title, null, titleRect, null);

            //PLAY BUTTON
            canvas.drawBitmap(play, null, playRect, null);

            //RANKINGS BUTTON
            canvas.drawBitmap(rank, null, rankRect, null);
        }

        //BIRD SPRITE
        if (currentState == GameState.STARTSCREEN | currentState == GameState.WAITFORTOUCH | currentState == GameState.PLAY) {
            //when sprite is onscreen
            subB.set(spriteNum * (bwidth /3), 0, (spriteNum * (bwidth / 3)) + (bwidth / 3), bheight);
            b.set(bX, bY, bX + (int)(SCALE*(bwidth / 3)), bY + (int)(SCALE*bheight));
            canvas.save();
            canvas.rotate(angle);
            canvas.drawBitmap(birds, subB, b, null);
            canvas.restore();
        }



        //TAP PROMPTER
        if (currentState == GameState.WAITFORTOUCH) {
            //begin screen
            canvas.drawBitmap(prompter, null, prompt, null);
        }


        if (currentState == GameState.PLAY | currentState == GameState.WAITFORTOUCH) {
            //display score
            onesRect.set((onesDigit * digitWidth), 0, (onesDigit * digitWidth) + digitWidth, digitHeight);
            tensRect.set((tensDigit * digitWidth), 0, (tensDigit * digitWidth) + digitWidth, digitHeight);
            hundsRect.set((hundsDigit * digitWidth), 0, (hundsDigit * digitWidth) + digitWidth, digitHeight);

            if (score < 10) {
                sOnesRect.set(scoreX - (digitWidth / 2), scoreY, scoreX  + (digitWidth / 2), scoreY + digitHeight);
            } else {
                if (score < 100) {
                    sOnesRect.set(scoreX, scoreY, scoreX + digitWidth, scoreY + digitHeight);
                    sTensRect.set(scoreX - digitWidth, scoreY, scoreX, scoreY + digitHeight);
                } else {
                    sOnesRect.set(scoreX + (int)(0.5 * digitWidth), scoreY, scoreX + (int)(1.5 * digitWidth), scoreY + digitHeight);
                    sTensRect.set(scoreX - (int)(0.5 * digitWidth), scoreY, scoreX + (int)(0.5 * digitWidth), scoreY + digitHeight);
                    sHundsRect.set(scoreX - (int)(1.5 * digitWidth), scoreY, scoreX - (int)(0.5 * digitWidth), scoreY + digitHeight);

                    canvas.drawBitmap(hundsMap, hundsRect, sHundsRect, null);
                }
                canvas.drawBitmap(tensMap, tensRect, sTensRect, null);
            }
            canvas.drawBitmap(onesMap, onesRect, sOnesRect, null);

        }




        //redraw the screen
        this.invalidate();
    }

    //thread stuff
    Thread updateThread = null;
    volatile boolean isRunning = true; //volatile is important when variable is used by mult threads

    //run method is required by all classes implementing runnable
    //when thread is instantiated, the thread runs this method
    public void run() {
        //the main loop
        while(this.isRunning) {
            this.Update();
        }
    }

    public void resume() { //creates new thread each time app resumes
        this.isRunning = true;
        updateThread = new Thread(this); //"this" must implement Runnable and the run() method
        updateThread.start();
    }

    public void pause() { //waits until thread is fully shut down before returning
        /*
        checks if isRunning is volatile, otherwise compiler may execute this line
        after while loop and cause an infinite loop
         */

        this.isRunning = false;
        while(true) {
            try {
                updateThread.join(); //merge with main thread
                break; //gets out of while loop
            } catch (InterruptedException e) {
                //loops repeatedly until thread is fully closed
            }
        }

    }
}

