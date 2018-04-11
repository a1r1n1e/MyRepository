package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;

/**
 * Created by vovch on 21.02.2018.
 */

public class SessionCheckerTask extends AsyncTask<Object, Void, Integer> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Integer doInBackground(Object... loginPair) {
        int result = 0;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        if(activeActivityProvider.userSessionData.isSession()){
            result = activeActivityProvider.userSessionData.checkSession();
        }
        return result;
    }

    @Override
    public void onPostExecute(Integer result) {
        if(result == 1){                                                            //if there is bad connection to internet we allow user to log in
            activeActivityProvider.goodSessionCheck();                              //in case he won't be able to do smth through web server with wrong session key
        } else if(result == 0){
            activeActivityProvider.badSessionCheck();
        } else if(result == 5){
            activeActivityProvider.goodSessionCheck();
            activeActivityProvider.activeListsNoInternet();
        }
        activeActivityProvider = null;
    }
}
