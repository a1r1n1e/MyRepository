package com.example.vovch.listogram_20.data_types;

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
    public boolean buttonEquals(ItemButton newButton){
        boolean result = false;
        if(button.equals(newButton)){
            result = true;
        }
        return result;
    }
    public void clear(){
        layout = null;
        button = null;
    }
    public int getId(){
        return id;
    }
    public void setId(int newId){
        id = newId;
    }
    public boolean getState(){
        return  state;
    }
    public void setState(boolean newState){
        state = newState;
    }
    public String getName(){
        return name;
    }
    public String getComment(){
        return comment;
    }
    public void setList(SList newList){
        list = newList;
    }
    public SList getList(){
        return list;
    }
    public void setLayout(LinearLayout newLayout){
        layout = newLayout;
    }
    public LinearLayout getLayout(){
        return layout;
    }
    public void setButton(ItemButton newButton){
        button = newButton;
    }
    public ItemButton getButton(){
        return button;
    }

}
