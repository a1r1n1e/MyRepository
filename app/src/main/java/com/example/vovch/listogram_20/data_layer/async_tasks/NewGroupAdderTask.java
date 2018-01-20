package com.example.vovch.listogram_20.data_layer.async_tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 07.01.2018.
 */

public class NewGroupAdderTask extends AsyncTask<String, Void, UserGroup> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public UserGroup doInBackground(String... loginPair) {
        UserGroup result = null;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.newGroupAdding(loginPair[0]);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(result != null){
            activeActivityProvider.showNewGroupAddedGood(result);
        }
        else{
            activeActivityProvider.showNewGroupAddedBad(result);
        }
    }
}
