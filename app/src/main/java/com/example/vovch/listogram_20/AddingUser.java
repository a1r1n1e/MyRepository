package com.example.vovch.listogram_20;

import android.widget.Button;

/**
 * Created by vovch on 07.01.2018.
 */

class AddingUser {
    private String userName;
    private String userId;
    private UserButton button;
    protected AddingUser(){
        userId = null;
        userName = null;
        button = null;
    }
    protected void setData(String newName, String newId){
        userId = newId;
        userName = newName;
    }
    protected String getUserId(){
        return userId;
    }
    protected String getUserName(){
        return userName;
    }
    protected UserButton getButton(){
        return  button;
    }
    protected void setButton(UserButton newButton){
        button = newButton;
    }
}
