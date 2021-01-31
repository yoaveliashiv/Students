package com.code.game;

import android.widget.ImageView;

import java.util.ArrayList;


public class BackgammonPlayer {
    //ArrayList<Stack<ImageView>> arrayList;
    private int gameStones = 15;
    private int dice1;
    private int dice2;

    public BackgammonPlayer() {
    }
    public void throw2Dice() {
        Dice dice = new Dice();
        dice1 = dice.throwDice();
         dice2 = dice.throwDice();
    }
    public int getGameStones() {
        return gameStones;
    }

    public void setGameStones(int gameStones) {
        this.gameStones = gameStones;
    }
}
