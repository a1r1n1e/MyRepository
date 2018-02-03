package com.example.vovch.listogram_20.data_layer;

import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ListInformer;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.TempItem;
import com.example.vovch.listogram_20.data_types.UserGroup;

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
    private ArrayList<AddingUser> AddingUsers = new ArrayList<>();
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

    protected void setTempItems(TempItem[] tempItem){
        if(tempItem != null && tempItem.length > -1) {
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
        TempItems.clear();
        TempItems.trimToSize();
    }




    protected void setAddingUsers(AddingUser[] addingUsers){
        if(addingUsers != null) {
            if(addingUsers.length > 0) {
                AddingUsers = new ArrayList<>(Arrays.asList(addingUsers));
            }
            else{
                AddingUsers = new ArrayList<>();
            }
        }
    }
    protected AddingUser[] getAddingUsers(){
        AddingUser[] result = new AddingUser[0];
        if(AddingUsers.size() > 0){
            result = new AddingUser[AddingUsers.size()];
            result = AddingUsers.toArray(result);
        }
        return result;
    }
    protected void clearAddingUsers(){
        AddingUsers = new ArrayList<>();
        AddingUsers.trimToSize();
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
    protected void addOneAddingUser(AddingUser user){
        if(!AddingUsers.contains(user)){
            AddingUsers.add(user);
        }
    }
    protected AddingUser removeOneAddingUser(AddingUser user){
        if(AddingUsers.contains(user)){
            AddingUsers.remove(user);
            AddingUsers.trimToSize();
            return user;
        }
        else{
            return null;
        }
    }
    protected boolean isAddingUser(AddingUser user){
        boolean result = false;
        if(user != null){
            if(AddingUsers.contains(user)){
                result = true;
            }
        }
        return  result;
    }


    protected UserGroup updateGroup(UserGroup oldVersionOfGroup, UserGroup newVersionOfGroup, boolean membersAppendingNeeded){
        UserGroup result = null;
        if(oldVersionOfGroup != null && newVersionOfGroup != null){
            int changingGroupNumber = Groups.indexOf(oldVersionOfGroup);
            AddingUser[] users;
            if(membersAppendingNeeded){
                users = new AddingUser[oldVersionOfGroup.getMembers().length + newVersionOfGroup.getMembers().length];
                System.arraycopy(oldVersionOfGroup.getMembers(), 0, users, 0, oldVersionOfGroup.getMembers().length);
                System.arraycopy(newVersionOfGroup.getMembers(), 0, users, oldVersionOfGroup.getMembers().length, newVersionOfGroup.getMembers().length);
                newVersionOfGroup.setMembers(users);
            }
            Groups.remove(changingGroupNumber);
            Groups.add(newVersionOfGroup);
            result = newVersionOfGroup;
        }
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
        Groups = new ArrayList<>(Arrays.asList(groups));
        return groups;
    }
    protected boolean isAnyGroupActiveLists(String groupId){
        boolean result = false;
        int i;
        for(i = 0; i < Groups.size(); i++){
            if(Groups.get(i).getId().equals(groupId)){
                break;
            }
        }
        if(i < Groups.size()){
            if(Groups.get(i).getActiveLists() != null) {
                if(Groups.get(i).getActiveLists().length > 0) {
                    result = true;
                }
            }
        }
        return result;
    }
    protected SList[] getGroupActive(String groupId){
        int i;
        SList[] lists = null;
        for(i = 0; i < Groups.size(); i++){
            if(Groups.get(i).getId().equals(groupId)) {
                break;
            }
        }
        if(i < Groups.size()){
            if(Groups.get(i).getActiveLists() != null) {
                lists = Groups.get(i).getActiveLists();
            }
        }
        return lists;
    }
    protected void setGroupActiveData(SList[] lists, String groupId){
        int i;
        for(i = 0; i < Groups.size(); i++){
            if( Groups.get(i).getId().equals(groupId)) {
                break;
            }
        }
        if(i < Groups.size()){
            if(Groups.get(i).getActiveLists() != null) {
                Groups.get(i).setActiveLists(lists);
            }
        }
    }




    protected boolean isAnyGroupHistory(String groupId){
        boolean result = false;
        int i;
        for(i = 0; i < Groups.size(); i++){
            if(Groups.get(i).getId().equals(groupId)){
                break;
            }
        }
        if(i < Groups.size()){
            if(Groups.get(i).getHistoryLists() != null) {
                if(Groups.get(i).getHistoryLists().length > 0) {
                    result = true;
                }
            }
        }
        return result;
    }
    protected SList[] getGroupHistory(String groupId){
        int i;
        SList[] lists = null;
        for(i = 0; i < Groups.size(); i++){
            if(Groups.get(i).getId().equals(groupId)) {
                break;
            }
        }
        if(i < Groups.size()){
            if(Groups.get(i).getHistoryLists() != null) {
                lists = Groups.get(i).getHistoryLists();
            }
        }
        return lists;
    }
    protected void setGroupHistoryData(SList[] lists, String groupId){
        int i;
        for(i = 0; i < Groups.size(); i++){
            if(Groups.get(i).getId().equals(groupId)) {
                break;
            }
        }
        if(i < Groups.size()){
            if(Groups.get(i).getHistoryLists() != null) {
                Groups.get(i).setHistoryLists(lists);
            }
        }
    }




    protected void clearOfflineActive(){
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
            tempItems = lists[i].getItems();
            tempLength = tempItems.length;
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
            tempItems = lists[i].getItems();
            tempLength = tempItems.length;
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
