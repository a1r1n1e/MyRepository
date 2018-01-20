package com.example.vovch.listogram_20;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by vovch on 17.01.2018.
 */

public class GroupButton extends AppCompatButton {
    private UserGroup group;

    public GroupButton(Context context) {
        super(context);
        group = null;
    }
    public GroupButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        group = null;
    }
    public GroupButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        group = null;
    }

    protected void setGroup(UserGroup newGroup){
        group = newGroup;
    }
    protected UserGroup getGroup(){
        return group;
    }
}
