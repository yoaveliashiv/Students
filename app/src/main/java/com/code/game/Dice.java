package com.code.game;

public class Dice {

    public Dice() {
    }

    public int throwDice() {
       int num = (int)(Math.random()*6+1);
        return num;
    }
}
