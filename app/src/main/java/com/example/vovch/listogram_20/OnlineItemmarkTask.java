package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

/**
 * Created by vovch on 06.01.2018.
 */

class OnlineItemmarkTask extends AsyncTask <Item, Item, Item>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private Item markedItem;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected Item doInBackground(Item... loginPair) {
        Item result = null;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        markedItem = loginPair[0];
        if (markedItem != null){
            publishProgress(markedItem);
            result = activeActivityProvider.dataExchanger.itemmarkOnline(markedItem);
        }
        return result;
    }
    @Override
    protected void onProgressUpdate(Item... progress) {
        activeActivityProvider.showItemmarkProcessingToUser(progress[0]);
    }
    @Override
    protected void onPostExecute(Item result){
        if(result != null){
            activeActivityProvider.showOnlineItemmarkedGood(result);
        }
        else{
            activeActivityProvider.showOnlineItemmarkedBad(markedItem);
        }
        markedItem = null;
    }
}
