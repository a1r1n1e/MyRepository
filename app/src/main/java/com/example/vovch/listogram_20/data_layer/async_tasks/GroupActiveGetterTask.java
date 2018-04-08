package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 05.01.2018.
 */

public class GroupActiveGetterTask extends AsyncTask <Object, Void, SList[]> {
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;

    @Override
    public SList[] doInBackground(Object... loginPair){
        SList[] result;
        group = (UserGroup) loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.getGroupActiveData(group);
        return result;
    }
    @Override
    public void onPostExecute(SList[] result){
        if(result == null || result.length == 0){
            activeActivityProvider.showGroupActiveListsBad(group.getId());
        }  else {
            activeActivityProvider.showGroupActiveListsGood(result, group.getId());
        }
    }
}
