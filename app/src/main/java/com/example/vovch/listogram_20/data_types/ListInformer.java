package com.example.vovch.listogram_20.data_types;

import android.widget.Button;

import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 07.01.2018.
 */

public class ListInformer {
    private String id;
    private String name;
    private UserGroup group;
    private Button button;
    public ListInformer(String groupId, String groupName){
        id = groupId;
        name = groupName;
        button = null;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setGroup(UserGroup newGroup){
        group = newGroup;
    }
    public UserGroup getGroup(){
        return group;
    }
    public void setButton(Button newButoon){
        button = newButoon;
    }
    public Button getButton(){
        return button;
    }
}
