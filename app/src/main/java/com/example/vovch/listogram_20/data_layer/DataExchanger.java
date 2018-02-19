package com.example.vovch.listogram_20.data_layer;

import android.content.Context;
import android.icu.text.DateFormat;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ListInformer;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.TempItem;
import com.example.vovch.listogram_20.data_types.UserGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vovch on 24.12.2017.
 */

public class DataExchanger{
    private DataStorage storage;
    private static DataExchanger instance;
    private Context context;
    private DataExchanger(Context newContext){
        storage = DataStorage.getInstance();
        context = newContext;
    }
    public static DataExchanger getInstance(Context newContext){
        if(instance == null){
            instance = new DataExchanger(newContext);
        }
        return instance;
    }


    public String loginTry(String login, String password) {
        StringBuilder result = null;
        String tempResult = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        String token = provider.userSessionData.getToken();
        if (token != null && login != null && password != null) {
            tempResult = webCall.callServer(login, password, token, "login", context.getString(R.string.server_pie), "5", "5", "5");
            if(tempResult != null) {
                result = new StringBuilder(tempResult.substring(0, 3));
                if (tempResult.substring(0, 3).equals("200")) {
                    result.append(webCall.getStringFromJsonString(tempResult.substring(3), "id"));
                }
            }
        }
        if(result != null) {
            return result.toString();
        }
        else {
            return null;
        }
    }
    public String registrationTry(String login, String password){
        StringBuilder result = null;
        String tempResult;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        String token = provider.userSessionData.getToken();
        if (token != null && login != null && password != null) {
            tempResult = webCall.callServer(login, password, token, "registration", context.getString(R.string.server_pie), context.getString(R.string.version), "5", "5");
            if(tempResult != null) {
                result = new StringBuilder(tempResult.substring(0, 3));
                if (tempResult.substring(0, 3).equals("200")) {
                    result.append(webCall.getStringFromJsonString(tempResult.substring(3), "id"));
                }
            }
        }
        if(result != null) {
            return result.toString();
        }
        else {
            return null;
        }
    }
    public ListInformer[] getListInformers(String userId){
        ListInformer[] informers = null;
        WebCall webCall = new WebCall();
        String resultJsonString = null;
        if(userId != null) {
            resultJsonString = webCall.callServer(userId, "2", "2", "checkactives", context.getString(R.string.server_pie), context.getString(R.string.version), "5", "5");
            if (resultJsonString.substring(0, 3).equals("200")) {
                informers = webCall.getListInformersFromJsonString(resultJsonString.substring(3));
                informers = storage.setListInformers(informers);
            }
        }
        return informers;
    }




    public void clearAddingUsers(){
        storage.clearAddingUsers();
    }
    public UserGroup newGroupAdding(String name){
        UserGroup result = null;
        UserGroup[] tempResult = null;
        String resultString = null;
        AddingUser[] users = storage.getAddingUsers();
        int length = users.length;
        try {
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            if(provider.userSessionData.getId() != null && name != null) {
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
                resultString = webCall.callServer(name, jsonString, provider.userSessionData.getId(), "newgroup", context.getString(R.string.server_pie), context.getString(R.string.version), "4", "4");
                if (resultString.substring(0, 3).equals("200")) {
                    tempResult = webCall.getGroupsFromJsonString(resultString.substring(3));
                    result = tempResult[0];
                    storage.addGroup(result);
                    clearAddingUsers();
                }
            }
        }
        catch(JSONException e){                                                                         //TODO

        }
        return result;
    }




    private String checkUserWeb(String id){
        String result = null;
        if(id != null) {
            String tempResultString = null;
            WebCall webCall = new WebCall();
            tempResultString = webCall.callServer(id, "3", "3", "checkuser", context.getString(R.string.server_pie), context.getString(R.string.version), "4", "4");
            if (tempResultString != null && tempResultString.substring(0, 3).equals("200")) {
                result = webCall.getStringFromJsonString(tempResultString.substring(3), "name");
            }
        }
        return result;
    }
    public boolean checkUserRAM(String id){
        AddingUser[] allUsers = storage.getAddingUsers();
        for (AddingUser user : allUsers) {
            if (user.getUserId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    public AddingUser addUser(String userId){
        AddingUser newUser = null;
        String result = checkUserWeb(userId);
        if(result != null){
            newUser = new AddingUser();
            newUser.setData(result, userId);
            storage.addOneAddingUser(newUser);
            storage.addOneAddingUser(newUser);
        }
        return newUser;
    }
    public AddingUser removeUser(AddingUser user){
        if(user != null){
            if(storage.isAddingUser(user)) {
                user = storage.removeOneAddingUser(user);
            }
        }
        return user;
    }
    public AddingUser[] getAddingUsers(){
        return storage.getAddingUsers();
    }
    public AddingUser[] makeAllUsersPossible(UserGroup group){
        AddingUser[] users = null;
        if(group != null && group.getMembers() != null){
            storage.clearAddingUsers();
            storage.setAddingUsers(group.getMembers());
            users = getAddingUsers();
        }
        return users;
    }




    public boolean checkGroupActiveData(UserGroup group){
        boolean result = false;
        if(group != null && group.getId() != null) {
            result = storage.isAnyGroupActiveLists(group);
        }
        return result;
    }
    public SList[] getGroupActiveData(UserGroup group){
        SList[] lists;
                                            // this way just now
        lists = getGroupActiveDataFromWeb(group);
        return lists;
    }
    private SList[] getGroupActiveDataFromWeb(UserGroup group){
        SList[] lists = null;
        if(group != null && group.getId() != null) {
            WebCall webCall = new WebCall();
            String resultJsonString = webCall.callServer(group.getId(), "3", "3", "gettinglistograms", context.getString(R.string.server_pie), context.getString(R.string.version), "3");
            if (resultJsonString.substring(0, 3).equals("200")) {
                lists = webCall.getGroupListsFromJsonString(resultJsonString.substring(3), group);
            }
            setActiveListsToGroup(group, lists);
        }
        return lists;
    }


    public SList[] getGroupHistoryData(UserGroup group){
        SList[] lists;
                    // this way just now
        lists = getGroupDataFromWeb(group);
        return lists;
    }
    private SList[] getGroupDataFromWeb(UserGroup group){
        SList[] lists = null;
        if (group != null) {
            WebCall webCall = new WebCall();
            String lastListCreationTime = group.getLastListCreationTime();
            if (lastListCreationTime == null) {
                lastListCreationTime = "0000";
            }
            String resultJsonString = webCall.callServer(group.getId(), lastListCreationTime, "3", "gettinghistory", context.getString(R.string.server_pie), context.getString(R.string.version), "3");
            if (resultJsonString.substring(0, 3).equals("200")) {
                lists = webCall.getGroupListsFromJsonString(resultJsonString.substring(3), group);
            }
            setHistoryListsToGroup(group, lists);
        }
        return lists;
    }


    private void setHistoryListsToGroup(UserGroup group, SList[] lists){
        if(group != null){
            group.setHistoryLists(lists);
            int length = lists.length;
            for(int i = 0; i < length; i++){
                lists[i].setGroup(group);
            }
        }
    }
    private void setActiveListsToGroup(UserGroup group, SList[] lists){
        if(group != null){
            group.setActiveLists(lists);
            int length = lists.length;
            for(int i = 0; i < length; i++){
                lists[i].setGroup(group);
            }
        }
    }
    public boolean updateGroupData(String groupId, String groupName){
        UserGroup[] groups = storage.getGroups();
        int i = 0;
        int length = groups.length;
        for(i = 0; i < length; i++){
            if(groups[i].getId().equals(groupId)) {
                break;
            }
        }
        if(i == length){
                UserGroup newGroup = new UserGroup(groupName, groupId);
                storage.addGroup(newGroup);
        }
        return true;
    }
    public UserGroup confirmGroupChanges(UserGroup group){
        UserGroup result = null;
        String resultString;
        AddingUser[] oldUsers;
        AddingUser[] newUsers;
        if(group.getMembers() != null) {
            oldUsers = group.getMembers();
        }
        else{
            oldUsers = new AddingUser[0];
        }
        if(storage.getAddingUsers() != null) {
            newUsers = storage.getAddingUsers();
        }
        else{
            newUsers = new AddingUser[0];
        }

        AddingUser[] users = new AddingUser[oldUsers.length + newUsers.length];
        System.arraycopy(oldUsers, 0, users, 0, oldUsers.length);
        System.arraycopy(newUsers, 0, users, oldUsers.length, newUsers.length);

        int length = users.length;
        try {
            JSONArray members = new JSONArray();
            for (int i = 0; i < length; i++) {
                members.put(i, users[i].getUserId());
            }
            String jsonString = members.toString();
            WebCall webCall = new WebCall();
            resultString = webCall.callServer(group.getId(), jsonString, group.getName(), "changegroup", context.getString(R.string.server_pie), context.getString(R.string.version), "4", "4");
            if(resultString.substring(0, 3).equals("200")){
                group.setMembers(users);
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                boolean mergingUsersNeeded = true;
                if(group.getOwner() != null) {
                    if (group.getOwner().equals(provider.userSessionData.getId())) {
                        mergingUsersNeeded = false;
                    }
                    result = storage.updateGroup(group, group, mergingUsersNeeded);
                }
            }
        }
        catch(JSONException e){                                                                         //TODO

        }
        return result;
    }
    public UserGroup leaveGroup(UserGroup group){
        String resultString = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        resultString = webCall.callServer(group.getId(), provider.userSessionData.getId(), "4", "leavegroup", context.getString(R.string.server_pie), context.getString(R.string.version), "4", "4");
        if(!resultString.substring(0, 3).equals("200")){
            group = null;
        }
        else{
            storage.removeGroup(group);
        }
        return group;
    }


    public UserGroup addOnlineList(Item[] itemsArray, UserGroup group){
        UserGroup resultGroup = null;
        String result = null;
        if(itemsArray!= null && group != null){
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            WebCall webCall = new WebCall();
            String jsonString = webCall.prepareItemsJSONString(itemsArray);
            result = webCall.callServer(provider.userSessionData.getId(), jsonString, group.getId(), "sendlistogram", context.getString(R.string.server_pie), context.getString(R.string.version), "6", "6");
            if(result.substring(0, 3).equals("200")){
                resultGroup = group;
            }
        }
        return resultGroup;
    }

    public UserGroup resendListToGroup(SList list, UserGroup group){
        UserGroup resultGroup = null;
        String jsonString;
        if(list != null && group != null && list.getItems() != null){
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            WebCall webCall = new WebCall();
            String itemsJSONString = webCall.prepareItemsJSONString(list.getItems());
            jsonString = webCall.callServer(provider.userSessionData.getId(), String.valueOf(list.getId()), group.getId(), "resendlist", context.getString(R.string.server_pie), context.getString(R.string.version), "66", itemsJSONString);
            if(jsonString.substring(0,3).equals("200")){
                resultGroup = group;
            }
        }
        return resultGroup;
    }


    public UserGroup[] getGroupsFromWeb(){
        UserGroup[] groups = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        String userId = String.valueOf(provider.userSessionData.getId());
        String resultJsonString = webCall.callServer(userId, "2", "2", "groupsearch", context.getString(R.string.server_pie), "5", "5");
        if(resultJsonString.substring(0, 3).equals("200")) {
            groups = webCall.getGroupsFromJsonString(resultJsonString.substring(3));
            groups = storage.setGroups(groups);
        }
        return groups;
    }

    public SList disactivateOnlineList(SList list){
        SList result = null;
        if (list != null && list.getGroup() != null) {
            UserGroup group = list.getGroup();
            WebCall webCall = new WebCall();
            String groupId = list.getGroup().getId();
            String listId = String.valueOf(list.getId());
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String userId = String.valueOf(provider.userSessionData.getId());
            String resultString = webCall.callServer(groupId, listId, userId, "disactivatelist", context.getString(R.string.server_pie), context.getString(R.string.version), "7");
            if (resultString.substring(0, 3).equals("200")) {
                group.disactivateList(list);
                result = list;
            }
        }
        return result;
    }
    public Item itemmarkOnline(Item item){
        Item result = null;
        if(item != null && item.getList() != null && item.getList().getGroup() != null) {
            WebCall webCall = new WebCall();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String userId = String.valueOf(provider.userSessionData.getId());
            String itemId = String.valueOf(item.getId());
            String listId = String.valueOf(item.getList().getId());
            String groupId = item.getList().getGroup().getId();
            String resultString = webCall.callServer(userId, itemId, listId, "itemmark", context.getString(R.string.server_pie), context.getString(R.string.version), groupId);
            if (resultString != null) {
                if (resultString.codePointAt(0) == '2') {
                    result = item;
                    UserGroup group = item.getList().getGroup();
                    group.itemmark(item);
                }
            }
        }
        return result;
    }



    public Item itemmarkOffline(Item item){
        Item[] items = storage.getOfflineItemsOfActiveLists();
        Item result = null;
        if(item != null){
            result = item;
            DataBaseTask2 memoryTask = new DataBaseTask2(context);
            memoryTask.itemMarkOffline(String.valueOf(result.getId()));
        }

        storage.itemmarkOffline(result);

        return result;
    }
    public SList disactivateOfflineList(SList list){
        SList result = null;
        if(list != null){
            result = list;
            DataBaseTask2 memoryTask = new DataBaseTask2(context);
            memoryTask.disactivateOfflineList(String.valueOf(result.getId()));
        }

        if(result != null) {
            storage.removeOfflineListFromActive(result);
        }

        return result;
    }
    public SList addOfflineList(Item[] items){
        SList list;
        DataBaseTask2 addTask = new DataBaseTask2(context);
        list = addTask.addList(items);

        if(list != null) {
            storage.addNewList(list);
        }

        return list;
    }

    public TempItem[]  makeTempItemsFromItems(Item[] items){
        TempItem[] tempItems = null;
        if(items != null){
            tempItems = new TempItem[items.length];
            for(int i = 0; i < items.length; i++){
                tempItems[i] = new TempItem(items[i].getName(), items[i].getComment());
                tempItems[i].setId(items[i].getId());
                tempItems[i].setState(items[i].getState());
            }
        }
        return  tempItems;
    }

    public void saveTempItems(TempItem[] tempItems){
        storage.setTempItems(tempItems);
    }
    public TempItem[] getTempItems(){
        TempItem[] result;
        result = storage.getTempItems();
        return result;
    }
    public void clearTempItems(){
        storage.clearTempItems();
    }


    public SList redactOfflineList(SList list, Item[] items){
        SList resultList = null;
        if(list != null && items != null){
            DataBaseTask2 dataBaseTask2 = new DataBaseTask2(context);
            resultList = dataBaseTask2.redactOfflineList(list, items);
        }
        return resultList;
    }

    public SList redactOnlineList(SList list, Item[] items){
        SList resultList = null;
        if(list != null && items != null && list.getGroup().getId() != null){
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            WebCall webCall = new WebCall();
            String itemsJSONString = webCall.prepareItemsJSONString(items);
            String resultString = webCall.callServer(provider.userSessionData.getId(), String.valueOf(list.getId()), itemsJSONString, "redactlist", context.getString(R.string.server_pie), context.getString(R.string.version), list.getGroup().getId());
            if(resultString.substring(0, 3).equals("200")){
                list.setItems(items);
                resultList = list;
            }
        }
        return resultList;
    }


    public SList[] getOfflineActiveData(){
        SList[] lists;
        if(storage.isAnyOfflineActive()){
            lists = storage.getOfflineListsActive();
        }
        else{
            lists = getOfflineActiveDataFromMemory();
            if(lists != null) {
                storage.setOfflineActiveData(lists);
            }
        }
        return lists;
    }
    public boolean checkOfflineActiveLists(){
        boolean result = false;
        result = storage.isAnyOfflineActive();
        return result;
    }
    public SList[] getOfflineHistoryData(){
        SList[] lists;
        if(storage.isAnyOfflineHistory()){
            lists = storage.getOfflineListsHistory();
        }
        else{
            lists = getOfflineHistoryDataFromMemory();
            if(lists != null) {
                storage.setOfflineHistoryData(lists);
            }
        }
        return lists;
    }

    private SList[] getOfflineActiveDataFromMemory(){
        SList[] lists;
        DataBaseTask2 task2 = new DataBaseTask2(context);
        lists = task2.getOffline(1);
        return lists;
    }
    private SList[] getOfflineHistoryDataFromMemory(){
        SList[] lists;
        DataBaseTask2 task2 = new DataBaseTask2(context);
        lists = task2.getOffline(0);
        return lists;
    }
    public void setOfflineActiveDataToDataStorage(SList[] lists){

    }
    public void setOfflineHistoryDataToDataStorage(SList[] lists){

    }
}