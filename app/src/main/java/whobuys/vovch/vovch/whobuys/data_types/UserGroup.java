package whobuys.vovch.vovch.whobuys.data_types;

import android.support.v7.widget.CardView;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by vovch on 03.01.2018.
 */

public class UserGroup /*implements Parcelable*/ {
    private String name;
    private String id;
    private String state;
    private ArrayList<SList> activeLists;
    private ArrayList<SList> historyLists;
    private ArrayList<AddingUser> members;
    private CardView cardView;
    private Button button;
    private String owner;
    private String ownerName;
    private boolean updateNeeded;
    private int numberOfHistoryLists;
    private String lastUpdateTime;

    public final static String DEFAULT_GROUP_STATE_UNWATCHED = "n";
    public final static String DEFAULT_GROUP_STATE_WATCHED = "o";
    private final static int DEFAULT_HISTORY_LISTS_STEP = 5;
    private final static int DEFAULT_HISTORY_MINIMUM_VALUE = 0;

    public UserGroup(String newName, String newId, SList[] newActiveLists, SList[] newHistoryLists, AddingUser[] newMembers) {
        name = newName;
        id = newId;
        owner = null;
        activeLists = new ArrayList<>(Arrays.asList(newActiveLists));
        historyLists = new ArrayList<>(Arrays.asList(newHistoryLists));
        members = new ArrayList<>(Arrays.asList(newMembers));
        button = null;
        updateNeeded = false;
        state = "1";
        setMinimumHistoryListsNumber();
        lastUpdateTime = null;
    }

    public UserGroup(String newName, String newId, AddingUser[] newMembers) {
        name = newName;
        id = newId;
        owner = null;
        members = null;
        if (newMembers != null) {
            setMembers(newMembers);
        }
        activeLists = new ArrayList<>();
        historyLists = new ArrayList<>();
        button = null;
        updateNeeded = false;
        state = "1";
        setMinimumHistoryListsNumber();
        lastUpdateTime = null;
    }

    public UserGroup(String newName, String newId) {
        name = newName;
        id = newId;
        owner = null;
        members = null;
        activeLists = new ArrayList<>();
        historyLists = new ArrayList<>();
        button = null;
        updateNeeded = false;
        state = "1";
        setMinimumHistoryListsNumber();
        lastUpdateTime = null;
    }

    public void setState(String newState){
        state = newState;
    }

    public void resetGroupState(){
        if(state.equals(DEFAULT_GROUP_STATE_UNWATCHED)){
            state = DEFAULT_GROUP_STATE_WATCHED;
        } else {
            state = DEFAULT_GROUP_STATE_UNWATCHED;
        }
    }

    public String getState(){
        return state;
    }

    public String getId() {
        return id;
    }

    public void setName(String newName){
        name = newName;
    }

    public String getName() {
        return name;
    }

    public String getLastUpdateTime(){
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String newTime){
        lastUpdateTime = newTime;
    }

    public void setCardView(CardView newCardView) {
        cardView = newCardView;
    }

    public void setButton(Button newButton) {
        button = newButton;
    }

    public Button getButton() {
        return button;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void clear() {
        cardView = null;
    }

    public void setOwner(String newOwner) {
        owner = newOwner;
    }

    public void setOwnerName(String newOwnerName){
        ownerName = newOwnerName;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void disactivateList(SList list) {
        if (activeLists != null) {
            activeLists.remove(list);
            activeLists.trimToSize();
            list.setState(false);
        }
    }

    public void itemmark(Item item) {
        if (item != null) {
            item.setState(!item.getState());
        }
    }

    public void setActiveLists(SList[] lists) {
        if (lists != null) {
            activeLists = new ArrayList<>(Arrays.asList(lists));
        } else {
            activeLists = new ArrayList<>();
        }
    }

    public void addActiveList(SList list){
        if(list != null){
            activeLists.add(list);
        }
    }

    public void addHistoryList(SList list){
        if(list != null){
            historyLists.add(list);
        }
        setMinimumHistoryListsNumber();
    }

    public SList[] getActiveLists() {
        SList[] result = null;
        if (activeLists != null) {
            result = new SList[activeLists.size()];
            result = activeLists.toArray(result);
        } else {
            activeLists = new ArrayList<>();
            result = new SList[activeLists.size()];
            result = activeLists.toArray(result);
        }
        return result;
    }

    public void setHistoryLists(SList[] lists) {
        if (lists != null) {
            historyLists = new ArrayList<>(Arrays.asList(lists));
            if(historyLists.size() < DEFAULT_HISTORY_LISTS_STEP){
                numberOfHistoryLists = historyLists.size();
            } else {
                numberOfHistoryLists = DEFAULT_HISTORY_LISTS_STEP;
            }
        } else {
            historyLists = new ArrayList<>();
            numberOfHistoryLists = DEFAULT_HISTORY_MINIMUM_VALUE;
        }
    }

    public SList[] getHistoryLists() {
        SList[] result = null;
        if (historyLists != null) {
            result = new SList[numberOfHistoryLists];
            result = historyLists.subList(0, numberOfHistoryLists).toArray(result);
        } else {
            historyLists = new ArrayList<>();
            result = new SList[0];
        }

        Collections.reverse(Arrays.asList(result));

        return result;
    }

    public SList[] getAllHistoryLists(){
        SList[] result = null;
        if (historyLists != null) {
            result = new SList[historyLists.size()];
            result = historyLists.toArray(result);
        } else {
            historyLists = new ArrayList<>();
            result = new SList[0];
        }
        return result;
    }

    public String getLastListCreationTime() {
        String creationTime = null;
        if(historyLists != null && historyLists.size() > 0){
            creationTime = historyLists.get(0).getCreationTime();
        }
        return creationTime;
    }

    public void addMember(AddingUser memberId) {
        if (members == null) {
            members = new ArrayList<>();
        }
        if (!members.contains(memberId)) {
            members.add(memberId);
        }
    }

    public void deleteMember(String memberId) {
        if (members != null) {
            if (members.contains(memberId)) {
                members.remove(memberId);
            }
        }
    }

    public void setMembers(AddingUser[] newMembers) {
        if (newMembers != null) {
            members = new ArrayList<>(Arrays.asList(newMembers));
        }
    }

    public AddingUser[] getMembers() {
        AddingUser[] result = null;
        if (members != null) {
            result = new AddingUser[members.size()];
            result = members.toArray(result);
        }
        return result;
    }

    public void changeUpdateNeededFlag(){
        updateNeeded = !updateNeeded;
    }

    public boolean getUpdateNeededFlag(){
        return updateNeeded;
    }

    public void getMoreHistoryLists(){
        int maxValue = historyLists.size();
        if(numberOfHistoryLists + DEFAULT_HISTORY_LISTS_STEP > maxValue){
            numberOfHistoryLists = maxValue;
        } else {
            numberOfHistoryLists = numberOfHistoryLists + DEFAULT_HISTORY_LISTS_STEP;
        }
    }

    public void setMinimumHistoryListsNumber(){
        if(historyLists.size() < DEFAULT_HISTORY_LISTS_STEP) {
            numberOfHistoryLists = historyLists.size();
        } else {
            numberOfHistoryLists = DEFAULT_HISTORY_LISTS_STEP;
        }
    }

    public static boolean newLastUpdateTimeBigger(String lastTime, String newTime){
        return true;                                                                                    //TODO
    }
}
