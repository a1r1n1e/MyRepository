package whobuys.vovch.vovch.light.data_layer.runnables.uilayer;

import android.util.Log;

import whobuys.vovch.vovch.light.ActiveActivityProvider;

public class RegistrationTaskUI implements Runnable {
    public ActiveActivityProvider provider;

    public RegistrationTaskUI(ActiveActivityProvider provider){
        this.provider = provider;
    }

    @Override
    public void run() {
        try {
            provider.updateAllGroups();
        } catch (SecurityException e){
            Log.d("auchan_test", "phone permission");
        }
    }
}
