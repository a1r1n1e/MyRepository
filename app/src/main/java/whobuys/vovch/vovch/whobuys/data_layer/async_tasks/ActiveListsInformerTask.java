package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.ListInformer;

/**
 * Created by vovch on 07.01.2018.
 */

public class ActiveListsInformerTask extends AsyncTask <Object, Void, ListInformer[]> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public ListInformer[] doInBackground(Object... loginPair){
        ListInformer[] result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.getListInformers((String) loginPair[0]);
        return result;
    }
    @Override
    public void onPostExecute(ListInformer[] result){
        if(result == null){
            activeActivityProvider.showListInformersGottenBad();
        }  else {
            activeActivityProvider.showListInformersGottenGood(result);
        }
    }
}
