package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.Item;

/**
 * Created by vovch on 06.01.2018.
 */

public class OnlineItemmarkTask extends AsyncTask <Object, Item, Item>{
    private ActiveActivityProvider activeActivityProvider;
    private Item markedItem;

    @Override
    public Item doInBackground(Object... loginPair) {
        Item result = null;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        markedItem = (Item) loginPair[0];
        if (markedItem != null){
            publishProgress(markedItem);
            result = activeActivityProvider.dataExchanger.itemmarkOnline(markedItem);
        }
        return result;
    }
    @Override
    public void onProgressUpdate(Item... progress) {
        activeActivityProvider.showItemmarkProcessingToUser(progress[0]);
    }
    @Override
    public void onPostExecute(Item result){
        if(result != null){
            activeActivityProvider.showOnlineItemmarkedGood(result);
        }
        else{
            activeActivityProvider.showOnlineItemmarkedBad(markedItem);
        }
        markedItem = null;
    }
}
