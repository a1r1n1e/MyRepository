package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;

public class DBSynchronizerTask extends AsyncTask<Object, Void, Boolean> {

    @Override
    public Boolean doInBackground(Object... loginPair){
        Boolean result;
        ActiveActivityProvider activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        result = activeActivityProvider.dataExchanger.synchronizeDB();
        return result;
    }
    @Override
    public void onPostExecute(Boolean result) {
    }
}
