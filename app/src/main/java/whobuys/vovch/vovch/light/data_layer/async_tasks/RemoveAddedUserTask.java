package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.AddingUser;

/**
 * Created by vovch on 07.01.2018.
 */

public class RemoveAddedUserTask extends AsyncTask <Object, Void, AddingUser>{
    private ActiveActivityProvider activeActivityProvider;
    private int activityType;
    @Override
    public AddingUser doInBackground(Object... loginPair) {
        AddingUser result = null;
        if(loginPair[0] != null && loginPair[1] != null && loginPair[2] != null) {
            result = (AddingUser) loginPair[0];
            activityType = (Integer) loginPair[1];
            activeActivityProvider = (ActiveActivityProvider) loginPair[2];
            result = activeActivityProvider.dataExchanger.removeUser(result);
        }
        return result;
    }
    @Override
    public void onPostExecute(AddingUser result){
        if (activityType == 5) {
            if (result != null) {
                activeActivityProvider.showRemoveAddedUserNewGroupGood(result);
            } else {
                activeActivityProvider.showRemoveAddedUserNewGroupBad(null);
            }
        } else if (activityType == 7) {
            if (result != null) {
                activeActivityProvider.showRemoveAddedUserSettingsGood(result);
            } else {
                activeActivityProvider.showRemoveAddedUserSettingsBad(null);
            }
        }
        activeActivityProvider = null;
    }
}
