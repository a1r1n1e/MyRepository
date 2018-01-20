package com.example.vovch.listogram_20.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 03.01.2018.
 */

public class GroupHistoryGetterTask extends AsyncTask<String, Void, SList[]> {
        private Context applicationContext;
        private ActiveActivityProvider activeActivityProvider;
        private String groupId;

        public void setApplicationContext(Context ctf){
        applicationContext = ctf;
        }
        @Override
        public SList[] doInBackground(String... loginPair){
                SList[] result;
                groupId = loginPair[0];
                activeActivityProvider = (ActiveActivityProvider) applicationContext;
                result = activeActivityProvider.dataExchanger.getGroupHistoryData(groupId);
                return result;
        }
        @Override
        public void onPostExecute(SList[] result) {
                if (result == null || result.length == 0) {
                        activeActivityProvider.showGroupHistoryListsBad(null, groupId);
                } else {
                        activeActivityProvider.showGroupHistoryListsGood(result, groupId);
                }
        }
}
