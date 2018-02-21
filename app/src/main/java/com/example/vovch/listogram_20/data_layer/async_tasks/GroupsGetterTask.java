package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 06.01.2018.
 */

public class GroupsGetterTask extends AsyncTask <Object, Void, UserGroup[]> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public UserGroup[] doInBackground(Object... loginPair) {
        UserGroup[] result = null;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        result = activeActivityProvider.dataExchanger.getGroupsFromWeb();
        return result;
    }

    @Override
    public void onPostExecute(UserGroup[] result) {
        if(result != null) {
            activeActivityProvider.showGroupsGottenGood(result);
        }
        else{
            activeActivityProvider.showGroupsGottenBad(null);
        }
    }
}
