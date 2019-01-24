package whobuys.vovch.vovch.light.data_layer.runnables.uilayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import whobuys.vovch.vovch.light.R;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.activities.complex.ActiveListsActivity;
import whobuys.vovch.vovch.light.data_types.UserGroup;

public class NotificationsTask implements Runnable {
    private ActiveActivityProvider provider;
    private  String message;
    private boolean forceNeeded;

    public NotificationsTask(ActiveActivityProvider provider, String message, boolean forceNeeded){
        this.provider = provider;
        this.message = message;
        this.forceNeeded = forceNeeded;
    }

    @Override
    public void run() {
        try {
            if(!forceNeeded) {
                UserGroup[] groups = provider.dataExchanger.getGroupsFromRAM();
                boolean notificationsNeeded = false;
                for (UserGroup group : groups) {
                    if (group.getState().equals(UserGroup.DEFAULT_GROUP_STATE_UNWATCHED)/* && group.getActiveLists().length > 0*/) {
                        notificationsNeeded = true;
                    }
                }
                if (notificationsNeeded) {
                    addNotification(message);
                } else {
                    removeNotification();
                }
            } else{
                addNotification(message);
            }
        } catch (Exception e){
            Log.d("WhoBuys","NotificationsTask");
        }
    }
    private void addNotification(String message) {
        try {
            provider.setActiveListsActivityLoadType(0);
            Intent intent = new Intent(provider, ActiveListsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultIntent = PendingIntent.getActivity(provider, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder mNotificationBuilder = null;
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationManager notificationManager =
                        (NotificationManager) provider.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel("default",
                        "Channel name",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Channel description");

                if(notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);


                    mNotificationBuilder = new NotificationCompat.Builder(provider, "default")
                            .setSmallIcon(R.mipmap.ic_launcher_test)
                            .setContentTitle("WhoBuys")
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(notificationSoundURI)
                            .setContentIntent(resultIntent);
                }
            } else{
                mNotificationBuilder = new NotificationCompat.Builder(provider)
                        .setSmallIcon(R.mipmap.ic_launcher_test)
                        .setContentTitle("WhoBuys")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(notificationSoundURI)
                        .setContentIntent(resultIntent);
            }

            NotificationManager notificationManager =
                    (NotificationManager) provider.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null && mNotificationBuilder != null) {
                notificationManager.notify(0, mNotificationBuilder.build());
            }
        } catch (Exception e) {
            Log.v("WhoBuys", "smthin msg service");
        }
    }
    private void removeNotification() {
        try {
            if (provider.dataExchanger != null && provider.userSessionData != null) {
                if (provider.userSessionData.getId() != null) {
                    NotificationManager notificationManager = (NotificationManager) provider.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (notificationManager != null) {
                        notificationManager.cancelAll();
                    }
                }
            }
        } catch (Exception e) {
            Log.v("WhoBuys", "smthin msg service");
        }
    }
}
