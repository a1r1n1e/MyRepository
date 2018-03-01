package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;

/**
 * Created by vovch on 21.02.2018.
 */

public class LogouterTask extends AsyncTask<Object, Void, String> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public String doInBackground(Object... loginPair) {
        String result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        result = activeActivityProvider.userSessionData.endSession();
        return result;
    }

    @Override
    public void onPostExecute(String result) {
        if(result != null && result.substring(0, 3).equals("200")) {
            activeActivityProvider.userSessionData.showExitGood();
        }
        else{
            activeActivityProvider.userSessionData.showExitBad();
        }
    }
}
