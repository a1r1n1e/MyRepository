package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by vovch on 03.01.2018.
 */

class OnlineCreateListogramTask extends AsyncTask <Item[], Void, String>{
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;
    private String groupId;
    private String userId;
    private Item[] items;

    @Override
    protected String doInBackground(Item[]... loginPair){
        String result = null;
        items = loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        int length = items.length;
        JSONArray itemsArray = new JSONArray();
        for(int i = 0; i < length; i++){
            JSONObject item = new JSONObject();
            try {
                item.put("item_name", items[i].getName());
                item.put("item_comment", items[i].getComment());
                itemsArray.put(item);
            }
            catch (JSONException e){
                                                                                                    //TODO
            }
        }

        String jsonString = itemsArray.toString();
        WebCall webCall = new WebCall();
        result = webCall.callServer(userId, jsonString, groupId, "sendlistogram", "6", "6", "6", "6");

        return result;
    }
    @Override
    protected void onPostExecute(String result){
        activeActivityProvider.showOnlineListogramCreatedGood();
        activeActivityProvider.dataExchanger.clearTempItems();
    }
    protected void setUserId(String newUserId){
        userId = newUserId;
    }
    protected void setGroupId(String newGroupId){
        groupId = newGroupId;
    }
    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
}
