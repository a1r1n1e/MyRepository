package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import com.example.vovch.listogram_20.R;

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
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        result = activeActivityProvider.dataExchanger.updateGroups();
        informers = activeActivityProvider.dataExchanger.createListinformers();
        return informers;
    }
    @Override
    public void onPostExecute(ListInformer[] result){
        if(result == null){
            if(activeActivityProvider.userSessionData.isLoginned()) {
                activeActivityProvider.showListInformersGottenBad();
            } else{
                activeActivityProvider.badLoginTry(activeActivityProvider.getString(R.string.log_yourself_in));
            }
        }  else {
            activeActivityProvider.showListInformersGottenGood(result);
        }
    }
}
