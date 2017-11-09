package com.example.vovch.listogram_20;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by vovch on 09.11.2017.
 */

public class ActiveActivityProvider extends Application {

    private Context activeActivity;
    private int activeActivityNumber;
    private int groupId;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("MY", "onCreate MyApp");
    }
    protected Context getActiveActivity(){
        return activeActivity;
    }
    protected void setActiveActivity(int whichOne, Context context){
        activeActivity = context;
        activeActivityNumber = whichOne;
    }
    protected void setGroupId(int id){
        groupId = id;
    }
    protected int getGroupId(){
        return groupId;
    }
    protected void nullActiveActivity(){
        activeActivity = null;
        activeActivityNumber = - 1;
        groupId = - 1;
    }
    protected int getActiveActivityNumber(){
        return  activeActivityNumber;
    }
}
