package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

/**
 * Created by vovch on 18.01.2018.
 */

public class GroupLeaverTask extends AsyncTask<Object, Void, UserGroup> {
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;

    @Override
    public UserGroup doInBackground(Object... loginPair) {
        UserGroup result = null;
        group = (UserGroup) loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.leaveGroup(group);
        return result;
    }

    @Override
    public void onPostExecute(UserGroup result) {
        if(result == null){
            activeActivityProvider.leaveGroupBad(group);
        }
        else{
            activeActivityProvider.leaveGroupGood(result);
        }
        activeActivityProvider = null;
    }
}

