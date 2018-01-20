package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vovch on 18.01.2018.
 */

class GroupLeaverTask extends AsyncTask<UserGroup, Void, UserGroup> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;

    @Override
    protected UserGroup doInBackground(UserGroup... loginPair) {
        UserGroup result = null;
        group = loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.leaveGroup(group);
        return result;
    }

    @Override
    protected void onPostExecute(UserGroup result) {
        if(result == null){
            activeActivityProvider.leaveGroupBad(group);
        }
        else{
            activeActivityProvider.leaveGroupGood(result);
        }
    }
    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
}

