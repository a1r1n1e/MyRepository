package whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer;

import android.os.Looper;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class ItemmarkerOnlineTaskDE implements Runnable {
    public Item item;
    public UserGroup group;
    public ActiveActivityProvider provider;
    private String resultString;

    public ItemmarkerOnlineTaskDE(UserGroup group, Item item, ActiveActivityProvider provider, String resultString){
        this.group = group;
        this.item = item;
        this.provider = provider;
        this.resultString = resultString;
    }

    @Override
    public void run() {
        try{
            if (resultString.substring(0, 3).equals("205")) {
                provider.dataExchanger.itemmarkOnlineOff(item, group);
            } else if (resultString.substring(0, 3).equals("200")) {
                provider.dataExchanger.itemmarkOnlineOn(item, resultString, group);
            } else{

                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                ErrorItemmarkOnlinePublisherRunnable showerRunnable = new ErrorItemmarkOnlinePublisherRunnable(group);
                handler.post(showerRunnable);

            }
        } catch (Exception e){
            Log.d("WhoBuys", "ItemmarkerOnlineTaskDE");
        }
    }
    public class ErrorItemmarkOnlinePublisherRunnable implements Runnable{
        private  UserGroup group;

        ErrorItemmarkOnlinePublisherRunnable(UserGroup group){
            this.group = group;
        }

        @Override
        public void run() {
            provider.showOnlineItemmarkedBad(group);
        }
    }
}
