package com.example.vovch.listogram_20.data_types;

import android.support.v7.widget.CardView;
import android.widget.Button;

import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.SList;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vovch on 03.01.2018.
 */

public class UserGroup {
    private String name;
    private String id;
    private ArrayList<SList> activeLists;
    private ArrayList<SList> historyLists;
    private ArrayList<AddingUser> members;
    private CardView cardView;
    private Button button;
    private String owner;

    public UserGroup(String newName, String newId, SList[] newActiveLists, SList[] newHistoryLists, AddingUser[] newMembers) {
        name = newName;
        id = newId;
        owner = null;
        activeLists = new ArrayList<>(Arrays.asList(newActiveLists));
        historyLists = new ArrayList<>(Arrays.asList(newHistoryLists));
        members = new ArrayList<>(Arrays.asList(newMembers));
        button = null;
    }

    public UserGroup(String newName, String newId, AddingUser[] newMembers) {
        name = newName;
        id = newId;
        owner = null;
        members = null;
        if (newMembers != null) {
            setMembers(newMembers);
        }
        activeLists = null;
        historyLists = null;
        button = null;
    }

    public UserGroup(String newName, String newId) {
        name = newName;
        id = newId;
        owner = null;
        members = null;
        activeLists = null;
        historyLists = null;
        button = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setCardView(CardView newCardView) {
        cardView = newCardView;
    }

    public void setButton(Button newButton) {
        button = newButton;
    }

    public Button getButton() {
        return button;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void clear() {
        cardView = null;
    }

    public void setOwner(String newOwner) {
        owner = newOwner;
    }

    public String getOwner() {
        return owner;
    }

    public void disactivateList(SList list) {
        if (activeLists != null) {
            activeLists.remove(list);
            activeLists.trimToSize();
            list.setState(false);
        }
    }

    public void itemmark(Item item) {
        if (item != null) {
            item.setState(!item.getState());
        }
    }

    public void setActiveLists(SList[] lists) {
        if (lists != null) {
            activeLists = new ArrayList<>(Arrays.asList(lists));
        } else {
            activeLists = new ArrayList<>();
        }
    }

    public SList[] getActiveLists() {
        SList[] result = null;
        if (activeLists != null) {
            result = new SList[activeLists.size()];
            result = activeLists.toArray(result);
        } else {
            activeLists = new ArrayList<>();
            result = new SList[activeLists.size()];
            result = activeLists.toArray(result);
        }
        return result;
    }

    public void setHistoryLists(SList[] lists) {
        if (lists != null) {
            historyLists = new ArrayList<>(Arrays.asList(lists));
        } else {
            historyLists = new ArrayList<>();
        }
    }

    public SList[] getHistoryLists() {
        SList[] result = null;
        if (historyLists != null) {
            result = new SList[historyLists.size()];
            result = historyLists.toArray(result);
        } else {
            historyLists = new ArrayList<>();
            result = new SList[historyLists.size()];
            result = historyLists.toArray(result);
        }
        return result;
    }

    public String getLastListCreationTime() {
        String creationTime = null;
        if(historyLists != null && historyLists.size() > 0){
            creationTime = historyLists.get(0).getCreationTime();
        }
        return creationTime;
    }

    public void addMember(AddingUser memberId) {
        if (members == null) {
            members = new ArrayList<>();
        }
        if (!members.contains(memberId)) {
            members.add(memberId);
        }
    }

    public void deleteMember(String memberId) {
        if (members != null) {
            if (members.contains(memberId)) {
                members.remove(memberId);
            }
        }
    }

    public void setMembers(AddingUser[] newMembers) {
        if (newMembers != null) {
            members = new ArrayList<>(Arrays.asList(newMembers));
        }
    }

    public AddingUser[] getMembers() {
        AddingUser[] result = null;
        if (members != null) {
            result = new AddingUser[members.size()];
            result = members.toArray(result);
        }
        return result;
    }
}
