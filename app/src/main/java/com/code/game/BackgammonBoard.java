package com.code.game;

import android.widget.ImageView;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
@IgnoreExtraProperties

public class BackgammonBoard {
private List<Integer> boardNum=new ArrayList<>();
private List<String> boardColor=new ArrayList<>();

public void build(){
    for (int i = 0; i <24 ; i++) {
        boardNum.add(0);
        boardColor.add("no");
    }
}
    public BackgammonBoard() {

    }

    public List<Integer> getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(List<Integer> boardNum) {
        this.boardNum = boardNum;
    }

    public List<String> getBoardColor() {
        return boardColor;
    }

    public void setBoardColor(List<String> boardColor) {
        this.boardColor = boardColor;
    }
}
