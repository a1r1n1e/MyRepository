package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;

/**
 * Created by vovch on 14.12.2017.
 */

public class OfflineCreateListTask extends AsyncTask<Object, Void, SList> {
    private ActiveActivityProvider activeActivityProvider;
    private WithLoginActivity incomingActivity;

    @Override
    public SList doInBackground(Object... loginPair){
        Item[] items = (Item[]) loginPair[0];
        incomingActivity = (WithLoginActivity) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        SList list;

        list = activeActivityProvider.dataExchanger.addOfflineList(items);

        return list;
    }
    @Override
    public void onPostExecute(SList result){
        if(result != null){
            activeActivityProvider.showOfflineListCreatedGood(result, incomingActivity);
        }
        else{
            activeActivityProvider.showOfflineListCreatedBad(null, incomingActivity);
        }
        activeActivityProvider = null;
        incomingActivity = null;
    }
}
