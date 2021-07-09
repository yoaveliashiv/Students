package com.code.Hikers;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RegisterInformation2 {
    // private static  RegisterInformation2 registerInformation2=null;
    private String name = "";
    private String email = "";
    private String deviceToken = "";
    private String nameCollegeEnglish = "";
    private String nameCollegeHebrew = "";
    private int idImageSend = 0;
    private String imageUrl = "";
    private String dateRegister = "";
   // private Boolean termsOk= false;

    public RegisterInformation2() {
        ;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getIdImageSend() {
        return idImageSend;
    }

    public void setIdImageSend(int idImageSend) {
        this.idImageSend = idImageSend;
    }

    public String getNameCollegeEnglish() {
        return nameCollegeEnglish;
    }

    public void setNameCollegeEnglish(String nameCollegeEnglish) {
        this.nameCollegeEnglish = nameCollegeEnglish;
    }

    public String getNameCollegeHebrew() {
        return nameCollegeHebrew;
    }

    public void setNameCollegeHebrew(String nameCollegeHebrew) {
        this.nameCollegeHebrew = nameCollegeHebrew;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(String dateRegister) {
        this.dateRegister = dateRegister;
    }

//    public Boolean getTermsOk() {
//        return termsOk;
//    }
//
//    public void setTermsOk(Boolean termsOk) {
//        this.termsOk = termsOk;
//    }
}
