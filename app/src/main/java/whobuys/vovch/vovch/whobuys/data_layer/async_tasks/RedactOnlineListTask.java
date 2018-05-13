package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

/**
 * Created by vovch on 16.02.2018.
 */

public class RedactOnlineListTask extends AsyncTask<Object, Void, UserGroup> {
    private ActiveActivityProvider activeActivityProvider;
    private int activityType;

    @Override
    public UserGroup doInBackground(Object... loginPair){
        UserGroup result;
        SList list = (SList) loginPair[0];
        Item[] items = (Item[]) loginPair[1];
        activityType = (Integer) loginPair[2];
        activeActivityProvider = (ActiveActivityProvider) loginPair[3];
        result = activeActivityProvider.dataExchanger.redactOnlineList(list, items);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result) {
        if (result != null) {
            activeActivityProvider.showOnlineListRedactedGood(activityType, result);
        } else {
            activeActivityProvider.showOnlineListRedactedBad(null, activityType);
        }
        activeActivityProvider = null;
    }
}
