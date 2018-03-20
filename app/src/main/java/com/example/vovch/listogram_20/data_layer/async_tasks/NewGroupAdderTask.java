package com.example.vovch.listogram_20.data_layer.async_tasks;


import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 07.01.2018.
 */

public class NewGroupAdderTask extends AsyncTask<Object, Void, UserGroup> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public UserGroup doInBackground(Object... loginPair) {
        UserGroup result = null;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        String groupName = (String) loginPair[0];
        result = activeActivityProvider.dataExchanger.newGroupAdding(groupName);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(result != null){
            activeActivityProvider.showNewGroupAddedGood(result);
        }
        else{
            activeActivityProvider.showNewGroupAddedBad(null);
        }
        activeActivityProvider = null;
    }
}
