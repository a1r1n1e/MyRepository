package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.R;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.ListInformer;
import whobuys.vovch.vovch.light.data_types.UserGroup;

public class GroupsUpdateTask extends AsyncTask<Object, ListInformer[], ListInformer[]> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public ListInformer[] doInBackground(Object... loginPair) {
        UserGroup[] result;
        ListInformer[] informers;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];

        result = activeActivityProvider.dataExchanger.getGroupsFromHardMemory();
        informers = activeActivityProvider.dataExchanger.createListinformers();

        publishProgress(informers);

        result = activeActivityProvider.dataExchanger.updateGroups();
        informers = activeActivityProvider.dataExchanger.createListinformers();

        return informers;
    }

    @Override
    protected void onProgressUpdate(ListInformer[]... progress) {
        super.onProgressUpdate();
        presenter(progress[0], false);
    }

    @Override
    public void onPostExecute(ListInformer[] result) {
        presenter(result, true);
    }

    private void presenter(ListInformer[] result, boolean isUpdateComplete){
        if (result == null) {
            if (activeActivityProvider.userSessionData.isLoginned()) {
                activeActivityProvider.showListInformersGottenBad();
            } else {
                activeActivityProvider.badLoginTry(activeActivityProvider.getString(R.string.log_yourself_in));
            }
        } else {
            activeActivityProvider.showListInformersGottenGood(result, isUpdateComplete);
        }
    }
}
