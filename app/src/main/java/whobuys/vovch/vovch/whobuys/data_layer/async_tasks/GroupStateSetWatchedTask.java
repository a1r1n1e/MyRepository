package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class GroupStateSetWatchedTask extends AsyncTask<Object, Void, Void> {

    @Override
    public Void doInBackground(Object... loginPair) {
        UserGroup group = (UserGroup) loginPair[0];
        ActiveActivityProvider activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        activeActivityProvider.dataExchanger.setGroupStateToWatched(group);
        return null;
    }
}
