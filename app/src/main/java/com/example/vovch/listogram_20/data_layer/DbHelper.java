package com.example.vovch.listogram_20.data_layer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vovch.listogram_20.data_layer.SqLiteBaseContruct;

/**
 * Created by vovch on 15.12.2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SqLiteBase.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String
            SQL_CREATE_LISTS = "CREATE TABLE " + SqLiteBaseContruct.Lists.TABLE_NAME + " (" +
            SqLiteBaseContruct.Lists._ID + " INTEGER PRIMARY KEY," +
            SqLiteBaseContruct.Lists.COLUMN_NAME_ACTIVE + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Lists.COLUMN_NAME_CREATION_TIME + TEXT_TYPE + " )";
    private static final String SQL_DROP_LISTS = "DROP TABLE IF EXISTS " + SqLiteBaseContruct.Lists.TABLE_NAME;
    private static final String
            SQL_CREATE_ITEMS = "CREATE TABLE " + SqLiteBaseContruct.Items.TABLE_NAME + " (" +
            SqLiteBaseContruct.Items._ID + " INTEGER PRIMARY KEY," +
            SqLiteBaseContruct.Items.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_COMMENT + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_CREATION_TIME + TEXT_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_LIST + INTEGER_TYPE + COMMA_SEP +
            SqLiteBaseContruct.Items.COLUMN_NAME_ACTIVE + TEXT_TYPE + " )";
    private static final String SQL_DROP_ITEMS = "DROP TABLE IF EXISTS " + SqLiteBaseContruct.Lists.TABLE_NAME;
    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_LISTS);
        db.execSQL(SQL_CREATE_ITEMS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DROP_LISTS);
        db.execSQL(SQL_DROP_ITEMS);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
    protected void clear(SQLiteDatabase db){
        db.delete(SqLiteBaseContruct.Items.TABLE_NAME, null, null);
        db.delete(SqLiteBaseContruct.Lists.TABLE_NAME, null, null);
    }
}
