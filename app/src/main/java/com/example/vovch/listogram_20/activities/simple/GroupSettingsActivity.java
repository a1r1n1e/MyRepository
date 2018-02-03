package com.example.vovch.listogram_20.activities.simple;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.UserGroup;

public class GroupSettingsActivity extends NewGroup {
    private static final int THIS_ACTIVITY_NUMBER = 7;

    @Override
    protected int getThisActivityNumber(){
        return THIS_ACTIVITY_NUMBER;
    }

    @Override
    protected Context getThisActivityContext(){
        return GroupSettingsActivity.this;
    }

    @Override
    protected String getThisActivityType(){
        return "GroupSettingsActivity";
    }

    @Override
    protected void initLayout(){
        final EditText groupNameEditText = (EditText) findViewById(R.id.newgroupnameview);
        groupNameEditText.setText(provider.getActiveGroup().getName());
        groupNameEditText.setFocusable(false);
        groupNameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                groupNameEditText.setFocusableInTouchMode(true);

                return false;
            }
        });

        final EditText userIdEditText = (EditText) findViewById(R.id.new_user_id_edittext);
        userIdEditText.setFocusable(false);
        userIdEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                userIdEditText.setFocusableInTouchMode(true);

                return false;
            }
        });


        Button leaveGroupButton = (Button) findViewById(R.id.leavegroupbutton);
        leaveGroupButton.setFocusable(true);
        leaveGroupButton.setClickable(true);
        leaveGroupButton.setVisibility(View.VISIBLE);
        leaveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup();
            }
        });

        AddingUser[] users;
        if (!provider.getActiveGroup().getOwner().equals(provider.userSessionData.getId())) {
            drawOldMembers();
            users = provider.getPossibleMembers();
        } else {
            users = provider.makeAllMembersPossible();
        }
        drawNewMembers(users);
    }

    @Override
    public void onBackPressed(){
        goOutOfActivity(Group2Activity.class);
    }

    public void drawOldMembers() {
        AddingUser[] oldMembers = provider.getActiveGroup().getMembers();
        boolean type = false;
        if (oldMembers != null) {
            if (provider.getActiveGroup().getOwner().equals(provider.userSessionData.getId())) {
                type = true;
            }
            for (AddingUser oldMember : oldMembers) {
                drawNewUserLayout(oldMember, type);
            }
        }
    }

    public void drawNewMembers(AddingUser[] newMembers) {
        for (AddingUser newMember : newMembers) {
            drawNewUserLayout(newMember, true);
        }
    }

    @Override
    protected boolean providerCheckUser(String id){
        return provider.checkUser(id) || isOldMember(id);
    }

    private boolean isOldMember(String id) {
        AddingUser[] oldUsers = provider.getActiveGroup().getMembers();
        for (AddingUser user : oldUsers)
            if (user.getUserId().equals(id)) {
                return true;
            }
        return false;
    }

    public void showUserCheckGood(AddingUser result) {
        drawNewUserLayout(result, true);
    }


    @Override
    protected void providerAddNewGroup(String newGroupName) {
        UserGroup changedGroup = new UserGroup(newGroupName, provider.getActiveGroup().getId());
        changedGroup.setOwner(provider.getActiveGroup().getOwner());
        provider.confirmGroupSettingsChange(changedGroup);
    }

    public void confirmGood(UserGroup result) {
        provider.setActiveGroup(result);
        onBackPressed();
    }

    public void confirmBad(UserGroup result) {
        writeSomeError();
    }

    private void writeSomeError(){
        TextView erView = (TextView) findViewById(R.id.newgrouperrorreporter);
        erView.setText(R.string.some_error);
    }

    public void leaveGroup() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getThisActivityContext());
        ad.setMessage("Want To Leave?");
        ad.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(getThisActivityContext(), "You Are Still In", Toast.LENGTH_LONG)
                        .show();
            }
        });
        ad.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                provider.leaveGroup();
                Toast.makeText(getThisActivityContext(), "Processing",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getThisActivityContext(), "Nothing Happened",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.show();
    }

    public void leaveGroupGood(UserGroup result) {
        goOutOfActivity(GroupList2Activity.class);
    }

    public void leaveGroupBad(UserGroup result) {
        writeSomeError();
    }
}
