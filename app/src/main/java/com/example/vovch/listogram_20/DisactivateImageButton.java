package com.example.vovch.listogram_20;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

/**
 * Created by vovch on 11.01.2018.
 */

public class DisactivateImageButton extends AppCompatImageButton {
    private SList list;
    private UserGroup group;
    public DisactivateImageButton(Context context){
        super(context);
    }
    public DisactivateImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DisactivateImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void setList(SList newList){
        list = newList;
    }
    protected SList getList(){
        return list;
    }
    protected void setGroup(UserGroup newGroup){
        group = newGroup;
    }
    protected UserGroup getGroup(){
        return group;
    }
}
