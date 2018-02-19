package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.Item;

/**
 * Created by vovch on 24.12.2017.
 */

public class OfflineItemmarkTask extends AsyncTask <Object, Void, Item>{
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Item doInBackground(Object... loginPair){
        Item result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.itemmarkOffline((Item) loginPair[0]);
        return result;
    }
    @Override
    public void onPostExecute(Item item){
        if (item != null) {
            activeActivityProvider.showOfflineActiveListsItemmarkedGood(item);
        } else {
            activeActivityProvider.showOfflineActiveListsItemmarkedBad(null);
        }
    }
}

