package whobuys.vovch.vovch.whobuys.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_layer.DataBaseTask2;
import whobuys.vovch.vovch.whobuys.data_layer.DataExchanger;
import whobuys.vovch.vovch.whobuys.data_layer.WebCall;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.ItemmarkerOfflineTaskDE;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.ItemmarkerOnlineTaskDE;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class ItemmarkerOfflineTask implements Runnable {
    private ActiveActivityProvider provider;
    private Item item;

    public ItemmarkerOfflineTask(Item item, ActiveActivityProvider provider){
        this.provider = provider;
        this.item = item;
    }


    @Override
    public void run() {
        try {

            Handler handler = new Handler(Looper.getMainLooper());
            ItemmarkerOfflineTask.ProgressPublisherRunnable showerRunnable = new ItemmarkerOfflineTask.ProgressPublisherRunnable(item);
            handler.post(showerRunnable);

            DataBaseTask2 memoryTask = new DataBaseTask2(provider);
            memoryTask.itemMarkOffline(String.valueOf(item.getId()));

            ItemmarkerOfflineTaskDE runnable = new ItemmarkerOfflineTaskDE(item, provider);
            handler.post(runnable);

        } catch (Exception e){

            Log.d("WhoBuys", "ItemmarkerOnlineTask");

            Handler handler = new Handler(Looper.getMainLooper());
            ItemmarkerOfflineTask.ErrorItemmarkOfflinePublisherRunnable errorRunnable = new ItemmarkerOfflineTask.ErrorItemmarkOfflinePublisherRunnable(item);
            handler.post(errorRunnable);
        }
    }

    private class ProgressPublisherRunnable implements Runnable{
        private  Item item;

        ProgressPublisherRunnable(Item item){
            this.item = item;
        }

        @Override
        public void run() {
            provider.showItemmarkProcessingToUser(item);
        }
    }
    public class ErrorItemmarkOfflinePublisherRunnable implements Runnable{
        private Item item;

        public ErrorItemmarkOfflinePublisherRunnable(Item item){
            this.item = item;
        }

        @Override
        public void run() {
            provider.showOfflineActiveListsItemmarkedBad(item);
        }
    }
}
