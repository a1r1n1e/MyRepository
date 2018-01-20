package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.AddingUser;

/**
 * Created by vovch on 07.01.2018.
 */

public class RemoveAddedUserTask extends AsyncTask <AddingUser, Void, AddingUser>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String activityType;
    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public AddingUser doInBackground(AddingUser... loginPair) {
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
    public void onPostExecute(AddingUser result){
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
