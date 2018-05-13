package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.UserButton;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

/**
 * Created by vovch on 05.01.2018.
 */

public class OnlineDisactivateTask extends AsyncTask <Object, Void, UserGroup>{
    private ActiveActivityProvider activeActivityProvider;
    private SList taskList;

    @Override
    public UserGroup doInBackground(Object... loginPair){
        UserGroup result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        taskList = (SList) loginPair[0];
        result = activeActivityProvider.dataExchanger.disactivateOnlineList(taskList);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup group){
        if (group != null) {
            activeActivityProvider.showOnlineDisactivateListGood(group);
        } else {
            activeActivityProvider.showOnlineDisactivateListBad(taskList);
        }
        taskList = null;
        activeActivityProvider = null;
    }
}
