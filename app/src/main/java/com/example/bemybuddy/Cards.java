package com.example.bemybuddy;

import android.net.Uri;

public class Cards {
    private String userId;
    private String name;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String descp;
    private String imgUrl;
    private String date;


    public Cards(String userId, String name, String descp, String date, String imgUrl) {
        this.userId = userId;
        this.name = name;
        this.descp = descp;
        this.date = date;
        this.imgUrl = imgUrl;
    }
}
