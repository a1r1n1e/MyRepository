package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.ListInformer;

/**
 * Created by vovch on 07.01.2018.
 */

public class ActiveListsInformerTask extends AsyncTask <Object, Void, ListInformer[]> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public ListInformer[] doInBackground(Object... loginPair){
        ListInformer[] result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.getListInformers((String) loginPair[0]);
        return result;
    }
    @Override
    public void onPostExecute(ListInformer[] result){
        if(result == null || result.length == 0){
            activeActivityProvider.showListInformersGottenBad();
        }  else {
            activeActivityProvider.showListInformersGottenGood(result);
        }
    }
}
