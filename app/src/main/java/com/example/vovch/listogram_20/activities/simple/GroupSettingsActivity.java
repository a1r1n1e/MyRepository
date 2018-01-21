package com.example.vovch.listogram_20.activities.simple;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.example.vovch.listogram_20.data_types.UserButton;
import com.example.vovch.listogram_20.data_types.UserGroup;

public class GroupSettingsActivity extends AppCompatActivity {
    private ActiveActivityProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(7, GroupSettingsActivity.this);

        Toolbar toolbar= (Toolbar) findViewById(R.id.group_settings_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        LinearLayout baseLayout = (LinearLayout) findViewById(R.id.group_settings_group_editing_layout);

        LinearLayout editingLayout = (LinearLayout) LayoutInflater.from(baseLayout.getContext()).inflate(R.layout.activity_new_group, baseLayout, false);
        baseLayout.addView(editingLayout);

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

        Button addUserButton = (Button)findViewById(R.id.newgroupadduserbutton);
        View.OnClickListener addUserButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        };
        addUserButton.setOnClickListener(addUserButtonListenner);
        Button confirmButton = (Button)findViewById(R.id.newgroupsubmitbutton);
        View.OnClickListener confirmListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmChanges();
            }
        };
        confirmButton.setOnClickListener(confirmListenner);
        LinearLayout newGroupBaseLayout = (LinearLayout) findViewById(R.id.newgroupcontainerlayout);
        Button leaveGroupButton = new Button(newGroupBaseLayout.getContext());
        leaveGroupButton.setFocusable(true);
        leaveGroupButton.setClickable(true);
        leaveGroupButton.setText("Leave Group");
        leaveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup();
            }
        });
        newGroupBaseLayout.addView(leaveGroupButton);
    }
    @Override
    public void onBackPressed(){
        if(provider.getActiveActivityNumber() == 7) {
            provider.nullActiveActivity();
        }
        provider.clearNewGroupPossibleMembers();
        Intent intent = new Intent(GroupSettingsActivity.this, Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onStart(){
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(7, GroupSettingsActivity.this);
        LinearLayout oldMembersLayout = (LinearLayout) findViewById(R.id.group_members_linear_layout);
        if(!provider.getActiveGroup().getOwner().equals(provider.userSessionData.getId())) {
            drawOldMembers(oldMembersLayout);
        }
        else{
            provider.makeAllMembersPossible();
        }
        drawNewMembers(oldMembersLayout, provider.getPossibleMembers());
    }
    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 7) {
            provider.nullActiveActivity();
        }
        LinearLayout oldMembersLayout = (LinearLayout) findViewById(R.id.group_members_linear_layout);
        oldMembersLayout.removeAllViewsInLayout();
        super.onStop();
    }
    public void drawOldMembers(LinearLayout parentLayout){
        AddingUser[] oldMembers = provider.getActiveGroup().getMembers();
        boolean type = false;
        if(oldMembers != null) {
            if(provider.getActiveGroup().getOwner().equals(provider.userSessionData.getId())){
                type = true;
            }
            for (int i = 0; i < oldMembers.length; i++) {
                drawNewUserLayout(oldMembers[i], type, parentLayout);
            }
        }
    }
    public void drawNewMembers(LinearLayout parentLayout, AddingUser[] newMembers){
        for(int i = 0; i < newMembers.length; i++){
            drawNewUserLayout(newMembers[i], true, parentLayout);
        }
    }
    private void addUser(){
        String id = getAddingUserId();
        if(id != null) {
            boolean userAlreadyAdded = false;
            if (provider.checkUser(id) || isOldMember(id)) {
                userAlreadyAdded = true;
            }
            if (userAlreadyAdded) {
                TextView erView = (TextView) findViewById(R.id.newgrouperrorreporter);
                erView.setText("User Already Added");
            } else {
                checkUser(id);
            }
        }
        else{
            TextView erView = (TextView) findViewById(R.id.newgrouperrorreporter);
            erView.setText("Enter Any User Id");
        }
    }
    private boolean isOldMember(String id){
        boolean result = false;
        AddingUser[] oldUsers = provider.getActiveGroup().getMembers();
        if(oldUsers != null) {
            int length = oldUsers.length;
            int i = 0;
            for (i = 0; i < length; i++) {
                if(oldUsers[i].getUserId().equals(id)){
                    break;
                }
            }
            if(i < length){
                result = true;
            }
        }
        return result;
    }
    private String getAddingUserId(){
        String value;
        TextView newUserIdTextView = (TextView)findViewById(R.id.new_user_id_edittext);
        value = newUserIdTextView.getText().toString();
        String id;
        if(!value.equals("") && android.text.TextUtils.isDigitsOnly(value)) {
            id = newUserIdTextView.getText().toString();
        }
        else{
            id = null;
        }
        return id;
    }
    private void checkUser(String id){
        provider.addUserToGroup(String.valueOf(id), "GroupSettingsActivity");
    }
    public void drawNewUserLayout(AddingUser user, boolean loadType, LinearLayout parentLayout){
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams addedUserButtonParameters = new LinearLayout.LayoutParams(matchParent, 180);
        UserButton newUserButton = new UserButton(parentLayout.getContext());
        newUserButton.setLayoutParams(addedUserButtonParameters);
        newUserButton.setGravity(Gravity.CENTER_HORIZONTAL);
        newUserButton.setAllCaps(false);
        newUserButton.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            newUserButton.setTextColor(getColor(R.color.whoBuysGray));
        } else{
            newUserButton.setTextColor(getResources().getColor(R.color.whoBuysGray));
        }
        newUserButton.setText(user.getUserName());
        if(loadType) {
            user.setButton(newUserButton);
            newUserButton.setLongClickable(true);
            View.OnLongClickListener addedUserListenner = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    UserButton button = (UserButton) v;
                    provider.removeAddedUser(button.getUser(), "GroupSettingsActivity");
                    return false;
                }
            };
            newUserButton.setOnLongClickListener(addedUserListenner);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                newUserButton.setBackground(getDrawable(R.color.elementLayoutColor2));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                newUserButton.setBackground(getResources().getDrawable(R.color.elementLayoutColor2));
            } else{
                newUserButton.setBackgroundDrawable(getResources().getDrawable(R.color.elementLayoutColor2));
            }
            newUserButton.setUser(user);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                newUserButton.setBackground(getDrawable(R.color.elementLayoutColor1));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                newUserButton.setBackground(getResources().getDrawable(R.color.elementLayoutColor1));
            } else{
                newUserButton.setBackgroundDrawable(getResources().getDrawable(R.color.elementLayoutColor1));
            }
        }
        parentLayout.addView(newUserButton);
    }
    public void showUserCheckGood(AddingUser result){
        nullUserIdEditText();
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.group_members_linear_layout) ;
        drawNewUserLayout(result, true, parentLayout);
    }
    public void showUserCheckBad(AddingUser result){
        nullUserIdEditText();
        TextView tView = (TextView)findViewById(R.id.newgrouperrorreporter);
        tView.setText("No Such User(");
    }
    public void showRemoveUserGood(AddingUser user){
        Button button = (Button) user.getButton();
        button.setVisibility(View.GONE);
    }
    public void showRemoveUserBad(AddingUser user){                                                  //TODO ?

    }                                               //TODO ?
    private void nullUserIdEditText(){
        EditText edit = (EditText) findViewById(R.id.new_user_id_edittext) ;
        edit.setHint(edit.getText());
        edit.setText("");
    }
    private void confirmChanges(){
        TextView nameTextView = (TextView) findViewById(R.id.newgroupnameview);
        String newName = nameTextView.getText().toString();
        String groupId = provider.getActiveGroup().getId();
        String owner = provider.getActiveGroup().getOwner();
        UserGroup changedGroup= new UserGroup(newName, groupId);
        changedGroup.setOwner(owner);
        provider.confirmGroupSettingsChange(changedGroup);
    }
    public void confirmGood(UserGroup result){
        if(provider.getActiveActivityNumber() == 7) {
            provider.nullActiveActivity();
        }
        provider.clearNewGroupPossibleMembers();
        provider.setActiveGroup(result);
        Intent intent = new Intent(GroupSettingsActivity.this, Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }
    public void confirmBad(UserGroup result){
        TextView erView = (TextView) findViewById(R.id.newgrouperrorreporter);
        erView.setText("Something Went Wrong");
    }
    public void leaveGroup(){

        AlertDialog.Builder ad = new AlertDialog.Builder(GroupSettingsActivity.this);
        ad.setMessage("Want To Leave?");
        ad.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(GroupSettingsActivity.this, "You Are Still In", Toast.LENGTH_LONG)
                        .show();
            }
        });
        ad.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                provider.leaveGroup();
                Toast.makeText(GroupSettingsActivity.this, "Processing",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(GroupSettingsActivity.this, "Nothing Happened",
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.show();
    }
    public void leaveGroupGood(UserGroup result){
        if(provider.getActiveActivityNumber() == 7) {
            provider.nullActiveActivity();
        }
        provider.clearNewGroupPossibleMembers();
        Intent intent = new Intent(GroupSettingsActivity.this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }
    public void leaveGroupBad(UserGroup result){
        TextView erView = (TextView) findViewById(R.id.newgrouperrorreporter);
        erView.setText("Something Went Wrong");
    }
}
