package com.example.vovch.listogram_20;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class CurrentActivityProvider extends Service {
    private Context mainActivity;
    private Context activeListsActivity;
    private Context group2Activity;
    private Context groupList2Activity;
    private Context newGroupActivity;
    public CurrentActivityProvider() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
