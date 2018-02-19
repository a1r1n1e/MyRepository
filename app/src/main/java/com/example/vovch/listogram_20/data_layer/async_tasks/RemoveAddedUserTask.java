package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.simple.GroupSettingsActivity;
import com.example.vovch.listogram_20.activities.simple.NewGroup;
import com.example.vovch.listogram_20.data_types.AddingUser;

/**
 * Created by vovch on 07.01.2018.
 */

public class RemoveAddedUserTask extends AsyncTask <Object, Void, AddingUser>{
    private ActiveActivityProvider activeActivityProvider;
    private WithLoginActivity activity;
    @Override
    public AddingUser doInBackground(Object... loginPair) {
        AddingUser result = null;
        if(loginPair[0] != null && loginPair[1] != null && loginPair[2] != null) {
            result = (AddingUser) loginPair[0];
            activity = (WithLoginActivity) loginPair[1];
            activeActivityProvider = (ActiveActivityProvider) loginPair[2];
            result = activeActivityProvider.dataExchanger.removeUser(result);
        }
        return result;
    }
    @Override
    public void onPostExecute(AddingUser result){
        if(activity != null) {
            if (!(activity instanceof GroupSettingsActivity) && (activity instanceof NewGroup)) {
                if (result != null) {
                    activeActivityProvider.showRemoveAddedUserNewGroupGood(result);
                } else {
                    activeActivityProvider.showRemoveAddedUserNewGroupBad(null);
                }
            } else if (activity instanceof GroupSettingsActivity) {
                if (result != null) {
                    activeActivityProvider.showRemoveAddedUserSettingsGood(result);
                } else {
                    activeActivityProvider.showRemoveAddedUserSettingsBad(null);
                }
            }
        }
        activeActivityProvider = null;
        activity = null;
    }
}
