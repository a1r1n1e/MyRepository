package whobuys.vovch.vovch.light.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_layer.DataBaseTask2;
import whobuys.vovch.vovch.light.data_layer.runnables.uilayer.OfflineDisactivateTaskDE;
import whobuys.vovch.vovch.light.data_types.SList;

public class OfflineDisactivateTask implements Runnable {
    private ActiveActivityProvider provider;
    private SList list;

    public OfflineDisactivateTask(SList list, ActiveActivityProvider provider) {
        this.provider = provider;
        this.list = list;
    }


    @Override
    public void run() {
        try {
            if(list != null) {
                DataBaseTask2 memoryTask = new DataBaseTask2(provider);
                memoryTask.disactivateOfflineList(String.valueOf(list.getId()));

                Handler handler = new Handler(Looper.getMainLooper());
                OfflineDisactivateTaskDE runnable = new OfflineDisactivateTaskDE(list, provider);
                handler.post(runnable);
            }
        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineListogramCreatorTask");
        }
    }
}
