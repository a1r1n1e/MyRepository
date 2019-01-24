package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.SList;

/**
 * Created by vovch on 24.12.2017.
 */

public class NewDataBaseTask extends AsyncTask<Object, Void, SList[]> {
    private ActiveActivityProvider activeActivityProvider;
    private boolean type = false;

    @Override
    public SList[] doInBackground(Object... loginPair){
        type = (Boolean) loginPair[0];
        SList[] result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        if(type) {
            result = activeActivityProvider.dataExchanger.getOfflineActiveData();
        }
        else {
            result = activeActivityProvider.dataExchanger.getOfflineHistoryData();
        }

        return result;
    }
    @Override
    public void onPostExecute(SList[] result){
            if (result == null || result.length == 0) {
                if (type) {
                    activeActivityProvider.showOfflineActiveListsBad(result);
                } else {
                    activeActivityProvider.showOfflineHistoryListsBad(result);
                }
            } else {
                if (type) {
                    activeActivityProvider.showOfflineActiveListsGood(result);
                } else {
                    activeActivityProvider.showOfflineHistoryListsGood(result);
                }
            }
            activeActivityProvider = null;
    }
}
