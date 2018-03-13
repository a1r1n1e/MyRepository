package com.example.vovch.listogram_20.activities.simple;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;

/**
 * Created by Asus on 13.03.2018.
 */

public class SendBugActivity extends WithLoginActivity {
    protected ActiveActivityProvider provider;
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
        if(provider.getActiveActivityNumber() == 6) {
            provider.nullActiveActivity();
        }
        provider.setActiveListsActivityLoadType(1);
        Intent intent = null;
        intent = new Intent(SendBugActivity.this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        SendBugActivity.this.finish();
    }

    protected void sendBug(){

    }

    public void showGood(){

    }

    public void showBad(){

    }
}
