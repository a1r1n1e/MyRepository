package com.example.vovch.listogram_20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WithLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_login);
    }
    protected class FirstLoginAttemptTask{
        private String userName;
        private String userPassword;
        private String userAction;
        private String userToken;
        private String whichActivity;
        private String taskType;
        private String groupNumber;
        FirstLoginAttemptTask(String uName, String uPassword, String action, String whichOne, String type){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = "";
            whichActivity = whichOne;
            taskType = type;
            groupNumber = "";
        }
        FirstLoginAttemptTask(String uName, String uPassword, String token, String action, String whichOne, String type, String groupId){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = token;
            whichActivity = whichOne;
            taskType = type;
            groupNumber = groupId;
        }
        FirstLoginAttemptTask(String groupId, String action, String whichOne, String type){
            userName = groupId;
            userPassword = " ";
            userAction = action;
            userToken = "";
            whichActivity = whichOne;
            taskType = type;
            groupNumber = "";
        }
        FirstLoginAttemptTask(String uName, String uPassword, String token, String action, String whichOne, String type){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
            userToken = token;
            whichActivity = whichOne;
            taskType = type;
            groupNumber = "";
        }
        protected void work(){
            LoginToDatabase tDLogin = new LoginToDatabase();
            tDLogin.setApplicationContext((ActiveActivityProvider) getApplicationContext());
            tDLogin.execute(userName, userPassword, userToken, userAction, whichActivity, taskType, groupNumber);
        }
    }
}
