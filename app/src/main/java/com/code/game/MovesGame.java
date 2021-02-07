package com.code.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovesGame {
    private String infoTo = "";
    private String type = "";
    private int dice1;
    private int dice2;
    private int dice3;
    private int dice4;
    private int posion1;
    private int posion1MoveTo;
    private int countMove;
    private boolean lose = false;
    private HashMap<String, List<Integer>> hashMapBord = new HashMap<>();
    private HashMap<String, String> hashMapUid = new HashMap<>();

    private HashMap<String, String> hashMapColor = new HashMap<>();
    private String sendUidToRive = "";
    private String uidPrimery="";
    private String tournUid="";
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


    public String getInfoTo() {
        return infoTo;
    }

    public void setInfoTo(String infoTo) {
        this.infoTo = infoTo;
    }

    public int getCountMove() {
        return countMove;
    }

    public void setCountMove(int countMove) {
        this.countMove = countMove;
    }

    public boolean getLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }

    public HashMap<String, List<Integer>> getHashMapBord() {
        return hashMapBord;
    }

    public void setHashMapBord(HashMap<String, List<Integer>> hashMapBord) {
        this.hashMapBord = hashMapBord;
    }

    public HashMap<String, String> getHashMapColor() {
        return hashMapColor;
    }

    public void setHashMapColor(HashMap<String, String> hashMapColor) {
        this.hashMapColor = hashMapColor;
    }

    public String getSendUidToRive() {
        return sendUidToRive;
    }

    public void setSendUidToRive(String sendUidToRive) {
        this.sendUidToRive = sendUidToRive;
    }

    public String getTournUid() {
        return tournUid;
    }

    public void setTournUid(String tournUid) {
        this.tournUid = tournUid;
    }

    public String getUidPrimery() {
        return uidPrimery;
    }

    public void setUidPrimery(String uidPrimery) {
        this.uidPrimery = uidPrimery;
    }

    public HashMap<String, String> getHashMapUid() {
        return hashMapUid;
    }

    public void setHashMapUid(HashMap<String, String> hashMapUid) {
        this.hashMapUid = hashMapUid;
    }

    public int getDice3() {
        return dice3;
    }

    public void setDice3(int dice3) {
        this.dice3 = dice3;
    }

    public int getDice4() {
        return dice4;
    }

    public void setDice4(int dice4) {
        this.dice4 = dice4;
    }
}
