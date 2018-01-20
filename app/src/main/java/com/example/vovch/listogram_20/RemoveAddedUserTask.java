package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

/**
 * Created by vovch on 07.01.2018.
 */

class RemoveAddedUserTask extends AsyncTask <AddingUser, Void, AddingUser>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String activityType;
    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected AddingUser doInBackground(AddingUser... loginPair) {
        AddingUser result = loginPair[0];
        if(loginPair[1] != null){
            activityType = "NewGroup";
        }
        else{
            activityType = "GroupSettingsActivity";
        }
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.removeUser(result);
        return result;
    }
    @Override
    protected void onPostExecute(AddingUser result){
        if(activityType.equals("NewGroup")) {
            if (result != null) {
                activeActivityProvider.showRemoveAddedUserNewGroupGood(result);
            } else {
                activeActivityProvider.showRemoveAddedUserNewGroupBad(result);
            }
        }
        else if(activityType.equals("GroupSettingsActivity")){
            if (result != null) {
                activeActivityProvider.showRemoveAddedUserSettingsGood(result);
            } else {
                activeActivityProvider.showRemoveAddedUserSettingsBad(result);
            }
        }
    }
}
