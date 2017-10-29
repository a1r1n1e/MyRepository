package com.example.vovch.listogram_20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends WithLoginActivity {
    public  MainActivity.LoginnerTask lTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View.OnClickListener NewListenner1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id == R.id.button1){
                    EditText editText1 = (EditText)findViewById(R.id.edittext1);
                    EditText editText2 = (EditText)findViewById(R.id.edittext2);
                    String uName = editText1.getText().toString();
                    String uPassword = editText2.getText().toString();
                    lTask = new LoginnerTask(uName, uPassword, "login");
                    lTask.work();
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
    private void finisher(){
        this.finish();
    }
    @Override
    public WithLoginActivity.FirstLoginAttemptTask getFirstLoginAttemptTask(){
        return lTask;
    }
    protected class LoginnerTask extends FirstLoginAttemptTask{
        LoginnerTask(String username, String userpassword, String action){
            super(username, userpassword, action);
        }
        @Override
        protected void onGoodResult(String result){
            //saveLoginPair(result);
            Intent intent = new Intent(MainActivity.this, ActiveListsActivity.class);
            intent.putExtra("userId", result);
            startActivity(intent);
            finisher();
        }
        @Override
        protected void onBedResult(String result){
            TextView tView = (TextView)findViewById(R.id.textview1);
            tView.setText(result);
        }
    }
}
