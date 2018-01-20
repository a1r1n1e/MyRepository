package com.example.vovch.listogram_20;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.Calendar;

/**
 * Created by vovch on 14.12.2017.
 */

public class OfflineCreateListTask extends AsyncTask<Item[], Void, SList> {
    private Context applicationContext;
    private ActiveActivityProvider activeActivityProvider;

    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected SList doInBackground(Item[]... loginPair){
        activeActivityProvider = (ActiveActivityProvider) applicationContext;
        SList list;

        list = activeActivityProvider.dataExchanger.addOfflineList(loginPair[0]);

        return list;
    }
    @Override
    protected void onPostExecute(SList result){
        if(result != null){
            activeActivityProvider.showOfflineListCreatedGood(result);
        }
        else{
            activeActivityProvider.showOfflineListCreatedBad(result);
        }
    }
}
