package com.example.vovch.listogram_20;

import android.content.Context;
import android.text.InputType;
import android.widget.Button;
import android.widget.ImageButton;

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


    protected String loginTry(String login, String password) {
        String result = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        String token = provider.userSessionData.getToken();
        if (token != null){
            result = webCall.callServer(login, password, token, "login", "5", "5", "5", "5");
        }
        return result;
    }
    protected String registrationTry(String login, String password){
        String result = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        String token = provider.userSessionData.getToken();
        if (token != null) {
            result = webCall.callServer(login, password, token, "registration", "5", "5", "5", "5");
        }
        return result;
    }
    protected ListInformer[] getListInformers(String userId){
        ListInformer[] informers = null;
        WebCall webCall = new WebCall();
        String resultJsonString = null;
        resultJsonString = webCall.callServer(userId, "2", "2", "checkactives", "4", "5", "5", "5");
        if(resultJsonString.substring(0, 3).equals("200")){
            informers = webCall.getListInformersFromJsonString(resultJsonString.substring(3));
            informers = storage.setListInformers(informers);
        }
        return informers;
    }




    protected void clearAddingUsers(){
        storage.clearAddingUsers();
    }
    protected UserGroup newGroupAdding(String name){
        UserGroup result = null;
        String resultString = null;
        AddingUser[] users = storage.getAddingUsers();
        int length = users.length;
        try {
            JSONArray members = new JSONArray();
            for (int i = 0; i < length; i++) {
                members.put(i, users[i].getUserId());
            }
            String jsonString = members.toString();
            WebCall webCall = new WebCall();
            resultString = webCall.callServer(name, jsonString, "5", "newgroup", "3", "4", "4", "4");
            if(resultString.substring(0, 3).equals("200")){
                result = new UserGroup(name, resultString);
                clearAddingUsers();
            }
        }
        catch(JSONException e){                                                                         //TODO

        }
        return result;
    }




    protected String checkUserWeb(String id){
        String result = null;
        String resultString = null;
        WebCall webCall = new WebCall();
        resultString = webCall.callServer(id, "3", "3", "checkuser", "3", "3", "4", "4");
        if(resultString.substring(0, 3).equals("200")) {
            result = resultString.substring(3);
        }
        return result;
    }
    protected boolean checkUserRAM(String id){
        boolean result = false;
        AddingUser[] allUsers = storage.getAddingUsers();
        int i = 0;
        int length = allUsers.length;
        for(i = 0; i < length; i++){
            if(allUsers[i].getUserId().equals(id)){
                break;
            }
        }
        if(i < length){
            result = true;
        }
        return result;
    }
    protected AddingUser addUser(String userId){
        AddingUser newUser = null;
        String result = checkUserWeb(userId);
        if(!result.equals("No Such User(")){
            newUser = new AddingUser();
            newUser.setData(result, userId);
            storage.addOneAddingUser(newUser);
            storage.addOneAddingUser(newUser);
        }
        return newUser;
    }
    protected AddingUser removeUser(AddingUser user){
        if(user != null){
            user = storage.removeOneAddingUser(user);
        }
        return user;
    }
    protected AddingUser[] getAddingUsers(){
        AddingUser[] users = storage.getAddingUsers();
        return users;
    }




    protected boolean checkGroupActiveData(String groupId){
        boolean result = false;
        if(groupId != null) {
            result = storage.isAnyGroupActiveLists(groupId);
        }
        return result;
    }
    protected SList[] getGroupActiveData(String groupId){
        SList[] lists = null;
        /*if(storage.isAnyGroupActiveLists(groupId)){
            lists = storage.getGroupActive(groupId);
        }
        else{
            lists = getGroupActiveDataFromWeb(groupId);
            //storage.setGroupActiveData(lists, groupId);
        }*/
        lists = getGroupActiveDataFromWeb(groupId);
        return lists;
    }
    protected SList[] getGroupActiveDataFromWeb(String groupId){
        SList[] lists = null;
        WebCall webCall = new WebCall();
        String resultJsonString = webCall.callServer(groupId, "3", "3", "gettinglistograms", "3", "3", "3");
        if(resultJsonString.substring(0, 3).equals("200")){
            lists = webCall.getGroupListsFromJsonString(resultJsonString.substring(3));
        }
        setActiveListsToGroup(groupId, lists);
        return lists;
    }


    protected SList[] getGroupHistoryData(String groupId){
        SList[] lists;
        /*if(storage.isAnyGroupHistory(groupId)){
            lists = storage.getGroupHistory(groupId);
        }
        else{
            lists = getGroupDataFromWeb(groupId);
            //storage.setGroupHistoryData(lists, groupId);
        }*/
        lists = getGroupDataFromWeb(groupId);
        return lists;
    }
    protected SList[] getGroupDataFromWeb(String groupId){
        SList[] lists = null;
        WebCall webCall = new WebCall();
        String resultJsonString = webCall.callServer(groupId, "3", "3", "gettinghistory", "3", "3", "3");
        if(resultJsonString.substring(0, 3).equals("200")){
            lists = webCall.getGroupListsFromJsonString(resultJsonString.substring(3));
        }
        setHistoryListsToGroup(groupId, lists);
        return lists;
    }


    protected void setHistoryListsToGroup(String groupId, SList[] lists){
        UserGroup group = findGroupById(groupId);
        if(group != null){
            group.setHistoryLists(lists);
            int length = lists.length;
            for(int i = 0; i < length; i++){
                lists[i].setGroup(Integer.parseInt(groupId));
            }
        }
    }
    protected void setActiveListsToGroup(String groupId, SList[] lists){
        UserGroup group = findGroupById(groupId);
        if(group != null){
            group.setActiveLists(lists);
            int length = lists.length;
            for(int i = 0; i < length; i++){
                lists[i].setGroup(Integer.parseInt(groupId));
            }
        }
    }
    protected UserGroup findGroupById(String groupId){
        UserGroup[] groups = null;
        UserGroup result = null;
        groups = storage.getGroups();
        int i = 0;
        if(groups != null) {
            int length = groups.length;
            for (i = 0; i < length; i++) {
                if (groups[i].getId().equals(groupId)) {
                    break;
                }
            }
            if (i < length) {
                result  = groups[i];
            }
        }
        return result;
    }
    protected boolean updateGroupData(String groupId, String groupName){
        boolean result = false;                                                                         //?
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
        result = true;
        return result;
    }
    protected UserGroup confirmGroupChanges(UserGroup group){
        UserGroup result = null;
        String resultString;
        AddingUser[] users = storage.getAddingUsers();
        int length = users.length;
        try {
            JSONArray members = new JSONArray();
            for (int i = 0; i < length; i++) {
                members.put(i, users[i].getUserId());
            }
            String jsonString = members.toString();
            WebCall webCall = new WebCall();
            resultString = webCall.callServer(group.getId(), jsonString, group.getName(), "changegroup", "3", "4", "4", "4");
                                                                                                        //TODO decoding users from JSON
            if(resultString.substring(0, 3).equals("200")){
                group.setMembers(users);
                result = storage.updateGroup(findGroupById(group.getId()), group, true);
            }
        }
        catch(JSONException e){                                                                         //TODO

        }
        return result;
    }
    protected UserGroup leaveGroup(UserGroup group){
        String resultString = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        resultString = webCall.callServer(group.getId(), provider.userSessionData.getId(), "4", "leavegroup", "3", "4", "4", "4");
        if(!resultString.substring(0, 3).equals("200")){
            group = null;
        }
        else{
            storage.removeGroup(group);
        }
        return group;
    }



    protected UserGroup[] getGroupsFromWeb(){
        UserGroup[] groups = null;
        WebCall webCall = new WebCall();
        ActiveActivityProvider provider = (ActiveActivityProvider) context;
        String userId = String.valueOf(provider.userSessionData.getId());
        String resultJsonString = webCall.callServer(userId, "2", "2", "groupsearch", "5", "5", "5");
        if(resultJsonString.substring(0, 3).equals("200")) {
            groups = webCall.getGroupsFromJsonString(resultJsonString.substring(3));
            groups = storage.setGroups(groups);
        }
        return groups;
    }

    protected SList disactivateOnlineList(SList list){                                         //TODO optimise search by extending Button class and keeping direct cursor
        SList result = null;
        if (list != null) {
            WebCall webCall = new WebCall();
            String groupId = String.valueOf(list.getGroup());
            String listId = String.valueOf(list.getId());
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String userId = String.valueOf(provider.userSessionData.getId());
            String resultString = webCall.callServer(groupId, listId, userId, "disactivatelist", "3", "2", "2", "7");
            if (resultString.substring(0, 3).equals("200")) {
                int i;
                UserGroup[] groups = storage.getGroups();
                int length = groups.length;
                for(i = 0; i < length; i++) {
                    if(groups[i].getId().equals(groupId)){
                        break;
                    }
                }
                if(i < length){
                    groups[i].disactivateList(list);
                    result = list;
                }
            }
        }
        return result;
    }
    protected Item itemmarkOnline(Item item){
        Item result = null;
        if(item != null) {
            WebCall webCall = new WebCall();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String userId = String.valueOf(provider.userSessionData.getId());
            String itemId = String.valueOf(item.getId());
            String listId = String.valueOf(item.getList().getId());
            UserGroup[] groups = storage.getGroups();
            int i = 0;

            int length = groups.length;
            for (i = 0; i < length; i++) {
                if (groups[i].getId().equals(String.valueOf(item.getList().getGroup()))) {
                    break;
                }
            }
            if (i < length) {
                UserGroup group = groups[i];
                String groupId = group.getId();
                String resultString = webCall.callServer(userId, itemId, listId, "itemmark", "6", "6", groupId);
                if (resultString != null) {
                    if (resultString.codePointAt(0) == '2') {
                        result = item;
                        group.itemmark(item);
                    }
                }
            }
        }
        return result;
    }



    protected Item itemmarkOffline(Item item){
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
    protected SList disactivateOfflineList(SList list){
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
    protected SList addOfflineList(Item[] items){
        SList list;
        DataBaseTask2 addTask = new DataBaseTask2(context);
        list = addTask.addList(items);

        if(list != null) {
            storage.addNewList(list);
        }

        return list;
    }



    protected void saveTempItems(TempItem[] tempItems){
        storage.setTempItems(tempItems);
    }
    protected TempItem[] getTempItems(){
        TempItem[] result;
        result = storage.getTempItems();
        return result;
    }
    protected void clearTempItems(){
        storage.clearTempItems();
    }



    protected SList[] getOfflineActiveData(){
        SList[] lists;
        if(storage.isAnyOfflineActive()){
            lists = storage.getOfflineListsActive();
        }
        else{
            lists = getOfflineActiveDataFromMemory();
            storage.setOfflineActiveData(lists);
        }
        return lists;
    }
    protected boolean checkOfflineActiveLists(){
        boolean result = false;
        result = storage.isAnyOfflineActive();
        return result;
    }
    protected SList[] getOfflineHistoryData(){
        SList[] lists;
        if(storage.isAnyOfflineHistory()){
            lists = storage.getOfflineListsHistory();
        }
        else{
            lists = getOfflineHistoryDataFromMemory();
            storage.setOfflineHistoryData(lists);
        }
        return lists;
    }

    protected SList[] getOfflineActiveDataFromMemory(){
        SList[] lists;
        DataBaseTask2 task2 = new DataBaseTask2(context);
        lists = task2.getOffline(1);
        return lists;
    }
    protected SList[] getOfflineHistoryDataFromMemory(){
        SList[] lists;
        DataBaseTask2 task2 = new DataBaseTask2(context);
        lists = task2.getOffline(0);
        return lists;
    }
    protected void setOfflineActiveDataToDataStorage(SList[] lists){

    }
    protected void setOfflineHistoryDataToDataStorage(SList[] lists){

    }
}