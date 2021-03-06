package whobuys.vovch.vovch.whobuys.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_layer.DataBaseTask2;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.RedactOfflineListogramTaskDE;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;

public class RedactOfflineListogramTask implements Runnable {
    private ActiveActivityProvider provider;
    private int activityType;
    private Item[] items;
    private SList list;
    private String listName;

    public RedactOfflineListogramTask(SList list, int activityType, Item[] items, ActiveActivityProvider provider, String listName) {
        this.provider = provider;
        this.list = list;
        this.items = items;
        this.activityType = activityType;
        this.listName = listName;
    }


    @Override
    public void run() {
        try {
            SList resultList = null;
            if (list != null && items != null) {
                DataBaseTask2 dataBaseTask2 = new DataBaseTask2(provider);
                resultList = dataBaseTask2.redactOfflineList(list, items, listName);

                Handler handler = new Handler(Looper.getMainLooper());
                RedactOfflineListogramTaskDE runnable = new RedactOfflineListogramTaskDE(resultList, activityType, items, provider);
                handler.post(runnable);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "RedactOfflineListogramTask");
        }
    }
}
