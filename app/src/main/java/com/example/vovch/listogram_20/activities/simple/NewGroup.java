package com.example.vovch.listogram_20.activities.simple;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.CreateListEditText;
import com.example.vovch.listogram_20.data_types.Item;
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

    TextView.OnEditorActionListener editorListenerOne = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND|| actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                addUser();
                return true;
            }
            else {
                return true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(getThisActivityNumber(), getThisActivityContext());

        setContentView(R.layout.activity_new_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.group_settings_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);

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

        CreateListEditText editText = (CreateListEditText) findViewById(R.id.new_user_id_edittext);
        editText.setOnEditorActionListener(editorListenerOne);

        initLayout();
    }

    protected void initLayout(){
        if(!providerCheckUser(provider.userSessionData.getId())) {
            provider.addUserToGroup(provider.userSessionData.getId(), getThisActivityType());
        }
        AddingUser[] users = provider.getPossibleMembers();
        drawNewMembers(users);
    }

    public void drawNewMembers(AddingUser[] newMembers) {
        for (AddingUser newMember : newMembers) {
            drawNewUserLayout(newMember, true);
        }
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
                Toast.makeText(NewGroup.this, "User Already Added", Toast.LENGTH_LONG)
                        .show();
            } else {
                nullUserIdEditText();
                providerAddUser(id); // error id checked inside
            }
        } else {
            Toast.makeText(NewGroup.this, "Enter Any User Id", Toast.LENGTH_LONG)
                    .show();
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

        LinearLayout addingUsersLayout = (LinearLayout) findViewById(R.id.group_members_linear_layout);
        CardView cardView = (CardView) LayoutInflater.from(addingUsersLayout.getContext()).inflate(R.layout.list_card, addingUsersLayout, false);
        LinearLayout.LayoutParams addedUserButtonParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        UserButton newUserButton = new UserButton(cardView.getContext());
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
            user.setCardView(cardView);
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
        cardView.addView(newUserButton);
        addingUsersLayout.addView(cardView);
    }

    public void showRemoveUserGood(AddingUser user) {
        CardView cardView = user.getCardView();
        cardView.setVisibility(View.GONE);
    }

    public void showRemoveUserBad(AddingUser user) {
        Toast.makeText(NewGroup.this, "You Can't remove This User :)", Toast.LENGTH_LONG)
                .show();
    }

    protected void nullUserIdEditText() {
        EditText edit = (EditText) findViewById(R.id.new_user_id_edittext);
        edit.setHint(R.string.enter_new_user_id);
        edit.setText("");
    }

    protected void confirmAll() {
        EditText nameTextView = (EditText) findViewById(R.id.group_settings_group_name_textview);
        String newGroupName = nameTextView.getText().toString();
        if (!newGroupName.equals("")) {
            providerAddNewGroup(newGroupName);
        } else {
            Toast.makeText(NewGroup.this, "Enter Your Group Name", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public static class ConfirmDialogFragment extends DialogFragment {
        private ActiveActivityProvider activeActivityProvider;
        private String name;
        private Button confirmButton;
        boolean clickable;
        protected void setConfirmButton(Button newButton){
            confirmButton = newButton;
        }
        protected void setNewName(String newName){
            name = newName;
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
                    activeActivityProvider.addNewGroup(name);
                    Toast.makeText(getActivity(), "Processing",
                            Toast.LENGTH_LONG).show();
                }
            });
            confirmButton.setFocusable(clickable);
            confirmButton.setClickable(clickable);
            builder.setCancelable(false);
            return builder.create();
        }
    }

    protected void providerAddNewGroup(String newGroupName){

        Button confirmButton = (Button) findViewById(R.id.newgroupsubmitbutton);
        confirmButton.setFocusable(false);
        confirmButton.setClickable(false);

        ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment();
        dialogFragment.setActiveActivityProvider(provider);
        dialogFragment.setNewName(newGroupName);
        dialogFragment.setConfirmButton(confirmButton);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        dialogFragment.show(transaction, "dialog");
    }

    public void showGood(UserGroup result) {
        Intent intent = new Intent(getThisActivityContext(), Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        provider.setActiveGroup(result);
        startActivity(intent);
    }

    public void showBad(UserGroup result) {
        Button confirmButton = (Button) findViewById(R.id.newgroupsubmitbutton);
        confirmButton.setFocusable(true);
        confirmButton.setClickable(true);
    }

    public void showUserCheckGood(AddingUser result) {
        drawNewUserLayout(result, true);
    }

    public void showUserCheckBad(AddingUser result) {
        Toast.makeText(NewGroup.this, "No Such User :(", Toast.LENGTH_LONG)
                .show();
    }
}