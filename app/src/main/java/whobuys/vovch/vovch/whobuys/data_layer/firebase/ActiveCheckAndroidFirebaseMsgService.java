package whobuys.vovch.vovch.whobuys.data_layer.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.complex.ActiveListsActivity;

import com.example.vovch.listogram_20.R;
import whobuys.vovch.vovch.whobuys.data_types.ListInformer;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ActiveCheckAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";

    public ActiveCheckAndroidFirebaseMsgService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage.getData().get("type").equals("newlistogram") || remoteMessage.getData().get("type").equals("groupcontentchange") || remoteMessage.getData().get("type").equals("listogramdeleted")) {
                updateActivities(remoteMessage.getData().get("group"), remoteMessage.getData().get("message"), remoteMessage.getData().get("type"));
            }
        } catch (Exception e) {

        }
    }

    private void updateActivities(String groupListAddedToId, String message, String type) {
        try {
            ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
            if (provider.getActiveActivityNumber() != -1) {
                activeUpdate();
                groupUpdate(groupListAddedToId);
            } else if (type.equals("newlistogram")) {
                addNotification(message);
            } else if (type.equals("listogramdeleted")) {
                checkAndRemoveNotification();
            }
        } catch (Exception e) {

        }
    }

    private void addNotification(String message) {
        try {
            Intent intent = new Intent(this, ActiveListsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_test)
                    .setContentTitle("WhoBuys")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(notificationSoundURI)
                    .setContentIntent(resultIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, mNotificationBuilder.build());
        } catch (Exception e) {

        }
    }

    private void checkAndRemoveNotification() {
        try {
            ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
            if (provider.dataExchanger != null && provider.userSessionData != null) {
                if (provider.userSessionData.getId() != null) {
                    ListInformer[] informers = provider.dataExchanger.getListInformers(provider.userSessionData.getId());
                    if (informers == null || informers.length == 0) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private void groupUpdate(String groupId) {
        try {
            final ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
            if (provider.getActiveGroup() != null) {
                if (provider.getActiveGroup().getId().equals(groupId)) {
                    Handler mainHandler = new Handler(provider.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (provider.getActiveGroup() != null) {
                                provider.getGroupActiveLists(provider.getActiveGroup());
                                provider.getGroupHistoryLists(provider.getActiveGroup());
                            }
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            }
        } catch (Exception e) {

        }
    }

    private void activeUpdate() {
        try {
            final ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
            Handler mainHandler = new Handler(provider.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    provider.getActiveActivityActiveLists();
                }
            };
            mainHandler.post(myRunnable);
        } catch (Exception e) {

        }
    }
}
