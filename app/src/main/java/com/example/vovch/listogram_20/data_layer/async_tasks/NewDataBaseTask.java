package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 24.12.2017.
 */

public class NewDataBaseTask extends AsyncTask<Object, Void, SList[]> {
    private ActiveActivityProvider activeActivityProvider;
    private boolean type = false;

    @Override
    public SList[] doInBackground(Object... loginPair){
        type = (Boolean) loginPair[0];
        SList[] result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
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
            activeActivityProvider = null;
    }
}
