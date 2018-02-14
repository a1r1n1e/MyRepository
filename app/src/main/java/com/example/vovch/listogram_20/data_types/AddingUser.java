package com.example.vovch.listogram_20.data_types;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vovch on 07.01.2018.
 */

public class AddingUser /*implements Parcelable*/ {
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

    /*protected AddingUser(Parcel in) {
        userName = in.readString();
        userId = in.readString();
        //button = (UserButton) in.readValue(UserButton.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
        //dest.writeValue(button);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddingUser> CREATOR = new Parcelable.Creator<AddingUser>() {
        @Override
        public AddingUser createFromParcel(Parcel in) {
            return new AddingUser(in);
        }

        @Override
        public AddingUser[] newArray(int size) {
            return new AddingUser[size];
        }
    };*/
}
