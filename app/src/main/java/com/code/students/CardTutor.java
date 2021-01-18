package com.code.students;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
@IgnoreExtraProperties

public class CardTutor {
    private String key = "";
    private int seeCard = 0;
    private String email = "";
    private int numImage = 0;
    private int numExpertise = 0;
    private int id = 0;
    private int permissionToPublish = 0;
    private String name = "";
    private String phone = "";
    private String priceToLesson = "";
    private String area = "";
    private String city = "";
    private ArrayList<String> arrayListExpertise = new ArrayList();
    private String remarks = "";
    private String rejection = "";
    private List<String> imageViewArrayListName = new ArrayList<>();


    public CardTutor() {
    }

    public CardTutor(String name) {
        this.name = name;
    }

    public CardTutor(CardTutor c) {
        this.id = c.id;
        this.permissionToPublish = c.permissionToPublish;
        this.name = c.name;

        this.phone = c.phone;
        this.priceToLesson = c.priceToLesson;
        this.area = c.area;
        this.city = c.city;
        this.remarks = c.remarks;
        this.rejection = c.rejection;
        this.imageViewArrayListName = c.imageViewArrayListName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void removeImageViewArrayListName(String url) {
        imageViewArrayListName.remove(url);
    }

    public void addImageViewArrayListName(String url) {
        imageViewArrayListName.add(url);
    }

    public List<String> getImageViewArrayListName() {
        return imageViewArrayListName;
    }

    public void setImageViewArrayListName(List<String> imageViewArrayListName) {
        this.imageViewArrayListName = imageViewArrayListName;
    }

    public int getPermissionToPublish() {
        return permissionToPublish;
    }

    public void setPermissionToPublish(int permissionToPublish) {
        this.permissionToPublish = permissionToPublish;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPriceToLesson() {
        return priceToLesson;
    }

    public void setPriceToLesson(String priceToLesson) {
        this.priceToLesson = priceToLesson;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumImage() {
        return numImage;
    }

    public void setNumImage(int numImage) {
        this.numImage = numImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRejection() {
        return rejection;
    }

    public void setRejection(String rejection) {
        this.rejection = rejection;
    }

    public int getSeeCard() {
        return seeCard;
    }

    public void addOneSeeCard() {
        this.seeCard++;
    }

    public void setSeeCard(int seeCard) {
        this.seeCard = seeCard;
    }

    public ArrayList getArrayListExpertise() {
        return arrayListExpertise;
    }

    public void addExpertise(String expertise) {
        arrayListExpertise.add(expertise);
    }



    public int getNumExpertise() {
        return numExpertise;
    }

    public void setNumExpertise(int numExpertise) {
        this.numExpertise = numExpertise;
    }

    public void setArrayListExpertise(ArrayList<String> arrayListExpertise) {
        this.arrayListExpertise = arrayListExpertise;
    }
}
