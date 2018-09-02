package whobuys.vovch.vovch.whobuys.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;

public class NetworkReciever extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr != null) {
            NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                    mobile != null && mobile.isConnectedOrConnecting();
            if (isConnected) {
                ActiveActivityProvider provider = (ActiveActivityProvider) context.getApplicationContext();
                provider.updateAllGroups();
            } else {
                Log.d("Network Available ", "NO");
            }
        }
    }
}
