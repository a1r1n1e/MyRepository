package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 14.12.2017.
 */

public class OfflineCreateListTask extends AsyncTask<Object, Void, SList> {
    private ActiveActivityProvider activeActivityProvider;
    private WithLoginActivity incomingActivity;

    @Override
    public SList doInBackground(Object... loginPair){
        Item[] items = (Item[]) loginPair[0];
        incomingActivity = (WithLoginActivity) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        SList list;

        list = activeActivityProvider.dataExchanger.addOfflineList(items);

        return list;
    }
    @Override
    public void onPostExecute(SList result){
        if(result != null){
            activeActivityProvider.showOfflineListCreatedGood(result, incomingActivity);
        }
        else{
            activeActivityProvider.showOfflineListCreatedBad(result, incomingActivity);
        }
        activeActivityProvider = null;
        incomingActivity = null;
    }
}
