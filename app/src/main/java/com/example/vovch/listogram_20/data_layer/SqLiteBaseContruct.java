package com.example.vovch.listogram_20.data_layer;

import android.provider.BaseColumns;

/**
 * Created by vovch on 15.12.2017.
 */

public final class SqLiteBaseContruct {
    public SqLiteBaseContruct(){
        //empty to prevent any creation
    }
    public static abstract class Lists implements BaseColumns{
        public static final String TABLE_NAME = "lists";
        public static final String COLUMN_NAME_CREATION_TIME = "creation_time";
        public static final String COLUMN_NAME_ACTIVE = "active";
    }
    public static abstract class Items implements BaseColumns{
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_LIST = "list";
        public static final String COLUMN_NAME_ACTIVE = "active";
        public static final String COLUMN_NAME_CREATION_TIME = "creation_time";
    }
}
