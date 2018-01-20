package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vovch on 07.01.2018.
 */

class ActiveListsInformerTask extends AsyncTask <String, Void, ListInformer[]> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected ListInformer[] doInBackground(String... loginPair){
        ListInformer[] result;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.getListInformers(loginPair[0]);
        return result;
    }
    @Override
    protected void onPostExecute(ListInformer[] result){
        if(result == null || result.length == 0){
            activeActivityProvider.showListInformersGottenBad(result);
        }  else {
            activeActivityProvider.showListInformersGottenGood(result);
        }
    }
}
