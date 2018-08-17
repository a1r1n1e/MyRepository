package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_layer.DataBaseTask2;

/**
 * Created by vovch on 21.02.2018.
 */

public class LogouterTask extends AsyncTask<Object, Void, String> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public String doInBackground(Object... loginPair) {
        String result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        result = activeActivityProvider.userSessionData.endSession();

        DataBaseTask2 dataBaseTask2 = new DataBaseTask2(activeActivityProvider);
        dataBaseTask2.dropAllGroups();

        return result;
    }

    @Override
    public void onPostExecute(String result) {
        if(result != null && result.substring(0, 3).equals("200")) {
            activeActivityProvider.userSessionData.showExitGood();
        }
        else{
            activeActivityProvider.userSessionData.showExitBad();
        }
        activeActivityProvider = null;
    }
}
