package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 14.12.2017.
 */

public class OfflineCreateListTask extends AsyncTask<Item[], Void, SList> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public SList doInBackground(Item[]... loginPair){
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        SList list;

        list = activeActivityProvider.dataExchanger.addOfflineList(loginPair[0]);

        return list;
    }
    @Override
    public void onPostExecute(SList result){
        if(result != null){
            activeActivityProvider.showOfflineListCreatedGood(result);
        }
        else{
            activeActivityProvider.showOfflineListCreatedBad(result);
        }
    }
}
