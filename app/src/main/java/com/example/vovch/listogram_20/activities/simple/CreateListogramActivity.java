package com.example.vovch.listogram_20.activities.simple;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.CreateListEditText;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ItemButton;
import com.example.vovch.listogram_20.data_types.SList;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_listogram_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);

        loadType = getIntent().getExtras().getInt("loadtype");                                      //getting information what type of action is it

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
        ImageButton sendListogramButton = (ImageButton) findViewById(R.id.create_listogram_send_list_button);
        sendListogramButton.setOnClickListener(sendListagramListener);
        scrollToEnd();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.create_listogram_add_punct_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPunct(null);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        clearer();
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
        super.onStop();
    }
    @Override
    public void onBackPressed(){
        if(provider.getActiveActivityNumber() == 6) {
            provider.nullActiveActivity();
        }
        Intent intent = null;
        if(loadType == 1 || loadType == 3) {                                                            //Possibly refactor to odds and evens
            intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        } else if (loadType == 0 || loadType == 2) {
            provider.setActiveListsActivityLoadType(1);
            intent = new Intent(CreateListogramActivity.this, ActiveListsActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        itemNameEditText.setOnEditorActionListener(editorListenerTwo);
        addingListogramLayout.addView(itemNameEditText);
        /*CreateListEditText itemKommentEditText;
        itemKommentEditText = (CreateListEditText) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.create_listogram_edittext, addingListogramLayout, false);
        if(hasData){
            itemKommentEditText.setText(tempItem.getComment());
        }
        itemKommentEditText.setHint("Comment");
        itemKommentEditText.setTempItem(tempItem);
        itemKommentEditText.setOnEditorActionListener(editorListenerTwo);
        addingListogramLayout.addView(itemKommentEditText);*/
        FrameLayout buttonFrame = (FrameLayout) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.list_header_imagebutton_frame, addingListogramLayout, false);
        ImageButton imageButton = (ImageButton) LayoutInflater.from(buttonFrame.getContext()).inflate(R.layout.list_header_resend_image_button, buttonFrame, false);
        Uri uri = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/cross_48");
        imageButton.setImageURI(uri);
        imageButton.setFocusable(false);
        imageButton.setClickable(false);
        ItemButton addingButton = (ItemButton) LayoutInflater.from(buttonFrame.getContext()).inflate(R.layout.list_element_button, buttonFrame, false);
        View.OnClickListener deleteListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePunct((ItemButton) v);
            }
        };
        addingButton.setFocusable(true);
        addingButton.setClickable(true);
        addingButton.setOnClickListener(deleteListenner);
        buttonFrame.addView(imageButton);
        buttonFrame.addView(addingButton);
        addingListogramLayout.addView(buttonFrame);
        cardView.addView(addingListogramLayout);
        tempItem.setButton(addingButton);
        tempItem.setItemNameEditText(itemNameEditText);
        //tempItem.setItemCommentEditText(itemKommentEditText);
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
            for(int i = 0; i < tempItems.length; i++){
                tempItems[i] = new TempItem();
                tempItems[i].setName(TempItems.get(i).getNameEditText().getText().toString());
                //tempItems[i].setComment(TempItems.get(i).getItemCommentEditText().getText().toString());
            }
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
            //tempItems[i].setComment(tempItems[i].getItemCommentEditText().getText().toString());
        }
        return tempItems;
    }
    public void sendListogram(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_listogram_send_list_button);
        Item[] items = makeSendingItemsArray();
        if(items != null){
            CompleteDialogFragment dialogFragment = new CompleteDialogFragment();
            dialogFragment.setActiveActivityProvider(provider);
            dialogFragment.setItems(items);
            dialogFragment.setLoadType(loadType);
            dialogFragment.setFab(fab);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, "dialog");
        }
    }

    public static class CompleteDialogFragment extends DialogFragment {
        private int loadType;
        private boolean clickable;
        private Item[] items;
        private FloatingActionButton fab;
        private ActiveActivityProvider activeActivityProvider;
        protected void setFab(FloatingActionButton newFab){
            fab = newFab;
        }
        protected void setLoadType(int newLoadType){
            loadType = newLoadType;
        }
        protected void setItems(Item[] newItems){
            items = newItems;
        }
        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider){
            activeActivityProvider = newActiveActivityProvider;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            clickable = true;
            String message = "Are You Sure?";
            String button2String = "Yes";
            String button1String = "No";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Nothing Happened", Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clickable = false;
                    if(loadType == 1) {
                        activeActivityProvider.createOnlineListogram(activeActivityProvider.getActiveGroup(), items, (WithLoginActivity) activeActivityProvider.getActiveActivity());
                    }
                    else if(loadType == 0){
                        activeActivityProvider.createListogramOffline(items, (WithLoginActivity) activeActivityProvider.getActiveActivity());
                    } else if(loadType == 2){
                        activeActivityProvider.redactOfflineListogram(items, activeActivityProvider.getResendingList(), (WithLoginActivity) activeActivityProvider.getActiveActivity());
                    } else if(loadType == 3){
                        activeActivityProvider.redactOnlineListogram(items, activeActivityProvider.getResendingList(), (WithLoginActivity) activeActivityProvider.getActiveActivity());
                    }
                    Toast.makeText(getActivity(), "Processing",
                            Toast.LENGTH_LONG).show();
                }
            });
            fab.setFocusable(clickable);
            fab.setClickable(clickable);
            builder.setCancelable(false);
            return builder.create();
        }
    }

    public void showAddListOfflineGood(){
        TempItems.clear();
        TempItems.trimToSize();
        Intent intent;
        provider.setActiveListsActivityLoadType(1);
        intent = new Intent(CreateListogramActivity.this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void showAddListOfflineBad(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_listogram_send_list_button);
        fab.setFocusable(true);
        fab.setClickable(true);
    }

    public void showAddListOnlineGood(){
        TempItems.clear();
        TempItems.trimToSize();
        Intent intent;
        intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    public void showAddListOnlineBad(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_listogram_send_list_button);
        fab.setFocusable(true);
        fab.setClickable(true);
    }
}
