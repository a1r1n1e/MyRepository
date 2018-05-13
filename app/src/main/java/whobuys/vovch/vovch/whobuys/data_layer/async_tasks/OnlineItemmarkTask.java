package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

/**
 * Created by vovch on 06.01.2018.
 */

public class OnlineItemmarkTask extends AsyncTask <Object, Item, UserGroup>{
    private ActiveActivityProvider activeActivityProvider;
    private Item markedItem;

    @Override
    public UserGroup doInBackground(Object... loginPair) {
        UserGroup result = null;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        markedItem = (Item) loginPair[0];
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
            activeActivityProvider.showOnlineItemmarkedGood(result);
        }
        else{
            activeActivityProvider.showOnlineItemmarkedBad(result);
        }
        markedItem = null;
        activeActivityProvider = null;
    }
}
