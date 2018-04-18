package whobuys.vovch.vovch.whobuys.data_layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vovch on 24.12.2017.
 */

public class DataBaseTask2 {

    private DbHelper dbHelper;

    public DataBaseTask2(Context context) {
        dbHelper = new DbHelper(context);
    }

    public SList[] getOffline(int taskType) {
        String typeTask;
        SList[] sLists = null;
        Item[] items;
        int listNumber, itemNumber;
        SList tempSlist;
        Item tempItem;
        try {
            boolean type;
            if (taskType == 1) {
                typeTask = "t";
            } else {
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
                            if (cursor.getString(2).equals("t")) {
                                type = true;
                            }
                            tempItem = new Item(cursor.getInt(3), cursor.getString(0), cursor.getString(1), type);
                            items[j] = tempItem;
                            if (j + 1 < itemNumber) {
                                cursor.moveToNext();
                            }
                        }
                        type = false;
                        if (listCursor.getString(1).equals("t")) {
                            type = true;
                        }
                        tempSlist = new SList(items, listCursor.getInt(0), null, false, type, 0, null, listCursor.getString(2));
                        sLists[i] = tempSlist;
                        if (i + 1 < listNumber) {
                            listCursor.moveToNext();
                        }
                    }
                    if(cursor != null) {
                        cursor.close();
                    }
                    db.close();
                }
            }
            if(listCursor != null){
                listCursor.close();
            }
            return sLists;
        } catch (Exception e) {
            return sLists;
        }
    }

    public void disactivateOfflineList(String listId) {                                           //REFACTOR FOR DATASTORAGE
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            String[] args = {listId};
            values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE, "f");
            db.update(SqLiteBaseContruct.Lists.TABLE_NAME, values, SqLiteBaseContruct.Lists._ID + "=?", args);
            values.clear();
            db.close();
        } catch (Exception e) {
            Log.v("WhoBuys", "smth in DBT");
        }
    }

    public void itemMarkOffline(String itemId) {                                           //REFACTOR FOR DATASTORAGE
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] projection = {SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE,
                    SqLiteBaseContruct.Items._ID};
            String[] args = {itemId};
            Cursor cursor = db.query(SqLiteBaseContruct.Items.TABLE_NAME, projection, SqLiteBaseContruct.Items._ID + "=?", args, null, null, null);
            ContentValues values = new ContentValues();
            String type;
            StringBuilder resultType = new StringBuilder("1");
            if (cursor != null) {
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
            db.close();
            if(cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.v("WhoBuys", "smth in DBT");
        }
    }

    public SList addList(Item[] items) {                                                        //REFACTOR FOR DATASTORAGE
        int i, j;
        int length = 0;
        if (items != null) {
            length = items.length;
        }
        Item[] incomingItems = new Item[length];
        SList result;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
            String creationTime = dateFormat.format(Calendar.getInstance().getTime());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE, "t");
            values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME, creationTime);
            long listId;
            listId = db.insert(SqLiteBaseContruct.Lists.TABLE_NAME, SqLiteBaseContruct.Lists._ID, values);
            values.clear();
            long itemId;
            for (i = 0; i < length; i++) {
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_NAME, items[i].getName());
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT, items[i].getComment());
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST, listId);
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME, creationTime);
                values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE, "t");
                itemId = db.insert(SqLiteBaseContruct.Items.TABLE_NAME, SqLiteBaseContruct.Items._ID, values);
                incomingItems[i] = new Item(items[i].getName(), items[i].getComment(), true);
                incomingItems[i].setId((int) itemId);                                                                               // long to int careful
                values.clear();
            }
            values.clear();
            result = new SList(incomingItems, (int) listId, null, false, true, 0, null, creationTime);                       //BIG VALUES OF LISTID WILL BREAK EVERYTHING
            db.close();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    protected SList redactOfflineList(SList list, Item[] items) {
        SList resultList = null;
        if (list != null && items != null) {
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                SimpleDateFormat dateFormat;
                String creationTime;
                StringBuilder active = new StringBuilder("");
                db.execSQL("DELETE FROM " + SqLiteBaseContruct.Items.TABLE_NAME + " WHERE " + SqLiteBaseContruct.Items.COLUMN_NAME_LIST + "= '" + list.getId() + "'");
                for (int i = 0; i < items.length; i++) {
                    active.setLength(0);
                    dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
                    creationTime = dateFormat.format(Calendar.getInstance().getTime());
                    if (items[i].getState()) {
                        active.append("t");
                    } else {
                        active.append("f");
                    }
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_NAME, items[i].getName());
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT, items[i].getComment());
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST, list.getId());
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME, creationTime);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE, active.toString());
                    long itemId = db.insert(SqLiteBaseContruct.Items.TABLE_NAME, SqLiteBaseContruct.Items._ID, values);
                    items[i].setId((int) itemId);                                                                                   // long to int careful
                    values.clear();
                }
                list.setItems(items);
                resultList = list;
                db.close();
            } catch (Exception e) {
                return  null;
            }
        }
        return resultList;
    }

    protected boolean dropHistory(){
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("DELETE FROM " + SqLiteBaseContruct.Lists.TABLE_NAME + " WHERE " + SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE + "= '" + "f" + "'");
            db.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
