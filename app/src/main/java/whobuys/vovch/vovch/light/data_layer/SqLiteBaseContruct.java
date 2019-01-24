package whobuys.vovch.vovch.light.data_layer;

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
        public static final String COLUMN_NAME_NAME = "list_name";
        public static final String COLUMN_NAME_LIST_ID = "list_id";
        public static final String COLUMN_NAME_CREATION_TIME = "list_creation_time";
        public static final String COLUMN_NAME_ACTIVE = "list_active";
        public static final String COLUMN_NAME_OWNER = "list_owner";
        public static final String COLUMN_NAME_GROUP = "list_group";
        public static final String COLUMN_NAME_OWNER_ID = "list_owner_id";

        public static final String LIST_OFFLINE_DEFAULT_VALUE = "0000";
        public static final int LIST_OFFLINE_DEFAULT_NUMBER = 0;
    }
    public static abstract class Items implements BaseColumns{
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ITEM_ID = "item_id";
        public static final String COLUMN_NAME_NAME = "item_name";
        public static final String COLUMN_NAME_COMMENT = "item_comment";
        public static final String COLUMN_NAME_LIST_ONLINE = "item_list_online";
        public static final String COLUMN_NAME_LIST_OFFLINE = "item_list_offline";
        public static final String COLUMN_NAME_ACTIVE = "item_active";
        public static final String COLUMN_NAME_CREATION_TIME = "item_creation_time";
        public static final String COLUMN_NAME_OWNER = "item_owner";
        public static final String COLUMN_NAME_OWNER_ID = "item_owner_id";

        public static final String ITEM_OFFLINE_DEFAULT_VALUE = "0000";
        public static final int ITEM_OFFLINE_DEFAULT_NUMBER = 0;

    }
    public static abstract class Users implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_NAME = "user_name";
        public static final String COLUMN_NAME_ID = "user_id";
    }
    public static abstract class Groups implements BaseColumns{
        public static final String TABLE_NAME = "groups";
        public static final String COLUMN_NAME_NAME = "group_name";
        public static final String COLUMN_NAME_ID = "group_id";
        public static final String COLUMN_NAME_OWNER_NAME = "group_owner_name";
        public static final String COLUMN_NAME_OWNER_ID = "group_owner_id";
        public static final String COLUMN_NAME_STATE = "group_state";
        public static final String COLUMN_NAME_LAST_UPDATE_TIME = "last_update_time";
    }
    public static abstract class UsersAndGroups implements BaseColumns{
        public static final String TABLE_NAME = "usersandGroups";
        public static final String COLUMN_NAME_USERS = "usersandgroups_users";
        public static final String COLUMN_NAME_GROUPS = "usersandgroups_groups";
    }
}
