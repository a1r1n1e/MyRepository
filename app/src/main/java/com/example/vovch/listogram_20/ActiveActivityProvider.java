package com.example.vovch.listogram_20;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.simple.CreateListogramActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.activities.simple.GroupList2Activity;
import com.example.vovch.listogram_20.activities.simple.GroupSettingsActivity;
import com.example.vovch.listogram_20.activities.simple.NewGroup;
import com.example.vovch.listogram_20.data_layer.DataExchanger;
import com.example.vovch.listogram_20.data_layer.UserSessionData;
import com.example.vovch.listogram_20.data_layer.async_tasks.ActiveListsInformerTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.AddUserTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.GroupActiveGetterTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.GroupChangeConfirmTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.GroupDataSetterTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.GroupHistoryGetterTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.GroupLeaverTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.GroupsGetterTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.LoginnerTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.NewDataBaseTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.NewGroupAdderTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.OfflineCreateListTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.OfflineDisactivateTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.OfflineItemmarkTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.OnlineCreateListogramTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.OnlineDisactivateTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.OnlineItemmarkTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.RegistrationTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.RemoveAddedUserTask;
import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ListInformer;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.TempItem;
import com.example.vovch.listogram_20.data_types.UserGroup;

/**
 * Created by vovch on 09.11.2017.
 */

public class ActiveActivityProvider extends Application {

    private Context activeActivity;
    private int activeActivityNumber;
    private UserGroup activeGroup;
    public DataExchanger dataExchanger;
    public UserSessionData userSessionData;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("MY", "onCreate WhoBuys ActiveActivityProvider");
        dataExchanger = DataExchanger.getInstance(ActiveActivityProvider.this);
        userSessionData = UserSessionData.getInstance(ActiveActivityProvider.this);
        nullActiveActivity();
    }
    public Context getActiveActivity(){
        return activeActivity;
    }
    public void setActiveActivity(int whichOne, Context context){
        activeActivity = context;
        activeActivityNumber = whichOne;
    }
    public void setActiveGroup(UserGroup newActiveGroup){
        activeGroup = newActiveGroup;
    }
    public UserGroup getActiveGroup(){
        return activeGroup;
    }
    public void nullActiveActivity(){
        activeActivity = null;
        activeActivityNumber = -1;
    }
    public int getActiveActivityNumber(){
        return  activeActivityNumber;
    }




    public void tryToLoginFromPrefs(){
        boolean prefCheck = false;
        boolean internetCheck = false;
        if(userSessionData.isAnyPrefsData()){
            prefCheck = true;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            internetCheck = true;
        }
        if (prefCheck && internetCheck) {
            tryToLoginWeb(userSessionData.getLogin(), userSessionData.getPassword());
        } else if(internetCheck){
            badLoginTry("Log Yourself In");
        } else {
            activeListsNoInternet();
        }
    }
    public void activeListsNoInternet(){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.noInternet();
        }
    }
    public void tryToLoginFromForms(String login, String password){
        tryToLoginWeb(login, password);
    }
    public void tryToLoginWeb(String login, String password){
        LoginnerTask loginnerTask = new LoginnerTask();
        loginnerTask.setApplicationContext(ActiveActivityProvider.this);
        loginnerTask.execute(login, password);
    }
    public void badLoginTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.onLoginFailed(result);
        }
    }
    public void goodLoginTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.loginToActiveFragmentChange();
        }
    }
    public void registrationTry(String login, String password){
        RegistrationTask registrationTask = new RegistrationTask();
        registrationTask.setApplicationContext(ActiveActivityProvider.this);
        registrationTask.execute(login, password);
    }
    public void goodRegistrationTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.registrationToActiveListsOnlineFragmentChange();
        }
    }
    public void badRegistrationTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.badRegistrationTry(result);
        }
    }





    public void startOfflineGetterDatabaseTask(boolean type){
        NewDataBaseTask newDataBaseTask = new NewDataBaseTask();
        newDataBaseTask.setApplicationContext(ActiveActivityProvider.this);
        newDataBaseTask.execute(String.valueOf(type));
    }
    public void activeActivityDisactivateList(SList list){
        OfflineDisactivateTask offlineDisactivateTask = new OfflineDisactivateTask();
        offlineDisactivateTask.setApplicationContext(ActiveActivityProvider.this);
        offlineDisactivateTask.execute(list);
    }
    public void activeListsItemmark(Item item){
        OfflineItemmarkTask offlineItemmarkTask = new OfflineItemmarkTask();
        offlineItemmarkTask.setApplicationContext(ActiveActivityProvider.this);
        offlineItemmarkTask.execute(item);
    }
    public void createListogramOffline(Item[] items){
        OfflineCreateListTask offlineCreateListTask = new OfflineCreateListTask();
        offlineCreateListTask.setApplicationContext(ActiveActivityProvider.this);
        offlineCreateListTask.execute(items);
    }




    public void getActiveActivityActiveLists(){
        ActiveListsInformerTask activeListsInformerTask = new ActiveListsInformerTask();
        activeListsInformerTask.setApplicationContext(ActiveActivityProvider.this);
        activeListsInformerTask.execute(String.valueOf(userSessionData.getId()));
    }
    public void showListInformersGottenGood(ListInformer[] result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showGood(result);
        }
    }
    public void showListInformersGottenBad(ListInformer[] result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showBad(result);
        }
    }




    public void confirmGroupSettingsChange(UserGroup changedGroup){
        GroupChangeConfirmTask groupChangeConfirmTask = new GroupChangeConfirmTask();
        groupChangeConfirmTask.setApplicationContext(ActiveActivityProvider.this);
        groupChangeConfirmTask.execute(changedGroup);
    }
    public void showGroupSettingsChangeGood(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmGood(result);
        }
    }
    public void showGroupSettingsChangeBad(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmBad(result);
        }
    }
    public void leaveGroup(){
        UserGroup group = getActiveGroup();
        GroupLeaverTask groupLeaverTask = new GroupLeaverTask();
        groupLeaverTask.setApplicationContext(ActiveActivityProvider.this);
        groupLeaverTask.execute(group);

    }
    public void leaveGroupGood(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupGood(result);
        }
    }
    public void leaveGroupBad(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupBad(result);
        }
    }




    public void addNewGroup(String groupName){
        NewGroupAdderTask newGroupAdderTask = new NewGroupAdderTask();
        newGroupAdderTask.setApplicationContext(ActiveActivityProvider.this);
        newGroupAdderTask.execute(groupName);
    }
    public AddingUser[] getPossibleMembers(){
        AddingUser[] users = dataExchanger.getAddingUsers();
        return users;
    }
    public void clearNewGroupPossibleMembers(){
        dataExchanger.clearAddingUsers();
    }
    public void showNewGroupAddedGood(UserGroup result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showGood(result);
        }
    }
    public void showNewGroupAddedBad(UserGroup result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showBad(result);
        }
    }
    public boolean checkUser(String id){
        boolean result = false;
        result = dataExchanger.checkUserRAM(id);
        return result;
    }
    public void addUserToGroup(String userId, String activityType){
        AddUserTask addUserTask = new AddUserTask();
        addUserTask.setApplicationContext(ActiveActivityProvider.this);
        addUserTask.execute(userId, activityType);
    }
    public void removeAddedUser(AddingUser user, String activityType){                                //TODO костыль
        if(activityType.equals("NewGroup")) {
            RemoveAddedUserTask removeAddedUserTask = new RemoveAddedUserTask();
            removeAddedUserTask.setApplicationContext(ActiveActivityProvider.this);
            removeAddedUserTask.execute(user, user);
        }
        else if(activityType.equals("GroupSettingsActivity")){
            RemoveAddedUserTask removeAddedUserTask = new RemoveAddedUserTask();
            removeAddedUserTask.setApplicationContext(ActiveActivityProvider.this);
            removeAddedUserTask.execute(user, null);
        }
    }
    public void showRemoveAddedUserNewGroupGood(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }
    public void showRemoveAddedUserNewGroupBad(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }
    public void showCheckUserNewGroupGood(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }
    public void showCheckUserNewGroupBad(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }
    public void showCheckUserSettingsGood(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }
    public void showCheckUserSettingsBad(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }
    public void showRemoveAddedUserSettingsGood(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }
    public void showRemoveAddedUserSettingsBad(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }




    public void getGroupHistoryLists(String groupId){
        GroupHistoryGetterTask groupHistoryGetterTask = new GroupHistoryGetterTask();
        groupHistoryGetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupHistoryGetterTask.execute(groupId);
    }
    public void getGroupActiveLists(String groupId){
        GroupActiveGetterTask groupActiveGetterTask = new GroupActiveGetterTask();
        groupActiveGetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupActiveGetterTask.execute(groupId);
    }
    public void disactivateGroupList(SList list){
        OnlineDisactivateTask onlineDisactivateTask = new OnlineDisactivateTask();
        onlineDisactivateTask.setApplicationContext(ActiveActivityProvider.this);
        onlineDisactivateTask.execute(list);
    }
    public void itemmark(Item item){
        OnlineItemmarkTask onlineItemmarkTask = new OnlineItemmarkTask();
        onlineItemmarkTask.setApplicationContext(ActiveActivityProvider.this);
        onlineItemmarkTask.execute(item);
    }




    public void createOnlineListogram(String groupId, Item[] items){
        OnlineCreateListogramTask onlineCreateListogramTask = new OnlineCreateListogramTask();
        onlineCreateListogramTask.setApplicationContext(ActiveActivityProvider.this);
        onlineCreateListogramTask.setUserId(String.valueOf(userSessionData.getId()));
        onlineCreateListogramTask.setGroupId(groupId);
        onlineCreateListogramTask.execute(items);
    }
    public void saveTempItems(TempItem[] tempItems){
        dataExchanger.saveTempItems(tempItems);
    }
    public TempItem[] getTempItems(){
        TempItem[] result;
        result = dataExchanger.getTempItems();
        return result;
    }




    public void getGroups(){
        GroupsGetterTask groupsGetterTask = new GroupsGetterTask();
        groupsGetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupsGetterTask.execute();
    }
    public void showGroupsGottenGood(UserGroup[] result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showGood(result);
        }
    }
    public void showGroupsGottenBad(UserGroup[] result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showBad(result);
        }
    }



    public void showTouchedGroupGoingGood(UserGroup result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.goToGroup(result);
        }
    }
    public void showTouchedGroupGoingBad(UserGroup result){                                          //TODO
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.update();
        }
    }




    public void setGroupData(String id, String name){
        GroupDataSetterTask groupDataSetterTask = new GroupDataSetterTask();
        groupDataSetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupDataSetterTask.execute(id, name);
    }
    public void showGroupDataSettledGood(String id){
        if(getActiveActivityNumber() == 3){
            if(getActiveGroup().getId().equals(id)){
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.showGroupDataSettledGood();
            }
        }
    }
    public void showGroupDataSettledBad(String id){
        if(getActiveActivityNumber() == 3){
            if(getActiveGroup().getId().equals(id)){
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.showGroupDataSettledBad();
            }
        }
    }




    public void showOnlineItemmarkedGood(Item item){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(item.getList().getGroup()))){
                activity.showThirdGood(item);
            }
        }
    }
    public void showOnlineItemmarkedBad(Item item){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(item.getList().getGroup()))){
                activity.showThirdBad(item);
            }
        }
    }
    public void showItemmarkProcessingToUser(Item item){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(item.getList().getGroup()))){
                activity.showItemmarkProcessing(item);
            }
        }
    }





    public void showOnlineDisactivateListGood(SList result){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(result.getGroup()))) {
                activity.showSecondGood(result);
                if(!dataExchanger.checkGroupActiveData(getActiveGroup().getId())){
                    activity.showBad(new SList[0]);
                }
            }
        }
    }
    public void showOnlineDisactivateListBad(SList result){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(result.getGroup()))) {
                activity.showSecondBad(result);
            }
        }
    }




    public void showOnlineListogramCreatedGood(){
        if(getActiveActivityNumber() == 6){
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showGood();
        }
    }
    public void showOnlineListogramCreatedBad(){                                                 //TODO

    }




    public void showGroupHistoryListsGood(SList[] lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)) {
                activity.historyLoadOnGood(lists);
            }
        }
    }
    public void showGroupHistoryListsBad(String lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)){
                activity.historyLoadOnBad("");                                                          //TODO error controlling
            }
        }
    }




    public void showGroupActiveListsGood(SList[] lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)){
                activity.showGood(lists);
            }
        }
    }
    public void showGroupActiveListsBad(SList[] lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)) {
                activity.showBad(lists);                                                                   //TODO error controlling
            }
        }
    }




    public void showOfflineListCreatedGood(SList list){
        if(getActiveActivityNumber() == 6){
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineGood();
        }
    }
    public void showOfflineListCreatedBad(SList list){
        if(getActiveActivityNumber() == 6){
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineBad();
        }
    }

    public void showOfflineActiveListsItemmarkedGood(Item item){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineGood(item);
        }
    }
    public void showOfflineActiveListsItemmarkedBad(Item item){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineBad(item);
        }
    }
    public void showOfflineActiveListsDisactivatedGood(SList list){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineGood(list);
            if(!dataExchanger.checkOfflineActiveLists()) {
                activity.showActiveOfflineBad(new SList[0]);
            }
        }
    }
    public void showOfflineActiveListsDisactivatedBad(SList list){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineBad(list);
        }
    }
    public void showOfflineActiveListsGood(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineGood(lists);
        }
    }
    public void showOfflineActiveListsBad(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineBad(lists);
        }
    }
    public void showOfflineHistoryListsGood(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineGood(lists);
        }
    }
    public void showOfflineHistoryListsBad(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineBad(lists);
        }
    }
}
