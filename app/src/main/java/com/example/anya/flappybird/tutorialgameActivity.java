package com.example.anya.flappybird;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by anya on 7/7/2017.
 */

public class tutorialgameActivity extends Activity {

    Game game;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new Game(this);
        setContentView(game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        game.resume();
    }



}
