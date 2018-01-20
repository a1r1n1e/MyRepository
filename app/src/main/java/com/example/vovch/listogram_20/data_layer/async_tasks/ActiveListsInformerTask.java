package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.ListInformer;

/**
 * Created by vovch on 07.01.2018.
 */

public class ActiveListsInformerTask extends AsyncTask <String, Void, ListInformer[]> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public ListInformer[] doInBackground(String... loginPair){
        ListInformer[] result;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.getListInformers(loginPair[0]);
        return result;
    }
    @Override
    public void onPostExecute(ListInformer[] result){
        if(result == null || result.length == 0){
            activeActivityProvider.showListInformersGottenBad(result);
        }  else {
            activeActivityProvider.showListInformersGottenGood(result);
        }
    }
}
