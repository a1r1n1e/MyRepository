package com.example.vovch.listogram_20;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.messaging.RemoteMessage;

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
    }
    protected Context getActiveActivity(){
        return activeActivity;
    }
    protected void setActiveActivity(int whichOne, Context context){
        activeActivity = context;
        activeActivityNumber = whichOne;
    }
    protected void setActiveGroup(UserGroup newActiveGroup){
        activeGroup = newActiveGroup;
    }
    protected UserGroup getActiveGroup(){
        return activeGroup;
    }
    protected void nullActiveActivity(){
        activeActivity = null;
        activeActivityNumber = -1;
    }
    protected int getActiveActivityNumber(){
        return  activeActivityNumber;
    }




    protected void tryToLoginFromPrefs(){
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
    protected void activeListsNoInternet(){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.noInternet();
        }
    }
    protected void tryToLoginFromForms(String login, String password){
        tryToLoginWeb(login, password);
    }
    protected void tryToLoginWeb(String login, String password){
        LoginnerTask loginnerTask = new LoginnerTask();
        loginnerTask.setApplicationContext(ActiveActivityProvider.this);
        loginnerTask.execute(login, password);
    }
    protected void badLoginTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.onLoginFailed(result);
        }
    }
    protected void goodLoginTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.loginToActiveFragmentChange();
        }
    }
    protected void registrationTry(String login, String password){
        RegistrationTask registrationTask = new RegistrationTask();
        registrationTask.setApplicationContext(ActiveActivityProvider.this);
        registrationTask.execute(login, password);
    }
    protected void goodRegistrationTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.registrationToActiveListsOnlineFragmentChange();
        }
    }
    protected void badRegistrationTry(String result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.badRegistrationTry(result);
        }
    }





    protected void startOfflineGetterDatabaseTask(boolean type){
        NewDataBaseTask newDataBaseTask = new NewDataBaseTask();
        newDataBaseTask.setApplicationContext(ActiveActivityProvider.this);
        newDataBaseTask.execute(String.valueOf(type));
    }
    protected void activeActivityDisactivateList(SList list){
        OfflineDisactivateTask offlineDisactivateTask = new OfflineDisactivateTask();
        offlineDisactivateTask.setApplicationContext(ActiveActivityProvider.this);
        offlineDisactivateTask.execute(list);
    }
    protected void activeListsItemmark(Item item){
        OfflineItemmarkTask offlineItemmarkTask = new OfflineItemmarkTask();
        offlineItemmarkTask.setApplicationContext(ActiveActivityProvider.this);
        offlineItemmarkTask.execute(item);
    }
    protected void createListogramOffline(Item[] items){
        OfflineCreateListTask offlineCreateListTask = new OfflineCreateListTask();
        offlineCreateListTask.setApplicationContext(ActiveActivityProvider.this);
        offlineCreateListTask.execute(items);
    }




    protected void getActiveActivityActiveLists(){
        ActiveListsInformerTask activeListsInformerTask = new ActiveListsInformerTask();
        activeListsInformerTask.setApplicationContext(ActiveActivityProvider.this);
        activeListsInformerTask.execute(String.valueOf(userSessionData.getId()));
    }
    protected void showListInformersGottenGood(ListInformer[] result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showGood(result);
        }
    }
    protected void showListInformersGottenBad(ListInformer[] result){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showBad(result);
        }
    }




    protected void confirmGroupSettingsChange(UserGroup changedGroup){
        GroupChangeConfirmTask groupChangeConfirmTask = new GroupChangeConfirmTask();
        groupChangeConfirmTask.setApplicationContext(ActiveActivityProvider.this);
        groupChangeConfirmTask.execute(changedGroup);
    }
    protected void showGroupSettingsChangeGood(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmGood(result);
        }
    }
    protected void showGroupSettingsChangeBad(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmBad(result);
        }
    }
    protected void leaveGroup(){
        UserGroup group = getActiveGroup();
        GroupLeaverTask groupLeaverTask = new GroupLeaverTask();
        groupLeaverTask.setApplicationContext(ActiveActivityProvider.this);
        groupLeaverTask.execute(group);

    }
    protected void leaveGroupGood(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupGood(result);
        }
    }
    protected void leaveGroupBad(UserGroup result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupBad(result);
        }
    }




    protected void addNewGroup(String groupName){
        NewGroupAdderTask newGroupAdderTask = new NewGroupAdderTask();
        newGroupAdderTask.setApplicationContext(ActiveActivityProvider.this);
        newGroupAdderTask.execute(groupName);
    }
    protected AddingUser[] getPossibleMembers(){
        AddingUser[] users = dataExchanger.getAddingUsers();
        return users;
    }
    protected void clearNewGroupPossibleMembers(){
        dataExchanger.clearAddingUsers();
    }
    protected void showNewGroupAddedGood(UserGroup result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showGood(result);
        }
    }
    protected void showNewGroupAddedBad(UserGroup result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showBad(result);
        }
    }
    protected boolean checkUser(String id){
        boolean result = false;
        result = dataExchanger.checkUserRAM(id);
        return result;
    }
    protected void addUserToGroup(String userId, String activityType){
        AddUserTask addUserTask = new AddUserTask();
        addUserTask.setApplicationContext(ActiveActivityProvider.this);
        addUserTask.execute(userId, activityType);
    }
    protected  void removeAddedUser(AddingUser user, String activityType){                                //TODO костыль
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
    protected void showRemoveAddedUserNewGroupGood(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }
    protected void showRemoveAddedUserNewGroupBad(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }
    protected void showCheckUserNewGroupGood(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }
    protected void showCheckUserNewGroupBad(AddingUser result){
        if(getActiveActivityNumber() == 5){
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }
    protected void showCheckUserSettingsGood(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }
    protected void showCheckUserSettingsBad(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }
    protected void showRemoveAddedUserSettingsGood(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }
    protected void showRemoveAddedUserSettingsBad(AddingUser result){
        if(getActiveActivityNumber() == 7){
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }




    protected void getGroupHistoryLists(String groupId){
        GroupHistoryGetterTask groupHistoryGetterTask = new GroupHistoryGetterTask();
        groupHistoryGetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupHistoryGetterTask.execute(groupId);
    }
    protected void getGroupActiveLists(String groupId){
        GroupActiveGetterTask groupActiveGetterTask = new GroupActiveGetterTask();
        groupActiveGetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupActiveGetterTask.execute(groupId);
    }
    protected void disactivateGroupList(SList list){
        OnlineDisactivateTask onlineDisactivateTask = new OnlineDisactivateTask();
        onlineDisactivateTask.setApplicationContext(ActiveActivityProvider.this);
        onlineDisactivateTask.execute(list);
    }
    protected void itemmark(Item item){
        OnlineItemmarkTask onlineItemmarkTask = new OnlineItemmarkTask();
        onlineItemmarkTask.setApplicationContext(ActiveActivityProvider.this);
        onlineItemmarkTask.execute(item);
    }




    protected void createOnlineListogram(String groupId, Item[] items){
        OnlineCreateListogramTask onlineCreateListogramTask = new OnlineCreateListogramTask();
        onlineCreateListogramTask.setApplicationContext(ActiveActivityProvider.this);
        onlineCreateListogramTask.setUserId(String.valueOf(userSessionData.getId()));
        onlineCreateListogramTask.setGroupId(groupId);
        onlineCreateListogramTask.execute(items);
    }
    protected void saveTempItems(TempItem[] tempItems){
        dataExchanger.saveTempItems(tempItems);
    }
    protected TempItem[] getTempItems(){
        TempItem[] result;
        result = dataExchanger.getTempItems();
        return result;
    }




    protected void getGroups(){
        GroupsGetterTask groupsGetterTask = new GroupsGetterTask();
        groupsGetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupsGetterTask.execute();
    }
    protected void showGroupsGottenGood(UserGroup[] result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showGood(result);
        }
    }
    protected void showGroupsGottenBad(UserGroup[] result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showBad(result);
        }
    }



    protected void showTouchedGroupGoingGood(UserGroup result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.goToGroup(result);
        }
    }
    protected void showTouchedGroupGoingBad(UserGroup result){                                          //TODO
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.update();
        }
    }




    protected void setGroupData(String id, String name){
        GroupDataSetterTask groupDataSetterTask = new GroupDataSetterTask();
        groupDataSetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupDataSetterTask.execute(id, name);
    }
    protected void showGroupDataSettledGood(String id){
        if(getActiveActivityNumber() == 3){
            if(getActiveGroup().getId().equals(id)){
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.showGroupDataSettledGood();
            }
        }
    }
    protected void showGroupDataSettledBad(String id){
        if(getActiveActivityNumber() == 3){
            if(getActiveGroup().getId().equals(id)){
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.showGroupDataSettledBad();
            }
        }
    }




    protected void showOnlineItemmarkedGood(Item item){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(item.getList().getGroup()))){
                activity.showThirdGood(item);
            }
        }
    }
    protected void showOnlineItemmarkedBad(Item item){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(item.getList().getGroup()))){
                activity.showThirdBad(item);
            }
        }
    }
    protected void showItemmarkProcessingToUser(Item item){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(item.getList().getGroup()))){
                activity.showItemmarkProcessing(item);
            }
        }
    }





    protected void showOnlineDisactivateListGood(SList result){
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
    protected void showOnlineDisactivateListBad(SList result){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(String.valueOf(result.getGroup()))) {
                activity.showSecondBad(result);
            }
        }
    }




    protected void showOnlineListogramCreatedGood(){
        if(getActiveActivityNumber() == 6){
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showGood();
        }
    }
    protected void showOnlineListogramCreatedBad(){                                                 //TODO

    }




    protected void showGroupHistoryListsGood(SList[] lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)) {
                activity.historyLoadOnGood(lists);
            }
        }
    }
    protected void showGroupHistoryListsBad(String lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)){
                activity.historyLoadOnBad("");                                                          //TODO error controlling
            }
        }
    }




    protected void showGroupActiveListsGood(SList[] lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)){
                activity.showGood(lists);
            }
        }
    }
    protected void showGroupActiveListsBad(SList[] lists, String groupId){
        if(getActiveActivityNumber() == 3){
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if(getActiveGroup().getId().equals(groupId)) {
                activity.showBad(lists);                                                                   //TODO error controlling
            }
        }
    }




    protected void showOfflineListCreatedGood(SList list){
        if(getActiveActivityNumber() == 6){
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineGood();
        }
    }
    protected void showOfflineListCreatedBad(SList list){
        if(getActiveActivityNumber() == 6){
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineBad();
        }
    }

    protected void showOfflineActiveListsItemmarkedGood(Item item){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineGood(item);
        }
    }
    protected void showOfflineActiveListsItemmarkedBad(Item item){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineBad(item);
        }
    }
    protected void showOfflineActiveListsDisactivatedGood(SList list){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineGood(list);
            if(!dataExchanger.checkOfflineActiveLists()) {
                activity.showActiveOfflineBad(new SList[0]);
            }
        }
    }
    protected void showOfflineActiveListsDisactivatedBad(SList list){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineBad(list);
        }
    }
    protected void showOfflineActiveListsGood(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineGood(lists);
        }
    }
    protected void showOfflineActiveListsBad(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineBad(lists);
        }
    }
    protected void showOfflineHistoryListsGood(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineGood(lists);
        }
    }
    protected void showOfflineHistoryListsBad(SList[] lists){
        if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineBad(lists);
        }
    }
}
