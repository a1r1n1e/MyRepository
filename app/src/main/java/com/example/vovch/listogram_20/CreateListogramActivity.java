package com.example.vovch.listogram_20;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class CreateListogramActivity extends WithLoginActivity {
    private CreateListogramActivity.SendListogramTask slTask;
    protected static ArrayList<LinearLayout> AddedLayouts = new ArrayList<>();
    protected static ArrayList <EditText> Items = new ArrayList<>();
    protected static ArrayList <EditText> Komments = new ArrayList<>();
    protected static ArrayList <Button> Buttons = new ArrayList<>();
    protected static SharedPreferences loginPasswordPair;
    private int NumberOfLines = 0;
    private static final int sGravity = Gravity.START;
    private int PUNCT_LAYOUTS_BIG_NUMBER = 1000000;
    private int PUNCT_ITEMS_BIG_NUMBER = 2000000;
    private int PUNCT_KOMMENTS_BIG_NUMBER = 3000000;
    private int PUNCT_BUTTONS_BIG_NUMBER = 4000000;
    private String groupName;
    private String groupId;
    private String userId;
    private ActiveActivityProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(6, CreateListogramActivity.this);

        setContentView(R.layout.activity_create_listogram);
        groupName = getIntent().getExtras().getString("name");                                  //получаем данные о группе
        groupId = getIntent().getExtras().getString("groupid");
        userId = getIntent().getExtras().getString("userid");

        View.OnClickListener addPunctListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPunct();
                scrollToEnd();
            }
        };
        View.OnClickListener sendListagramListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendListogram();
            }
        };
        Button sendListogramButton = (Button)findViewById(R.id.listogramsenddownbutton);
        sendListogramButton.setOnClickListener(sendListagramListener);
        Button addPunctButton = (Button) findViewById(R.id.addlistogrampunct);
        addPunctButton.setClickable(true);
        addPunctButton.setOnClickListener(addPunctListenner);
        createPunct();
        scrollToEnd();
    }
    @Override
    protected void onResume(){
        super.onResume();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(6, CreateListogramActivity.this);
    }
    @Override
    protected void onPause(){
        provider.nullActiveActivity();
        clearer();
        super.onPause();
    }
    @Override
    public void onBackPressed(){
        //timer.cancel();
        clearer();
        Intent intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        intent.putExtra("name", groupName);
        intent.putExtra("groupid", groupId);
        intent.putExtra("userid", userId);
        startActivity(intent);
        this.finish();
    }
    private void clearer(){
        Items.clear();
        Komments.clear();
        AddedLayouts.clear();
        Buttons.clear();
        NumberOfLines = 0;
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
    protected void createPunct(){
        LinearLayout listogramContainerLayout = (LinearLayout) findViewById(R.id.listogrampunctslayout);
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams layoutParameters = new LinearLayout.LayoutParams(matchParent, 180);
        LinearLayout.LayoutParams itemNameParameters = new LinearLayout.LayoutParams(300, matchParent);
        LinearLayout.LayoutParams itemKommentParameters = new LinearLayout.LayoutParams(300, matchParent);
        LinearLayout.LayoutParams buttonParameters = new LinearLayout.LayoutParams(300, matchParent);
        layoutParameters.gravity = sGravity;
        itemNameParameters.gravity = sGravity;
        itemKommentParameters.gravity = sGravity;
        buttonParameters.gravity = sGravity;
        LinearLayout addingListogramLayout = new LinearLayout(listogramContainerLayout.getContext());
        addingListogramLayout.setLayoutParams(layoutParameters);
        addingListogramLayout.setOrientation(LinearLayout.HORIZONTAL);
        addingListogramLayout.setId(PUNCT_LAYOUTS_BIG_NUMBER + NumberOfLines);
        AddedLayouts.add(AddedLayouts.size(), addingListogramLayout);
        EditText itemNameEditText = new EditText(addingListogramLayout.getContext());
        itemNameEditText.setLayoutParams(itemNameParameters);
        itemNameEditText.setHint("Enter Item Name");
        itemNameEditText.setClickable(true);
        itemNameEditText.setId(PUNCT_ITEMS_BIG_NUMBER + NumberOfLines);
        addingListogramLayout.addView(itemNameEditText);
        Items.add(Items.size(), itemNameEditText);
        EditText itemKommentEditText = new EditText(addingListogramLayout.getContext());
        itemKommentEditText.setLayoutParams(itemNameParameters);
        itemKommentEditText.setHint("Enter Any Komment You Like");
        itemKommentEditText.setClickable(true);
        itemKommentEditText.setId(PUNCT_KOMMENTS_BIG_NUMBER + NumberOfLines);
        addingListogramLayout.addView(itemKommentEditText);
        Komments.add(Komments.size(), itemKommentEditText);
        Button addingButton = new Button(addingListogramLayout.getContext());
        addingButton.setLayoutParams(buttonParameters);
        addingButton.setId(PUNCT_BUTTONS_BIG_NUMBER + NumberOfLines);
        addingButton.setText("Delete Item");
        View.OnLongClickListener deleteListenner = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int id = v.getId() - PUNCT_BUTTONS_BIG_NUMBER;
                AddedLayouts.remove(findViewById(id + PUNCT_LAYOUTS_BIG_NUMBER));
                AddedLayouts.trimToSize();
                Items.remove(findViewById(id + PUNCT_ITEMS_BIG_NUMBER));
                Items.trimToSize();
                Komments.remove(findViewById(id + PUNCT_KOMMENTS_BIG_NUMBER));
                Komments.trimToSize();
                Buttons.remove(findViewById(id + PUNCT_BUTTONS_BIG_NUMBER));
                Buttons.trimToSize();
                findViewById(id + PUNCT_LAYOUTS_BIG_NUMBER).setVisibility(View.GONE);
                findViewById(id + PUNCT_BUTTONS_BIG_NUMBER).setVisibility(View.GONE);
                findViewById(id + PUNCT_ITEMS_BIG_NUMBER).setVisibility(View.GONE);
                findViewById(id + PUNCT_KOMMENTS_BIG_NUMBER).setVisibility(View.GONE);
                LinearLayout removingLayout = (LinearLayout) findViewById(id + PUNCT_LAYOUTS_BIG_NUMBER);
                removingLayout.removeView(findViewById(id + PUNCT_LAYOUTS_BIG_NUMBER));
                removingLayout.removeView(findViewById(id + PUNCT_BUTTONS_BIG_NUMBER));
                removingLayout.removeView(findViewById(id + PUNCT_ITEMS_BIG_NUMBER));
                removingLayout.removeView(findViewById(id + PUNCT_KOMMENTS_BIG_NUMBER));
                LinearLayout parent = (LinearLayout) findViewById(R.id.creatinglistogrampunctlistcontainer);
                parent.removeView(removingLayout);
                return true;
            }
        };
        addingButton.setFocusable(true);
        addingButton.setLongClickable(true);
        addingButton.setClickable(true);
        addingButton.setOnLongClickListener(deleteListenner);
        addingListogramLayout.addView(addingButton);
        Buttons.add(Buttons.size(), addingButton);
        listogramContainerLayout.addView(addingListogramLayout);
        NumberOfLines++;
    }
    protected String makeSendingString(){
        StringBuilder result = new StringBuilder("");
        EditText eText1;
        EditText eText2;
        for(int i = 0;i < Items.size();i++){
            eText1 = Items.get(i);
            eText2 = Komments.get(i);
            result.append(eText1.getText().toString() + "%");
            result.append(eText2.getText().toString() + "%");
        }
        clearer();
        return result.toString();
    }
    protected void sendListogram(){
        String listogram = makeSendingString();
        StringBuilder idString = new StringBuilder(userId);
        idString.append("%");
        idString.append(groupId);
        slTask = new SendListogramTask(userId, groupId, listogram, "sendlistogram");
        slTask.work();
    }

    protected void showGood(){
        Intent intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        intent.putExtra("name", groupName);
        intent.putExtra("groupid", groupId);
        intent.putExtra("userid", userId);
        startActivity(intent);
    }
    protected void showBad(String result){

    }

    protected class SendListogramTask extends FirstLoginAttemptTask{
        SendListogramTask(String username, String userpassword, String third, String action){
            super(username, third, userpassword, action, "6", "0");
        }
    }
}
