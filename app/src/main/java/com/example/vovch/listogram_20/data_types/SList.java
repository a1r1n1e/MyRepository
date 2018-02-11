package com.example.vovch.listogram_20.data_types;

import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vovch on 23.12.2017.
 */

public class SList {
    private int id;
    private boolean state;
    private int group;
    private boolean type;
    private Item[] items;
    private final String creationTime;
    private String humanCreationTime;
    private int owner;
    private String ownerName;
    private CardView cardView;
    private ImageButton disButton;
    private ImageButton resendButton;

    public SList(Item[] newItems){
        items = newItems;
        id = 0;
        group = 0;
        owner = -1;
        type = false;
        state = true;
        creationTime = Calendar.getInstance().getTime().toString();
        setHumanCreationTime();
    }
    public SList(Item[] newItems, int newId, int newGroup, boolean newType, boolean newState, int newOwner, String newOwnerName, String newCreationTime){
        setItems(newItems);
        setId(newId);
        setGroup(newGroup);
        setType(newType);
        setState(newState);
        setOwner(newOwner);
        setOwnerName(newOwnerName);
        if(newType) {
            creationTime = Calendar.getInstance().getTime().toString();
        } else{
            creationTime = newCreationTime;
        }
        setHumanCreationTime();
    }
    private void setHumanCreationTime(){
        if (creationTime != null) {
            if(!type) {
                try {
                    SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateFormatParser.parse(creationTime);
                    SimpleDateFormat dateFormatFormater = new SimpleDateFormat("dd-MM-yy HH:mm");
                    humanCreationTime = dateFormatFormater.format(date);
                } catch (ParseException e) {                                                                 //TODO

                }
            }
            else{

            }
        }
    }
    public void clear(){
        cardView = null;
    }

    public void setDisButton(ImageButton newDisButton){
        disButton = newDisButton;
    }
    public ImageButton getDisButton(){
        return disButton;
    }
    public void setResendButton(ImageButton newResendButton){
        resendButton = newResendButton;
    }
    public ImageButton getResendButton(){
        return resendButton;
    }
    public int getOwner(){
        return owner;
    }
    public void setOwner(int newOwner){
        owner = newOwner;
    }
    public String getOwnerName(){
        return ownerName;
    }
    public void  setOwnerName(String newOwnerName){
        ownerName = newOwnerName;
    }
    public int getId(){
        return id;
    }
    public void setId(int newId){
        id = newId;
    }
    public int getGroup(){
        return group;
    }
    public void setGroup(int newGroup){
        group = newGroup;
    }
    public boolean getType(){
        return type;
    }
    public void setType(boolean newType){
        type = newType;
    }
    public boolean getState(){
        return  state;
    }
    public void setState(boolean newState){
        state = newState;
    }
    public Item[] getItems(){
        return items;
    }
    public void setItems(Item[] newItems){
        items = newItems;
    }
    public String getCreationTime(){
        return creationTime;
    }
    public String getHumanCreationTime(){
        return humanCreationTime;
    }

    public SList createAlmostCopy(){
        SList copy = new SList(items);
        return copy;
    }
    public void disactivate(){
        setState(false);
    }
    public CardView getCardView(){
        return cardView;
    }
    public void setCardView(CardView newCardView){
        cardView = newCardView;
    }
}
