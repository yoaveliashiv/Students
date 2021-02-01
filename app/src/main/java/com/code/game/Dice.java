package com.code.game;

public class Dice {

    public Dice() {
    }

    public static int throwDice() {
       int num = (int)(Math.random()*6+1);
        return num;
    }
}
