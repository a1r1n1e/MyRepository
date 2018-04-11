package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.GroupSettingsActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.NewGroup;
import whobuys.vovch.vovch.whobuys.data_types.AddingUser;

/**
 * Created by vovch on 07.01.2018.
 */

public class RemoveAddedUserTask extends AsyncTask <Object, Void, AddingUser>{
    private ActiveActivityProvider activeActivityProvider;
    private WithLoginActivity activity;
    @Override
    public AddingUser doInBackground(Object... loginPair) {
        AddingUser result = null;
        if(loginPair[0] != null && loginPair[1] != null && loginPair[2] != null) {
            result = (AddingUser) loginPair[0];
            activity = (WithLoginActivity) loginPair[1];
            activeActivityProvider = (ActiveActivityProvider) loginPair[2];
            result = activeActivityProvider.dataExchanger.removeUser(result);
        }
        return result;
    }
    @Override
    public void onPostExecute(AddingUser result){
        if(activity != null) {
            if (!(activity instanceof GroupSettingsActivity) && (activity instanceof NewGroup)) {
                if (result != null) {
                    activeActivityProvider.showRemoveAddedUserNewGroupGood(result);
                } else {
                    activeActivityProvider.showRemoveAddedUserNewGroupBad(null);
                }
            } else if (activity instanceof GroupSettingsActivity) {
                if (result != null) {
                    activeActivityProvider.showRemoveAddedUserSettingsGood(result);
                } else {
                    activeActivityProvider.showRemoveAddedUserSettingsBad(null);
                }
            }
        }
        activeActivityProvider = null;
        activity = null;
    }
}
