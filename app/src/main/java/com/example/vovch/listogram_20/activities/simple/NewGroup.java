package com.example.vovch.listogram_20.activities.simple;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.UserButton;
import com.example.vovch.listogram_20.data_types.UserGroup;

import java.util.ArrayList;

public class NewGroup extends WithLoginActivity {
    private ArrayList<AddingUser> AddingUsers = new ArrayList<>();
    private String groupName;
    protected boolean stepOneDone = false;
    private ActiveActivityProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(5, NewGroup.this);

        setContentView(R.layout.activity_new_group);

        provider.addUserToGroup(String.valueOf(provider.userSessionData.getId()), "NewGroup");

        Button addUserButton = (Button)findViewById(R.id.newgroupadduserbutton);
        View.OnClickListener addUserButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        };
        addUserButton.setOnClickListener(addUserButtonListenner);
        Button confirmGroupAdding = (Button)findViewById(R.id.newgroupsubmitbutton);
        View.OnClickListener confirmListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAdding();
            }
        };
        confirmGroupAdding.setOnClickListener(confirmListenner);
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
    protected void onStart(){
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(5, NewGroup.this);
    }
    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 5) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }
    @Override
    public void onBackPressed(){
        if(provider.getActiveActivityNumber() == 5) {
            provider.nullActiveActivity();
        }
        provider.clearNewGroupPossibleMembers();
        Intent intent = new Intent(NewGroup.this, GroupList2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }
    public void addUser(){
        String id = getAddingUserId();
        if(id != null) {
            boolean userAlreadyAdded = false;
            if (provider.checkUser(id)) {
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
    public String getAddingUserId(){
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
        provider.addUserToGroup(id, "NewGroup");
    }
    public void drawnewUserLayout(AddingUser user){
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams addedUserButtonParameters = new LinearLayout.LayoutParams(matchParent, 180);
        LinearLayout addingUsersLayout = (LinearLayout) findViewById(R.id.group_members_linear_layout) ;
        UserButton newUserButton = new UserButton(addingUsersLayout.getContext());
        newUserButton.setLayoutParams(addedUserButtonParameters);
        newUserButton.setGravity(Gravity.CENTER_HORIZONTAL);
        newUserButton.setText(user.getUserName());
        user.setButton(newUserButton);
        newUserButton.setLongClickable(true);
        View.OnLongClickListener addedUserListenner = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UserButton button = (UserButton) v;
                provider.removeAddedUser(button.getUser(), "NewGroup");
                return  false;
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
        addingUsersLayout.addView(newUserButton);
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
    protected void confirmAdding(){
        EditText eText = (EditText)findViewById(R.id.newgroupnameview);
        groupName = eText.getText().toString();
        if(!groupName.equals("")) {
            addGroup(groupName);
        }
        else{
            TextView erText = (TextView) findViewById(R.id.newgrouperrorreporter);
            erText.setText("Enter Your Group Name");
        }
    }
    public void addGroup(String name){
        provider.addNewGroup(name);
    }
    public void showGood(UserGroup result){
        Intent intent = new Intent(NewGroup.this, Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        provider.setActiveGroup(result);
        startActivity(intent);
    }
    public void showBad(UserGroup result){

    }
    public void showUserCheckGood(AddingUser result){
        nullUserIdEditText();
        drawnewUserLayout(result);
    }
    public void showUserCheckBad(AddingUser result){
        nullUserIdEditText();
        TextView tView = (TextView)findViewById(R.id.newgrouperrorreporter);
        tView.setText("No Such User(");
    }
}

