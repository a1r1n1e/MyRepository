package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        private Context cntxt;
        private Context rsltcntxt;
        FirstLoginAttemptTask(String uName, String uPassword, String action){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = "";
        }
        FirstLoginAttemptTask(String groupId, String action){
            userName = groupId;
            userPassword = " ";
            userAction = action;
            userToken = "";
        }
        FirstLoginAttemptTask(String uName, String uPassword, String token, String action){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = token;
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
            tDLogin.execute(userName, userPassword, userToken, userAction);
        }
    }
}
