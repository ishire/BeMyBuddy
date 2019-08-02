package com.example.bemybuddy;

public class messageobj {
    public   messageobj( String message, Boolean currentBool){
        this.currentBool = currentBool;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentBool() {
        return currentBool;
    }

    public void setCurrentBool(Boolean currentBool) {
        this.currentBool = currentBool;
    }

    private  String message;
    private   Boolean currentBool;


}
