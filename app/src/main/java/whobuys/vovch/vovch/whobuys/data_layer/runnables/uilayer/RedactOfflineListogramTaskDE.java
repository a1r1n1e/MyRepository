package whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer;

import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;

public class RedactOfflineListogramTaskDE implements Runnable {
    private ActiveActivityProvider provider;
    private SList list;
    protected Item[] items;
    private int activityType;

    public RedactOfflineListogramTaskDE(SList list, int activityType, Item[] items, ActiveActivityProvider provider) {
        this.provider = provider;
        this.list = list;
        this.items = items;
        this.activityType = activityType;
    }


    @Override
    public void run() {
        try {

            if(list != null && items != null) {
                provider.dataExchanger.redactOfflineList(list);
            }

            if (list != null) {
                provider.showOfflineListRedactedGood(list, activityType);
            } else {
                provider.showOfflineListRedactedBad(null, activityType);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTaskDE");

            provider.showOfflineListRedactedBad(null, activityType);

        }
    }
}
