package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;

/**
 * Created by vovch on 09.01.2018.
 */

public class RegistrationTask extends AsyncTask<String, Void, String> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String userLogin;
    private String userPassword;

    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public String doInBackground(String... loginPair){
        String result;
        userLogin = loginPair[0];
        userPassword = loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.registrationTry(loginPair[0], loginPair[1]);
        return result;
    }
    @Override
    public void onPostExecute(String result){
        if(result.substring(0, 3).equals("200")){
            activeActivityProvider.goodRegistrationTry(result.substring(3));
            activeActivityProvider.userSessionData.checkUserData(result.substring(3), userLogin, userPassword);
        }  else {
            if(result.substring(0, 3).equals("403")){
                result = "Username Is Already Used";
            }
            else{
                result = "Something Went Wrong";
            }
            activeActivityProvider.badRegistrationTry(result);
        }
    }
}
