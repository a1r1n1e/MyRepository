package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 18.01.2018.
 */

public class GroupChangeConfirmTask extends AsyncTask<UserGroup, Void, UserGroup> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;
    public void setApplicationContext(Context ctf) {
        applicationContext = ctf;
    }

    @Override
    public UserGroup doInBackground(UserGroup... loginPair) {
        UserGroup result = null;
        group = loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.confirmGroupChanges(group);
        return result;
    }

    @Override
    public void onPostExecute(UserGroup result) {
        if (result != null) {
            activeActivityProvider.showGroupSettingsChangeGood(group);
        } else {
            activeActivityProvider.showGroupSettingsChangeBad(group);
        }
    }
}
