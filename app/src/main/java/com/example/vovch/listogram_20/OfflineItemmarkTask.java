package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by vovch on 24.12.2017.
 */

class OfflineItemmarkTask extends AsyncTask <Item, Void, Item>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected Item doInBackground(Item... loginPair){
        Item result;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.itemmarkOffline(loginPair[0]);
        return result;
    }
    @Override
    protected void onPostExecute(Item item){
        if (item != null) {
            activeActivityProvider.showOfflineActiveListsItemmarkedGood(item);
        } else {
            activeActivityProvider.showOfflineActiveListsItemmarkedBad(null);
        }
    }
}

