package whobuys.vovch.vovch.light.data_layer.runnables.uilayer;

import android.util.Log;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.SList;

public class OfflineListogramCreatorTaskDE implements Runnable {
    private ActiveActivityProvider provider;
    private SList list;
    private int incomingActivityType;

    public OfflineListogramCreatorTaskDE(SList list, int incomingActivityType, ActiveActivityProvider provider) {
        this.list = list;
        this.provider = provider;
        this.incomingActivityType = incomingActivityType;
    }


    @Override
    public void run() {
        try {

            if(list != null) {
                list = provider.dataExchanger.addOfflineList(list);
            }
            if(list != null){
                provider.showOfflineListCreatedGood(list, incomingActivityType);
            }
            else{
                provider.showOfflineListCreatedBad(null, incomingActivityType);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTaskDE");
        }
    }
}
