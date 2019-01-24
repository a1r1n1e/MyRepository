package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;

/**
 * Created by Asus on 14.03.2018.
 */

public class SendBugTask extends AsyncTask<Object, Void, Boolean> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Boolean doInBackground(Object... loginPair){
        Boolean result;
        String bug = (String) loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.sendBug(bug);
        return result;
    }
    @Override
    public void onPostExecute(Boolean result){
        if(result){
            activeActivityProvider.showSendBugGood();
        } else{
            activeActivityProvider.showSendBugBad();
        }
        activeActivityProvider = null;
    }
}
