package com.example.vovch.listogram_20;

import android.widget.Button;

/**
 * Created by vovch on 07.01.2018.
 */

class ListInformer {
    private String id;
    private String name;
    private UserGroup group;
    private Button button;
    protected ListInformer(String groupId, String groupName){
        id = groupId;
        name = groupName;
        button = null;
    }
    protected String getId(){
        return id;
    }
    protected String getName(){
        return name;
    }
    protected void setGroup(UserGroup newGroup){
        group = newGroup;
    }
    protected UserGroup getGroup(){
        return group;
    }
    protected void setButton(Button newButoon){
        button = newButoon;
    }
    protected Button getButton(){
        return button;
    }
}
