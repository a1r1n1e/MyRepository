package com.example.vovch.listogram_20.activities.simple;

import android.content.Context;
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
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.UserButton;
import com.example.vovch.listogram_20.data_types.UserGroup;

public class NewGroup extends WithLoginActivity {
    private static final int THIS_ACTIVITY_NUMBER = 5;
    protected ActiveActivityProvider provider;

    protected int getThisActivityNumber(){
        return THIS_ACTIVITY_NUMBER;
    }

    protected Context getThisActivityContext(){
        return NewGroup.this;
    }

    protected String getThisActivityType(){
        return "NewGroup";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(getThisActivityNumber(), getThisActivityContext());

        setContentView(R.layout.activity_new_group);

        Button addUserButton = (Button) findViewById(R.id.newgroupadduserbutton);
        View.OnClickListener addUserButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
                nullUserIdEditText();
            }
        };
        addUserButton.setOnClickListener(addUserButtonListener);
        Button confirmGroupAdding = (Button) findViewById(R.id.newgroupsubmitbutton);
        View.OnClickListener confirmListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAll();
            }
        };
        confirmGroupAdding.setOnClickListener(confirmListener);

        initLayout();
    }

    protected void initLayout(){
        provider.addUserToGroup(String.valueOf(provider.userSessionData.getId()), getThisActivityType());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(getThisActivityNumber(), getThisActivityContext());
    }

    @Override
    protected void onStop() {
        if (provider.getActiveActivityNumber() == getThisActivityNumber()) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        goOutOfActivity(GroupList2Activity.class);
    }

    protected void goOutOfActivity(Class<?> cls){
        if (provider.getActiveActivityNumber() == getThisActivityNumber()) {
            provider.nullActiveActivity();
        }
        provider.clearNewGroupPossibleMembers();
        Intent intent = new Intent(getThisActivityContext(), cls);
        intent.putExtra("loadtype", 0);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);                                             //troubles
        startActivity(intent);
        this.finish();
    }

    public void addUser() {
        String id = getAddingUserId();
        if (id != null) {
            if (providerCheckUser(id)) { // if user already added
                TextView erView = (TextView) findViewById(R.id.newgrouperrorreporter);
                erView.setText(R.string.user_added_error);
            } else {
                providerAddUser(id); // error id checked inside
            }
        } else {
            TextView erView = (TextView) findViewById(R.id.newgrouperrorreporter);
            erView.setText(R.string.no_user_id_error);
        }
    }

    protected boolean providerCheckUser(String id){
        return provider.checkUser(id);
    }

    protected void providerAddUser(String id){
        provider.addUserToGroup(id, getThisActivityType());
    }

    public String getAddingUserId() {
        String value;
        TextView newUserIdTextView = (TextView) findViewById(R.id.new_user_id_edittext);
        value = newUserIdTextView.getText().toString();
        String id;
        if (!value.equals("") && android.text.TextUtils.isDigitsOnly(value)) {
            id = newUserIdTextView.getText().toString();
        } else {
            id = null;
        }
        return id;
    }

    public void drawNewUserLayout(AddingUser user, boolean loadType) {
        LinearLayout.LayoutParams addedUserButtonParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout addingUsersLayout = (LinearLayout) findViewById(R.id.group_members_linear_layout);
        UserButton newUserButton = new UserButton(addingUsersLayout.getContext());
        newUserButton.setLayoutParams(addedUserButtonParameters);
        newUserButton.setGravity(Gravity.CENTER_HORIZONTAL);
        newUserButton.setText(user.getUserName());
        newUserButton.setAllCaps(false);
        newUserButton.setTextSize(20);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            newUserButton.setTextColor(getColor(R.color.whoBuysGray));
        } else {
            newUserButton.setTextColor(getResources().getColor(R.color.whoBuysGray));
        }

        if (loadType) {
            user.setButton(newUserButton);
            newUserButton.setFocusable(true);
            newUserButton.setLongClickable(true);
            newUserButton.setUser(user);
            View.OnLongClickListener addedUserListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    UserButton button = (UserButton) v;
                    provider.removeAddedUser(button.getUser(), NewGroup.this);
                    return false;
                }
            };
            newUserButton.setOnLongClickListener(addedUserListener);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                newUserButton.setBackground(getDrawable(R.color.elementLayoutColor2));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                newUserButton.setBackground(getResources().getDrawable(R.color.elementLayoutColor2));
            } else {
                newUserButton.setBackgroundDrawable(getResources().getDrawable(R.color.elementLayoutColor2));
            }
        } else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                newUserButton.setBackground(getDrawable(R.color.elementLayoutColor1));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                newUserButton.setBackground(getResources().getDrawable(R.color.elementLayoutColor1));
            } else {
                newUserButton.setBackgroundDrawable(getResources().getDrawable(R.color.elementLayoutColor1));
            }
        }
        addingUsersLayout.addView(newUserButton);
    }

    public void showRemoveUserGood(AddingUser user) {
        Button button = user.getButton();
        button.setVisibility(View.GONE);
    }

    public void showRemoveUserBad(AddingUser user) {                                                  //TODO ?

    }

    protected void nullUserIdEditText() {
        EditText edit = (EditText) findViewById(R.id.new_user_id_edittext);
        edit.setHint(R.string.enter_new_user_id);
        edit.setText("");
    }

    protected void confirmAll() {
        EditText nameTextView = (EditText) findViewById(R.id.newgroupnameview);
        String newGroupName = nameTextView.getText().toString();
        if (!newGroupName.equals("")) {
            providerAddNewGroup(newGroupName);
        } else {
            TextView erText = (TextView) findViewById(R.id.newgrouperrorreporter);
            erText.setText(R.string.enter_your_group_name);
        }
    }

    protected void providerAddNewGroup(String newGroupName){
        provider.addNewGroup(newGroupName);
    }

    public void showGood(UserGroup result) {
        Intent intent = new Intent(getThisActivityContext(), Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        provider.setActiveGroup(result);
        startActivity(intent);
    }

    public void showBad(UserGroup result) {

    }

    public void showUserCheckGood(AddingUser result) {
        drawNewUserLayout(result, true);
    }

    public void showUserCheckBad(AddingUser result) {
        TextView tView = (TextView) findViewById(R.id.newgrouperrorreporter);
        tView.setText(R.string.error_no_user);
    }
}