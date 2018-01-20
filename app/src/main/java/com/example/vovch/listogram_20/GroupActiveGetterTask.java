package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vovch on 05.01.2018.
 */

class GroupActiveGetterTask extends AsyncTask <String, Void, SList[]> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String groupId;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected SList[] doInBackground(String... loginPair){
        SList[] result;
        groupId = loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.getGroupActiveData(groupId);
        return result;
    }
    @Override
    protected void onPostExecute(SList[] result){
        if(result == null || result.length == 0){
            activeActivityProvider.showGroupActiveListsBad(result, groupId);
        }  else {
            activeActivityProvider.showGroupActiveListsGood(result, groupId);
        }
    }
}
