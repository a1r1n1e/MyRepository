package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class UpdateOneGroupTask extends AsyncTask<Object, Void, UserGroup> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public UserGroup doInBackground(Object... loginPair) {

        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        String groupId = (String) loginPair[1];
        UserGroup result = activeActivityProvider.dataExchanger.updateGroupData(groupId);
       return result;
    }

    @Override
    public void onPostExecute(UserGroup result) {
        if(result != null){
            activeActivityProvider.showGroupChangeOutside(result);
        }
    }
}
