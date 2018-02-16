package com.example.vovch.listogram_20.activities.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.CreateListEditText;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ItemButton;
import com.example.vovch.listogram_20.data_types.TempItem;
import com.example.vovch.listogram_20.data_types.UserGroup;

import java.util.ArrayList;

public class CreateListogramActivity extends WithLoginActivity {
    protected ArrayList<TempItem> TempItems = new ArrayList<>();
    private String groupId;
    private ActiveActivityProvider provider;

    private int loadType;

    TextView.OnEditorActionListener editorListenerOne = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId ==EditorInfo.IME_ACTION_UNSPECIFIED) {
                CreateListEditText currentEditText = (CreateListEditText) v;
                CreateListEditText nextEditText = currentEditText.getTempItem().getItemCommentEditText();
                Selection.setSelection(nextEditText.getText(), nextEditText.getSelectionStart());
                nextEditText.requestFocus();
                return true;
            }
            else {
                return true;
            }
        }
    };
    TextView.OnEditorActionListener editorListenerTwo = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                CreateListEditText createListEditText = (CreateListEditText) v;
                TempItem item = createListEditText.getTempItem();
                int number = TempItems.indexOf(item);
                if(number >= TempItems.size() - 1) {
                    createPunct(null);
                }
                CreateListEditText nextEditText =  TempItems.get(number + 1).getNameEditText();
                Selection.setSelection(nextEditText.getText(), nextEditText.getSelectionStart());
                nextEditText.requestFocus();
                return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(6, CreateListogramActivity.this);

        setContentView(R.layout.activity_create_listogram);

        loadType = getIntent().getExtras().getInt("loadtype");                                      //gettitg information what type of action is it

        if(loadType == 1 || loadType == 3){
            UserGroup currentGroup = provider.getActiveGroup();
            if(currentGroup != null){
                groupId = currentGroup.getId();
            }
        }

        View.OnClickListener sendListagramListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendListogram();
            }
        };
        ImageButton sendListogramButton = (ImageButton) findViewById(R.id.listogramsenddownbutton);
        sendListogramButton.setOnClickListener(sendListagramListener);
        scrollToEnd();
    }
    @Override
    protected void onStart(){
        super.onStart();
        getAndShowTempItemsState();
    }
    @Override
    protected void onResume(){
        super.onResume();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(6, CreateListogramActivity.this);
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 6) {
            provider.nullActiveActivity();
        }
        saveTempItemsState();
        clearer();
        super.onStop();
    }
    @Override
    public void onBackPressed(){
        clearer();
        if(provider.getActiveActivityNumber() == 6) {
            provider.nullActiveActivity();
        }
        Intent intent = null;
        if(loadType == 1 || loadType == 3) {                                                            //Possibly refactor to odds and evens
            intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        } else if (loadType == 0 || loadType == 2) {
            intent = new Intent(CreateListogramActivity.this, ActiveListsActivity.class);
        }
        startActivity(intent);
        CreateListogramActivity.this.finish();
    }
    private void clearer(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.listogrampunctslayout);
        layout.removeAllViewsInLayout();
        TempItems.clear();
    }
    private void scrollToEnd(){
        getScrollView().post(new Runnable() {
            @Override
            public void run() {
                getScrollView().fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
    private ScrollView getScrollView(){
        ScrollView scrView = (ScrollView) findViewById(R.id.createlistogramscrollview);
        return scrView;
    }
    public void createPunct(TempItem tempItem) {
        LinearLayout listogramContainerLayout = (LinearLayout) findViewById(R.id.listogrampunctslayout);
        CardView cardView = (CardView) LayoutInflater.from(listogramContainerLayout.getContext()).inflate(R.layout.list_card, listogramContainerLayout, false);
        if (tempItem == null) {
            tempItem = new TempItem();
            tempItem.setState(true);
            showViewsForPunct(tempItem, cardView, false);
        } else {
            if (tempItem.getCardView() != null && tempItem.getLayout() != null && tempItem.getNameEditText() != null && tempItem.getItemCommentEditText() != null) {
                cardView = tempItem.getCardView();
                tempItem.getNameEditText().setOnEditorActionListener(editorListenerOne);
                tempItem.getItemCommentEditText().setOnEditorActionListener(editorListenerTwo);
                cardView.setVisibility(View.VISIBLE);
                tempItem.setCardView(cardView);
                TempItems.add(tempItem);
            } else {
                showViewsForPunct(tempItem, cardView, true);
            }
        }
        listogramContainerLayout.addView(cardView);
    }

    private void showViewsForPunct(TempItem tempItem, CardView cardView, boolean hasData){
        LinearLayout addingListogramLayout = (LinearLayout) LayoutInflater.from(cardView.getContext()).inflate(R.layout.list_element_linear_layout, cardView, false);
        CreateListEditText itemNameEditText;
        itemNameEditText = (CreateListEditText) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.create_listogram_edittext, addingListogramLayout, false);
        if(hasData){
            itemNameEditText.setText(tempItem.getName());
        }
        itemNameEditText.setHint("Name");
        itemNameEditText.setTempItem(tempItem);
        itemNameEditText.setOnEditorActionListener(editorListenerOne);
        addingListogramLayout.addView(itemNameEditText);
        CreateListEditText itemKommentEditText;
        itemKommentEditText = (CreateListEditText) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.create_listogram_edittext, addingListogramLayout, false);
        if(hasData){
            itemKommentEditText.setText(tempItem.getComment());
        }
        itemKommentEditText.setHint("Comment");
        itemKommentEditText.setTempItem(tempItem);
        itemKommentEditText.setOnEditorActionListener(editorListenerTwo);
        addingListogramLayout.addView(itemKommentEditText);
        ItemButton addingButton = (ItemButton) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.list_element_right_side_button, addingListogramLayout, false);
        addingButton.setText("Delete");
        View.OnLongClickListener deleteListenner = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deletePunct((ItemButton) v);
                return false;
            }
        };
        addingButton.setFocusable(true);
        addingButton.setLongClickable(true);
        addingButton.setClickable(true);
        addingButton.setOnLongClickListener(deleteListenner);
        addingListogramLayout.addView(addingButton);
        cardView.addView(addingListogramLayout);
        tempItem.setButton(addingButton);
        tempItem.setItemNameEditText(itemNameEditText);
        tempItem.setItemCommentEditText(itemKommentEditText);
        tempItem.setCardView(cardView);
        TempItems.add(tempItem);
        addingButton.setTempItem(tempItem);
    }

    public void deletePunct(ItemButton button){
        int i = 0;
        if(button.getTempItem() != null){
            TempItem tempItem = button.getTempItem();
            tempItem.getCardView().setVisibility(View.GONE);
            TempItems.remove(tempItem);
            TempItems.trimToSize();
            saveTempItemsState();
        }
    }
    public void saveTempItemsState(){
        TempItem[] tempItems = new TempItem[TempItems.size()];
        if(tempItems.length > -1) {
            tempItems = TempItems.toArray(tempItems);
            provider.saveTempItems(tempItems);
        }
    }
    public void getAndShowTempItemsState(){
        TempItem[] tempItems;
        tempItems = provider.getTempItems();
        int length = tempItems.length;
        for(int i = 0; i < length; i++){
            createPunct(tempItems[i]);
        }
        if(length == 0){
            createPunct(null);
        }
    }
    public Item[] makeSendingItemsArray(){
        TempItem[] tempItems = new TempItem[TempItems.size()];
        tempItems = TempItems.toArray(tempItems);
        for(int i = 0; i < tempItems.length; i++){
            tempItems[i].setName(tempItems[i].getNameEditText().getText().toString());
            tempItems[i].setComment(tempItems[i].getItemCommentEditText().getText().toString());
        }
        return tempItems;
    }
    public void sendListogram(){
        Item[] items = makeSendingItemsArray();
        if(loadType == 1) {
            provider.createOnlineListogram(provider.getActiveGroup(), items, CreateListogramActivity.this);
        }
        else if(loadType == 0){
            provider.createListogramOffline(items, CreateListogramActivity.this);
        } else if(loadType == 2){
            provider.redactOfflineListogram(items, provider.getResendingList(), CreateListogramActivity.this);
        } else if(loadType == 3){
            provider.redactOnlineListogram(items, provider.getResendingList(), CreateListogramActivity.this);
        }
    }

    public void showGood(){
        TempItems.clear();
        TempItems.trimToSize();
        Intent intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        startActivity(intent);
    }
    public void showBad(){

    }
    public void showAddListOfflineGood(){
        TempItems.clear();
        TempItems.trimToSize();
        Intent intent;
        intent = new Intent(CreateListogramActivity.this, ActiveListsActivity.class);
        startActivity(intent);
    }

    public void showAddListOfflineBad(){

    }

    public void showAddListOnlineGood(){
        TempItems.clear();
        TempItems.trimToSize();
        Intent intent;
        intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        startActivity(intent);
    }
    public void showAddListOnlineBad(){

    }
}
