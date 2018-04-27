package whobuys.vovch.vovch.whobuys.data_layer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vovch on 15.12.2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "SqLiteBase.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String
            SQL_CREATE_LISTS = "CREATE TABLE " + SqLiteBaseContruct.Lists.TABLE_NAME + " (" +
            SqLiteBaseContruct.Lists._ID + " INTEGER PRIMARY KEY," +
            SqLiteBaseContruct.Lists.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Lists.COLUMN_NAME_LIST_ID + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Lists.COLUMN_NAME_OWNER_ID + INTEGER_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Lists.COLUMN_NAME_GROUP + INTEGER_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME + TEXT_TYPE + " )";
    private static final String SQL_DROP_LISTS = "DROP TABLE IF EXISTS " + SqLiteBaseContruct.Lists.TABLE_NAME;
    private static final String
            SQL_CREATE_ITEMS = "CREATE TABLE " + SqLiteBaseContruct.Items.TABLE_NAME + " (" +
            SqLiteBaseContruct.Items._ID + " INTEGER PRIMARY KEY," +
            SqLiteBaseContruct.Items.COLUMN_NAME_ITEM_ID + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_LIST_OFFLINE + INTEGER_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_LIST_ONLINE + INTEGER_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_OWNER + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_OWNER_ID + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE + TEXT_TYPE + " )";
    private static final String SQL_DROP_ITEMS = "DROP TABLE IF EXISTS " + SqLiteBaseContruct.Items.TABLE_NAME;
    private static final String
            SQL_CREATE_USERS = "CREATE TABLE " + SqLiteBaseContruct.Users.TABLE_NAME + " (" +
            SqLiteBaseContruct.Users._ID + " INTEGER PRIMARY KEY," +
            SqLiteBaseContruct.Users.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Users.COLUMN_NAME_NAME + TEXT_TYPE + " )";
    private static final String SQL_DROP_USERS = "DROP TABLE IF EXISTS " + SqLiteBaseContruct.Users.TABLE_NAME;
    private static final String
            SQL_CREATE_GROUPS = "CREATE TABLE " + SqLiteBaseContruct.Groups.TABLE_NAME + " (" +
            SqLiteBaseContruct.Groups._ID + " INTEGER PRIMARY KEY," +
            SqLiteBaseContruct.Groups.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Groups.COLUMN_NAME_OWNER_NAME + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Groups.COLUMN_NAME_OWNER_ID + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Groups.COLUMN_NAME_STATE + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Groups.COLUMN_NAME_NAME + TEXT_TYPE + " )";
    private static final String SQL_DROP_GROUPS = "DROP TABLE IF EXISTS " + SqLiteBaseContruct.Groups.TABLE_NAME;
    private static final String
            SQL_CREATE_USERSANDGROUPS = "CREATE TABLE " + SqLiteBaseContruct.UsersAndGroups.TABLE_NAME + " (" +
            SqLiteBaseContruct.UsersAndGroups._ID + " INTEGER PRIMARY KEY," +
            SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_USERS + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.UsersAndGroups.COLUMN_NAME_GROUPS + TEXT_TYPE + " )";
    private static final String SQL_DROP_USERSANDGROUPS = "DROP TABLE IF EXISTS " + SqLiteBaseContruct.UsersAndGroups.TABLE_NAME;
    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_LISTS);
        db.execSQL(SQL_CREATE_ITEMS);
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_GROUPS);
        db.execSQL(SQL_CREATE_USERSANDGROUPS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion - oldVersion > 0) {
            db.execSQL(SQL_DROP_LISTS);
            db.execSQL(SQL_DROP_ITEMS);
            db.execSQL(SQL_DROP_USERS);
            db.execSQL(SQL_DROP_GROUPS);
            db.execSQL(SQL_DROP_USERSANDGROUPS);
            onCreate(db);
        }
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
    protected void clear(SQLiteDatabase db){
        db.delete(SqLiteBaseContruct.Items.TABLE_NAME, null, null);
        db.delete(SqLiteBaseContruct.Lists.TABLE_NAME, null, null);
        db.delete(SqLiteBaseContruct.Users.TABLE_NAME, null, null);
        db.delete(SqLiteBaseContruct.Groups.TABLE_NAME, null, null);
        db.delete(SqLiteBaseContruct.UsersAndGroups.TABLE_NAME, null, null);
    }
}
