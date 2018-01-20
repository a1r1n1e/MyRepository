package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.Item;

/**
 * Created by vovch on 24.12.2017.
 */

public class OfflineItemmarkTask extends AsyncTask <Item, Void, Item>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public Item doInBackground(Item... loginPair){
        Item result;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.itemmarkOffline(loginPair[0]);
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

