package com.example.vovch.listogram_20;

import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by vovch on 23.12.2017.
 */

public class Item {
    private int id;
    private final String name;
    private final String comment;
    private SList list;
    private boolean state;
    private LinearLayout layout;
    private ItemButton button;
    public Item(int newId, String newName, String newComment, boolean newState){
        id = newId;
        name = newName;
        comment = newComment;
        state = newState;
    }
    public Item(String newName, String newComment, boolean newState){
        id = 0;
        name = newName;
        comment = newComment;
        state = newState;
    }
    public Item(String newName, String newComment) {
        name = newName;
        comment = newComment;
    }
    protected boolean buttonEquals(ItemButton newButton){
        boolean result = false;
        if(button.equals(newButton)){
            result = true;
        }
        return result;
    }
    protected void clear(){
        layout = null;
        button = null;
    }
    protected int getId(){
        return id;
    }
    protected void setId(int newId){
        id = newId;
    }
    protected boolean getState(){
        return  state;
    }
    protected void setState(boolean newState){
        state = newState;
    }
    protected String getName(){
        return name;
    }
    protected String getComment(){
        return comment;
    }
    protected void setList(SList newList){
        list = newList;
    }
    protected SList getList(){
        return list;
    }
    protected void setLayout(LinearLayout newLayout){
        layout = newLayout;
    }
    protected LinearLayout getLayout(){
        return layout;
    }
    protected void setButton(ItemButton newButton){
        button = newButton;
    }
    protected ItemButton getButton(){
        return button;
    }

}
