package com.example.vovch.listogram_20;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupList2Activity extends WithLoginActivity {
    protected static ArrayList<LinearLayout> Layouts = new ArrayList<>();
    protected static ArrayList <Button> Buttons = new ArrayList<>();
    protected static ArrayList <Integer> Identificators = new ArrayList<>();
    protected static SharedPreferences loginPasswordPair;
    protected GroupList2Activity.GroupListSearcherTask gTask;
    private String userId;
    private int groupId;
    private ActiveActivityProvider provider;

    private int GROUPS_BIG_NUMBER = 100000;
    private int BUTTONS_BIG_NUMBER = 200000;
    private int GROUPS_DIVIDER = 10301;
    private int INSIDE_DIVIDER = 10253;
    private int NumberOfLines = 0;

    private static final String  APP_PREFERENCES_USERID= "userid";
    private static final String APP_PREFERENCES_LOGIN = "login";
    private static final String APP_PREFERENCES_PASSWORD = "password";
    public static final String APP_PREFERENCES = "autentification";

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(4, GroupList2Activity.this);

        setContentView(R.layout.activity_group_list3);
        userId = getIntent().getExtras().getString("userId");
        Button groupAddButton = (Button) findViewById(R.id.groupaddbutton);
        groupAddButton.setText("Add New Group");
        View.OnClickListener addGroupListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                button.setText("Done");

                Intent intent = new Intent(GroupList2Activity.this, NewGroup.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        };
        groupAddButton.setClickable(true);
        groupAddButton.setOnClickListener(addGroupListenner);
    }
    protected void update(){
        gTask = new GroupListSearcherTask(userId, "groupsearch");
        gTask.work();
    }
    private void clearer(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.groupslayout);
        layout.removeAllViews();
        Layouts.clear();
        Buttons.clear();
        Identificators.clear();
        NumberOfLines = 0;
    }
    @Override
    protected void onResume(){
        super.onResume();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(4, GroupList2Activity.this);
        update();
    }
    @Override
    protected void onPause(){
        provider.nullActiveActivity();
        clearer();
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        //provider.nullActiveActivity();
        clearer();
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        clearer();
        provider.nullActiveActivity();
        Intent intent = new Intent(GroupList2Activity.this, ActiveListsActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        this.finish();
    }

    protected void showGood(String result){
        clearer();
        groupListMaker(result);
    }
    protected void showBad(){

    }

    protected void groupListMaker(String result){
        int length = result.length();
        StringBuilder tempNameString = new StringBuilder();
        StringBuilder tempIdString = new StringBuilder();
        for(int i = 0, j = 0;i < length;i++)
        {
            if(result.codePointAt(i) == INSIDE_DIVIDER){
                tempNameString.setLength(0);
                tempNameString.append(result.substring(j, i));
                j = i + 1;
            }
            else if(result.codePointAt(i) == GROUPS_DIVIDER){
                tempIdString.setLength(0);
                tempIdString.append(result.substring(j, i));
                j = i + 1;
                groupLayoutDrawer(tempNameString.toString(), tempIdString.toString());
            }
        }
    }
    protected void groupLayoutDrawer(String groupName, String groupId){
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(matchParent, 180);
        LinearLayout.LayoutParams buttonParameters = new LinearLayout.LayoutParams(matchParent, matchParent);
        LinearLayout addingLayout = new LinearLayout(findViewById(R.id.groupslayout).getContext());
        addingLayout.setLayoutParams(parameters);
        addingLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        addingLayout.setId(GROUPS_BIG_NUMBER + NumberOfLines);
        Layouts.add(Layouts.size(), addingLayout);
        Button groupButton = new Button(addingLayout.getContext());
        groupButton.setLayoutParams(buttonParameters);
        groupButton.setGravity(Gravity.CENTER_HORIZONTAL);
        groupButton.setId(BUTTONS_BIG_NUMBER + NumberOfLines);
        groupButton.setText(groupName);
        groupButton.setClickable(true);
        View.OnClickListener groupTochedListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGroupNameTochedAction((Button)v);
            }
        };
        groupButton.setOnClickListener(groupTochedListenner);
        Buttons.add(Buttons.size(), groupButton);
        addingLayout.addView(groupButton);
        LinearLayout basicLayout = (LinearLayout) findViewById(R.id.groupslayout);
        basicLayout.addView(addingLayout);
        Identificators.add(Identificators.size(), Integer.parseInt(groupId));
        NumberOfLines++;
    }
    protected void onGroupNameTochedAction(Button button){
        String text = button.getText().toString();
        int id = button.getId();
        groupId = Identificators.get(id - BUTTONS_BIG_NUMBER);
        Intent intent = new Intent(GroupList2Activity.this, Group2Activity.class);
        intent.putExtra("groupid", String.valueOf(groupId));
        intent.putExtra("userid", userId);
        intent.putExtra("name", text);
        startActivity(intent);
        this.finish();
    }
    private class GroupListSearcherTask extends FirstLoginAttemptTask{
        GroupListSearcherTask(String username, String action){
            super(username, action, "4", "0");
        }
    }
}
