package whobuys.vovch.vovch.whobuys.data_layer;

import android.content.Context;
import android.transition.Slide;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        try {
            storage.clearAll();
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
    }

    public boolean synchronizeDB(){                                                                     //not working properly. works only expanding side
        boolean result = false;
        try{
            DataBaseTask2 dataBaseTask2 = new DataBaseTask2(context);
            result = dataBaseTask2.synchronizeOffline(storage.getOfflineListsActive(), storage.getOfflineListsHistory());
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public ListInformer[] getListInformers(String userId) {                                         //shouldn't be used
        ListInformer[] informers = null;
        try {
            WebCall webCall = new WebCall();
            String resultJsonString = null;
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            if (userId != null) {
                JSONArray jsonArray = new JSONArray();
                String jsonString = jsonArray.toString();
                //resultJsonString = webCall.callServer(userId, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "checkactives", jsonString, provider.userSessionData);
                resultJsonString = webCall.callServer("2", BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "check_group_updates", jsonString, provider.userSessionData);
                if (resultJsonString != null && resultJsonString.length() > 2 && resultJsonString.substring(0, 3).equals("200")) {
                    informers = webCall.getListInformersFromJsonString(resultJsonString.substring(3));
                    informers = storage.setListInformers(informers);
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return informers;
    }

    public ListInformer[] createListinformers(){
        ListInformer[] informers = null;
        try {
            UserGroup[] groups = storage.getGroups();
            informers = new ListInformer[groups.length];
            for(int i = 0; i< groups.length; i++){
                informers[i] = new ListInformer(groups[i].getId(), groups[i].getName(), groups[i].getState());
                informers[i].setGroup(groups[i]);
            }
            storage.setListInformers(informers);
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return informers;
    }

    public ListInformer[] getListInformersRAM(){
        ListInformer[] informers = null;
        try {
            informers = storage.getListInformers();
            if (informers == null) {
                informers = new ListInformer[0];
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return informers;
    }


    public void clearDeletableUsers() {
        try {
            storage.clearDeletableUsers();
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
    }

    public void clearAddedUsers(){
        try {
            storage.clearAddedUsers();
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
    }

    public UserGroup newGroupAdding(String name) {
        UserGroup result = null;
        try {
            UserGroup[] tempResult = null;
            String resultString = null;
            AddingUser[] users = storage.getAddedUsers();
            int length = users.length;
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
                if (resultString != null && resultString.length() > 2 && resultString.substring(0, 3).equals("200")) {
                    tempResult = webCall.getGroupsFromJsonString(resultString.substring(3));
                    result = tempResult[0];
                    storage.addGroup(result);
                    clearAddedUsers(); }
            }
        } catch (JSONException e) {
            Log.v("WhoBuys", "smth in DE");
        }
        return result;
    }


    private String checkUserWeb(String id) {
        String result = null;
        try {
            if (id != null) {
                String tempResultString = null;
                WebCall webCall = new WebCall();
                JSONArray jsonArray = new JSONArray();
                String jsonString = jsonArray.toString();
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                tempResultString = webCall.callServer(id, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "checkuser", jsonString, provider.userSessionData);
                if (tempResultString != null && tempResultString.length() > 2 && tempResultString.substring(0, 3).equals("200")) {
                    result = webCall.getStringFromJsonString(tempResultString.substring(3), "name");
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public boolean checkUserRAM(String id) {
        try {
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
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
            return false;
        }
    }

    public AddingUser addUser(String userId) {
        AddingUser newUser = null;
        try {
            String result = checkUserWeb(userId);
            if (result != null) {
                newUser = new AddingUser();
                newUser.setData(result, userId);
                storage.addOneAddedUser(newUser);
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return newUser;
    }

    public AddingUser removeUser(AddingUser user) {
        try {
            if (user != null) {
                if (storage.isDeletableUser(user)) {
                    user = storage.removeOneDeletableUser(user);
                } else if (storage.isAddedUser(user)) {
                    user = storage.removeOneAddedUser(user);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return user;
    }

    public AddingUser[] getAddingUsers() {
        try {
            return storage.getDeletableUsers();
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
            return null;
        }
    }

    public AddingUser[] makeAllUsersPossible(UserGroup group) {
        AddingUser[] users = null;
        try {
            if (group != null && group.getMembers() != null) {
                storage.clearDeletableUsers();
                storage.setDeletableUsers(group.getMembers());
                users = getAddingUsers();
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return users;
    }

    public UserGroup[] updateGroups(){
        try {
            UserGroup[] result = null;
            UserGroup[] groups;
            groups = storage.getGroups();

            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String resultJsonString = webCall.callServer(provider.userSessionData.getId(), BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "update_groups", jsonString, provider.userSessionData);
            if(resultJsonString != null && resultJsonString.length() > 2){
                DataBaseTask2 dataBaseTask2 = new DataBaseTask2(context);
                if(resultJsonString.substring(0, 3).equals("200")) {
                    groups = webCall.getGroupsFromJsonString(resultJsonString.substring(3));
                    groups = storage.setGroups(groups);
                    result = dataBaseTask2.resetGroups(groups);
                } else if(resultJsonString.substring(0, 3).equals("502")) {
                    provider.userSessionData.setNotLoggedIn();
                } else {
                    groups = dataBaseTask2.getGroups();
                    groups = storage.setGroups(groups);
                    result = groups;
                }
            }
            return result;
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
            return null;
        }
    }

    public UserGroup updateGroup(UserGroup group){
        try {
            UserGroup result = null;

            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            String resultJsonString = webCall.callServer(group.getId(), BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "check_group_updates", jsonString, provider.userSessionData);
            if(resultJsonString != null && resultJsonString.length() > 2){
                if(resultJsonString.substring(0, 3).equals("200")) {
                    UserGroup newGroup = webCall.getGroupFromJSONString(resultJsonString.substring(3));
                    newGroup = storage.resetGroup(group, newGroup);
                    DataBaseTask2 dataBaseTask2 = new DataBaseTask2(context);
                    result = dataBaseTask2.addGroup(newGroup);
                } else {
                    result = group;
                }
            }
            return result;
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
            return null;
        }
    }


    public boolean checkGroupActiveData(UserGroup group) {
        boolean result = false;
        try {
            if (group != null && group.getId() != null) {
                result = storage.isAnyGroupActiveLists(group);
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public SList[] getGroupActiveData(UserGroup group) {
        SList[] lists = null;
        try {
            // this way just now
            //lists = getGroupActiveDataFromWeb(group);
            lists = getGroupActiveDataRAM(group);
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    public SList[] getGroupActiveDataRAM(UserGroup group){
        SList[] lists = null;
        try {
            lists = storage.getGroupActive(group);
            if (lists == null) {
                lists = new SList[0];
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    private SList[] getGroupActiveDataFromWeb(UserGroup group) {
        SList[] lists = null;
        try {
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
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }


    public SList[] getGroupHistoryData(UserGroup group) {
        SList[] lists = null;
        try {
            // this way just now
            //lists = getGroupDataFromWeb(group);
            lists = getGroupHistoryDataRAM(group);
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    public SList[] getGroupHistoryDataRAM(UserGroup group){
        SList[] lists = null;
        try {
            lists = storage.getGroupHistory(group);
            if (lists == null) {
                lists = new SList[0];
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    private SList[] getGroupDataFromWeb(UserGroup group) {
        SList[] lists = null;
        try {
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
                if (resultJsonString != null && resultJsonString.length() > 2 && resultJsonString.substring(0, 3).equals("200")) {
                    lists = webCall.getGroupListsFromJsonString(resultJsonString.substring(3), group);
                    setHistoryListsToGroup(group, lists);
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }


    protected UserGroup getGroupData(String groupId){
        UserGroup result = null;
        try{
            DataBaseTask2 dataBaseTask2= new DataBaseTask2(context);
            result = dataBaseTask2.getGroupData(groupId);

        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }


    private void setHistoryListsToGroup(UserGroup group, SList[] lists) {
        try {
            if (group != null) {
                group.setHistoryLists(lists);
                int length = lists.length;
                for (int i = 0; i < length; i++) {
                    lists[i].setGroup(group);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
    }

    private void setActiveListsToGroup(UserGroup group, SList[] lists) {
        try {
            if (group != null) {
                group.setActiveLists(lists);
                int length = lists.length;
                for (int i = 0; i < length; i++) {
                    lists[i].setGroup(group);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
    }

    public boolean updateGroupData(String groupId, String groupName) {
        try {
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
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
            return false;
        }

    }

    public UserGroup confirmGroupChanges(UserGroup group, String newGroupName) {
        UserGroup result = null;
        try {
            String resultString;
            AddingUser[] oldUsers;
            AddingUser[] addedUsers;
            AddingUser[] deletableUsers;
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
            JSONArray members = new JSONArray();
            for (int i = 0; i < length; i++) {
                members.put(i, users[i].getUserId());
            }
            String jsonString = members.toString();
            WebCall webCall = new WebCall();
            resultString = webCall.callServer(group.getId(), newGroupName, BLANK_WEBCALL_FIELD, "changegroup", jsonString, provider.userSessionData);
            if (resultString != null && resultString.length() > 2 && resultString.substring(0, 3).equals("200")) {
                if (group.getOwner() != null) {
                    UserGroup newGroup = new UserGroup(newGroupName, group.getId(), group.getActiveLists(), group.getHistoryLists(), users);
                    newGroup.setOwner(group.getOwner());
                    result = storage.updateGroup(group, newGroup);
                    if (result != null) {
                        provider.setActiveGroup(result);
                        storage.clearDeletableUsers();
                        if (result.getOwner().equals(provider.userSessionData.getId())) {
                            provider.makeAllMembersPossible(result);
                        }
                    }
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public UserGroup leaveGroup(UserGroup group) {
        try {
            String resultString = null;
            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            ActiveActivityProvider provider = (ActiveActivityProvider) context;
            resultString = webCall.callServer(provider.userSessionData.getId(), group.getId(), BLANK_WEBCALL_FIELD, "leavegroup", jsonString, provider.userSessionData);
            if (resultString != null && resultString.length() > 2 && resultString.substring(0, 3).equals("200")) {
                storage.removeGroup(group);
                clearAddedUsers();
                clearDeletableUsers();
            } else {
                group = null;
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return group;
    }


    public UserGroup addOnlineList(Item[] itemsArray, UserGroup group) {
        UserGroup resultGroup = null;
        try {
            String result = null;
            if (itemsArray != null && group != null) {
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                WebCall webCall = new WebCall();
                String jsonString = webCall.prepareItemsJSONString(itemsArray);
                result = webCall.callServer(provider.userSessionData.getId(), BLANK_WEBCALL_FIELD, group.getId(), "sendlistogram", jsonString, provider.userSessionData);
                if (result != null && result.length() > 2 && result.substring(0, 3).equals("200")) {
                    resultGroup = updateGroup(group);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return resultGroup;
    }

    public UserGroup resendListToGroup(SList list, UserGroup group) {
        UserGroup resultGroup = null;
        try {
            String jsonString;
            if (list != null && group != null && list.getItems() != null) {
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                WebCall webCall = new WebCall();
                String itemsJSONString = webCall.prepareItemsJSONString(list.getItems());
                jsonString = webCall.callServer(provider.userSessionData.getId(), String.valueOf(list.getId()), group.getId(), "resendlist", itemsJSONString, provider.userSessionData);
                if (jsonString != null && jsonString.length() > 2 && jsonString.substring(0, 3).equals("200")) {
                    UserGroup[] groups = updateGroups();
                    resultGroup = group;
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return resultGroup;
    }

    public UserGroup[] getGroupsFromRAM(){
        UserGroup[] groups = null;
        try {
            groups = storage.getGroups();
            if (groups == null) {
                groups = new UserGroup[0];
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return groups;
    }

    public UserGroup[] getGroupsFromWeb() {
        UserGroup[] groups = null;
        try {
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
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return groups;
    }

    public UserGroup disactivateOnlineList(SList list) {
        UserGroup result = null;
        try {
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
                if (resultString != null && resultString.length() > 2 && resultString.substring(0, 3).equals("200")) {
                    //group.disactivateList(list);
                    result = updateGroup(group);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public boolean sendBug(String text){
        boolean result = false;
        try {
            if (text != null) {
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                WebCall webCall = new WebCall();
                JSONArray jsonArray = new JSONArray();
                String jsonString = jsonArray.toString();
                String tempString = webCall.callServer(text, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "bugreport", jsonString, provider.userSessionData);
                if (tempString != null && tempString.length() > 2 && tempString.substring(0, 3).equals("200")) {
                    result = true;
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public boolean dropHistory(){
        boolean result = false;
        try {
            storage.clearOfflineHistory();
            result = true;
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public UserGroup itemmarkOnline(Item item) {
        UserGroup result = null;
        try {
            if (item != null && item.getList() != null && item.getList().getGroup() != null) {

                result = item.getList().getGroup();

                WebCall webCall = new WebCall();
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                String userId = String.valueOf(provider.userSessionData.getId());
                String itemId = String.valueOf(item.getId());
                String listId = String.valueOf(item.getList().getId());
                String groupId = result.getId();
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(0, userId);
                jsonArray.put(1, groupId);
                jsonArray.put(2, listId);
                jsonArray.put(3, itemId);
                String jsonString = jsonArray.toString();
                String resultString = webCall.callServer(userId, BLANK_WEBCALL_FIELD, BLANK_WEBCALL_FIELD, "itemmark", jsonString, provider.userSessionData);
                if (resultString != null && resultString.length() > 2) {
                    if (resultString.substring(0, 3).equals("205")) {
                        result = updateGroup(result);
                        //group.itemmark(item);
                        //item.setOwner(null);
                        //item.setOwnerName(null);
                    } else if (resultString.substring(0, 3).equals("200")) {
                        result = updateGroup(result);
                        //group.itemmark(item);
                        //String ownerId = webCall.getStringFromJsonString(resultString.substring(3), "owner_id");
                        //String ownerName = webCall.getStringFromJsonString(resultString.substring(3), "owner_name");
                        //item.setOwner(ownerId);
                        //item.setOwnerName(ownerName);
                    } else{
                        result = null;
                    }
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }


    public Item itemmarkOffline(Item item) {
        Item result = null;
        try {
            //Item[] items = storage.getOfflineItemsOfActiveLists();
            if (item != null) {
                result = item;
                DataBaseTask2 memoryTask = new DataBaseTask2(context);
                memoryTask.itemMarkOffline(String.valueOf(result.getId()));
            }
            storage.itemmarkOffline(item);
            result = item;
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public SList disactivateOfflineList(SList list) {
        SList result = null;
        try {
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
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public SList addOfflineList(Item[] items) {
        SList list = null;
        try {
            if(items != null){
                DataBaseTask2 addTask = new DataBaseTask2(context);
                list = addTask.addList(items, "t");
                if(list != null) {
                    storage.addNewList(list);
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return list;
    }

    public TempItem[] makeTempItemsFromItems(Item[] items) {
        TempItem[] tempItems = null;
        try {
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
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return tempItems;
    }

    public void saveTempItems(TempItem[] tempItems) {
        try {
            storage.setTempItems(tempItems);
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
    }

    public TempItem[] getTempItems() {
        TempItem[] result = null;
        try {
            result = storage.getTempItems();
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public void clearTempItems() {
        try {
            storage.clearTempItems();
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
    }


    public SList redactOfflineList(SList list, Item[] items) {
        SList resultList = null;
        try {
            if (list != null && items != null) {
                DataBaseTask2 dataBaseTask2 = new DataBaseTask2(context);
                resultList = dataBaseTask2.redactOfflineList(list, items);
                if(resultList != null) {
                    resultList = storage.redactOfflineList(list, items);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return resultList;
    }

    public UserGroup redactOnlineList(SList list, Item[] items) {
        UserGroup result = null;
         try {
             if (list != null && items != null && list.getGroup().getId() != null) {
                 ActiveActivityProvider provider = (ActiveActivityProvider) context;
                 WebCall webCall = new WebCall();
                 String itemsJSONString = webCall.prepareItemsJSONString(items);
                 String resultString = webCall.callServer(provider.userSessionData.getId(), String.valueOf(list.getId()), list.getGroup().getId(), "redactlist", itemsJSONString, provider.userSessionData);
                 if (resultString != null && resultString.length() > 2 && resultString.substring(0, 3).equals("200")) {
                     //list.setItems(items);
                     result = updateGroup(list.getGroup());
                 }
             }
         } catch(Exception e){
             Log.d("WhoBuys", "DE");
         }
        return result;
    }


    public SList[] getOfflineActiveData() {
        SList[] lists = null;
        try {
            if (storage.isAnyOfflineActive()) {
                lists = storage.getOfflineListsActive();
            } else {
                lists = getOfflineActiveDataFromMemory();
                if (lists != null) {
                    storage.setOfflineActiveData(lists);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    public boolean checkOfflineActiveLists() {
        boolean result = false;
        try {
            result = storage.isAnyOfflineActive();
        } catch (Exception e){
            Log.d("WhoBuys", "DE");
        }
        return result;
    }

    public SList[] getOfflineHistoryData() {
        SList[] lists = null;
        try {
            if (storage.isAnyOfflineHistory()) {
                lists = storage.getOfflineListsHistory();
            } else {
                lists = getOfflineHistoryDataFromMemory();
                if (lists != null) {
                    storage.setOfflineHistoryData(lists);
                }
            }
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    private SList[] getOfflineActiveDataFromMemory() {
        SList[] lists = null;
        try {
            DataBaseTask2 task2 = new DataBaseTask2(context);
            lists = task2.getOffline(1);
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    private SList[] getOfflineHistoryDataFromMemory() {
        SList[] lists = null;
        try {
            DataBaseTask2 task2 = new DataBaseTask2(context);
            lists = task2.getOffline(0);
        } catch(Exception e){
            Log.d("WhoBuys", "DE");
        }
        return lists;
    }

    public void setOfflineActiveDataToDataStorage(SList[] lists) {

    }

    public void setOfflineHistoryDataToDataStorage(SList[] lists) {

    }
}