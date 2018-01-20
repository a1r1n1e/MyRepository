package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 24.12.2017.
 */

public class OfflineDisactivateTask extends AsyncTask<SList, Void, SList>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private SList tempList;
    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public SList doInBackground(SList... loginPair){
        SList result;
        tempList = loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.disactivateOfflineList(tempList);
        return result;
    }
    @Override
    public void onPostExecute(SList list){
        if (list != null) {
            activeActivityProvider.showOfflineActiveListsDisactivatedGood(list);
        } else {
            activeActivityProvider.showOfflineActiveListsDisactivatedBad(tempList);
        }
        tempList = null;
    }
}
