package whobuys.vovch.vovch.whobuys.data_layer.firebase;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vovch on 30.10.2017.
 */

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;


public class ActiveCheckFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
        provider.userSessionData.setToken(refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        //provider.userSessionData.registerForPushes();
    }
}
