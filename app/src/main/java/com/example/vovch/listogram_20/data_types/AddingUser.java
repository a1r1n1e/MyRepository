package com.example.vovch.listogram_20.data_types;

/**
 * Created by vovch on 07.01.2018.
 */

public class AddingUser {
    private String userName;
    private String userId;
    private UserButton button;
    public AddingUser(){
        userId = null;
        userName = null;
        button = null;
    }
    public void setData(String newName, String newId){
        userId = newId;
        userName = newName;
    }
    public String getUserId(){
        return userId;
    }
    public String getUserName(){
        return userName;
    }
    public UserButton getButton(){
        return  button;
    }
    public void setButton(UserButton newButton){
        button = newButton;
    }
}
