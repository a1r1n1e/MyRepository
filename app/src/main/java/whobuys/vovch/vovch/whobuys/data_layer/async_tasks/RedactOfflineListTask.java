package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;

/**
 * Created by vovch on 15.02.2018.
 */

public class RedactOfflineListTask extends AsyncTask<Object, Void, SList> {
    private ActiveActivityProvider activeActivityProvider;
    private int activityType;

@Override
    public SList doInBackground(Object... loginPair){
        SList list = (SList) loginPair[0];
        Item[] items = (Item[]) loginPair[1];
        activityType = (Integer) loginPair[2];
        activeActivityProvider = (ActiveActivityProvider) loginPair[3];
        list = activeActivityProvider.dataExchanger.redactOfflineList(list, items);
        return list;
    }
@Override
    public void onPostExecute(SList result) {
        if (result != null) {
            activeActivityProvider.showOfflineListRedactedGood(result, activityType);
        } else {
            activeActivityProvider.showOfflineListRedactedBad(null, activityType);
        }
        activeActivityProvider = null;
    }
}
