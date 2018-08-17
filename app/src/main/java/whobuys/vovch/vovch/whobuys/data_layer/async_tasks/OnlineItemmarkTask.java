package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.TempItem;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

/**
 * Created by vovch on 06.01.2018.
 */

public class OnlineItemmarkTask extends AsyncTask <Object, Item, UserGroup>{
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;
    private Item markedItem;

    @Override
    public UserGroup doInBackground(Object... loginPair) {
        UserGroup result = null;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        markedItem = (Item) loginPair[0];
        if(markedItem != null && markedItem.getList() != null && markedItem.getList().getGroup() != null){
            group = markedItem.getList().getGroup();
        }
        if (markedItem != null){
            publishProgress(markedItem);
            result = activeActivityProvider.dataExchanger.itemmarkOnline(markedItem);
        }
        return result;
    }
    @Override
    public void onProgressUpdate(Item... progress) {
        activeActivityProvider.showItemmarkProcessingToUser(progress[0]);
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(result != null){
            if(result.equals(group)){
                activeActivityProvider.showOnlineItemmarkedGoodLight(markedItem);
            }
            activeActivityProvider.showOnlineItemmarkedGood(result);
        }
        else{
            activeActivityProvider.showOnlineItemmarkedBad(group);
        }
        group = null;
        activeActivityProvider = null;
    }
}
