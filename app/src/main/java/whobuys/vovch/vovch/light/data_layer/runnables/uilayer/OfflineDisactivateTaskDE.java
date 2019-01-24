package whobuys.vovch.vovch.light.data_layer.runnables.uilayer;

import android.util.Log;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.SList;

public class OfflineDisactivateTaskDE implements Runnable {
    private ActiveActivityProvider provider;
    private SList list;

    public OfflineDisactivateTaskDE(SList list, ActiveActivityProvider provider) {
        this.list = list;
        this.provider = provider;
    }


    @Override
    public void run() {
        try {

            list.setState(false);
            SList result = provider.dataExchanger.disactivateOfflineList(list);

            if (result != null) {
                provider.showOfflineActiveListsDisactivatedGood(result);
            } else {
                provider.showOfflineActiveListsDisactivatedBad(list);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTaskDE");
        }
    }
}
