package com.example.vovch.listogram_20;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ActiveCheckAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    public ActiveCheckAndroidFirebaseMsgService() {
        super();
    }
    @Override
    public  void onMessageReceived(RemoteMessage remoteMessage){
        //Log data to Log Cat
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
        String value = remoteMessage.toString();
        if(remoteMessage.getData().get("type").equals("newlistogram")) {
            createNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("group"));
        }
    }
    private void createNotification( String messageBody, String groupListAddedToId) {

        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();

        if(provider.getActiveActivityNumber() == 2){
            ActiveListsActivity activeActivity = (ActiveListsActivity) provider.getActiveActivity();
            activeActivity.update();
        }
        else if(provider.getActiveActivityNumber() == 3 && provider.getGroupId() == Integer.parseInt(groupListAddedToId)){
            Group2Activity groupActivity = (Group2Activity) provider.getActiveActivity();
            groupActivity.update();
        }

        Intent intent = new Intent( this , MainActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Something")
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
