package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.simple.CreateListogramActivity;
import com.example.vovch.listogram_20.activities.simple.GroupList2Activity;
import com.example.vovch.listogram_20.data_layer.WebCall;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.UserGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by vovch on 03.01.2018.
 */

public class OnlineCreateListogramTask extends AsyncTask <Object, Void, UserGroup>{
    private ActiveActivityProvider activeActivityProvider;
    private WithLoginActivity activity;

    @Override
    public UserGroup doInBackground(Object... loginPair){
        UserGroup result;
        Item [] items = (Item[]) loginPair[0];
        UserGroup group = (UserGroup) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        activity = (WithLoginActivity) loginPair[3];
        result = activeActivityProvider.dataExchanger.addOnlineList(items, group);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(activity instanceof  CreateListogramActivity) {
            if (result != null) {
                activeActivityProvider.showOnlineListogramCreatedGood();
                activeActivityProvider.dataExchanger.clearTempItems();
            } else {
                activeActivityProvider.showOnlineListogramCreatedBad();
            }
        } else if(activity instanceof  GroupList2Activity){
            if(result != null){
                activeActivityProvider.resendListToGroupGood(result);
            }
            else{
                activeActivityProvider.resendListToGroupBad(null);
            }
        }
    }
}
