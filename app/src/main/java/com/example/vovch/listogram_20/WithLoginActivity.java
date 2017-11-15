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
        private String taskType;
        private Context cntxt;
        private Context rsltcntxt;
        FirstLoginAttemptTask(String uName, String uPassword, String action, String whichOne, String type){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = "";
            whichActivity = whichOne;
            taskType = type;
        }
        FirstLoginAttemptTask(String groupId, String action, String whichOne, String type){
            userName = groupId;
            userPassword = " ";
            userAction = action;
            userToken = "";
            whichActivity = whichOne;
            taskType = type;
        }
        FirstLoginAttemptTask(String uName, String uPassword, String token, String action, String whichOne, String type){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = token;
            whichActivity = whichOne;
            taskType = type;
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
            //tDLogin.setContextFrom(WithLoginActivity.this);
            tDLogin.setApplicationContext((ActiveActivityProvider) getApplicationContext());
            tDLogin.execute(userName, userPassword, userToken, userAction, whichActivity, taskType);
        }
    }
}
