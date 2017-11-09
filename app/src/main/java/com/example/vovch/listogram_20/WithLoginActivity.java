package com.example.vovch.listogram_20;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import static java.security.AccessController.getContext;

public class WithLoginActivity extends AppCompatActivity {
    public  MainActivity.FirstLoginAttemptTask ITask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_login);
    }
    public WithLoginActivity.FirstLoginAttemptTask getFirstLoginAttemptTask(){
        return ITask;
    }
    protected class FirstLoginAttemptTask{
        private String userName;
        private String userPassword;
        private String userAction;
        private String userToken;
        private String whichActivity;
        private Context cntxt;
        private Context rsltcntxt;
        FirstLoginAttemptTask(String uName, String uPassword, String action, String whichOne){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = "";
            whichActivity = whichOne;
        }
        FirstLoginAttemptTask(String groupId, String action, String whichOne){
            userName = groupId;
            userPassword = " ";
            userAction = action;
            userToken = "";
            whichActivity = whichOne;
        }
        FirstLoginAttemptTask(String uName, String uPassword, String token, String action, String whichOne){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = token;
            whichActivity = whichOne;
        }
        protected void setContext(Context c){
            cntxt = c;
        }
        protected void onGoodResult(String result) {
        }
        protected void onBedResult(String result){
        }
        protected void work(){
            LoginToDatabase tDLogin = new LoginToDatabase();
            tDLogin.setContextFrom(WithLoginActivity.this);
            tDLogin.execute(userName, userPassword, userToken, userAction, whichActivity);
        }
    }
}
