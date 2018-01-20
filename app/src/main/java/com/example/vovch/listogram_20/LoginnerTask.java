package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vovch on 08.01.2018.
 */

class LoginnerTask extends AsyncTask <String, Void, String>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String userLogin;
    private String userPassword;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected String doInBackground(String... loginPair){
        String result;
        userLogin = loginPair[0];
        userPassword = loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.loginTry(loginPair[0], loginPair[1]);
        return result;
    }
    @Override
    protected void onPostExecute(String result){
        if(result.substring(0, 3).equals("200")){
            activeActivityProvider.userSessionData.checkUserData(result.substring(3), userLogin, userPassword);
            activeActivityProvider.goodLoginTry(result.substring(3));
        }  else {
            if(result.substring(0, 3).equals("403")){
                result = "No Such User";
            }
            else{
                result = "Something Went Wrong";
            }
            activeActivityProvider.badLoginTry(result);
        }
    }
}
