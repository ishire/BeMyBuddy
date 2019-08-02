package com.example.bemybuddy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Serializable {

    public Event(String name, String event_descrp, String date, String userId,String imgUri) {
        this.user = name;
        this.userId = userId;

        this.event_description = event_descrp;
        this.date = date;
        this.imgUri = imgUri;


    }
    public  Event(){}
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

   // public User getUser2() {
        //return user2;
    //}

    //public void setUser2(User user2) {
       // this.user2 = user2;
    //}

    public String getEvent_descrp() {
        return event_description;
    }

    public void setEvent_descrp(String event_descrp) {
        this.event_description = event_descrp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String user;

    private String event_description;
    private String date;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    private String imgUri;








}
