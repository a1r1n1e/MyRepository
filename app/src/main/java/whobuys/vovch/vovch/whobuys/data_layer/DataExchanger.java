package whobuys.vovch.vovch.whobuys.data_layer;

import android.content.Context;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.AddingUser;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.ListInformer;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.TempItem;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vovch on 24.12.2017.
 */

public class DataExchanger {
    private DataStorage storage;
    private static DataExchanger instance;
    private static final String BLANK_WEBCALL_FIELD = "100000";
    private static final String NULL_LAST_LIST_CREATION_TIME = "0000";
    private Context context;

    private DataExchanger(Context newContext) {
        storage = DataStorage.getInstance();
        context = newContext;
    }

    public static DataExchanger getInstance(Context newContext) {
        if (instance == null) {
            instance = new DataExchanger(newContext);
        }
        return instance;
    }

    protected void clearStorage(){
        storage.clearAll();
    }

    public ListInformer[] getListInformers(String userId) {
        ListInformer[] informers = null;
        WebCall webCall = new WebCall();
        String resultJsonString = null;
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        if (userId != null) {
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            resultJsonString = webCall.callServer(userId, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "checkactives", jsonString, provider.userSessionData);
            if (resultJsonString != null && resultJsonString.length() > 2 && resultJsonString.substring(0, 3).equals("200")) {
                informers = webCall.getListInformersFromJsonString(resultJsonString.substring(3));
                informers = storage.setListInformers(informers);
            }
        }
        return informers;
    }

    public ListInformer[] getListInformersRAM(){
        ListInformer[] informers;
        informers = storage.getListInformers();
        if(informers == null){
            informers = new ListInformer[0];
        }
        return informers;
    }


    public void clearDeletableUsers() {
        storage.clearDeletableUsers();
    }

    public void clearAddedUsers(){
        storage.clearAddedUsers();
    }

    public UserGroup newGroupAdding(String name) {
        UserGroup result = null;
        UserGroup[] tempResult = null;
        String resultString = null;
        AddingUser[] users = storage.getAddedUsers();
        int length = users.length;
        try {
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            if (provider.userSessionData.getId() != null && name != null) {
                JSONArray members = new JSONArray();
                JSONObject tempMember;
                for (int i = 0; i < length; i++) {
                    tempMember = new JSONObject();
                    tempMember.put("name", users[i].getUserName());
                    tempMember.put("id", users[i].getUserId());
                    members.put(i, tempMember);
                }
                String jsonString = members.toString();
                WebCall webCall = new WebCall();
                resultString = webCall.callServer(provider.userSessionData.getId(), name, BLANK_WEBCALL_FIELD, "newgroup", jsonString, provider.userSessionData);
                if (resultString != null && resultString.length() > 2 &&  resultString.substring(0, 3).equals("200")) {
                    tempResult = webCall.getGroupsFromJsonString(resultString.substring(3));
                    result = tempResult[0];
                    storage.addGroup(result);
                    clearAddedUsers();
                }
            }
        } catch (JSONException e) {
            Log.v("WhoBuys", "smth in DE");
        }
        return result;
    }


    private String checkUserWeb(String id) {
        String result = null;
        if (id != null) {
            String tempResultString = null;
            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            tempResultString = webCall.callServer(id, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "checkuser", jsonString, provider.userSessionData);
            if (tempResultString != null &&  tempResultString.length() > 2 && tempResultString.substring(0, 3).equals("200")) {
                result = webCall.getStringFromJsonString(tempResultString.substring(3), "name");
            }
        }
        return result;
    }

    public boolean checkUserRAM(String id) {
        AddingUser[] allDeletableUsers = storage.getDeletableUsers();
        for (AddingUser user : allDeletableUsers) {
            if (user.getUserId().equals(id)) {
                return true;
            }
        }
        AddingUser[] allAddedUsers = storage.getAddedUsers();
        for (AddingUser user : allAddedUsers) {
            if (user.getUserId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public AddingUser addUser(String userId) {
        AddingUser newUser = null;
        String result = checkUserWeb(userId);
        if (result != null) {

            newUser = new AddingUser();
            newUser.setData(result, userId);
            storage.addOneAddedUser(newUser);
        }
        return newUser;
    }

    public AddingUser removeUser(AddingUser user) {
        if (user != null) {
            if (storage.isDeletableUser(user)) {
                user = storage.removeOneDeletableUser(user);
            } else if(storage.isAddedUser(user)){
                user = storage.removeOneAddedUser(user);
            }
        }
        return user;
    }

    public AddingUser[] getAddingUsers() {
        return storage.getDeletableUsers();
    }

    public AddingUser[] makeAllUsersPossible(UserGroup group) {
        AddingUser[] users = null;
        if (group != null && group.getMembers() != null) {
            storage.clearDeletableUsers();
            storage.setDeletableUsers(group.getMembers());
            users = getAddingUsers();
        }
        return users;
    }


    public boolean checkGroupActiveData(UserGroup group) {
        boolean result = false;
        if (group != null && group.getId() != null) {
            result = storage.isAnyGroupActiveLists(group);
        }
        return result;
    }

    public SList[] getGroupActiveData(UserGroup group) {
        SList[] lists;
        // this way just now
        lists = getGroupActiveDataFromWeb(group);
        return lists;
    }

    public SList[] getGroupActiveDataRAM(UserGroup group){
        SList[] lists;
        lists = storage.getGroupActive(group);
        if(lists == null){
            lists = new SList[0];
        }
        return lists;
    }

    private SList[] getGroupActiveDataFromWeb(UserGroup group) {
        SList[] lists = null;
        if (group != null && group.getId() != null) {
            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String resultJsonString = webCall.callServer(group.getId(), BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "gettinglistograms", jsonString, provider.userSessionData);
            if (resultJsonString != null && resultJsonString.length() > 2 && resultJsonString.substring(0, 3).equals("200")) {
                lists = webCall.getGroupListsFromJsonString(resultJsonString.substring(3), group);
                setActiveListsToGroup(group, lists);
            }
        }
        return lists;
    }


    public SList[] getGroupHistoryData(UserGroup group) {
        SList[] lists;
        // this way just now
        lists = getGroupDataFromWeb(group);
        return lists;
    }

    public SList[] getGroupHistoryDataRAM(UserGroup group){
        SList[] lists;
        lists = storage.getGroupHistory(group);
        if(lists == null){
            lists = new SList[0];
        }
        return lists;
    }

    private SList[] getGroupDataFromWeb(UserGroup group) {
        SList[] lists = null;
        if (group != null) {
            WebCall webCall = new WebCall();
            String lastListCreationTime = group.getLastListCreationTime();
            if (lastListCreationTime == null) {
                lastListCreationTime = NULL_LAST_LIST_CREATION_TIME;
            }
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String resultJsonString = webCall.callServer(group.getId(), lastListCreationTime, BLANK_WEBCALL_FIELD, "gettinghistory", jsonString, provider.userSessionData);
            if (resultJsonString != null && resultJsonString.length() > 2 &&  resultJsonString.substring(0, 3).equals("200")) {
                lists = webCall.getGroupListsFromJsonString(resultJsonString.substring(3), group);
                setHistoryListsToGroup(group, lists);
            }
        }
        return lists;
    }


    private void setHistoryListsToGroup(UserGroup group, SList[] lists) {
        if (group != null) {
            group.setHistoryLists(lists);
            int length = lists.length;
            for (int i = 0; i < length; i++) {
                lists[i].setGroup(group);
            }
        }
    }

    private void setActiveListsToGroup(UserGroup group, SList[] lists) {
        if (group != null) {
            group.setActiveLists(lists);
            int length = lists.length;
            for (int i = 0; i < length; i++) {
                lists[i].setGroup(group);
            }
        }
    }

    public boolean updateGroupData(String groupId, String groupName) {
        UserGroup[] groups = storage.getGroups();
        int i = 0;
        int length = groups.length;
        for (i = 0; i < length; i++) {
            if (groups[i].getId().equals(groupId)) {
                break;
            }
        }
        if (i == length) {
            UserGroup newGroup = new UserGroup(groupName, groupId);
            storage.addGroup(newGroup);
        }
        return true;
    }

    public UserGroup confirmGroupChanges(UserGroup group, String newGroupName) {
        UserGroup result = null;
        String resultString;
        AddingUser[] oldUsers;
        AddingUser[] addedUsers;
        AddingUser[]deletableUsers;
        boolean mergingUsersNeeded = true;
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        if (group.getOwner().equals(provider.userSessionData.getId())) {
            mergingUsersNeeded = false;
        }
        if (group.getMembers() != null && mergingUsersNeeded) {
            oldUsers = group.getMembers();
        } else {
            oldUsers = new AddingUser[0];
        }
        if (storage.getDeletableUsers() != null) {
            deletableUsers = storage.getDeletableUsers();
        } else {
            deletableUsers = new AddingUser[0];
        }
        if (storage.getAddedUsers() != null) {
            addedUsers = storage.getAddedUsers();
        } else {
            addedUsers = new AddingUser[0];
        }

        AddingUser[] users = new AddingUser[oldUsers.length + addedUsers.length + deletableUsers.length];
        System.arraycopy(oldUsers, 0, users, 0, oldUsers.length);
        System.arraycopy(deletableUsers, 0, users, oldUsers.length, deletableUsers.length);
        System.arraycopy(addedUsers, 0, users, oldUsers.length + deletableUsers.length, addedUsers.length);

        int length = users.length;
        try {
            JSONArray members = new JSONArray();
            for (int i = 0; i < length; i++) {
                members.put(i, users[i].getUserId());
            }
            String jsonString = members.toString();
            WebCall webCall = new WebCall();
            resultString = webCall.callServer(group.getId(), newGroupName, BLANK_WEBCALL_FIELD, "changegroup", jsonString, provider.userSessionData);
            if (resultString != null && resultString.length() > 2 &&  resultString.substring(0, 3).equals("200")) {
                if (group.getOwner() != null) {
                    UserGroup newGroup = new UserGroup(newGroupName, group.getId(), group.getActiveLists(), group.getHistoryLists(), users);
                    newGroup.setOwner(group.getOwner());
                    result = storage.updateGroup(group, newGroup);
                    if (result != null) {
                        provider.setActiveGroup(result);
                        storage.clearDeletableUsers();
                        if(result.getOwner().equals(provider.userSessionData.getId())) {
                            provider.makeAllMembersPossible(result);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.v("WhoBuys", "smth in DE");
        }
        return result;
    }

    public UserGroup leaveGroup(UserGroup group) {
        String resultString = null;
        WebCall webCall = new WebCall();
        JSONArray jsonArray = new JSONArray();
        String jsonString = jsonArray.toString();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        resultString = webCall.callServer(provider.userSessionData.getId(), group.getId(), BLANK_WEBCALL_FIELD, "leavegroup", jsonString, provider.userSessionData);
        if (resultString != null && resultString.length() > 2 &&  resultString.substring(0, 3).equals("200")) {
            storage.removeGroup(group);
        } else {
            group = null;
        }
        return group;
    }


    public UserGroup addOnlineList(Item[] itemsArray, UserGroup group) {
        UserGroup resultGroup = null;
        String result = null;
        if (itemsArray != null && group != null) {
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            WebCall webCall = new WebCall();
            String jsonString = webCall.prepareItemsJSONString(itemsArray);
            result = webCall.callServer(provider.userSessionData.getId(), BLANK_WEBCALL_FIELD, group.getId(), "sendlistogram", jsonString, provider.userSessionData);
            if (result != null && result.length() > 2 &&  result.substring(0, 3).equals("200")) {
                resultGroup = group;
            }
        }
        return resultGroup;
    }

    public UserGroup resendListToGroup(SList list, UserGroup group) {
        UserGroup resultGroup = null;
        String jsonString;
        if (list != null && group != null && list.getItems() != null) {
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            WebCall webCall = new WebCall();
            String itemsJSONString = webCall.prepareItemsJSONString(list.getItems());
            jsonString = webCall.callServer(provider.userSessionData.getId(), String.valueOf(list.getId()), group.getId(), "resendlist", itemsJSONString, provider.userSessionData);
            if (jsonString != null && jsonString.length() > 2 &&  jsonString.substring(0, 3).equals("200")) {
                resultGroup = group;
            }
        }
        return resultGroup;
    }

    public UserGroup[] getGroupsFromRAM(){
        UserGroup[] groups = null;
        groups = storage.getGroups();
        if(groups == null){
            groups = new UserGroup[0];
        }
        return groups;
    }

    public UserGroup[] getGroupsFromWeb() {
        UserGroup[] groups = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        JSONArray jsonArray = new JSONArray();
        String jsonString = jsonArray.toString();
        String userId = String.valueOf(provider.userSessionData.getId());
        String resultJsonString = webCall.callServer(userId, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "groupsearch", jsonString, provider.userSessionData);
        if (resultJsonString != null && resultJsonString.length() > 2 && resultJsonString.substring(0, 3).equals("200")) {
            groups = webCall.getGroupsFromJsonString(resultJsonString.substring(3));
            groups = storage.setGroups(groups);
        }
        return groups;
    }

    public SList disactivateOnlineList(SList list) {
        SList result = null;
        if (list != null && list.getGroup() != null) {
            UserGroup group = list.getGroup();
            WebCall webCall = new WebCall();
            String groupId = list.getGroup().getId();
            String listId = String.valueOf(list.getId());
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String userId = String.valueOf(provider.userSessionData.getId());
            String resultString = webCall.callServer(groupId, listId, userId, "disactivatelist", jsonString, provider.userSessionData);
            if (resultString != null && resultString.length() > 2 &&  resultString.substring(0, 3).equals("200")) {
                group.disactivateList(list);
                result = list;
            }
        }
        return result;
    }

    public boolean sendBug(String text){
        boolean result = false;
        if(text != null){
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            String tempString = webCall.callServer(text, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "bugreport", jsonString, provider.userSessionData);
            if(tempString != null && tempString.length() > 2 && tempString.substring(0, 3).equals("200")){
                result = true;
            }
        }
        return result;
    }

    public boolean dropHistory(){
        boolean result;
        DataBaseTask2 dataBaseTask2 = new DataBaseTask2(context);
        result = dataBaseTask2.dropHistory();
        if(result){
            storage.clearOfflineHistory();
        }
        return result;
    }

    public Item itemmarkOnline(Item item) {
        Item result = null;
        if (item != null && item.getList() != null && item.getList().getGroup() != null) {
            WebCall webCall = new WebCall();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String userId = String.valueOf(provider.userSessionData.getId());
            String itemId = String.valueOf(item.getId());
            String listId = String.valueOf(item.getList().getId());
            String groupId = item.getList().getGroup().getId();
            try {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(0, userId);
                jsonArray.put(1, groupId);
                jsonArray.put(2, listId);
                jsonArray.put(3, itemId);
                String jsonString = jsonArray.toString();
                String resultString = webCall.callServer(userId, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "itemmark", jsonString, provider.userSessionData);
                if (resultString != null && resultString.length() > 2) {
                    UserGroup group = item.getList().getGroup();
                    if (resultString.substring(0, 3).equals("205")) {
                        result = item;
                        group.itemmark(item);
                        item.setOwner(null);
                        item.setOwnerName(null);
                    }
                    if(resultString.substring(0, 3).equals("200")){
                        result = item;
                        group.itemmark(item);
                        String ownerId = webCall.getStringFromJsonString(resultString.substring(3), "owner_id");
                        String ownerName = webCall.getStringFromJsonString(resultString.substring(3), "owner_name");
                        item.setOwner(ownerId);
                        item.setOwnerName(ownerName);
                    }
                }
            } catch (Exception e) {
                Log.v("WhoBuys", "smth in DE");
            }
        }
        return result;
    }


    public Item itemmarkOffline(Item item) {
        Item[] items = storage.getOfflineItemsOfActiveLists();
        Item result = null;
        if (item != null) {
            result = item;
            DataBaseTask2 memoryTask = new DataBaseTask2(context);
            memoryTask.itemMarkOffline(String.valueOf(result.getId()));
        }

        storage.itemmarkOffline(result);

        return result;
    }

    public SList disactivateOfflineList(SList list) {
        SList result = null;
        if (list != null) {
            list.setState(false);
            result = list;
            DataBaseTask2 memoryTask = new DataBaseTask2(context);
            memoryTask.disactivateOfflineList(String.valueOf(result.getId()));
            storage.addOfflineListToHistory(list);
        }

        if (result != null) {
            storage.removeOfflineListFromActive(result);
        }

        return result;
    }

    public SList addOfflineList(Item[] items) {
        SList list;
        DataBaseTask2 addTask = new DataBaseTask2(context);
        list = addTask.addList(items);

        if (list != null) {
            storage.addNewList(list);
        }

        return list;
    }

    public TempItem[] makeTempItemsFromItems(Item[] items) {
        TempItem[] tempItems = null;
        if (items != null) {
            tempItems = new TempItem[items.length];
            for (int i = 0; i < items.length; i++) {
                tempItems[i] = new TempItem(items[i].getName(), items[i].getComment());
                tempItems[i].setId(items[i].getId());
                tempItems[i].setState(items[i].getState());
                tempItems[i].setOwner(items[i].getOwner());
                tempItems[i].setOwnerName(items[i].getOwnerName());
            }
        }
        return tempItems;
    }

    public void saveTempItems(TempItem[] tempItems) {
        storage.setTempItems(tempItems);
    }

    public TempItem[] getTempItems() {
        TempItem[] result;
        result = storage.getTempItems();
        return result;
    }

    public void clearTempItems() {
        storage.clearTempItems();
    }


    public SList redactOfflineList(SList list, Item[] items) {
        SList resultList = null;
        if (list != null && items != null) {
            DataBaseTask2 dataBaseTask2 = new DataBaseTask2(context);
            resultList = dataBaseTask2.redactOfflineList(list, items);
        }
        return resultList;
    }

    public SList redactOnlineList(SList list, Item[] items) {
        SList resultList = null;
        if (list != null && items != null && list.getGroup().getId() != null) {
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            WebCall webCall = new WebCall();
            String itemsJSONString = webCall.prepareItemsJSONString(items);
            String resultString = webCall.callServer(provider.userSessionData.getId(), String.valueOf(list.getId()), list.getGroup().getId(), "redactlist", itemsJSONString, provider.userSessionData);
            if (resultString != null && resultString.length() > 2 &&  resultString.substring(0, 3).equals("200")) {
                list.setItems(items);
                resultList = list;
            }
        }
        return resultList;
    }


    public SList[] getOfflineActiveData() {
        SList[] lists;
        if (storage.isAnyOfflineActive()) {
            lists = storage.getOfflineListsActive();
        } else {
            lists = getOfflineActiveDataFromMemory();
            if (lists != null) {
                storage.setOfflineActiveData(lists);
            }
        }
        return lists;
    }

    public boolean checkOfflineActiveLists() {
        boolean result = false;
        result = storage.isAnyOfflineActive();
        return result;
    }

    public SList[] getOfflineHistoryData() {
        SList[] lists;
        if (storage.isAnyOfflineHistory()) {
            lists = storage.getOfflineListsHistory();
        } else {
            lists = getOfflineHistoryDataFromMemory();
            if (lists != null) {
                storage.setOfflineHistoryData(lists);
            }
        }
        return lists;
    }

    private SList[] getOfflineActiveDataFromMemory() {
        SList[] lists;
        DataBaseTask2 task2 = new DataBaseTask2(context);
        lists = task2.getOffline(1);
        return lists;
    }

    private SList[] getOfflineHistoryDataFromMemory() {
        SList[] lists;
        DataBaseTask2 task2 = new DataBaseTask2(context);
        lists = task2.getOffline(0);
        return lists;
    }

    public void setOfflineActiveDataToDataStorage(SList[] lists) {

    }

    public void setOfflineHistoryDataToDataStorage(SList[] lists) {

    }
}