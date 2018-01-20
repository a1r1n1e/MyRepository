package com.example.vovch.listogram_20;

import android.support.v7.widget.CardView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vovch on 03.01.2018.
 */

class UserGroup {
    private String name;
    private String id;
    private ArrayList<SList> activeLists;
    private ArrayList<SList> historyLists;
    private ArrayList<AddingUser> members;
    private CardView cardView;
    private Button button;
    protected UserGroup(String newName, String newId, SList[] newActiveLists, SList[] newHistoryLists, AddingUser[] newMembers){
        name = newName;
        id = newId;
        activeLists = new ArrayList<>(Arrays.asList(newActiveLists));
        historyLists = new ArrayList<>(Arrays.asList(newHistoryLists));
        members = new ArrayList<>(Arrays.asList(newMembers));
        button = null;
    }
    protected UserGroup(String newName, String newId, AddingUser[] newMembers){
        name = newName;
        id = newId;
        members = null;
        if(newMembers != null) {
            setMembers(newMembers);
        }
        activeLists = null;
        historyLists = null;
        button = null;
    }
    protected UserGroup(String newName, String newId){
        name = newName;
        id = newId;
        members = null;
        activeLists = null;
        historyLists = null;
        button = null;
    }

    protected String getId(){
        return id;
    }
    protected String getName(){
        return name;
    }

    protected void setCardView(CardView newCardView){
        cardView = newCardView;
    }
    protected void setButton(Button newButton){
        button = newButton;
    }
    protected Button getButton(){
        return button;
    }
    protected CardView getCardView(){
        return cardView;
    }
    protected void clear(){
        cardView = null;
    }

    protected void disactivateList(SList list){
        if(activeLists != null) {
            activeLists.remove(list);
            activeLists.trimToSize();
            list.setState(false);
        }
    }
    protected void itemmark(Item item){
        if(item != null) {
            item.setState(!item.getState());
        }
    }

    protected void setActiveLists(SList[] lists){
        if(lists != null) {
            activeLists = new ArrayList<>(Arrays.asList(lists));
        }
        else{
            activeLists = new ArrayList<>();
        }
    }
    protected SList[] getActiveLists(){
        SList[] result = null;
        if(activeLists != null){
            result = new SList[activeLists.size()];
            result = activeLists.toArray(result);
        }
        else{
            activeLists = new ArrayList<>();
            result = new SList[activeLists.size()];
            result = activeLists.toArray(result);
        }
        return result;
    }
    protected void setHistoryLists(SList[] lists){
        if(lists != null) {
            historyLists = new ArrayList<>(Arrays.asList(lists));
        }
        else{
            historyLists = new ArrayList<>();
        }
    }
    protected SList[] getHistoryLists(){
        SList[] result = null;
        if(historyLists != null){
            result = new SList[historyLists.size()];
            result = historyLists.toArray(result);
        }
        else{
            historyLists = new ArrayList<>();
            result = new SList[historyLists.size()];
            result = historyLists.toArray(result);
        }
        return result;
    }

    protected void addMember(AddingUser memberId){
        if(members == null){
            members = new ArrayList<>();
        }
        if(!members.contains(memberId)) {
            members.add(memberId);
        }
    }
    protected void deleteMember(String memberId){
        if(members != null){
            if(members.contains(memberId)) {
                members.remove(memberId);
            }
        }
    }
    protected void setMembers(AddingUser[] newMembers){
        if(newMembers != null) {
            members = new ArrayList<>(Arrays.asList(newMembers));
        }
    }
    protected AddingUser[] getMembers(){
        AddingUser[] result = null;
        if(members != null){
            result = new AddingUser[members.size()];
            result = members.toArray(result);
        }
        return result;
    }
}
