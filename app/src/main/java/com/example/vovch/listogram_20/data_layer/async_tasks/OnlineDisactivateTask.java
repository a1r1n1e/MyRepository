package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 05.01.2018.
 */

public class OnlineDisactivateTask extends AsyncTask <Object, Void, SList>{
    private ActiveActivityProvider activeActivityProvider;
    private SList taskList;

    @Override
    public SList doInBackground(Object... loginPair){
        SList result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        taskList = (SList) loginPair[0];
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
