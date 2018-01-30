package com.example.vovch.listogram_20.data_types;

import android.support.v7.widget.CardView;
import android.widget.ImageButton;

import java.util.Calendar;

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
    private int owner;
    private CardView cardView;
    private ImageButton disButton;

    public SList(Item[] newItems){
        items = newItems;
        id = 0;
        group = 0;
        type = false;
        state = true;
        creationTime = Calendar.getInstance().getTime().toString();
    }
    public SList(Item[] newItems, int newId, int newGroup, boolean newType, boolean newState, int newOwner, String newCreationTime){
        setItems(newItems);
        setId(newId);
        setGroup(newGroup);
        setType(newType);
        setState(newState);
        setOwner(newOwner);
        if(newType) {
            creationTime = Calendar.getInstance().getTime().toString();
        } else{
            creationTime = newCreationTime;
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
    public int getOwner(){
        return owner;
    }
    public void setOwner(int newOwner){
        owner = newOwner;
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
