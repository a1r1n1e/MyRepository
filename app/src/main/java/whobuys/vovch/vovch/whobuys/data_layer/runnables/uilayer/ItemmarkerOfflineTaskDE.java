package whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer;

import android.os.Looper;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class ItemmarkerOfflineTaskDE implements Runnable {
    public Item item;
    public ActiveActivityProvider provider;

    public ItemmarkerOfflineTaskDE(Item item, ActiveActivityProvider provider){
        this.item = item;
        this.provider = provider;
    }

    @Override
    public void run() {
        try{
            if (item != null) {
                provider.dataExchanger.itemmarkOfflineNew(item);
                provider.showOfflineActiveListsItemmarkedGood(item);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ItemmarkerOnlineTaskDE");
        }
    }
}
