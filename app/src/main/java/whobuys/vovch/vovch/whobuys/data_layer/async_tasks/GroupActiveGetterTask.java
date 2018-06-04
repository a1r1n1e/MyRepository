package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class GroupActiveGetterTask extends AsyncTask<Object, Void, SList[]> {
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;

    @Override
    public SList[] doInBackground(Object... loginPair) {
        SList[] result;
        group = (UserGroup) loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.getGroupActiveData(group);
        return result;
    }

    @Override
    public void onPostExecute(SList[] result) {
        if (result == null || result.length == 0) {
            activeActivityProvider.showGroupActiveListsBad(group.getId());
        } else {
            activeActivityProvider.showGroupActiveListsGood(result, group.getId());
        }
        activeActivityProvider = null;
    }
}
