package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.Item;
import whobuys.vovch.vovch.light.data_types.SList;
import whobuys.vovch.vovch.light.data_types.UserGroup;

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
        String listName = (String) loginPair[4];
        result = activeActivityProvider.dataExchanger.redactOnlineList(list, items, listName);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result) {
        if (result != null) {
            activeActivityProvider.showOnlineListogramCreatedGood(result);
            activeActivityProvider.dataExchanger.clearTempItems();
        } else {
            //activeActivityProvider.showOnlineListRedactedBad(null, activityType);     TODO
        }
        activeActivityProvider = null;
    }
}
