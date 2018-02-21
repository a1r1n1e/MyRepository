package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;

/**
 * Created by vovch on 08.01.2018.
 */

public class LoginnerTask extends AsyncTask<Object, Void, String> {
    private ActiveActivityProvider activeActivityProvider;
    private String userLogin;
    private String userPassword;

    @Override
    public String doInBackground(Object... loginPair) {
        String result;
        userLogin = (String) loginPair[0];
        userPassword = (String) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.userSessionData.startSession(userLogin, userPassword);
        return result;
    }

    @Override
    public void onPostExecute(String result) {
        if (result != null && result.substring(0, 3).equals("200")) {
            activeActivityProvider.goodLoginTry(result.substring(3));
        } else {
            if (result != null && result.substring(0, 3).equals("403")) {
                result = "No Such User";
            } else {
                result = "Logged Off";
            }
            activeActivityProvider.badLoginTry(result);
        }
        activeActivityProvider = null;
    }
}
