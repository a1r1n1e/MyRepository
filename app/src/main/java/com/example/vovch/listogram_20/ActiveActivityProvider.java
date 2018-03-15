package com.example.vovch.listogram_20;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.simple.CreateListogramActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.activities.simple.GroupList2Activity;
import com.example.vovch.listogram_20.activities.simple.GroupSettingsActivity;
import com.example.vovch.listogram_20.activities.simple.NewGroup;
import com.example.vovch.listogram_20.activities.simple.SendBugActivity;
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
import com.example.vovch.listogram_20.data_layer.async_tasks.RedactOfflineListTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.RedactOnlineListTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.RegistrationTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.RemoveAddedUserTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.ResendListToGroupTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.SendBugTask;
import com.example.vovch.listogram_20.data_layer.async_tasks.SessionCheckerTask;
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
    private int activeListsActivityLoadType;
    private UserGroup activeGroup;
    private SList list;
    public DataExchanger dataExchanger;
    public UserSessionData userSessionData;
    public int debugger;

    @Override
    public void onCreate() {
        super.onCreate();
        debugger = 0;
        activeListsActivityLoadType = 1;
        Log.w("MY", "onCreate WhoBuys ActiveActivityProvider");
        dataExchanger = DataExchanger.getInstance(ActiveActivityProvider.this);
        userSessionData = UserSessionData.getInstance(ActiveActivityProvider.this);
        nullActiveActivity();
    }

    public Context getActiveActivity() {
        return activeActivity;
    }

    public void setActiveActivity(int whichOne, Context context) {
        activeActivity = context;
        activeActivityNumber = whichOne;
    }

    public int getActiveListsActivityLoadType(){
        return activeListsActivityLoadType;
    }

    public void setActiveListsActivityLoadType(int newType){
        activeListsActivityLoadType = newType;
    }

    public void setActiveGroup(UserGroup newActiveGroup) {
        activeGroup = newActiveGroup;
    }

    public UserGroup getActiveGroup() {
        return activeGroup;
    }

    public void nullActiveActivity() {
        activeActivity = null;
        activeActivityNumber = -1;
    }

    public int getActiveActivityNumber() {
        return activeActivityNumber;
    }

    public void setResendingList(SList newList){
        list = newList;
    }

    public SList getResendingList(){
        return list;
    }

    public void nullResendingList(){
        list = null;
    }

    public boolean isAnyResendingList(){
        boolean result = false;
        if(list != null){
            result = true;
        }
        return result;
    }


    public void tryToLoginFromPrefs() {
        boolean prefCheck = false;
        boolean internetCheck = false;
        if (userSessionData.isAnyPrefsData()) {
            prefCheck = true;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            internetCheck = true;
        }
        if (prefCheck && internetCheck) {
            checkSessionWeb();
        } else if (internetCheck) {
            badLoginTry(getResources().getString(R.string.log_yourself_in));
        } else {
            activeListsNoInternet();
        }
    }

    public void showExitGood(){
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.activeToLoginFragmentChange();
        }
    }

    public void showExitBad(){
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();                   //TODO
        }
    }

    public void activeListsNoInternet() {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.noInternet();
        }
    }

    public void tryToLoginFromForms(String login, String password) {
        boolean internetCheck = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            internetCheck = true;
        }
        if(internetCheck) {
            tryToLoginWeb(login, password);
        } else{
            activeListsNoInternet();
        }
    }

    public void tryToLoginWeb(String login, String password) {
        LoginnerTask loginnerTask = new LoginnerTask();
        loginnerTask.execute(login, password, ActiveActivityProvider.this);
    }

    public void  checkSessionWeb(){
        SessionCheckerTask sessionCheckerTask = new SessionCheckerTask();
        sessionCheckerTask.execute(ActiveActivityProvider.this);
    }

    public void goodSessionCheck(){
        goodLoginTry(null);
    }

    public void badSessionCheck(){
        badLoginTry(getString(R.string.some_error));
    }

    public void badLoginTry(String result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.onLoginFailed(result);
        }
    }

    public void goodLoginTry(String result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.loginToActiveFragmentChange();
        }
    }

    public void registrationTry(String login, String password) {
        RegistrationTask registrationTask = new RegistrationTask();
        registrationTask.execute(login, password, ActiveActivityProvider.this);
    }

    public void goodRegistrationTry(String result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.registrationToActiveListsOnlineFragmentChange();
        }
    }

    public void badRegistrationTry(String result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.badRegistrationTry(result);
        }
    }

    public void sendBug(String text){
        SendBugTask sendBugTask = new SendBugTask();
        sendBugTask.execute(text, ActiveActivityProvider.this);
    }

    public void showSendBugGood(){
        if (getActiveActivityNumber() == 8) {
            SendBugActivity activity = (SendBugActivity) getActiveActivity();
            activity.showGood();
        }
    }

    public void showSendBugBad(){
        if (getActiveActivityNumber() == 8) {
            SendBugActivity activity = (SendBugActivity) getActiveActivity();
            activity.showBad();
        }
    }

    public void startOfflineGetterDatabaseTask(boolean type) {
        NewDataBaseTask newDataBaseTask = new NewDataBaseTask();
        newDataBaseTask.execute(type, ActiveActivityProvider.this);
    }

    public void activeActivityDisactivateList(SList list) {
        OfflineDisactivateTask offlineDisactivateTask = new OfflineDisactivateTask();
        offlineDisactivateTask.execute(list, ActiveActivityProvider.this);
    }

    public void activeListsItemmark(Item item) {
        OfflineItemmarkTask offlineItemmarkTask = new OfflineItemmarkTask();
        offlineItemmarkTask.execute(item, ActiveActivityProvider.this);
    }

    public void createListogramOffline(Item[] items, WithLoginActivity activity) {
        OfflineCreateListTask offlineCreateListTask = new OfflineCreateListTask();
        offlineCreateListTask.execute(items, activity, ActiveActivityProvider.this);
    }

    public void getActiveActivityActiveLists() {
        ActiveListsInformerTask activeListsInformerTask = new ActiveListsInformerTask();
        activeListsInformerTask.execute(String.valueOf(userSessionData.getId()), ActiveActivityProvider.this);
    }

    public void showListInformersGottenGood(ListInformer[] result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showGood(result);
        }
    }

    public void showListInformersGottenBad(ListInformer[] result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showBad(result);
        }
    }


    public void confirmGroupSettingsChange(UserGroup changedGroup, String newGroupName) {
        GroupChangeConfirmTask groupChangeConfirmTask = new GroupChangeConfirmTask();
        groupChangeConfirmTask.execute(changedGroup, newGroupName, ActiveActivityProvider.this);
    }

    public void showGroupSettingsChangeGood(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmGood(result);
        }
    }

    public void showGroupSettingsChangeBad(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmBad(result);
        }
    }

    public void leaveGroup() {
        UserGroup group = getActiveGroup();
        GroupLeaverTask groupLeaverTask = new GroupLeaverTask();
        groupLeaverTask.setApplicationContext(ActiveActivityProvider.this);
        groupLeaverTask.execute(group);

    }

    public void leaveGroupGood(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupGood(result);
        }
    }

    public void leaveGroupBad(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupBad(result);
        }
    }


    public void addNewGroup(String groupName) {
        NewGroupAdderTask newGroupAdderTask = new NewGroupAdderTask();
        newGroupAdderTask.setApplicationContext(ActiveActivityProvider.this);
        newGroupAdderTask.execute(groupName);
    }

    public AddingUser[] getPossibleMembers() {
        AddingUser[] users = dataExchanger.getAddingUsers();
        return users;
    }

    public AddingUser[] makeAllMembersPossible(UserGroup group) {
        clearNewGroupPossibleMembers();
        AddingUser[] users = null;
        if (getActiveGroup() != null) {
            users = dataExchanger.makeAllUsersPossible(group);
        }
        return users;
    }

    public void clearNewGroupPossibleMembers() {
        dataExchanger.clearAddingUsers();
    }

    public void showNewGroupAddedGood(UserGroup result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showGood(result);
        }
    }

    public void showNewGroupAddedBad(UserGroup result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showBad(result);
        }
    }

    public boolean checkUser(String id) {
        boolean result = false;
        result = dataExchanger.checkUserRAM(id);
        return result;
    }

    public void addUserToGroup(String userId, String activityType) {
        AddUserTask addUserTask = new AddUserTask();
        addUserTask.setApplicationContext(ActiveActivityProvider.this);
        addUserTask.execute(userId, activityType);
    }

    public void removeAddedUser(AddingUser user, WithLoginActivity activity) {
        RemoveAddedUserTask removeAddedUserTask = new RemoveAddedUserTask();
        removeAddedUserTask.execute(user, activity, ActiveActivityProvider.this);
    }

    public void showRemoveAddedUserNewGroupGood(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }

    public void showRemoveAddedUserNewGroupBad(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }

    public void showCheckUserNewGroupGood(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }

    public void showCheckUserNewGroupBad(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }

    public void showCheckUserSettingsGood(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }

    public void showCheckUserSettingsBad(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }

    public void showRemoveAddedUserSettingsGood(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }

    public void showRemoveAddedUserSettingsBad(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }


    public void getGroupHistoryLists(UserGroup group) {
        GroupHistoryGetterTask groupHistoryGetterTask = new GroupHistoryGetterTask();
        groupHistoryGetterTask.execute(group, ActiveActivityProvider.this);
    }

    public void getGroupActiveLists(UserGroup group) {
        GroupActiveGetterTask groupActiveGetterTask = new GroupActiveGetterTask();
        groupActiveGetterTask.execute(group, ActiveActivityProvider.this);
    }

    public void disactivateGroupList(SList list) {
        OnlineDisactivateTask onlineDisactivateTask = new OnlineDisactivateTask();
        onlineDisactivateTask.execute(list, ActiveActivityProvider.this);
    }

    public void itemmark(Item item) {
        OnlineItemmarkTask onlineItemmarkTask = new OnlineItemmarkTask();
        onlineItemmarkTask.execute(item, ActiveActivityProvider.this);
    }


    public void createOnlineListogram(UserGroup group, Item[] items, WithLoginActivity activity) {
        OnlineCreateListogramTask onlineCreateListogramTask = new OnlineCreateListogramTask();
        onlineCreateListogramTask.execute(items, group, ActiveActivityProvider.this, activity);
    }

    public void redactOnlineListogram(Item[] items, SList redactingList ,WithLoginActivity activity){
        RedactOnlineListTask redactOnlineListTask = new RedactOnlineListTask();
        redactOnlineListTask.execute(redactingList, items, activity, ActiveActivityProvider.this);
    }

    public void showOnlineListRedactedGood(SList resultList, WithLoginActivity incomingActivity){
        if (getActiveActivityNumber() == 6 && incomingActivity instanceof CreateListogramActivity) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOnlineGood();
        }
    }

    public void showOnlineListRedactedBad(SList resultList, WithLoginActivity activity){

    }           //TODO

    public void redactOfflineListogram(Item[] items, SList redactingList ,WithLoginActivity activity){
        RedactOfflineListTask redactOfflineListTask = new RedactOfflineListTask();
        redactOfflineListTask.execute(redactingList, items, activity, ActiveActivityProvider.this);
    }

    public void showOfflineListRedactedGood(SList resultList, WithLoginActivity incomingActivity){
        if (getActiveActivityNumber() == 6 && incomingActivity instanceof CreateListogramActivity) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineGood();
        }
    }

    public void showOfflineListRedactedBad(SList resultList, WithLoginActivity activity){

    }           //TODO

    public void saveTempItems(TempItem[] tempItems) {
        dataExchanger.saveTempItems(tempItems);
    }

    public TempItem[] getTempItems() {
        TempItem[] result;
        result = dataExchanger.getTempItems();
        return result;
    }


    public void getGroups() {
        GroupsGetterTask groupsGetterTask = new GroupsGetterTask();
        groupsGetterTask.execute(ActiveActivityProvider.this);
    }

    public void showGroupsGottenGood(UserGroup[] result) {
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showGood(result);
        }
    }

    public void showGroupsGottenBad(UserGroup[] result) {
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showBad(result);
        }
    }

    public void resendListToGroup(SList resendingList, UserGroup group){
        ResendListToGroupTask resendListToGroupTask = new ResendListToGroupTask();
        resendListToGroupTask.execute(resendingList, group, ActiveActivityProvider.this);
    }

    public void resendListToGroupGood(UserGroup result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.goToGroup(result);
            nullResendingList();
        }
    }
    public void resendListToGroupBad(UserGroup result){

    }                                           //TODO


    public void showTouchedGroupGoingGood(UserGroup result) {
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.goToGroup(result);
        }
    }

    public void showTouchedGroupGoingBad(UserGroup result) {                                          //TODO
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.update();
        }
    }


    public void setGroupData(String id, String name) {
        GroupDataSetterTask groupDataSetterTask = new GroupDataSetterTask();
        groupDataSetterTask.setApplicationContext(ActiveActivityProvider.this);
        groupDataSetterTask.execute(id, name);
    }

    public void showGroupDataSettledGood(String id) {
        if (getActiveActivityNumber() == 3) {
            if (getActiveGroup().getId().equals(id)) {
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.showGroupDataSettledGood();
            }
        }
    }

    public void showGroupDataSettledBad(String id) {
        if (getActiveActivityNumber() == 3) {
            if (getActiveGroup().getId().equals(id)) {
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.showGroupDataSettledBad();
            }
        }
    }


    public void showOnlineItemmarkedGood(Item item) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().equals(item.getList().getGroup())) {
                activity.showThirdGood(item);
            }
        }
    }

    public void showOnlineItemmarkedBad(Item item) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().equals(item.getList().getGroup())) {
                activity.showThirdBad(item);
            }
        }
    }

    public void showItemmarkProcessingToUser(Item item) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(String.valueOf(item.getList().getGroup()))) {
                activity.showItemmarkProcessing(item);
            }
        }
    }


    public void showOnlineDisactivateListGood(SList result) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().equals(result.getGroup())) {
                activity.showSecondGood(result);
                if (!dataExchanger.checkGroupActiveData(getActiveGroup())) {
                    activity.showBad(new SList[0]);
                }
            }
        }
    }

    public void showOnlineDisactivateListBad(SList result) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().equals(result.getGroup())) {
                activity.showSecondBad(result);
            }
        }
    }


    public void showOnlineListogramCreatedGood() {
        if (getActiveActivityNumber() == 6) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOnlineGood();
        }
    }

    public void showOnlineListogramCreatedBad() {
        if (getActiveActivityNumber() == 6) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOnlineBad();
        }
    }

    public void showGroupHistoryListsGood(SList[] lists, String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.historyLoadOnGood(lists);
            }
        }
    }

    public void showGroupHistoryListsBad(String lists, String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.historyLoadOnBad("");                                                          //TODO error controlling
            }
        }
    }

    public void showGroupActiveListsGood(SList[] lists, String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.showGood(lists);
            }
        }
    }

    public void showGroupActiveListsBad(SList[] lists, String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.showBad(lists);                                                                   //TODO error controlling
            }
        }
    }


    public void showOfflineListCreatedGood(SList list, WithLoginActivity activity) {
        if (getActiveActivityNumber() == 6 && activity instanceof CreateListogramActivity) {
            CreateListogramActivity createListogramActivity = (CreateListogramActivity) getActiveActivity();
            createListogramActivity.showAddListOfflineGood();
        }
        else if(getActiveActivityNumber() == 2 && activity instanceof ActiveListsActivity){
            ActiveListsActivity activeListsActivity = (ActiveListsActivity) getActiveActivity();
            activeListsActivity.refreshOfflineLists();
        }
    }

    public void showOfflineListCreatedBad(SList list, WithLoginActivity activity) {
        if (getActiveActivityNumber() == 6 && activity instanceof CreateListogramActivity) {
            CreateListogramActivity createListogramActivity = (CreateListogramActivity) getActiveActivity();
            createListogramActivity.showAddListOfflineBad();
        }
        else if(getActiveActivityNumber() == 2 && activity instanceof ActiveListsActivity){
            ActiveListsActivity activeListsActivity = (ActiveListsActivity) getActiveActivity();
            activeListsActivity.refreshOfflineLists();
        }
    }

    public void showOfflineActiveListsItemmarkedGood(Item item) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineGood(item);
        }
    }

    public void showOfflineActiveListsItemmarkedBad(Item item) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineBad(item);
        }
    }

    public void showOfflineActiveListsDisactivatedGood(SList list) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineGood(list);
            if (!dataExchanger.checkOfflineActiveLists()) {
                activity.showActiveOfflineBad(new SList[0]);
            }
        }
    }

    public void showOfflineActiveListsDisactivatedBad(SList list) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineBad(list);
        }
    }

    public void showOfflineActiveListsGood(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineGood(lists);
        }
    }

    public void showOfflineActiveListsBad(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineBad(lists);
        }
    }

    public void showOfflineHistoryListsGood(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineGood(lists);
        }
    }

    public void showOfflineHistoryListsBad(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineBad(lists);
        }
    }
}
