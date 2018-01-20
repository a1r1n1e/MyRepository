package com.example.vovch.listogram_20.data_layer.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.data_types.ListInformer;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ActiveCheckAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    public ActiveCheckAndroidFirebaseMsgService() {
        super();
    }
    @Override
    public  void onMessageReceived(RemoteMessage remoteMessage){
        String value = remoteMessage.toString();
        if(remoteMessage.getData().get("type").equals("newlistogram") || remoteMessage.getData().get("type").equals("groupcontentchange")) {
            updateActivities(remoteMessage.getData().get("group"), remoteMessage.getData().get("message"));
        }
        else if(remoteMessage.getData().get("type").equals("listogramdeleted")){
            checkAndRemoveNotification();
        }
    }
    private void updateActivities(String groupListAddedToId, String message) {

        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();

        if(provider.getActiveActivityNumber() == 2){                                //провайдер устроен так, что если есть номер, то и контекст есть тоже
            activeUpdate();
        }
        else if(provider.getActiveActivityNumber() == 3){
            groupUpdate(groupListAddedToId);
        } else if(provider.getActiveActivityNumber() == -1){
            addNotification(message);
        }
    }
    private void addNotification(String message){
        Intent intent = new Intent(this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Something")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
    private void checkAndRemoveNotification(){
        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
        if(provider.dataExchanger != null && provider.userSessionData != null) {
            if(provider.userSessionData.getId() != null) {
                ListInformer[] informers = provider.dataExchanger.getListInformers(provider.userSessionData.getId());
                if(informers == null || informers.length == 0){
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                }
            }
        }
    }
    private void groupUpdate(String groupId){
        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
        if(provider.getActiveActivity() != null && provider.getActiveActivityNumber() == 3){
            final Group2Activity groupContext = (Group2Activity) provider.getActiveActivity();
            if(provider.getActiveGroup().getId().equals(groupId)) {
                Handler mainHandler = new Handler(groupContext.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {groupContext.refreshActiveLists();}
                };
                mainHandler.post(myRunnable);
            }
        }
    }
    private void activeUpdate(){
        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
        if(provider.getActiveActivityNumber() == 2){
            final ActiveListsActivity activeActivity = (ActiveListsActivity) provider.getActiveActivity();
            Handler mainHandler = new Handler(activeActivity.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {activeActivity.update();}
            };
            mainHandler.post(myRunnable);
        }
    }
}
