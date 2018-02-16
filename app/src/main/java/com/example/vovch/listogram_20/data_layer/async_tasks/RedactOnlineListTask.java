package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 16.02.2018.
 */

public class RedactOnlineListTask extends AsyncTask<Object, Void, SList> {
    private ActiveActivityProvider activeActivityProvider;
    private WithLoginActivity activity;

    @Override
    public SList doInBackground(Object... loginPair){
        SList list = (SList) loginPair[0];
        Item[] items = (Item[]) loginPair[1];
        activity = (WithLoginActivity) loginPair[2];
        activeActivityProvider = (ActiveActivityProvider) loginPair[3];
        list = activeActivityProvider.dataExchanger.redactOnlineList(list, items);
        return list;
    }
    @Override
    public void onPostExecute(SList result) {
        if (result != null) {
            activeActivityProvider.showOnlineListRedactedGood(result, activity);
        } else {
            activeActivityProvider.showOnlineListRedactedBad(result, activity);
        }
        activity = null;
        activeActivityProvider = null;
    }
}
