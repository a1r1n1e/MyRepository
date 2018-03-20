package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.AddingUser;

/**
 * Created by vovch on 07.01.2018.
 */

public class AddUserTask extends AsyncTask <String, Void, AddingUser>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String activityType;
    public void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    public AddingUser doInBackground(String... loginPair) {
        AddingUser result = null;
        String checkingUserId = loginPair[0];
        activityType = loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.addUser(checkingUserId);
        return result;
    }
    @Override
    public void onPostExecute(AddingUser result){
        if(activityType.equals("NewGroup")) {
            if (result != null) {
                activeActivityProvider.showCheckUserNewGroupGood(result);
            } else {
                activeActivityProvider.showCheckUserNewGroupBad(null);
            }
        }
        else if(activityType.equals("GroupSettingsActivity")){
            if (result != null) {
                activeActivityProvider.showCheckUserSettingsGood(result);
            } else {
                activeActivityProvider.showCheckUserSettingsBad(null);
            }
        }
    }
}
