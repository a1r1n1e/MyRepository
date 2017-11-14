package com.example.vovch.listogram_20;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class GroupList2Activity extends WithLoginActivity {
    protected static ArrayList<LinearLayout> Layouts = new ArrayList<>();
    protected static ArrayList <Button> Buttons = new ArrayList<>();
    protected static ArrayList <Integer> Identificators = new ArrayList<>();
    protected static SharedPreferences loginPasswordPair;
    protected GroupList2Activity.GroupListSearcherTask gTask;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list3);
        userId = getIntent().getExtras().getString("userId");

        gTask = new GroupListSearcherTask(userId, "groupsearch");
        gTask.work();

        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        Button groupAddButton = new Button(findViewById(R.id.groupslayout).getContext());
        LinearLayout.LayoutParams groupAddButtonParameters = new LinearLayout.LayoutParams(matchParent, matchParent);
        groupAddButton.setLayoutParams(groupAddButtonParameters);
        groupAddButton.setText("Add New Group");
        View.OnClickListener addGroupListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button)v;
                button.setText("Done");
                Intent intent = new Intent(GroupList2Activity.this, NewGroup.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        };
        groupAddButton.setClickable(true);
        groupAddButton.setOnClickListener(addGroupListenner);
        LinearLayout basicLayout = (LinearLayout)findViewById(R.id.groupslayout);
        basicLayout.addView(groupAddButton);
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(GroupList2Activity.this, ActiveListsActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        this.finish();
    }
    public WithLoginActivity.FirstLoginAttemptTask getFirstLoginAttemptTask(){
        return gTask;
    }


    protected class GroupListSearcherTask extends FirstLoginAttemptTask{
        private int GROUPS_BIG_NUMBER = 100000;
        private int BUTTONS_BIG_NUMBER = 200000;
        private int GROUPS_DIVIDER = 10301;
        private int INSIDE_DIVIDER = 10253;
        private int NumberOfLines = 0;
        private int groupId;
        GroupListSearcherTask(String username, String action){
            super(username, action, "4");
        }
        @Override
        protected void onGoodResult(String result){
            groupListMaker(result);
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
        }
    }
}
