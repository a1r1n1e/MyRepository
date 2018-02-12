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
    private String groupName;
    private String groupId;
    private ActiveActivityProvider provider;

    private boolean loadType;

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

        loadType = getIntent().getExtras().getBoolean("loadtype");                                      //gettitg information what type of activity is our invoker

        if(loadType){
            UserGroup currentGroup = provider.getActiveGroup();
            if(currentGroup != null){
                groupId = currentGroup.getId();
                groupName = currentGroup.getName();
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
        Intent intent;
        if(loadType) {
            intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        }
        else{
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
    public void createPunct(TempItem tempItem){
        LinearLayout listogramContainerLayout = (LinearLayout) findViewById(R.id.listogrampunctslayout);
        CardView cardView;
        if(tempItem == null) {
            tempItem = new TempItem();
            cardView = (CardView) LayoutInflater.from(listogramContainerLayout.getContext()).inflate(R.layout.list_card, listogramContainerLayout, false);
            LinearLayout addingListogramLayout = (LinearLayout) LayoutInflater.from(cardView.getContext()).inflate(R.layout.list_element_linear_layout, cardView, false);
            CreateListEditText itemNameEditText;
            itemNameEditText = (CreateListEditText) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.create_listogram_edittext, addingListogramLayout, false);
            itemNameEditText.setHint("Name");
            itemNameEditText.setTempItem(tempItem);
            itemNameEditText.setOnEditorActionListener(editorListenerOne);
            addingListogramLayout.addView(itemNameEditText);
            CreateListEditText itemKommentEditText;
            itemKommentEditText = (CreateListEditText) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.create_listogram_edittext, addingListogramLayout, false);
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
        else{
            cardView = tempItem.getCardView();
            tempItem.getNameEditText().setOnEditorActionListener(editorListenerOne);
            tempItem.getItemCommentEditText().setOnEditorActionListener(editorListenerTwo);
            cardView.setVisibility(View.VISIBLE);
            tempItem.setCardView(cardView);
            TempItems.add(tempItem);
        }
        listogramContainerLayout.addView(cardView);
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
        Item[] items = new Item[TempItems.size()];
        Item tempItem;
        StringBuilder eText1 = new StringBuilder("");
        StringBuilder eText2 = new StringBuilder("");
        for(int i = 0;i < TempItems.size();i++){
            eText1.append(TempItems.get(i).getNameEditText().getText().toString());
            eText2.append(TempItems.get(i).getItemCommentEditText().getText().toString());

            tempItem = new Item(eText1.toString(), eText2.toString(), true);

            eText1.setLength(0);
            eText2.setLength(0);
            items[i] = tempItem;
        }
        return items;
    }
    public void sendListogram(){
        Item[] items = makeSendingItemsArray();
        if(loadType) {
            provider.createOnlineListogram(groupId, items);
        }
        else{
            provider.createListogramOffline(items, CreateListogramActivity.this);
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
}
