package whobuys.vovch.vovch.light.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.light.ActiveActivityProvider;

/**
 * Created by Asus on 16.03.2018.
 */

public class DropHistoryTask extends AsyncTask<Object, Void, Boolean> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Boolean doInBackground(Object... loginPair){
        Boolean result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        result = activeActivityProvider.dataExchanger.dropHistory();

        return result;
    }
    @Override
    public void onPostExecute(Boolean result){
        if(result){
            activeActivityProvider.showDropHistoryGood();
        }
        else{
            activeActivityProvider.showDropHistoryBad();
        }
        activeActivityProvider = null;
    }
}
