package whobuys.vovch.vovch.whobuys.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_layer.DataBaseTask2;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.OfflineGetterTaskDE;
import whobuys.vovch.vovch.whobuys.data_types.SList;

public class OfflineGetterTask implements Runnable {
    private ActiveActivityProvider provider;

    public OfflineGetterTask(ActiveActivityProvider provider) {
        this.provider = provider;
    }


    @Override
    public void run() {
        try {

            SList[] activeLists = null;
            DataBaseTask2 task1 = new DataBaseTask2(provider);
            activeLists = task1.getOffline(1);

            SList[] historyLists = null;
            DataBaseTask2 task2 = new DataBaseTask2(provider);
            historyLists = task2.getOffline(0);

            Handler handler = new Handler(Looper.getMainLooper());
            OfflineGetterTaskDE runnable = new OfflineGetterTaskDE(activeLists, historyLists, provider);
            handler.post(runnable);

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTask");
        }
    }
}
