package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 05.01.2018.
 */

public class OnlineDisactivateTask extends AsyncTask <SList, Void, SList>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private SList taskList;

    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public SList doInBackground(SList... loginPair){
        SList result;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        taskList = loginPair[0];
        result = activeActivityProvider.dataExchanger.disactivateOnlineList(taskList);
        return result;
    }
    @Override
    public void onPostExecute(SList list){
        if (list != null) {
            activeActivityProvider.showOnlineDisactivateListGood(list);
        } else {
            activeActivityProvider.showOnlineDisactivateListBad(taskList);
        }
        taskList = null;
    }
}
