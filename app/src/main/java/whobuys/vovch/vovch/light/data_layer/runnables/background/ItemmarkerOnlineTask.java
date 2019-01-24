package whobuys.vovch.vovch.light.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_layer.DataExchanger;
import whobuys.vovch.vovch.light.data_layer.WebCall;
import whobuys.vovch.vovch.light.data_layer.runnables.uilayer.ItemmarkerOnlineTaskDE;
import whobuys.vovch.vovch.light.data_types.Item;
import whobuys.vovch.vovch.light.data_types.UserGroup;

public class ItemmarkerOnlineTask implements Runnable {
    private UserGroup group;
    private ActiveActivityProvider provider;
    private Item item;

    public ItemmarkerOnlineTask(ActiveActivityProvider provider, Item item){
        this.provider = provider;
        this.item = item;
    }


    @Override
    public void run() {
        try {

            Handler handler = new Handler(Looper.getMainLooper());

            if(item != null && item.getList() != null && item.getList().getGroup() != null){
                group = item.getList().getGroup();
            }
            if (item != null){

                ProgressPublisherRunnable showerRunnable = new ProgressPublisherRunnable(item);
                handler.post(showerRunnable);

                if (item != null && item.getList() != null && item.getList().getGroup() != null) {

                    WebCall webCall = new WebCall();
                    String userId = String.valueOf(provider.userSessionData.getId());
                    String itemId = String.valueOf(item.getId());
                    String listId = String.valueOf(item.getList().getId());
                    String groupId = group.getId();
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(0, userId);
                    jsonArray.put(1, groupId);
                    jsonArray.put(2, listId);
                    jsonArray.put(3, itemId);
                    String jsonString = jsonArray.toString();

                    String resultString = webCall.callServer( userId, DataExchanger.BLANK_WEBCALL_FIELD,
                                                                        DataExchanger.BLANK_WEBCALL_FIELD, "itemmark",
                                                                        jsonString, provider.userSessionData);

                    if (resultString != null && resultString.length() > 2) {

                        ItemmarkerOnlineTaskDE checkerRunnuble = new ItemmarkerOnlineTaskDE(group, item, provider, resultString);
                        handler.post(checkerRunnuble);

                    }
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ItemmarkerOnlineTask");
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
}
