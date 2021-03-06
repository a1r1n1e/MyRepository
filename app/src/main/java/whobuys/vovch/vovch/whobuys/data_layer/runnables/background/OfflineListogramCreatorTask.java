package whobuys.vovch.vovch.whobuys.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_layer.DataBaseTask2;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.OfflineGetterTaskDE;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.OfflineListogramCreatorTaskDE;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;

public class OfflineListogramCreatorTask implements Runnable {
    private ActiveActivityProvider provider;
    private Item[] items;
    private int incomingActivityType;
    private String listName;

    public OfflineListogramCreatorTask(Item[] items, int incomingActivityType, ActiveActivityProvider provider, String listName) {
        this.provider = provider;
        this.items = items;
        this.incomingActivityType = incomingActivityType;
        this.listName = listName;
    }


    @Override
    public void run() {
        try {

            SList list = null;
            DataBaseTask2 addTask = new DataBaseTask2(provider);
            list = addTask.addList(items, "t", listName);

            Handler handler = new Handler(Looper.getMainLooper());
            OfflineListogramCreatorTaskDE runnable = new OfflineListogramCreatorTaskDE(list, incomingActivityType, provider);
            handler.post(runnable);

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineListogramCreatorTask");
        }
    }
}
