package whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer;

import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.SList;

public class OfflineGetterTaskDE implements Runnable {
    private ActiveActivityProvider provider;
    private SList[] activeLists;
    private SList[] historyLists;

    public OfflineGetterTaskDE(SList[] activeLists, SList[] historyLists, ActiveActivityProvider provider) {
        this.activeLists = activeLists;
        this.historyLists = historyLists;
        this.provider = provider;
    }


    @Override
    public void run() {
        try {

            provider.dataExchanger.setOfflineData(activeLists, historyLists);

            if (activeLists == null || activeLists.length == 0) {
                provider.showOfflineActiveListsBad(activeLists);
            } else {
                provider.showOfflineActiveListsGood(activeLists);
            }

            if (historyLists == null || historyLists.length == 0) {
                provider.showOfflineHistoryListsBad(historyLists);
            } else {
                provider.showOfflineHistoryListsGood(historyLists);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTaskDE");
        }
    }
}

