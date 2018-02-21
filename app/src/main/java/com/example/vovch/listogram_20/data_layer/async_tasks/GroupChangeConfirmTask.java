package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 18.01.2018.
 */

public class GroupChangeConfirmTask extends AsyncTask<Object, Void, UserGroup> {
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;
    private String newGroupName;

    @Override
    public UserGroup doInBackground(Object... loginPair) {
        UserGroup result = null;
        group = (UserGroup) loginPair[0];
        newGroupName = (String) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.dataExchanger.confirmGroupChanges(group, newGroupName);
        return result;
    }

    @Override
    public void onPostExecute(UserGroup result) {
        if (result != null) {
            activeActivityProvider.showGroupSettingsChangeGood(result);
        } else {
            activeActivityProvider.showGroupSettingsChangeBad(group);
        }
        activeActivityProvider = null;
    }
}
