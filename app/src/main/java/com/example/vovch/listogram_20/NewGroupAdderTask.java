package com.example.vovch.listogram_20;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

/**
 * Created by vovch on 07.01.2018.
 */

public class NewGroupAdderTask extends AsyncTask<String, Void, UserGroup> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected UserGroup doInBackground(String... loginPair) {
        UserGroup result = null;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.newGroupAdding(loginPair[0]);
        return result;
    }
    @Override
    protected void onPostExecute(UserGroup result){
        if(result != null){
            activeActivityProvider.showNewGroupAddedGood(result);
        }
        else{
            activeActivityProvider.showNewGroupAddedBad(result);
        }
    }
}
