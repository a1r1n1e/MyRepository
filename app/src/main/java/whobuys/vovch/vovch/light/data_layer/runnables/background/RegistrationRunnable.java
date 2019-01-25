package whobuys.vovch.vovch.light.data_layer.runnables.background;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_layer.runnables.uilayer.RegistrationTaskUI;

public class RegistrationRunnable implements Runnable {
    public ActiveActivityProvider provider;

    public RegistrationRunnable(ActiveActivityProvider provider){
        this.provider = provider;
    }

    @Override
    public void run() {
        try {
            TelephonyManager tMgr = (TelephonyManager) provider.getSystemService(Context.TELEPHONY_SERVICE);
            if(tMgr != null) {
                String mPhoneNumber = tMgr.getDeviceId();

                String result = provider.userSessionData.startSession(mPhoneNumber, mPhoneNumber);
                if (result != null && result.length() >= 3) {
                    if (!result.substring(0, 3).equals("200")) {
                        provider.userSessionData.register(mPhoneNumber);
                    }

                    RegistrationTaskUI runnable = new RegistrationTaskUI(provider);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(runnable);

                }
            }
        } catch (SecurityException e){
            Log.d("auchan_test", "phone permission");
        }
    }
}

