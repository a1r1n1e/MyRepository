package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;

/**
 * Created by vovch on 21.02.2018.
 */

public class SessionCheckerTask extends AsyncTask<Object, Void, Boolean> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Boolean doInBackground(Object... loginPair) {
        Boolean result = false;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        if(activeActivityProvider.userSessionData.isSession()){
            result = activeActivityProvider.userSessionData.checkSession();
        }
        return result;
    }

    @Override
    public void onPostExecute(Boolean result) {
        if(result){
            activeActivityProvider.goodSessionCheck();
        } else{
            activeActivityProvider.badSessionCheck();
        }
        activeActivityProvider = null;
    }
}
