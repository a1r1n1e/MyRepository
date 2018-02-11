package com.example.vovch.listogram_20.data_layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.data_layer.DbHelper;
import com.example.vovch.listogram_20.data_layer.SqLiteBaseContruct;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.SList;

import java.util.Calendar;

/**
 * Created by vovch on 24.12.2017.
 */

public class DataBaseTask2 {

    DbHelper dbHelper;
    Context applicationContext;
    ActiveActivityProvider provider;
    public DataBaseTask2(Context context){
        applicationContext = context;
        provider = (ActiveActivityProvider) applicationContext;
    }
    public SList[] getOffline(int taskType){
        String typeTask;
        SList[] sLists = null;
        Item[] items;
        int listNumber, itemNumber;
        SList tempSlist;
        Item tempItem;
        boolean type;
        if(taskType == 1){
            typeTask = "t";
        }
        else{
            typeTask = "f";
        }
        Cursor cursor;
        Cursor listCursor;
        String[] listArgs = {typeTask};
        String[] listProjection = {
                SqLiteBaseContruct.Lists._ID,
                SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE,
                SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME};
        String orderBy = SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME + " DESC";
        String[] projection = {
                SqLiteBaseContruct.Items.COLUMN_NAME_NAME,
                SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT,
                SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE,
                SqLiteBaseContruct.Items._ID,
                SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME,
                SqLiteBaseContruct.Items.COLUMN_NAME_LIST};
        try {
            dbHelper = new DbHelper(applicationContext);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            listCursor = db.query(SqLiteBaseContruct.Lists.TABLE_NAME, listProjection, SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE + "=?", listArgs, null, null, null);
            if (listCursor != null) {
                listCursor.moveToFirst();
                listNumber = listCursor.getCount();
                sLists = new SList[listNumber];

                for (int i = 0; i < listNumber; i++) {
                    int listId = listCursor.getInt(0);
                    String[] arg = {String.valueOf(listId)};
                    cursor = db.query(SqLiteBaseContruct.Items.TABLE_NAME, projection, SqLiteBaseContruct.Items.COLUMN_NAME_LIST + "=?", arg, null, null, orderBy);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        itemNumber = cursor.getCount();
                        items = new Item[itemNumber];
                        for (int j = 0; j < itemNumber; j++) {
                            type = false;
                            if(cursor.getString(2).equals("t")){
                                type = true;
                            }
                            tempItem = new Item(cursor.getInt(3), cursor.getString(0), cursor.getString(1), type);
                            items[j] = tempItem;
                            if (j + 1 < itemNumber) {
                                cursor.moveToNext();
                            }
                        }
                        type = false;
                        if(listCursor.getString(1).equals("t")){
                            type = true;
                        }
                        tempSlist = new SList(items, listCursor.getInt(0), 0, false, type, 0, null, listCursor.getString(2));
                        sLists[i] = tempSlist;
                        if (i + 1 < listNumber) {
                            listCursor.moveToNext();
                        }
                    }
                }
            }

            dbHelper.close();
            return sLists;
        }
        catch (SQLException e){
            return sLists;
        }
    }
    public void disactivateOfflineList(String listId){                                           //REFACTOR FOR DATASTORAGE
        try {
            dbHelper = new DbHelper(applicationContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            String[] args = {listId};
            values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE, "f");
            db.update(SqLiteBaseContruct.Lists.TABLE_NAME, values, SqLiteBaseContruct.Lists._ID + "=?", args);
            values.clear();
            dbHelper.close();
            //return "1";
        }
        catch (SQLException e){
            //return "";
        }
    }
    public void itemMarkOffline(String itemId){                                           //REFACTOR FOR DATASTORAGE
        try {
            dbHelper = new DbHelper(applicationContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] projection = { SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE,
                    SqLiteBaseContruct.Items._ID};
            String[] args = {itemId};
            Cursor cursor = db.query(SqLiteBaseContruct.Items.TABLE_NAME, projection, SqLiteBaseContruct.Items._ID + "=?", args, null, null, null);
            ContentValues values = new ContentValues();
            String type;
            StringBuilder resultType = new StringBuilder("1");
            if(cursor != null) {
                cursor.moveToFirst();
                if (cursor.getString(0).equals("t")) {
                    type = "f";
                    resultType.setLength(0);
                    resultType.append("2");
                } else {
                    type = "t";
                    resultType.setLength(0);
                    resultType.append("0");
                }
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE, type);
                db.update(SqLiteBaseContruct.Items.TABLE_NAME, values, SqLiteBaseContruct.Items._ID + "=?", args);
                values.clear();
            }
            dbHelper.close();
            //return resultType.toString() + itemId + "%";
        }
        catch (SQLException e){
            //return "";
        }
    }
    public SList addList(Item[] incomingItems) {                                           //REFACTOR FOR DATASTORAGE

        int i, j;
        dbHelper = new DbHelper(applicationContext);
        SList result;
        try {
            String creationTime = Calendar.getInstance().getTime().toString();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE, "t");
            values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME, creationTime);
            long listId;
            listId = db.insert(SqLiteBaseContruct.Lists.TABLE_NAME, SqLiteBaseContruct.Lists._ID, values);
            values.clear();
            int length = incomingItems.length;
            long itemId;
            for (i = 0; i < length; i++) {
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_NAME, incomingItems[i].getName());
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT, incomingItems[i].getComment());
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST, listId);
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME, creationTime);
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE, "t");
                itemId = db.insert(SqLiteBaseContruct.Items.TABLE_NAME, SqLiteBaseContruct.Items._ID, values);
                incomingItems[i].setId((int) itemId);                                                                               // long to int careful
                values.clear();
            }
            values.clear();
            dbHelper.close();
            result = new SList(incomingItems, (int) listId, 0, false, true, 0, null, creationTime);                       //BIG VALUES OF LISTID WILL BREAK EVERYTHING
        } catch (SQLException e) {
            result = null;
        }
        return result;
    }
}
