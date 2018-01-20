package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vovch on 06.01.2018.
 */

class GroupDataSetterTask extends AsyncTask <String, Void, Boolean> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String groupId;
    private String groupName;

    protected void setApplicationContext(Context ctf) {
        applicationContext = ctf;
    }

    @Override
    protected Boolean doInBackground(String... loginPair) {
        Boolean result = false;
        groupId = loginPair[0];
        groupName = loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.updateGroupData(groupId, groupName);
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            activeActivityProvider.showGroupDataSettledGood(groupId);
        } else {
            activeActivityProvider.showGroupDataSettledBad(groupId);
        }
    }
}

