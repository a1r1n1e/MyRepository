package com.example.vovch.listogram_20.activities.simple;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.GroupButton;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.UserGroup;

public class GroupList2Activity extends WithLoginActivity {
    private ActiveActivityProvider provider;
    private int loadType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadType = getIntent().getExtras().getInt("loadtype");
        if(loadType == 1) {
            SList resendingList = (SList) getIntent().getParcelableExtra("sending_list");
        }

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(4, GroupList2Activity.this);

        setContentView(R.layout.activity_group_list3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_group_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.setElevation(24);
        }
        tabLayout.addTab(tabLayout.newTab().setText("Your Groups"));

        FloatingActionButton groupAddButton = (FloatingActionButton) findViewById(R.id.group_add_fab);
        View.OnClickListener addGroupListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupList2Activity.this, NewGroup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        };
        groupAddButton.setClickable(true);
        groupAddButton.setOnClickListener(addGroupListenner);
    }
    public void update(){
        provider.getGroups();
    }
    private void clearer(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.groupslayout);
        layout.removeAllViews();
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
        clearer();
        super.onPause();
    }
    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 4) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        clearer();
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        clearer();
        if(provider.getActiveActivityNumber() == 4) {
            provider.nullActiveActivity();
        }
        Intent intent = new Intent(GroupList2Activity.this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }

    public void showGood(UserGroup[] result){
        clearer();
        groupListMaker(result);
    }
    public void showBad(UserGroup[] result){

    }

    protected void groupListMaker(UserGroup[] result){
        int length = result.length;
        LinearLayout basicLayout = (LinearLayout) findViewById(R.id.groupslayout);
        int i = 0;
        for(i = 0; i < length; i++)
        {
                groupLayoutDrawer(basicLayout, result[i]);
        }
    }
    protected void groupLayoutDrawer(LinearLayout parentLayout, UserGroup group){
        CardView addingLayout = (CardView) LayoutInflater.from(parentLayout.getContext()).inflate(R.layout.list_card, parentLayout, false);
        GroupButton groupButton = (GroupButton) LayoutInflater.from(parentLayout.getContext()).inflate(R.layout.group_button, addingLayout, false);
        groupButton.setText(group.getName());

        groupButton.setGroup(group);

        View.OnClickListener groupTochedListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupButton button = (GroupButton) v;
                onGroupNameTochedAction(button.getGroup());
            }
        };
        groupButton.setOnClickListener(groupTochedListenner);
        addingLayout.addView(groupButton);
        group.setCardView(addingLayout);
        group.setButton(groupButton);
        parentLayout.addView(addingLayout);

    }
    protected void onGroupNameTochedAction(UserGroup group){
        if(group != null){
            if(group.getId() != null && group.getName() != null){
                goToGroup(group);
            }
        }
    }
    public void goToGroup(UserGroup group){
        provider.setActiveGroup(group);
        Intent intent = new Intent(GroupList2Activity.this, Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }
}
