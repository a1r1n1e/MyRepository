package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageButton;

/**
 * Created by vovch on 05.01.2018.
 */

class OnlineDisactivateTask extends AsyncTask <SList, Void, SList>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private SList taskList;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected SList doInBackground(SList... loginPair){
        SList result;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        taskList = loginPair[0];
        result = activeActivityProvider.dataExchanger.disactivateOnlineList(taskList);
        return result;
    }
    @Override
    protected void onPostExecute(SList list){
        if (list != null) {
            activeActivityProvider.showOnlineDisactivateListGood(list);
        } else {
            activeActivityProvider.showOnlineDisactivateListBad(taskList);
        }
        taskList = null;
    }
}
