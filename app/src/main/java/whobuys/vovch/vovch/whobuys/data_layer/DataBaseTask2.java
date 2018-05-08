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
                    values.put(SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME, list.getCreationTime());
                    if(list.getType()) {
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ITEM_ID, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER_ID, SqLiteBaseContruct.Items.ITEM_OFFLINE_DEFAULT_VALUE);
                    } else {
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE, list.getId());
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_ITEM_ID, item.getId());
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER, item.getOwnerName());
                        values.put(SqLiteBaseContruct.Items.COLUMN_NAME_OWNER_ID, item.getOwner());
                    }
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

    protected UserGroup setListsToGroup(UserGroup group){
        return null;
    }

    protected UserGroup[] setGroups(UserGroup[] groups){
        try{
            for(UserGroup group : groups){
                addGroup(group);
            }
            return groups;
        } catch (Exception e){
            Log.v("WhoBuys", "DBT2");
            return null;
        }
    }

    protected void dropGroups(){
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //String sqlString1 = "DELETE FROM " + SqLiteBaseContruct.Groups.TABLE_NAME + " " + "WHERE EXIST (SELECT" + " " + SqLiteBaseContruct.Groups._ID + " FROM " + SqLiteBaseContruct.Groups.TABLE_NAME + ")";
            //String sqlString2 = "DELETE FROM " + SqLiteBaseContruct.UsersAndGroups.TABLE_NAME + " WHERE EXIST ( SELECT" + SqLiteBaseContruct.UsersAndGroups._ID + "FROM" + SqLiteBaseContruct.UsersAndGroups.TABLE_NAME + ")"
            //Cursor cursor = db.rawQuery(sqlString1, null);
            db.delete(SqLiteBaseContruct.Groups.TABLE_NAME,null, null);
            db.delete(SqLiteBaseContruct.UsersAndGroups.TABLE_NAME, null, null);
            //cursor = db.rawQuery(sqlString2, null);
            //cursor.close();
            db.close();
        } catch (Exception e){
            Log.v("WhoBuys", "DBT2");
        }
    }

    protected UserGroup[] resetGroups(UserGroup[] groups){
        try{
            dropGroups();
            for(UserGroup group : groups){
                addGroup(group);
            }
            return groups;
        } catch (Exception e){
            Log.v("WhoBuys", "DBT2");
            return null;
        }
    }

    protected UserGroup addGroup(UserGroup group){
        try {
            UserGroup result;
            if (group != null && group.getId() != null) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String[] groupArgs = {group.getId()};
                db.delete(SqLiteBaseContruct.Groups.TABLE_NAME,SqLiteBaseContruct.Groups.COLUMN_NAME_ID + " = ?", groupArgs );

                String[] projection = {SqLiteBaseContruct.Lists.COLUMN_NAME_LIST_ID};
                Cursor cursor = db.query(SqLiteBaseContruct.Lists.TABLE_NAME, projection, SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP + " = ?", groupArgs, null, null, null);
                int length = cursor.getCount();
                String[] lists = new String[length];
                cursor.moveToFirst();
                for(int i = 0; i < length; i++){
                    lists[i] = cursor.getString(0);
                    if(i + 1 < length) {
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                db.delete(  SqLiteBaseContruct.Items.TABLE_NAME, SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE + " = ?", lists);

                db.delete(SqLiteBaseContruct.UsersAndGroups.TABLE_NAME, SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_GROUPS + " = ?", groupArgs);

                ContentValues values = new ContentValues();
                String state;
                values.put(SqLiteBaseContruct.Groups.COLUMN_NAME_ID, group.getId());
                values.put(SqLiteBaseContruct.Groups.COLUMN_NAME_NAME, group.getName());
                values.put(SqLiteBaseContruct.Groups.COLUMN_NAME_OWNER_ID, group.getOwner());
                values.put(SqLiteBaseContruct.Groups.COLUMN_NAME_OWNER_NAME, group.getOwnerName());
                values.clear();
                db.insert(SqLiteBaseContruct.Groups.TABLE_NAME, SqLiteBaseContruct.Groups._ID, values);

                for(AddingUser member : group.getMembers()){
                    makeUserCreatedAndLinked(db, member, group);
                }

                result = group;

                for(SList list : group.getActiveLists()){
                    addOnlineList(list, group);
                }

                for(SList list : group.getHistoryLists()){
                    addOnlineList(list, group);
                }

                db.close();
                return result;
            } else {
                return null;
            }
        } catch (Exception e){
            Log.v("WhoBuys", "DBT2");
            return null;
        }
    }

    protected void makeUserCreatedAndLinked(SQLiteDatabase db, AddingUser member, UserGroup group){
        String[] projection1 = {SqLiteBaseContruct.Users.COLUMN_NAME_ID};
        Cursor cursor = db.query(SqLiteBaseContruct.Users.TABLE_NAME, projection1, null, null, null, null, null);

        ContentValues values = new ContentValues();
        if(cursor.getCount() == 0){
            values.put(SqLiteBaseContruct.Users.COLUMN_NAME_ID, member.getUserId());
            values.put(SqLiteBaseContruct.Users.COLUMN_NAME_NAME, member.getUserName());
            db.insert(SqLiteBaseContruct.Users.TABLE_NAME, SqLiteBaseContruct.Users._ID, values);
            values.clear();
        }
        cursor.close();

        String[] args = {member.getUserId()};
        String[] projection = {SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_USERS};
        Cursor linkCursor = db.query(SqLiteBaseContruct.UsersAndGroups.TABLE_NAME, projection, SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_GROUPS + " = ?", args, null, null, null);

        if(linkCursor.getCount() == 0){
            values.put(SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_USERS, member.getUserId());
            values.put(SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_GROUPS, group.getId());
            db.insert(SqLiteBaseContruct.UsersAndGroups.TABLE_NAME, SqLiteBaseContruct.UsersAndGroups._ID, values);
            values.clear();
        }
        linkCursor.close();
    }

    protected SList addOnlineList(SList list, UserGroup group){
        try {
            SList result;
            if (list != null && group != null && list.getOwnerName() != null && list.getCreationTime() != null && group.getId() != null) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String state;
                if (list.getState()) {
                    state = "t";
                } else {
                    state = "f";
                }
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE, state);
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP, group.getId());
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_LIST_ID, list.getId());
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER, list.getOwnerName());
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER_ID, list.getOwner());
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME, list.getCreationTime());
                values.put(SqLiteBaseContruct.Lists.COLUMN_NAME_NAME, "");
                db.insert(SqLiteBaseContruct.Lists.TABLE_NAME, SqLiteBaseContruct.Lists._ID, values);
                values.clear();

                result = list;

                addItemsToList(result, result.getItems());

                db.close();
                return result;
            } else {
                return null;
            }
        } catch (Exception e){
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
            db.execSQL("DELETE FROM " + SqLiteBaseContruct.Lists.TABLE_NAME + " WHERE " + SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP + " = " + SqLiteBaseContruct.Lists.LIST_OFFLINE_DEFAULT_VALUE);
            db.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
