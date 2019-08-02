package com.example.bemybuddy;

import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {
    public User(String fname, String lname, String email, String descp, String picUrl) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.descp = descp;
        this.picUrl  = picUrl;
    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    private String fname;
   private String lname;
   private String email;
   private String descp;

    private String picUrl;
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public User(){}





}
