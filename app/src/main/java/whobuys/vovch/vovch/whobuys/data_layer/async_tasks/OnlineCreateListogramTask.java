package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.CreateListogramActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.GroupList2Activity;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;


/**
 * Created by vovch on 03.01.2018.
 */

public class OnlineCreateListogramTask extends AsyncTask <Object, Void, UserGroup>{
    private ActiveActivityProvider activeActivityProvider;
    private int activityType;

    @Override
    public UserGroup doInBackground(Object... loginPair){
        UserGroup result;
        Item[] items = (Item[]) loginPair[0];
        UserGroup group = (UserGroup) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        activityType = (Integer) loginPair[3];
        result = activeActivityProvider.dataExchanger.addOnlineList(items, group);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(activityType == 6) {
            if (result != null) {
                activeActivityProvider.showOnlineListogramCreatedGood();
                activeActivityProvider.dataExchanger.clearTempItems();
            } else {
                activeActivityProvider.showOnlineListogramCreatedBad();
            }
        } else if(activityType == 4){
            if(result != null){
                activeActivityProvider.resendListToGroupGood(result);
            }
            else{
                activeActivityProvider.resendListToGroupBad(null);
            }
        }
        activeActivityProvider = null;
    }
}
