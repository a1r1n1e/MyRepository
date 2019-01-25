package whobuys.vovch.vovch.light.data_layer.runnables.uilayer;

import android.util.Log;

import whobuys.vovch.vovch.light.R;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.ListInformer;
import whobuys.vovch.vovch.light.data_types.UserGroup;

public class ListInformersCreatorTask implements Runnable {
    private UserGroup[] groups;
    private ActiveActivityProvider provider;
    private boolean isUpdateComplete;

    public ListInformersCreatorTask(UserGroup[] groups, ActiveActivityProvider provider, boolean isUpdateComplete){
        this.groups = groups;
        this.provider = provider;
        this. isUpdateComplete = isUpdateComplete;
    }

    @Override
    public void run() {
        try {
            provider.dataExchanger.setGroups(groups);
            if(groups.length > 0 && groups[0] != null) {
                provider.setActiveGroup(groups[0]);
            }
            ListInformer[] informers = provider.dataExchanger.createListinformers();
            presenter(informers, isUpdateComplete);
        } catch (Exception e){
            Log.d("WhoBuys","ListinformersCreatorTask");
        }
    }
    private void presenter(ListInformer[] result, boolean isUpdateComplete){
        if (result == null) {
            if (provider.userSessionData.isLoginned()) {
                provider.showListInformersGottenBad();
            } else {
                provider.badLoginTry(provider.getString(R.string.log_yourself_in));
            }
        } else {
            provider.showListInformersGottenGood(result, isUpdateComplete);
        }
    }
}
