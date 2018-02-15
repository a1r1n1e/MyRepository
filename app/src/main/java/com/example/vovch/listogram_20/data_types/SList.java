package com.example.vovch.listogram_20.data_types;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vovch on 23.12.2017.
 */

public class SList /*implements Parcelable*/ {
    private int id;
    private boolean state;
    private UserGroup group;
    private boolean type;
    private Item[] items;
    private final String creationTime;
    private String humanCreationTime;
    private int owner;
    private String ownerName;
    private CardView cardView;
    private ImageButton disButton;
    private ImageButton resendButton;
    private ImageButton redactButton;

    public SList(Item[] newItems){
        items = newItems;
        id = 0;
        group = null;
        owner = -1;
        type = false;
        state = true;
        creationTime = Calendar.getInstance().getTime().toString();
        setHumanCreationTime();
    }
    public SList(Item[] newItems, int newId, UserGroup newGroup, boolean newType, boolean newState, int newOwner, String newOwnerName, String newCreationTime){
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
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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

    public ImageButton getRedactButton(){
        return redactButton;
    }
    public void setRedactButton(ImageButton newRedactButton){
        redactButton = newRedactButton;
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
    public UserGroup getGroup(){
        return group;
    }
    public void setGroup(UserGroup newGroup){
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

    public void disactivate(){
        setState(false);
    }
    public CardView getCardView(){
        return cardView;
    }
    public void setCardView(CardView newCardView){
        cardView = newCardView;
    }

    /*protected SList(Parcel in) {
        id = in.readInt();

        Parcelable[] parcelableArray =
                in.readParcelableArray(Item.class.getClassLoader());
        items = null;
        if (parcelableArray != null) {
            items = Arrays.copyOf(parcelableArray, parcelableArray.length, Item[].class);
        }

        state = in.readByte() != 0x00;
        group = (UserGroup) in.readValue(UserGroup.class.getClassLoader());
        type = in.readByte() != 0x00;
        creationTime = in.readString();
        humanCreationTime = in.readString();
        owner = in.readInt();
        ownerName = in.readString();
        //cardView = (CardView) in.readValue(CardView.class.getClassLoader());
        //disButton = (ImageButton) in.readValue(ImageButton.class.getClassLoader());
        //resendButton = (ImageButton) in.readValue(ImageButton.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (state ? 0x01 : 0x00));
        dest.writeValue(group);
        dest.writeTypedArray(items, flags);
        dest.writeByte((byte) (type ? 0x01 : 0x00));
        dest.writeString(creationTime);
        dest.writeString(humanCreationTime);
        dest.writeInt(owner);
        dest.writeString(ownerName);
        //dest.writeValue(cardView);
        //dest.writeValue(disButton);
        //dest.writeValue(resendButton);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SList> CREATOR = new Parcelable.Creator<SList>() {
        @Override
        public SList createFromParcel(Parcel in) {
            return new SList(in);
        }

        @Override
        public SList[] newArray(int size) {
            return new SList[size];
        }
    };*/
}