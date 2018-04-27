package whobuys.vovch.vovch.whobuys.data_layer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.transition.Slide;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.AddingUser;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

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
                    SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER_ID,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_LIST_ID,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_NAME};
            String orderBy = SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME + " DESC";
            String[] projection = {
                    SqLiteBaseContruct.Items.COLUMN_NAME_NAME,
                    SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT,
                    SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE,
                    SqLiteBaseContruct.Items._ID,
                    SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME,
                    SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE,
                    SqLiteBaseContruct.Items.COLUMN_NAME_OWNER,
                    SqLiteBaseContruct.Items.COLUMN_NAME_OWNER_ID,
                    SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE,
                    SqLiteBaseContruct.Items.COLUMN_NAME_ITEM_ID};
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            listCursor = db.query(SqLiteBaseContruct.Lists.TABLE_NAME, listProjection, SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE + "=?", listArgs, null, null, null);
            if (listCursor != null) {
                listCursor.moveToFirst();
                listNumber = listCursor.getCount();
                sLists = new SList[listNumber];

                for (int i = 0; i < listNumber; i++) {
                    int listId = listCursor.getInt(0);
                    String[] arg = {String.valueOf(listId)};
                    cursor = db.query(SqLiteBaseContruct.Items.TABLE_NAME, projection, SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE + "=?", arg, null, null, orderBy);
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
                            tempItem.setOwnerName(cursor.getString(6));
                            tempItem.setOwner(cursor.getString(7));
                            items[j] = tempItem;
                            if (j + 1 < itemNumber) {
                                cursor.moveToNext();
                            }
                        }
                        type = false;
                        if (listCursor.getString(1).equals("t")) {
                            type = true;
                        }
                        tempSlist = new SList(items, listCursor.getInt(0), null, false, type, listCursor.getInt(4), listCursor.getString(5), listCursor.getString(2));
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

    private SList[] getOnline(int taskType, int groupId) {
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
            String[] listArgs = {typeTask,String.valueOf(groupId)};
            String[] listProjection = {
                    SqLiteBaseContruct.Lists._ID,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER_ID,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_LIST_ID,
                    SqLiteBaseContruct.Lists.COLUMN_NAME_NAME};
            String orderBy = SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME + " DESC";
            String[] projection = {
                    SqLiteBaseContruct.Items.COLUMN_NAME_NAME,
                    SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT,
                    SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE,
                    SqLiteBaseContruct.Items._ID,
                    SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME,
                    SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE,
                    SqLiteBaseContruct.Items.COLUMN_NAME_OWNER,
                    SqLiteBaseContruct.Items.COLUMN_NAME_OWNER_ID,
                    SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE,
                    SqLiteBaseContruct.Items.COLUMN_NAME_ITEM_ID};
            SQLiteDatabase db = dbHelper.getReadableDatabase();

                listCursor = db.query(  SqLiteBaseContruct.Lists.TABLE_NAME, listProjection,
                                            SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE + "=?" + "AND" +
                                                     SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP + "=?",
                                        listArgs, null, null, null);

            if (listCursor != null) {
                listCursor.moveToFirst();
                listNumber = listCursor.getCount();
                sLists = new SList[listNumber];

                for (int i = 0; i < listNumber; i++) {
                    int listId = listCursor.getInt(0);
                    String[] arg = {String.valueOf(listId)};
                    cursor = db.query(SqLiteBaseContruct.Items.TABLE_NAME, projection, SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE + "=?", arg, null, null, orderBy);
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
                            tempItem.setOwnerName(cursor.getString(6));
                            tempItem.setOwner(cursor.getString(7));
                            items[j] = tempItem;
                            if (j + 1 < itemNumber) {
                                cursor.moveToNext();
                            }
                        }
                        type = false;
                        if (listCursor.getString(1).equals("t")) {
                            type = true;
                        }
                        tempSlist = new SList(items, listCursor.getInt(0), null, false, type, listCursor.getInt(4), listCursor.getString(5), listCursor.getString(2));
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
                                                                                                        //not working properly. works only expanding side
    public boolean synchronizeOffline(SList[] activeLists, SList[] historyLists){                       //lists can be added or modified. not deleted completely. Full deletion of
        try{                                                                                            //history lists is made outside of synchronization protocol
            for(SList list : activeLists){                                                              //NOT FULLY DEBUGGED
                if(checkList(list)){                                                                    //list is already in storage
                    deleteItemsOfList(list);
                    addItemsToList(list, list.getItems());
                } else{                                                                                 //no such list in storage
                    addList(list.getItems(), "t");
                }
            }
            for(SList list : historyLists){
                if(checkList(list)){                                                                    //list is already in storage
                    deleteItemsOfList(list);
                    addItemsToList(list, list.getItems());
                } else{                                                                                 //no such list in storage
                    addList(list.getItems(), "f");
                }
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public UserGroup getGroupData(String groupId){                                                      //potentially useful. NOT DEBUGGED
        UserGroup result = null;
        try {
            SList[] activeLists = getOnline(1, Integer.parseInt(groupId));
            SList[] historyLists = getOnline(0, Integer.parseInt(groupId));
            result = getGroupStateData(groupId);
            result.setActiveLists(activeLists);
            result.setHistoryLists(historyLists);
        } catch (Exception e){
            Log.d("WhoBuys", "DBT2");
        }
        return result;
    }

    private UserGroup getGroupStateData(String groupId){                                                //potentially useful NOT DEBUGGED
        UserGroup result = null;
        try {
            Cursor usersCursor;
            Cursor groupCursor;
            Cursor userAndGroupsCursor;
            String[] usersAndGroupsArgs = {groupId};
            String[] usersProjection = {
                    SqLiteBaseContruct.Users.COLUMN_NAME_ID,
                    SqLiteBaseContruct.Users.COLUMN_NAME_NAME};
            String[] groupsProjection = {
                    SqLiteBaseContruct.Groups.COLUMN_NAME_ID,
                    SqLiteBaseContruct.Groups.COLUMN_NAME_NAME,
                    SqLiteBaseContruct.Groups.COLUMN_NAME_STATE};
            String[] usersAndGroupProjection = {
                    SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_USERS,
                    SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_GROUPS};
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            groupCursor = db.query(SqLiteBaseContruct.Groups.TABLE_NAME, groupsProjection, SqLiteBaseContruct.Groups.COLUMN_NAME_ID + "=?", usersAndGroupsArgs, null, null, null);
            groupCursor.moveToFirst();
            String group_id = groupCursor.getString(0);
            String group_name = groupCursor.getString(1);
            String group_state = groupCursor.getString(2);

            userAndGroupsCursor = db.query(SqLiteBaseContruct.UsersAndGroups.TABLE_NAME, usersAndGroupProjection, SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_GROUPS + "=?", usersAndGroupsArgs, null, null, null);
            userAndGroupsCursor.moveToFirst();

            StringBuilder member_id = new StringBuilder("");
            StringBuilder member_name = new StringBuilder("");
            AddingUser addingUser;
            AddingUser[] members = new AddingUser[userAndGroupsCursor.getCount()];

            for (int i = 0; i < userAndGroupsCursor.getCount(); i++) {
                String[] usersArgs = {userAndGroupsCursor.getString(0)};

                usersCursor = db.query(SqLiteBaseContruct.Users.TABLE_NAME, usersProjection, SqLiteBaseContruct.Users.COLUMN_NAME_ID + "=?", usersArgs, null, null, null);
                usersCursor.moveToFirst();

                member_id.append(String.valueOf(usersCursor.getInt(0)));
                member_name.append(String.valueOf(usersCursor.getString(1)));

                addingUser = new AddingUser();
                addingUser.setData(member_name.toString(), member_id.toString());
                members[i] = addingUser;

                member_id.setLength(0);
                member_id.trimToSize();
                member_name.setLength(0);
                member_name.trimToSize();

                usersCursor.close();
                userAndGroupsCursor.moveToNext();
            }

            result = new UserGroup(group_name, group_id, members);
            result.setState(group_state);

            groupCursor.close();
            userAndGroupsCursor.close();
            db.close();
        } catch (Exception e){
            Log.d("WhoBuys", "DBT2");
        }
        return result;
    }

    private boolean checkList(SList list){
        try{
            boolean result = false;
            if(list.getId() != -1) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String[] listProjection = {
                        SqLiteBaseContruct.Lists._ID};
                String[] listArgs = {String.valueOf(list.getId())};
                Cursor cursor = db.query(SqLiteBaseContruct.Lists.TABLE_NAME, listProjection, SqLiteBaseContruct.Lists._ID + "=?", listArgs, null, null, null);
                if (cursor.getCount() > 0) {
                    result = true;
                }
                cursor.close();
                db.close();
            }
            return result;
        } catch (Exception e){
            return false;
        }
    }

    private void deleteItemsOfList(SList list){
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM " + SqLiteBaseContruct.Items.TABLE_NAME + " WHERE " + SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE + "= '" + String.valueOf(list.getId()) +"'");
            db.close();
        } catch (Exception e){
            Log.d("WhoBuys", "DBT2");
        }
    }

    private void addItemsToList(SList list, Item[] items){
        try {
            if(list != null && items != null) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String[] state = new String[2];
                state[0] = "f";
                state[1] = "t";
                String currentState;
                for (Item item : items) {
                    if(item.getState()){
                        currentState = state[1];
                    } else{
                        currentState = state[0];
                    }
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_NAME, item.getName());
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT, item.getComment());
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE, String.valueOf(list.getId()));
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME, list.getCreationTime());
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ITEM_ID, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER_ID, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE, currentState);
                    int itemId = (int) db.insert(SqLiteBaseContruct.Items.TABLE_NAME, SqLiteBaseContruct.Items._ID, values);                            // long to int careful
                    item.setId(itemId);
                    values.clear();
                }
                values.clear();
                db.close();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "DBT2");
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
            Log.v("WhoBuys", "DBT2");
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

    public SList addList(Item[] items, String isActive) {                                           
        SList result;
        try {
            if(items != null && isActive != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
                String creationTime = dateFormat.format(Calendar.getInstance().getTime());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE, isActive);
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP, SqLiteBaseContruct.Lists.LIST_OFFLINE_DEFAULT_VALUE);
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_LIST_ID, SqLiteBaseContruct.Lists.LIST_OFFLINE_DEFAULT_VALUE);
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER, SqLiteBaseContruct.Lists.LIST_OFFLINE_DEFAULT_VALUE);
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER_ID, SqLiteBaseContruct.Lists.LIST_OFFLINE_DEFAULT_VALUE);
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME, creationTime);
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_NAME, "");
                long listId;
                listId = db.insert(SqLiteBaseContruct.Lists.TABLE_NAME, SqLiteBaseContruct.Lists._ID, values);
                result = new SList(items, -1, null, false, true, 0, null, creationTime);
                result.setId((int) listId);                                                                //long to int careful
                values.clear();
                addItemsToList(result, result.getItems());
                db.close();
                return result;
            } else{
                return null;
            }
        } catch (Exception e) {
            Log.v("WhoBuys", "DBT2");
            return null;
        }
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
                db.execSQL("DELETE FROM " + SqLiteBaseContruct.Items.TABLE_NAME + " WHERE " + SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE + "= '" + String.valueOf(list.getId()) + "'");
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
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE, list.getId());
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME, creationTime);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE, active.toString());

                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ITEM_ID, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER_ID, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);

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
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM " + SqLiteBaseContruct.Lists.TABLE_NAME + " WHERE " + SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE + "= '" + "f" + "'");
            db.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
