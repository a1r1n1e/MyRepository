package com.example.vovch.listogram_20.data_types;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

/**
 * Created by vovch on 23.12.2017.
 */

public class Item /*implements Parcelable*/ {
    private int id;
    private String name;
    private String comment;
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
        id = 0;
        state = true;
        name = newName;
        comment = newComment;
    }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
    public void setName(String newName){
        name = newName;
    }
    public String getComment(){
        return comment;
    }
    public void setComment(String newComment){
        comment = newComment;
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


    /*protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        comment = in.readString();
        list = (SList) in.readValue(SList.class.getClassLoader());
        state = in.readByte() != 0x00;
        //layout = (LinearLayout) in.readValue(LinearLayout.class.getClassLoader());
        //button = (ItemButton) in.readValue(ItemButton.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        clear();
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(comment);
        dest.writeValue(list);
        dest.writeByte((byte) (state ? 0x01 : 0x00));
        //dest.writeValue(layout);
        //dest.writeValue(button);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };*/
}
