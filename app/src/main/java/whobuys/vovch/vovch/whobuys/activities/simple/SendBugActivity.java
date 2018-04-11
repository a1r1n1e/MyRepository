package whobuys.vovch.vovch.whobuys.activities.simple;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.activities.complex.ActiveListsActivity;
import whobuys.vovch.vovch.whobuys.data_types.CreateListEditText;

/**
 * Created by Asus on 13.03.2018.
 */

public class SendBugActivity extends WithLoginActivity {
    protected ActiveActivityProvider provider;
    private static final String FRAGMENT_TRANSACTION_DIALOG = "dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(8, SendBugActivity.this);

        setContentView(R.layout.activity_bug_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.bug_report_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);

        View.OnClickListener sendBugListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBug();
            }
        };
        FloatingActionButton sendBugFab = (FloatingActionButton) findViewById(R.id.bug_report_send_list_button);
        sendBugFab.setOnClickListener(sendBugListener);
    }

    @Override
    protected void onStart(){
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(8, SendBugActivity.this);
    }

    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 8) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed(){
        if(provider.getActiveActivityNumber() == 8) {
            provider.nullActiveActivity();
        }
        provider.setActiveListsActivityLoadType(1);
        Intent intent;
        intent = new Intent(SendBugActivity.this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        SendBugActivity.this.finish();
    }

    protected void sendBug(){
        CreateListEditText editText = (CreateListEditText) findViewById(R.id.bug_report_edittext);
        String text = editText.getText().toString();
        if(text != null) {
            CompleteDialogFragment dialogFragment = new CompleteDialogFragment();
            dialogFragment.setActiveActivityProvider(provider);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bug_report_send_list_button);
            dialogFragment.setFab(fab);
            dialogFragment.setText(text);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
        }
    }

    public static class CompleteDialogFragment extends DialogFragment {
        private boolean clickable;
        private FloatingActionButton fab;
        private String text;
        private ActiveActivityProvider activeActivityProvider;
        protected void setText(String newText){
            text = newText;
        }
        protected void setFab(FloatingActionButton newFab){
            fab = newFab;
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
                    activeActivityProvider.sendBug(text);
                    Toast.makeText(getActivity(), getString(R.string.dialog_confirm_action_processing),
                            Toast.LENGTH_LONG).show();
                }
            });
            if(fab != null) {
                fab.setFocusable(clickable);
                fab.setClickable(clickable);
            }
            builder.setCancelable(false);
            return builder.create();
        }
    }

    public void showGood(){
        provider.setActiveListsActivityLoadType(1);
        provider.nullActiveActivity();
        Intent intent;
        intent = new Intent(SendBugActivity.this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(SendBugActivity.this, getString(R.string.dialog_confirm_action_processing),
                Toast.LENGTH_LONG).show();
        SendBugActivity.this.finish();
    }

    public void showBad(){
        Toast.makeText(SendBugActivity.this, getString(R.string.some_error),
                Toast.LENGTH_LONG).show();
    }
}
