package com.example.vovch.listogram_20;

import android.widget.ImageButton;

/**
 * Created by vovch on 24.12.2017.
 */

public class DisButton {
    protected ImageButton button;
    protected SList list;
    public DisButton(ImageButton newButton, SList newList){
        list = newList;
        button = newButton;
    }
}
