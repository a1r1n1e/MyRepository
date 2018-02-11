package com.example.vovch.listogram_20.data_types;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 11.01.2018.
 */

public class ListImageButton extends AppCompatImageButton {
    private SList list;
    private UserGroup group;
    public ListImageButton(Context context){
        super(context);
    }
    public ListImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setList(SList newList){
        list = newList;
    }
    public SList getList(){
        return list;
    }
    public void setGroup(UserGroup newGroup){
        group = newGroup;
    }
    public UserGroup getGroup(){
        return group;
    }
}
