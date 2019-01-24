package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.Item;

/**
 * Created by vovch on 24.12.2017.
 */

public class OfflineItemmarkTask extends AsyncTask <Object, Void, Item>{
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Item doInBackground(Object... loginPair){
        Item result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.itemmarkOffline((Item) loginPair[0]);
        return result;
    }
    @Override
    public void onPostExecute(Item item){
        if (item != null) {
            activeActivityProvider.showOfflineActiveListsItemmarkedGood(item);
        } else {
            activeActivityProvider.showOfflineActiveListsItemmarkedBad(null);
        }
        activeActivityProvider = null;
    }
}

