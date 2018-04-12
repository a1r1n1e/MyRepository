package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

/**
 * Created by vovch on 06.01.2018.
 */

public class GroupsGetterTask extends AsyncTask <Object, Void, UserGroup[]> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public UserGroup[] doInBackground(Object... loginPair) {
        UserGroup[] result = null;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        result = activeActivityProvider.dataExchanger.getGroupsFromWeb();
        return result;
    }

    @Override
    public void onPostExecute(UserGroup[] result) {
        if(result != null) {
            activeActivityProvider.showGroupsGottenGood(result);
        }
        else{
            activeActivityProvider.showGroupsGottenBad();
        }
        activeActivityProvider = null;
    }
}
