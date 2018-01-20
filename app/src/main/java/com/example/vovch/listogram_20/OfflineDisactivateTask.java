package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageButton;

/**
 * Created by vovch on 24.12.2017.
 */

class OfflineDisactivateTask extends AsyncTask<SList, Void, SList>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private SList tempList;
    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected SList doInBackground(SList... loginPair){
        SList result;
        tempList = loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.disactivateOfflineList(tempList);
        return result;
    }
    @Override
    protected void onPostExecute(SList list){
        if (list != null) {
            activeActivityProvider.showOfflineActiveListsDisactivatedGood(list);
        } else {
            activeActivityProvider.showOfflineActiveListsDisactivatedBad(tempList);
        }
        tempList = null;
    }
}
