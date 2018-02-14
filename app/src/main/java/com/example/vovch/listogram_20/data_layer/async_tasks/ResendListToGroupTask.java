package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 14.02.2018.
 */

public class ResendListToGroupTask extends AsyncTask<Object, Void, UserGroup> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public UserGroup doInBackground(Object... loginPair){
        UserGroup result = null;
        SList resendingList = (SList) loginPair[0];
        UserGroup group = (UserGroup) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.dataExchanger.resendListToGroup(resendingList, group);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(result != null){
            activeActivityProvider.resendListToGroupGood(result);
        }
        else{
            activeActivityProvider.resendListToGroupBad(null);
        }
    }
}
