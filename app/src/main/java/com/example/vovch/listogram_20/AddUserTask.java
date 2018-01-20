package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

/**
 * Created by vovch on 07.01.2018.
 */

class AddUserTask extends AsyncTask <String, Void, AddingUser>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String activityType;
    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected AddingUser doInBackground(String... loginPair) {
        AddingUser result = null;
        String checkingUserId = loginPair[0];
        activityType = loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.addUser(checkingUserId);
        return result;
    }
    @Override
    protected void onPostExecute(AddingUser result){
        if(activityType.equals("NewGroup")) {
            if (result != null) {
                activeActivityProvider.showCheckUserNewGroupGood(result);
            } else {
                activeActivityProvider.showCheckUserNewGroupBad(result);
            }
        }
        else if(activityType.equals("GroupSettingsActivity")){
            if (result != null) {
                activeActivityProvider.showCheckUserSettingsGood(result);
            } else {
                activeActivityProvider.showCheckUserSettingsBad(result);
            }
        }
    }
}
