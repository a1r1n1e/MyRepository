package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 24.12.2017.
 */

public class NewDataBaseTask extends AsyncTask<String, Void, SList[]> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private boolean type = false;

    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public SList[] doInBackground(String... loginPair){
        type = Boolean.parseBoolean(loginPair[0]);
        SList[] result;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        if(type) {
            result = activeActivityProvider.dataExchanger.getOfflineActiveData();
        }
        else {
            result = activeActivityProvider.dataExchanger.getOfflineHistoryData();
        }

        return result;
    }
    @Override
    public void onPostExecute(SList[] result){
            if (result == null || result.length == 0) {
                if (type) {
                    activeActivityProvider.showOfflineActiveListsBad(null);
                } else {
                    activeActivityProvider.showOfflineHistoryListsBad(null);
                }
            } else {
                if (type) {
                    activeActivityProvider.showOfflineActiveListsGood(result);
                } else {
                    activeActivityProvider.showOfflineHistoryListsGood(result);
                }
            }
    }
}
