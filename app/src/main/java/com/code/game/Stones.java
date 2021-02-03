package com.code.game;

import java.util.ArrayList;
import java.util.List;

public class Stones {
    private List<Integer> listStonesBlack=new ArrayList<>();
    private List<Integer> listStonesWhite=new ArrayList<>();
    private List<Integer> listStonesColor=new ArrayList<>();
    private List<Integer> listStonesColorRivel=new ArrayList<>();

    public Stones() {
    }

    public List<Integer> getListStonesBlack() {
        return listStonesBlack;
    }

    public void setListStonesBlack(List<Integer> listStonesBlack) {
        this.listStonesBlack = listStonesBlack;
    }

    public List<Integer> getListStonesWhite() {
        return listStonesWhite;
    }

    public List<Integer> getListStonesColor() {
        return listStonesColor;
    }

    public void setListStonesColor(List<Integer> listStonesColor) {
        this.listStonesColor = listStonesColor;
    }

    public void setListStonesWhite(List<Integer> listStonesWhite) {
        this.listStonesWhite = listStonesWhite;
    }

    public List<Integer> getListStonesColorRivel() {
        return listStonesColorRivel;
    }

    public void setListStonesColorRivel(List<Integer> listStonesColorRivel) {
        this.listStonesColorRivel = listStonesColorRivel;
    }

    public void setListStonesByColor(String color) {
        if(color.equals("white")) {
            listStonesColor = listStonesWhite;
            listStonesColorRivel=listStonesBlack;
        }
        else {
            listStonesColor = listStonesBlack;
            listStonesColorRivel=listStonesWhite;

        }
    }

}
