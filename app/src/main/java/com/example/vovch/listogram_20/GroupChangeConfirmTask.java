package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vovch on 18.01.2018.
 */

class GroupChangeConfirmTask extends AsyncTask<UserGroup, Void, UserGroup> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;
    protected void setApplicationContext(Context ctf) {
        applicationContext = ctf;
    }

    @Override
    protected UserGroup doInBackground(UserGroup... loginPair) {
        UserGroup result = null;
        group = loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.confirmGroupChanges(group);
        return result;
    }

    @Override
    protected void onPostExecute(UserGroup result) {
        if (result != null) {
            activeActivityProvider.showGroupSettingsChangeGood(group);
        } else {
            activeActivityProvider.showGroupSettingsChangeBad(group);
        }
    }
}
