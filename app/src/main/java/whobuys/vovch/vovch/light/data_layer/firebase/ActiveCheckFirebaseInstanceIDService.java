package whobuys.vovch.vovch.light.data_layer.firebase;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vovch on 30.10.2017.
 */

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;


public class ActiveCheckFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
        provider.userSessionData.setToken(refreshedToken);

        try {
            TelephonyManager tMgr = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
            if(tMgr != null) {
                String mPhoneNumber = tMgr.getDeviceId();

                String result = provider.userSessionData.startSession(mPhoneNumber, mPhoneNumber);
                if (result != null && result.length() >= 3) {
                    if (!result.substring(0, 3).equals("200")) {
                        provider.userSessionData.register(mPhoneNumber);
                    }
                    provider.updateAllGroups();
                } else{
                    provider.userSessionData.register(mPhoneNumber);
                }
            }
        } catch (SecurityException e){
            Log.d("auchan_test", "phone permission");
        }
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        //provider.userSessionData.registerForPushes();
    }
}
