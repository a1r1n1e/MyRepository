package com.example.vovch.listogram_20.data_types;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.TempItem;

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
    public void setItem(Item newItem){
        item = newItem;
    }
    public Item getItem(){
        return item;
    }
    public void setList(SList newList){
        list = newList;
    }
    public SList getList(){
        return list;
    }
    public void setTempItem(TempItem newTempItem){
        tempItem = newTempItem;
    }
    public TempItem getTempItem(){
        return tempItem;
    }
}
