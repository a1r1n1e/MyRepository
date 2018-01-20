package com.example.vovch.listogram_20;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by vovch on 15.01.2018.
 */

public class CreateListEditText extends AppCompatEditText {
    private TempItem tempItem;
    public CreateListEditText(Context context){
        super(context);
        tempItem = null;
    }
    public CreateListEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        tempItem = null;
    }
    public CreateListEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tempItem = null;
    }
    protected void setTempItem(TempItem newTempItem){
        tempItem = newTempItem;
    }
    protected TempItem getTempItem(){
        return tempItem;
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }
}
