package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;

/**
 * Created by vovch on 06.01.2018.
 */

public class GroupDataSetterTask extends AsyncTask <String, Void, Boolean> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String groupId;
    private String groupName;

    public void setApplicationContext(Context ctf) {
        applicationContext = ctf;
    }

    @Override
    public Boolean doInBackground(String... loginPair) {
        Boolean result = false;
        groupId = loginPair[0];
        groupName = loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.updateGroupData(groupId, groupName);
        return result;
    }

    @Override
    public void onPostExecute(Boolean result) {
        if (result) {
            activeActivityProvider.showGroupDataSettledGood(groupId);
        } else {
            activeActivityProvider.showGroupDataSettledBad(groupId);
        }
    }
}

