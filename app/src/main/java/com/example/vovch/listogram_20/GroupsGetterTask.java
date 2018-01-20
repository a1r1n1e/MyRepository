package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vovch on 06.01.2018.
 */

class GroupsGetterTask extends AsyncTask <String, Void, UserGroup[]> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String userId;
    private UserGroup[] groups;

    @Override
    protected UserGroup[] doInBackground(String... loginPair) {
        UserGroup[] result = null;
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        result = activeActivityProvider.dataExchanger.getGroupsFromWeb();
        return result;
    }

    @Override
    protected void onPostExecute(UserGroup[] result) {
        if(result != null) {
            activeActivityProvider.showGroupsGottenGood(result);
        }
        else{
            activeActivityProvider.showGroupsGottenBad(result);
        }
    }
    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
}
