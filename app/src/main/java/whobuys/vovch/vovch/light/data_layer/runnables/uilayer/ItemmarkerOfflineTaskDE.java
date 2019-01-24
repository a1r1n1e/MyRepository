package whobuys.vovch.vovch.light.data_layer.runnables.uilayer;

import android.util.Log;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.Item;

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
