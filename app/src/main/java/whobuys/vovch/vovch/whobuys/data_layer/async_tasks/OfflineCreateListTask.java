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
    private int incomingActivityType;

    @Override
    public SList doInBackground(Object... loginPair){
        Item[] items = (Item[]) loginPair[0];
        incomingActivityType = (Integer) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        SList list = null;

        //list = activeActivityProvider.dataExchanger.addOfflineList(items);

        return list;
    }
    @Override
    public void onPostExecute(SList result){
        if(result != null){
            activeActivityProvider.showOfflineListCreatedGood(result, incomingActivityType);
        }
        else{
            activeActivityProvider.showOfflineListCreatedBad(null, incomingActivityType);
        }
        activeActivityProvider = null;
    }
}
