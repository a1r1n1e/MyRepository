package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.SList;

/**
 * Created by vovch on 24.12.2017.
 */

public class DisactivateOfflineTask extends AsyncTask<Object, Void, SList>{
    private ActiveActivityProvider activeActivityProvider;
    private SList tempList;
    @Override
    public SList doInBackground(Object... loginPair){
        SList result;
        tempList = (SList) loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.disactivateOfflineList(tempList);
        return result;
    }
    @Override
    public void onPostExecute(SList list){
        if (list != null) {
            activeActivityProvider.showOfflineActiveListsDisactivatedGood(list);
        } else {
            activeActivityProvider.showOfflineActiveListsDisactivatedBad(tempList);
        }if (list != null) {
            activeActivityProvider.showOfflineActiveListsDisactivatedGood(list);
        } else {
            activeActivityProvider.showOfflineActiveListsDisactivatedBad(tempList);
        }
        tempList = null;
        activeActivityProvider = null;
    }
}
