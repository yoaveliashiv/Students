package com.code.game;

public class MovesGame {
    private String infoTo="";
   private String type="";
    private  int dice1;
    private int dice2;
    private int posion1;
    private int posion1MoveTo;
    private int posion2;
    private int posion2MoveTo;

    public MovesGame() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDice1() {
        return dice1;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    public int getPosion1() {
        return posion1;
    }

    public void setPosion1(int posion1) {
        this.posion1 = posion1;
    }

    public int getPosion1MoveTo() {
        return posion1MoveTo;
    }

    public void setPosion1MoveTo(int posion1MoveTo) {
        this.posion1MoveTo = posion1MoveTo;
    }

    public int getPosion2() {
        return posion2;
    }

    public void setPosion2(int posion2) {
        this.posion2 = posion2;
    }

    public int getPosion2MoveTo() {
        return posion2MoveTo;
    }

    public void setPosion2MoveTo(int posion2MoveTo) {
        this.posion2MoveTo = posion2MoveTo;
    }

    public String getInfoTo() {
        return infoTo;
    }

    public void setInfoTo(String infoTo) {
        this.infoTo = infoTo;
    }
}
