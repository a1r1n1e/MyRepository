package com.example.vovch.listogram_20;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CurrentActivityProvider extends Service {
    private Context mainActivity;
    private Context activeListsActivity;
    private Context group2Activity;
    private Context groupList2Activity;
    private Context newGroupActivity;
    private Context createListogramActivity;
    MBinder binder = new MBinder();

    public CurrentActivityProvider(){
        super.onCreate();
        mainActivity = null;
        activeListsActivity = null;
        group2Activity = null;
        groupList2Activity = null;
        newGroupActivity = null;
        createListogramActivity = null;
    }
    protected void updateActivityContext(int whichOne, Context itsContext){
        switch (whichOne) {
            case 1:
                mainActivity = itsContext;
                break;
            case 2:
                activeListsActivity = itsContext;
                break;
            case 3:
                group2Activity = itsContext;
                break;
            case 4:
                groupList2Activity = itsContext;
                break;
            case 5:
                newGroupActivity = itsContext;
                break;
            case 6:
                createListogramActivity = itsContext;
                break;
        }
    }
    protected void nullAll(){
        mainActivity = null;
        activeListsActivity = null;
        group2Activity = null;
        groupList2Activity = null;
        newGroupActivity = null;
        createListogramActivity = null;
    }
    protected Context get(int whichOne){
        Context resultContext = null;
        switch (whichOne) {
            case 1:
                resultContext = mainActivity;
                break;
            case 2:
                resultContext = activeListsActivity;
                break;
            case 3:
                resultContext = group2Activity;
                break;
            case 4:
                resultContext = groupList2Activity;
                break;
            case 5:
                resultContext = newGroupActivity;
                break;
            case 6:
                resultContext = createListogramActivity;
                break;
        }
        return resultContext;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    protected class MBinder extends Binder {
        public CurrentActivityProvider getService(){
            return CurrentActivityProvider.this;
        }
    }
}
