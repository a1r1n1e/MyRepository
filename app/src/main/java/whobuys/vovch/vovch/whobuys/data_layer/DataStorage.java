package whobuys.vovch.vovch.whobuys.data_layer;

import whobuys.vovch.vovch.whobuys.data_types.AddingUser;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.ListInformer;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.TempItem;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vovch on 23.12.2017.
 */

public class DataStorage {
    private static DataStorage instance;
    private ArrayList<SList> ListsOfflineActive = new ArrayList<>();
    private ArrayList<Item> ItemsOfflineOfActiveLists = new ArrayList<>();
    private ArrayList<SList> ListsOfflineHistory = new ArrayList<>();
    private ArrayList<Item> ItemsOfflineOfHistoryLists = new ArrayList<>();
    private ArrayList<UserGroup> Groups = new ArrayList<>();
    private ArrayList<AddingUser> AddedUsers = new ArrayList<>();
    private ArrayList<AddingUser> DeletableUsers = new ArrayList<>();
    private ArrayList<ListInformer> ListInformers = new ArrayList<>();
    private ArrayList<TempItem> TempItems = new ArrayList();

    private DataStorage(){
    }
    public static DataStorage getInstance(){
        if(instance == null){
            instance = new DataStorage();
        }
        return instance;
    }

    protected void clearAll(){
        ListsOfflineActive = new ArrayList<>();
        ItemsOfflineOfActiveLists = new ArrayList<>();
        ListsOfflineHistory = new ArrayList<>();
        ItemsOfflineOfHistoryLists = new ArrayList<>();
        Groups = new ArrayList<>();
        AddedUsers = new ArrayList<>();
        DeletableUsers = new ArrayList<>();
        ListInformers = new ArrayList<>();
        TempItems = new ArrayList();
    }

    protected void setTempItems(TempItem[] tempItem){
        if(tempItem != null) {
            TempItems = new ArrayList<>(Arrays.asList(tempItem));
        }
    }
    protected TempItem[] getTempItems(){
        TempItem[] result = new TempItem[0];
        if(TempItems.size() > 0){
            result = new TempItem[TempItems.size()];
            result = TempItems.toArray(result);
        }
        return result;
    }
    protected void clearTempItems(){
        TempItems = new ArrayList<>();
        TempItems.trimToSize();
    }





    protected void setDeletableUsers(AddingUser[] addingUsers){
        if(addingUsers != null) {
            if(addingUsers.length > 0) {
                DeletableUsers = new ArrayList<>(Arrays.asList(addingUsers));
            }
            else{
                DeletableUsers = new ArrayList<>();
            }
        }
    }
    protected AddingUser[] getDeletableUsers(){
        AddingUser[] result = new AddingUser[0];
        if(DeletableUsers.size() > 0){
            result = new AddingUser[DeletableUsers.size()];
            for(int i = 0; i < result.length; i++){
                result[i] = DeletableUsers.get(i);
            }
        }
        return result;
    }
    protected void clearDeletableUsers(){
        DeletableUsers = new ArrayList<>();
        DeletableUsers.trimToSize();
    }




    protected ListInformer[] setListInformers(ListInformer[] informers){
        if(informers != null && informers.length > 0){
            int length = informers.length;
            UserGroup tempGroup;
            for(int i = 0; i < length; i++){
                tempGroup = informers[i].getGroup();
                addGroup(tempGroup);
            }
            ListInformers = new ArrayList<>(Arrays.asList(informers));
        }
        return informers;
    }
    protected ListInformer[] getListInformers(){
        ListInformer[] informers = null;
        if(ListInformers.size() > 0){
            informers = new ListInformer[ListInformers.size()];
            informers = ListInformers.toArray(informers);
        }
        return informers;
    }

    protected void addOneAddedUser(AddingUser user){
        if(!AddedUsers.contains(user)){
            AddedUsers.add(user);
        }
    }
    protected AddingUser removeOneAddedUser(AddingUser user){
        if(AddedUsers.contains(user)){
            AddedUsers.remove(user);
            AddedUsers.trimToSize();
            return user;
        }
        else{
            return null;
        }
    }
    protected boolean isAddedUser(AddingUser user){
        boolean result = false;
        if(user != null){
            if(AddedUsers.contains(user)){
                result = true;
            }
        }
        return  result;
    }

    protected void setAddedUsers(AddingUser[] addingUsers){
        if(addingUsers != null) {
            if(addingUsers.length > 0) {
                AddedUsers = new ArrayList<>(Arrays.asList(addingUsers));
            }
            else{
                AddedUsers = new ArrayList<>();
            }
        }
    }
    protected AddingUser[] getAddedUsers(){
        AddingUser[] result = new AddingUser[0];
        if(AddedUsers.size() > 0){
            result = new AddingUser[AddedUsers.size()];
            for(int i = 0; i < result.length; i++){
                result[i] = AddedUsers.get(i);
            }
        }
        return result;
    }
    protected void clearAddedUsers(){
        AddedUsers = new ArrayList<>();
        AddedUsers.trimToSize();
    }

    protected void addOneDeletableUser(AddingUser user){
        if(!DeletableUsers.contains(user)){
            DeletableUsers.add(user);
        }
    }
    protected AddingUser removeOneDeletableUser(AddingUser user){
        if(DeletableUsers.contains(user)){
            DeletableUsers.remove(user);
            DeletableUsers.trimToSize();
            return user;
        }
        else{
            return null;
        }
    }
    protected boolean isDeletableUser(AddingUser user){
        boolean result = false;
        if(user != null){
            if(DeletableUsers.contains(user)){
                result = true;
            }
        }
        return  result;
    }


    protected UserGroup updateGroup(UserGroup oldVersionOfGroup, UserGroup newVersionOfGroup){
        UserGroup result;
        Groups.remove(oldVersionOfGroup);
        Groups.add(newVersionOfGroup);
        result = newVersionOfGroup;
        return result;
    }
    protected UserGroup[] getGroups(){
        UserGroup[] result = new UserGroup[0];
        if(Groups.size() > 0){
            result = new UserGroup[Groups.size()];
            result = Groups.toArray(result);
        }
        return result;
    }
    protected void addGroup(UserGroup newGroup){
        if(!Groups.contains(newGroup)) {
            Groups.add(newGroup);
        }
    }
    protected void removeGroup(UserGroup group){
        if(group != null) {
            if (Groups.contains(group)) {
                Groups.remove(group);
            }
        }
    }
    protected boolean isAnyGroups(){
        boolean result = false;
        if(Groups.size() > 0){
            result = true;
        }
        return result;
    }
    protected UserGroup[] setGroups(UserGroup[] groups){
        if(groups != null) {
            Groups = new ArrayList<>(Arrays.asList(groups));
        }
        return groups;
    }
    protected boolean isAnyGroupActiveLists(UserGroup group){
        boolean result = false;
        if(group != null && group.getActiveLists() != null) {
            if(group.getActiveLists().length > 0) {
                result = true;
            }
        }
        return result;
    }
    protected SList[] getGroupActive(UserGroup group){
        SList[] lists = null;
        if(group.getActiveLists() != null) {
            lists = group.getActiveLists();
        }
        return lists;
    }
    protected void setGroupActiveData(SList[] lists, UserGroup group){
        if(group.getActiveLists() != null) {
            group.setActiveLists(lists);
        }
    }




    protected boolean isAnyGroupHistory(UserGroup group){
        boolean result = false;
        if(group != null && group.getHistoryLists() != null) {
            if(group.getHistoryLists().length > 0) {
                result = true;
            }
        }
        return result;
    }
    protected SList[] getGroupHistory(UserGroup group){
        SList[] lists = new SList[0];
        if(group.getHistoryLists() != null) {
            lists = group.getHistoryLists();
        }
        return lists;
    }
    protected void setGroupHistoryData(SList[] lists, UserGroup group){
        if(group.getHistoryLists() != null) {
            group.setHistoryLists(lists);
        }
    }




    private void clearOfflineActive(){
        ListsOfflineActive.clear();
        ItemsOfflineOfActiveLists.clear();
    }
    protected void clearOfflineHistory(){
        ListsOfflineHistory.clear();
        ItemsOfflineOfHistoryLists.clear();
    }
    protected void clearOffline(){
        clearOfflineActive();
        clearOfflineHistory();
    }



    protected void itemmarkOffline(Item item){
        boolean state = item.getState();
        item.setState(!state);
    }

    protected void addOfflineListToHistory(SList list){
        if(list != null){
            ListsOfflineHistory.add(list);
        }
    }

    protected void removeOfflineListFromActive(SList list){
        Item[] items = list.getItems();
        int length = items.length;
        for(int i = 0; i < length; i++){
            ItemsOfflineOfActiveLists.remove(items[i]);
        }
        ItemsOfflineOfActiveLists.trimToSize();
        ListsOfflineActive.remove(list);
        ListsOfflineActive.trimToSize();
    }


    protected void addNewList(SList list){
        ListsOfflineActive.add(ListsOfflineActive.size(), list);
        Item[] items = list.getItems();
        int itemsNumber = items.length;
        for(int i = 0; i < itemsNumber; i++){
            ItemsOfflineOfActiveLists.add(ItemsOfflineOfActiveLists.size(), items[i]);
        }
    }

    protected void setOfflineActiveData(SList[] lists){
        ListsOfflineActive.clear();
        ItemsOfflineOfActiveLists.clear();
        int length = lists.length;
        int tempLength;
        int i, j;
        Item tempIt;
        Item[] tempItems;
        for(i = 0; i < length; i++){
            tempLength = 0;
            tempItems = lists[i].getItems();
            if(tempItems != null) {
                tempLength = tempItems.length;
            }
            for(j = 0; j < tempLength; j++){
                tempIt = tempItems[j];
                ItemsOfflineOfActiveLists.add(ItemsOfflineOfActiveLists.size(), tempIt);
            }
            ListsOfflineActive.add(ListsOfflineActive.size(), lists[i]);
        }
    }
    protected SList[] getOfflineListsActive(){
        SList[] tempLists;
        tempLists = ListsOfflineActive.toArray(new SList[0]);
        return tempLists;
    }
    protected Item[] getOfflineItemsOfActiveLists(){
        Item[] tempItems = new Item[0];
        if(ItemsOfflineOfActiveLists.size() > 0) {
            tempItems = ItemsOfflineOfActiveLists.toArray(tempItems);
        }
        return tempItems;
    }

    protected boolean isAnyOfflineActive(){
        boolean result = false;
        if(ListsOfflineActive.size() != 0){
            result = true;
        }
        return result;
    }
    protected boolean isAnyOfflineHistory(){
        boolean result = false;
        if(ListsOfflineHistory.size() != 0) {
            result = true;
        }
        return result;
    }

    protected void setOfflineHistoryData(SList[] lists){
        ListsOfflineHistory.clear();
        ItemsOfflineOfHistoryLists.clear();
        int length = lists.length;
        int tempLength;
        int i, j;
        Item tempIt;
        Item[] tempItems;
        for(i = 0; i < length; i++){
            tempLength = 0;
            tempItems = lists[i].getItems();
            if(tempItems != null) {
                tempLength = tempItems.length;
            }
            for(j = 0; j < tempLength; j++){
                tempIt = tempItems[j];
                ItemsOfflineOfHistoryLists.add(ItemsOfflineOfHistoryLists.size(), tempIt);
            }
            ListsOfflineHistory.add(ListsOfflineHistory.size(), lists[i]);
        }
    }
    protected SList[] getOfflineListsHistory(){
        SList[] tempLists;
        tempLists = ListsOfflineHistory.toArray(new SList[0]);
        return tempLists;
    }
    protected Item[] getOfflineItemsOfHistoryLists(){
        Item[] tempItems;
        tempItems = ItemsOfflineOfHistoryLists.toArray(new Item[0]);
        return tempItems;
    }






}
