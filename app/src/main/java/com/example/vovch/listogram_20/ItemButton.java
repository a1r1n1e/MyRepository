package com.example.vovch.listogram_20;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by vovch on 11.01.2018.
 */

public class ItemButton extends AppCompatButton {
    private Item item;
    private SList list;
    private TempItem tempItem;
    public ItemButton(Context context){
        super(context);
    }
    public ItemButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ItemButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void setItem(Item newItem){
        item = newItem;
    }
    protected Item getItem(){
        return item;
    }
    protected void setList(SList newList){
        list = newList;
    }
    protected SList getList(){
        return list;
    }
    protected void setTempItem(TempItem newTempItem){
        tempItem = newTempItem;
    }
    protected TempItem getTempItem(){
        return tempItem;
    }
}
