package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;

/**
 * Created by vovch on 06.01.2018.
 */

public class GroupDataSetterTask extends AsyncTask <Object, Void, Boolean> {
    private ActiveActivityProvider activeActivityProvider;
    private String groupId;


    @Override
    public Boolean doInBackground(Object... loginPair) {
        Boolean result = false;
        groupId = (String) loginPair[0];
        String groupName = (String) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.dataExchanger.updateGroupData(groupId, groupName);
        return result;
    }

    @Override
    public void onPostExecute(Boolean result) {
        if (result) {
            activeActivityProvider.showGroupDataSettledGood(groupId);
        } else {
            activeActivityProvider.showGroupDataSettledBad(groupId);
        }
        activeActivityProvider = null;
    }
}

