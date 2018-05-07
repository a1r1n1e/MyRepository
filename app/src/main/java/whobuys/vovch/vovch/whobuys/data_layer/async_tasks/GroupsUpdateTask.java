package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import java.util.List;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.ListInformer;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class GroupsUpdateTask extends AsyncTask<Object, Void, ListInformer[]> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public ListInformer[] doInBackground(Object... loginPair){
        UserGroup[] result;
        ListInformer[] informers;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.updateGroups();
        informers = activeActivityProvider.dataExchanger.createListinformers();
        return informers;
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
