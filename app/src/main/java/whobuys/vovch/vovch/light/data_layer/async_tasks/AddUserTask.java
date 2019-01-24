package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.AddingUser;

/**
 * Created by vovch on 07.01.2018.
 */

public class AddUserTask extends AsyncTask <Object, Void, AddingUser>{
    private ActiveActivityProvider activeActivityProvider;
    private String activityType;
    @Override
    public AddingUser doInBackground(Object... loginPair) {
        AddingUser result = null;
        String checkingUserId = (String) loginPair[0];
        activityType = (String) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.dataExchanger.addUser(checkingUserId);
        return result;
    }
    @Override
    public void onPostExecute(AddingUser result){
        if(activityType.equals("NewGroup")) {
            if (result != null) {
                activeActivityProvider.showCheckUserNewGroupGood(result);
            } else {
                activeActivityProvider.showCheckUserNewGroupBad(null);
            }
        }
        else if(activityType.equals("GroupSettingsActivity")){
            if (result != null) {
                activeActivityProvider.showCheckUserSettingsGood(result);
            } else {
                activeActivityProvider.showCheckUserSettingsBad(null);
            }
        }
    }
}
