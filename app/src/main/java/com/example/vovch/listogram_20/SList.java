package com.example.vovch.listogram_20;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

    private SList(Item[] newItems){
        items = newItems;
        id = 0;
        group = 0;
        type = false;
        state = true;
        creationTime = Calendar.getInstance().getTime().toString();
    }
    protected SList(Item[] newItems, int newId, int newGroup, boolean newType, boolean newState, int newOwner){
        setItems(newItems);
        setId(newId);
        setGroup(newGroup);
        setType(newType);
        setState(newState);
        setOwner(newOwner);
        creationTime = Calendar.getInstance().getTime().toString();
    }
    protected void clear(){
        cardView = null;
    }

    protected void setDisButton(ImageButton newDisButton){
        disButton = newDisButton;
    }
    protected ImageButton getDisButton(){
        return disButton;
    }
    protected int getOwner(){
        return owner;
    }
    protected void setOwner(int newOwner){
        owner = newOwner;
    }
    protected int getId(){
        return id;
    }
    protected void setId(int newId){
        id = newId;
    }
    protected int getGroup(){
        return group;
    }
    protected void setGroup(int newGroup){
        group = newGroup;
    }
    protected boolean getType(){
        return type;
    }
    protected void setType(boolean newType){
        type = newType;
    }
    protected boolean getState(){
        return  state;
    }
    protected void setState(boolean newState){
        state = newState;
    }
    protected Item[] getItems(){
        return items;
    }
    protected void setItems(Item[] newItems){
        items = newItems;
    }
    protected String getCreationTime(){
        return creationTime;
    }

    protected SList createAlmostCopy(){
        SList copy = new SList(items);
        return copy;
    }
    protected void disactivate(){
        setState(false);
    }
    protected CardView getCardView(){
        return cardView;
    }
    protected void setCardView(CardView newCardView){
        cardView = newCardView;
    }
}
