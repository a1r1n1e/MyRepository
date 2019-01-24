package whobuys.vovch.vovch.light.activities.simple;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.R;

import whobuys.vovch.vovch.light.activities.complex.ActiveListsActivity;
import whobuys.vovch.vovch.light.data_types.AddingUser;
import whobuys.vovch.vovch.light.data_types.UserGroup;

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
        final EditText groupNameEditText = (EditText) findViewById(R.id.group_settings_group_name_textview);
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
        leaveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup();
            }
        });

        CardView leaveCardView = (CardView) findViewById(R.id.group_settings_leave_button_cardview);
        leaveCardView.setVisibility(View.VISIBLE);

        AddingUser[] users;
        if (!provider.getActiveGroup().getOwner().equals(provider.userSessionData.getId())) {
            drawNotDeletableMembers();
        }
        users = provider.getPossibleMembers();
        if(users != null) {
            drawDeletableMembers(users);
        }

    }

    @Override
    public void onBackPressed(){
        if(provider != null) {
            provider.clearAddedUsers();
            if (provider.getActiveActivityNumber() == THIS_ACTIVITY_NUMBER) {
                provider.nullActiveActivity();
            }
        }
        super.onBackPressed();
        //goOutOfActivity(Group2Activity.class);
    }

    public void drawNotDeletableMembers() {
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

    @Override
    protected boolean providerCheckUser(String id){
        return provider.checkUser(id);
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

            String message = getString(R.string.dialog_confirm_question);
            String button2String = getString(R.string.Yes);
            String button1String = getString(R.string.No);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened), Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clickable = false;
                    UserGroup changingGroup = activeActivityProvider.getActiveGroup();
                    activeActivityProvider.confirmGroupSettingsChange(changingGroup, name);
                    Toast.makeText(getActivity(), getString(R.string.dialog_confirm_action_processing),
                            Toast.LENGTH_LONG).show();
                }
            });
            if(confirmButton != null) {
                confirmButton.setFocusable(clickable);
                confirmButton.setClickable(clickable);
            }
            builder.setCancelable(false);
            return builder.create();
        }
    }


    @Override
    protected void providerAddNewGroup(String newGroupName) {

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

    public void confirmGood(UserGroup result) {
        provider.setActiveGroup(result);
        onBackPressed();
    }

    public void confirmBad(UserGroup result) {
        writeSomeError();
    }

    private void writeSomeError(){
        Toast.makeText(GroupSettingsActivity.this, getString(R.string.some_error), Toast.LENGTH_LONG)
                .show();
    }

    public void leaveGroup() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getThisActivityContext());
        ad.setMessage(getString(R.string.dialog_want_to_leave_question));
        ad.setNegativeButton(getString(R.string.dialog_stay), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(getThisActivityContext(), getString(R.string.dialog_you_are_still_in), Toast.LENGTH_LONG)
                        .show();
            }
        });
        ad.setPositiveButton(getString(R.string.dialog_leave), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                provider.leaveGroup();
                Toast.makeText(getThisActivityContext(), getString(R.string.dialog_kill_action_processing),
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getThisActivityContext(), getString(R.string.dialog_nothing_happened),
                        Toast.LENGTH_LONG).show();
            }
        });
        ad.show();
    }

    public void leaveGroupGood(UserGroup result) {
        provider.setActiveListsActivityLoadType(0);
        goOutOfActivity(ActiveListsActivity.class);
    }

    public void leaveGroupBad(UserGroup result) {
        writeSomeError();
    }
}
