package com.example.vovch.listogram_20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WithLoginActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "autentification";
    public static final String APP_PREFERENCES_LOGIN = "login";
    public static final String APP_PREFERENCES_PASSWORD = "password";
    public static final String APP_PREFERENCES_USERID = "userid";
    public  MainActivity.FirstLoginAttemptTask ITask;
    protected SharedPreferences loginPasswordPair;
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
        private Context cntxt;
        private Context rsltcntxt;
        FirstLoginAttemptTask(String uName, String uPassword, String action){
            userName = uName;
            userPassword = uPassword;
            userAction = action;
        }
        FirstLoginAttemptTask(String groupId, String action){
            userName = groupId;
            userPassword = " ";
            userAction = action;
        }
        /*protected void saveLoginPair(String id){
            loginPasswordPair = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = loginPasswordPair.edit();
            editor.putString(APP_PREFERENCES_LOGIN, userName);
            editor.putString(APP_PREFERENCES_PASSWORD, userPassword);
            editor.putString(APP_PREFERENCES_USERID, id);
            editor.apply();
        }*/
        protected void setContext(Context c){
            cntxt = c;
        }
        protected void setResultContext(Context cn){
            rsltcntxt = cn;
        }
        protected void onGoodResult(String result) {
                //saveLoginPair(result);
                Activity activity = (Activity) cntxt;
                Intent intent = new Intent(activity, GroupList2Activity.class);                                      //ЖИРНЫЙ КОСТЫЛЬ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                startActivity(intent);
        }
        protected void onBedResult(String result){

        }
        protected void work(){
            LoginToDatabase tDLogin = new LoginToDatabase();
            tDLogin.setContextFrom(WithLoginActivity.this);
            tDLogin.execute(userName, userPassword, userAction);
        }
    }
}
