package com.example.vovch.listogram_20;

import android.support.v7.widget.CardView;
import android.widget.EditText;

import com.example.vovch.listogram_20.Item;

/**
 * Created by vovch on 10.01.2018.
 */

public class TempItem extends com.example.vovch.listogram_20.Item {
    private CreateListEditText itemNameEditText;
    private CreateListEditText itemCommentEditText;
    private CardView cardView;
    public TempItem(){
        super(null, null);
        itemCommentEditText = null;
        itemNameEditText = null;
        cardView = null;
    }
    protected void setItemNameEditText(CreateListEditText editText){
        itemNameEditText = editText;
    }
    protected void setItemCommentEditText(CreateListEditText editText){
        itemCommentEditText = editText;
    }
    protected CreateListEditText getNameEditText(){
        return itemNameEditText;
    }
    protected CreateListEditText getItemCommentEditText(){
        return itemCommentEditText;
    }
    protected void setCardView(CardView newCardView){
        cardView = newCardView;
    }
    protected CardView getCardView(){
        return cardView;
    }
}
