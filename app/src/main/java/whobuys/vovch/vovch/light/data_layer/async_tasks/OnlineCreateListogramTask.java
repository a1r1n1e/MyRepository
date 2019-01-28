package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.Item;
import whobuys.vovch.vovch.light.data_types.UserGroup;


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
        String storeName = (String) loginPair[4];
        String storeTime = (String) loginPair[5];
        result = activeActivityProvider.dataExchanger
                .addOnlineList(items, group, storeName, storeTime);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(activityType == 6) {
            if (result != null) {
                activeActivityProvider.showOnlineListogramCreatedGood(result);
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
        } else if(activityType == 2){
            if(result != null) {
                activeActivityProvider.showGroupChangeOutside(result);
            } else{
                                                                                    //TODO
            }
        }
        activeActivityProvider = null;
    }
}
