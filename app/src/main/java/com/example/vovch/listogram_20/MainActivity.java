package com.example.vovch.listogram_20;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends WithLoginActivity {
    public  MainActivity.LoginnerTask lTask;
    public static final String APP_PREFERENCES = "autentification";
    private static final String  APP_PREFERENCES_TOKEN= "token";
    private static final String  APP_PREFERENCES_USERID= "userid";
    private static final String APP_PREFERENCES_LOGIN = "login";
    private static final String APP_PREFERENCES_PASSWORD = "password";
    private SharedPreferences preferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentOne = new Intent(MainActivity.this, ActiveCheckAndroidFirebaseMsgService.class);
        Intent intentTwo = new Intent(MainActivity.this, ActiveCheckFirebaseInstanceIDService.class);
        startService(intentOne);
        startService(intentTwo);

        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(preferences.getString(APP_PREFERENCES_PASSWORD, null) != null && preferences.getString(APP_PREFERENCES_LOGIN, null) != null && preferences.getString(APP_PREFERENCES_TOKEN, null) != null){
            lTask = new LoginnerTask(preferences.getString(APP_PREFERENCES_LOGIN, null), preferences.getString(APP_PREFERENCES_PASSWORD, null), preferences.getString(APP_PREFERENCES_TOKEN, null), "login");
            lTask.work();
        }
            setContentView(R.layout.activity_main);
            View.OnClickListener NewListenner1 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    TextView errorTextView = (TextView) findViewById(R.id.textview1);
                    errorTextView.setText("");
                    token = preferences.getString(APP_PREFERENCES_TOKEN, null);
                    if(id == R.id.button1 && token != null){
                        EditText editText1 = (EditText)findViewById(R.id.edittext1);
                        EditText editText2 = (EditText)findViewById(R.id.edittext2);
                        String uName = editText1.getText().toString();
                        String uPassword = editText2.getText().toString();
                        if(!uName.equals("") && !uPassword.equals("")) {
                            lTask = new LoginnerTask(uName, uPassword, token, "login");
                            lTask.work();
                        }
                        else{
                            errorTextView.setText("Enter Missing Value");
                        }
                    }
                    else if(token == null){
                        errorTextView.setText("There is some problem with registration token of your device. Try to reinstall the app");
                    }
                }
            };
            View.OnClickListener NewListenner2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if(id == R.id.button2){
                        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                    }
                }
            };
            Button Btn1 = (Button) findViewById(R.id.button1);
            Btn1.setOnClickListener(NewListenner1);
            Button Btn2 = (Button) findViewById(R.id.button2);
            Btn2.setOnClickListener(NewListenner2);
    }
    @Override
    protected  void onPause(){
        super.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    private void finisher(){
        this.finish();
    }
    @Override
    public WithLoginActivity.FirstLoginAttemptTask getFirstLoginAttemptTask(){
        return lTask;
    }
    protected class LoginnerTask extends FirstLoginAttemptTask{
        String userName;
        String userPasssword;
        LoginnerTask(String username, String userpassword, String token, String action){
            super(username, userpassword, token, action, "1");
            userName = username;
            userPasssword = userpassword;
        }
        @Override
        protected void onGoodResult(String result){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_PREFERENCES_LOGIN, userName);
            editor.putString(APP_PREFERENCES_PASSWORD, userPasssword);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, ActiveListsActivity.class);
            intent.putExtra("userId", result);
            startActivity(intent);
            finisher();
        }
        @Override
        protected void onBedResult(String result){
            TextView tView = (TextView)findViewById(R.id.textview1);
            tView.setText(result.substring(1));
        }
    }
}
