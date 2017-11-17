package com.example.vovch.listogram_20;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class Group2Activity extends WithLoginActivity {
    protected static ArrayList<LinearLayout> Layouts1 = new ArrayList<>();
    protected static ArrayList <Button> Buttons1 = new ArrayList<>();
    protected static ArrayList <EditText> ListItemsTexts = new ArrayList<>();
    protected static ArrayList <EditText> ListCommentTexts = new ArrayList<>();
    protected static ArrayList <Integer> Lists = new ArrayList<>();
    protected static ArrayList <Boolean> ActiveLists = new ArrayList<>();
    protected static ArrayList <Integer> ListOwners = new ArrayList<>();
    protected static ArrayList <Boolean> ItemMarks = new ArrayList<>();
    protected static ArrayList <Integer> Items = new ArrayList<>();
    protected static ArrayList <Button> DisButtons = new ArrayList<>();
    protected static ArrayList<ArrayList<String>> ItemObjects = new ArrayList<>();
    protected Group2Activity.ListogramsGetter lTask;
    protected Group2Activity.ItemMarkTask itemMarkTask;
    protected Group2Activity.DisactivateListTask disactivateListTask;
    private String groupName;
    private String groupId;
    private String userId;
    private int NumberOfLists = 0;
    private int DisNumberOfLists = 0;
    private int LISTOGRAM_BUTTON_BIG_NUMBER = 70000000;
    private int LISTOGRAM_DIS_BUTTON_BIG_NUMBER = 80000000;
    private ActiveActivityProvider provider;

    private int NumberOfLines = 0;
    private int LISTOGRAM_LINE_BIG_NUMBER = 50000000;
    private int LISTOGRAM_BIG_NUMBER = 60000000;
    private int LISTOGRAM_ITEM_BIG_NUMBER = 30000000;
    private int LISTOGRAM_COMMENT_BIG_NUMBER = 40000000;
    private int LISTS_DIVIDER = 10301;
    private int INSIDE_DIVIDER = 10253;
    private int LISTOGRAM_LINE_INSIDE_DIVIDER = 37;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupName = getIntent().getExtras().getString("name");                                  //получаем данные о группе
        groupId = getIntent().getExtras().getString("groupid");
        userId = getIntent().getExtras().getString("userid");


        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(3, Group2Activity.this);
        provider.setGroupId(Integer.parseInt(groupId));

        setContentView(R.layout.activity_group3);
        update();
        //timer = new Timer();
        //timerTask = new TimerClass();
        //timer.schedule(timerTask, 15000, 15000);
    }
    @Override
    protected void onResume(){
        super.onResume();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(3, Group2Activity.this);
        provider.setGroupId(Integer.parseInt(groupId));
    }
    @Override
    public void onBackPressed(){
        //timer.cancel();
        Layouts1.clear();
        Buttons1.clear();
        ListItemsTexts.clear();
        ListCommentTexts.clear();
        Lists.clear();
        ActiveLists.clear();
        ListOwners.clear();
        ItemMarks.clear();
        Items.clear();
        DisButtons.clear();
        groupId = null;
        groupName = null;
        Intent intent = new Intent(Group2Activity.this, GroupList2Activity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onDestroy(){
        cleaner();
        super.onDestroy();
    }
    private void cleaner(){
        Layouts1.clear();
        Buttons1.clear();
        ListItemsTexts.clear();
        ListCommentTexts.clear();
        Lists.clear();
        ActiveLists.clear();
        ListOwners.clear();
        ItemMarks.clear();
        Items.clear();
        DisButtons.clear();
        NumberOfLines = 0;
        NumberOfLists = 0;
    }
    @Override
    public void onPause(){
        cleaner();
        provider.nullActiveActivity();
        super.onPause();
    }
    protected void update(){
        lTask = new ListogramsGetter(groupId, "gettinglistograms");
        lTask.work();
        View.OnClickListener DownButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendListogram();
            }
        };
        Button Btn3 = (Button)findViewById(R.id.groupdownbutton);
        Btn3.setOnClickListener(DownButtonListenner);
        ScrollView groupScrollView = (ScrollView) findViewById(R.id.groupscroll);
        groupScrollView.fullScroll(View.FOCUS_DOWN);
    }
    protected String getUserId(){
        return userId;
    }
    protected void showGood(String result){
        cleaner();
        LinearLayout bigScrollLayout = (LinearLayout) findViewById(R.id.listogramslayout);
        bigScrollLayout.removeAllViewsInLayout();
        listsListMaker(result);
    }
    protected void showBad(String result){
        cleaner();
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(matchParent, 180);
        TextView emptyInformer = new TextView(findViewById(R.id.listogramslayout).getContext());
        emptyInformer.setLayoutParams(parameters);
        if(result.equals("")) {
            emptyInformer.setText("No listograms jet(");
        }
        else{
            emptyInformer.setText("Something went wrong");
        }
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.listogramslayout);
        parentLayout.addView(emptyInformer);
    }
    protected void showSecondGood(String result){
        int thatMarkedItemId = deconstructReultStringToElementId(result);
        int markId = Items.indexOf(thatMarkedItemId);
        if(result.codePointAt(0) == '0'){
            Button itemMarkTouchedButton = (Button) findViewById(markId + LISTOGRAM_BUTTON_BIG_NUMBER);
            itemMarkTouchedButton.setClickable(true);
            itemMarkTouchedButton.setFocusable(true);
            itemMarkTouchedButton.setGravity(Gravity.CENTER_HORIZONTAL);
            itemMarkTouchedButton.setText("Needed");
            itemMarkTouchedButton.setBackgroundColor(Color.GREEN);
            ItemMarks.set(markId, true);
        }
        else if(result.codePointAt(0) == '2'){
            Button itemMarkTouchedButton = (Button) findViewById(markId + LISTOGRAM_BUTTON_BIG_NUMBER);
            itemMarkTouchedButton.setClickable(true);
            itemMarkTouchedButton.setFocusable(true);
            itemMarkTouchedButton.setGravity(Gravity.CENTER_HORIZONTAL);
            itemMarkTouchedButton.setText("Bought");
            itemMarkTouchedButton.setBackgroundColor(Color.BLUE);
            ItemMarks.set(markId, false);
        }
        else{
            Button itemMarkTouchedButton = (Button) findViewById(markId + LISTOGRAM_BUTTON_BIG_NUMBER);
            itemMarkTouchedButton.setClickable(true);
            itemMarkTouchedButton.setFocusable(true);
            itemMarkTouchedButton.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }
    protected void showSecondBad(String result){
        int markId = Items.indexOf(deconstructReultStringToElementId(result));
        Button itemMarkButton = (Button) findViewById(markId);
        itemMarkButton.setClickable(true);
        itemMarkButton.setFocusable(true);
        itemMarkButton.setGravity(Gravity.CENTER_HORIZONTAL);
    }
    protected void showThirdGood(String result){
        /*int disId = Lists.indexOf(deconstructReultStringToElementId(result));
        Button disButton = DisButtons.get(disId);
        disButton.setClickable(false);
        disButton.setFocusable(false);
        disButton.setText("Done");*/
        update();
    }
    protected void showThirdBad(String result){

    }

    private int deconstructReultStringToElementId(String result){
        int i;
        for(i = 1; result.codePointAt(i)!= '%'; i++){}
        return Integer.parseInt(result.substring(1, i).toString());
    }

    protected String getGroupId(){
        return groupId;
    }
    protected void listsLayoutDrawer(String listName, final String listId, String ownerId, boolean listActive){
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(matchParent, wrapContent);
        LinearLayout.LayoutParams buttonParameters = new LinearLayout.LayoutParams(300, wrapContent);
        LinearLayout.LayoutParams itemParameters = new LinearLayout.LayoutParams(300, wrapContent);
        LinearLayout.LayoutParams commentParameters = new LinearLayout.LayoutParams(300, wrapContent);

        LinearLayout listogramLayout = new LinearLayout(findViewById(R.id.listogramslayout).getContext());
        listogramLayout.setOrientation(LinearLayout.VERTICAL);
        listogramLayout.setLayoutParams(parameters);
        listogramLayout.setId(LISTOGRAM_BIG_NUMBER + NumberOfLists++);
        if(listActive) {
            ActiveLists.add(ActiveLists.size(), true);
        }
        else{
            ActiveLists.add(ActiveLists.size(), false);
        }

        StringBuilder itemNameString = new StringBuilder("");
        StringBuilder itemCommentString = new StringBuilder("");
        StringBuilder itemIdString = new StringBuilder("");
        int itemId;
        boolean itemActive = false;
        int flag1 = 0;
        for(int i = 0, j = 0;i < listName.length();i++){
            if(listName.codePointAt(i) == LISTOGRAM_LINE_INSIDE_DIVIDER&&flag1 % 4 == 0) {
                itemNameString.append(listName.substring(j,i));
                j = i + 1;
                flag1++;
            }
            else if(listName.codePointAt(i) == LISTOGRAM_LINE_INSIDE_DIVIDER&&flag1 % 4 == 1){
                itemCommentString.append(listName.substring(j,i));
                j = i + 1;
                flag1++;
            }
            else if(listName.codePointAt(i) == LISTOGRAM_LINE_INSIDE_DIVIDER&&flag1 % 4 == 2){
                if(listName.substring(j,i).equals("t")){
                    itemActive = true;
                }
                else{
                    itemActive = false;
                }
                j = i + 1;
                flag1++;
            }
            else if(listName.codePointAt(i) == LISTOGRAM_LINE_INSIDE_DIVIDER&&flag1 % 4 == 3){
                itemId = Integer.parseInt(listName.substring(j,i));
                makeListogramLine(listogramLayout, parameters, itemParameters, commentParameters, buttonParameters, itemNameString.toString(), itemCommentString.toString(), itemActive, itemId, listActive);
                itemNameString.delete(0, itemNameString.length());
                itemCommentString.delete(0, itemCommentString.length());
                j = i + 1;
                flag1++;
            }
        }
        listogramLayout.setPadding(0, 80, 0, 80);
        ListOwners.add(ListOwners.size(), Integer.parseInt(ownerId));
        View.OnClickListener disactivateListButtonOnClickListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disactivateList(groupId, listId, v.getId());
            }
        };
        if(ownerId.equals(userId)) {
            Button disactivateListButton = new Button(listogramLayout.getContext());
            disactivateListButton.setClickable(true);
            if(listActive){
                disactivateListButton.setText("Disactivate list");
            }
            else{
                disactivateListButton.setClickable(false);
                disactivateListButton.setFocusable(false);
                disactivateListButton.setText("Done");
            }
            disactivateListButton.setId(DisNumberOfLists + LISTOGRAM_DIS_BUTTON_BIG_NUMBER);
            disactivateListButton.setOnClickListener(disactivateListButtonOnClickListenner);
            listogramLayout.addView(disactivateListButton);
            DisButtons.add(DisButtons.size(), disactivateListButton);
        }
        else{
            DisButtons.add(DisButtons.size(), null);
        }
        DisNumberOfLists++;
        LinearLayout basicLayout = (LinearLayout) findViewById(R.id.listogramslayout);
        basicLayout.addView(listogramLayout);
        Lists.add(Lists.size(), Integer.parseInt(listId));
    }
    private void disactivateList(String groupId, String listId, int disButtonId){
        disactivateListTask = new DisactivateListTask(groupId, listId);
        disactivateListTask.work();
    }
    private void makeListogramLine(LinearLayout listogramLayout, LinearLayout.LayoutParams parameters, LinearLayout.LayoutParams itemParameters,
                                   LinearLayout.LayoutParams commentParameters, LinearLayout.LayoutParams buttonParameters, String item, String comment, boolean active, int itemId, boolean listActive){
        FrameLayout addingFrameLayout = new FrameLayout(listogramLayout.getContext());
        addingFrameLayout.setLayoutParams(parameters);
        addingFrameLayout.setPadding(0, 5, 0, 0);
        LinearLayout addingLayout = new LinearLayout(listogramLayout.getContext());
        addingLayout.setBaselineAligned(false);
        addingLayout.setBackgroundColor(Color.CYAN);
        addingLayout.setLayoutParams(parameters);
        addingLayout.setGravity(Gravity.START);
        addingLayout.setOrientation(LinearLayout.HORIZONTAL);
        addingLayout.setId(LISTOGRAM_LINE_BIG_NUMBER + NumberOfLines);
        Layouts1.add(Layouts1.size(), addingLayout);
        EditText itemName = new EditText(addingLayout.getContext());
        itemName.setLayoutParams(itemParameters);
        itemName.setFocusable(false);
        itemName.setClickable(false);
        itemName.setBackgroundColor(Color.TRANSPARENT);
        itemName.setText(item);
        itemName.setId(LISTOGRAM_ITEM_BIG_NUMBER + NumberOfLines);
        ListItemsTexts.add(ListItemsTexts.size(), itemName);
        EditText itemComment = new EditText(addingLayout.getContext());
        itemComment.setLayoutParams(commentParameters);
        itemComment.setFocusable(false);
        itemComment.setClickable(false);
        itemComment.setBackgroundColor(Color.TRANSPARENT);
        itemComment.setText(comment);
        itemComment.setId(LISTOGRAM_COMMENT_BIG_NUMBER + NumberOfLines);
        ListCommentTexts.add(ListCommentTexts.size(), itemComment);
        Button groupButton = new Button(addingLayout.getContext());
        groupButton.setLayoutParams(buttonParameters);
        groupButton.setGravity(Gravity.CENTER_HORIZONTAL);
        groupButton.setId(LISTOGRAM_BUTTON_BIG_NUMBER + NumberOfLines);
        View.OnClickListener ItemMarkButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemMarkButtonTouchedAction((Button) v);
            }
        };
        if(active){
            groupButton.setText("Needed");
            if(listActive) {
                groupButton.setClickable(true);                                                 //!!!!!!!!!РАЗОБРАТЬСЯ!!!!!!!!!
                groupButton.setOnClickListener(ItemMarkButtonListenner);
                groupButton.setBackgroundColor(Color.GREEN);
            }
            else
            {
                groupButton.setClickable(false);
                groupButton.setFocusable(false);
                groupButton.setBackgroundColor(Color.GRAY);
            }
            ItemMarks.add(ItemMarks.size(), true);
        }
        else{
            groupButton.setText("Bought");
            if(listActive) {
                groupButton.setClickable(true);
                groupButton.setOnClickListener(ItemMarkButtonListenner);
                groupButton.setBackgroundColor(Color.BLUE);
            }
            else{
                groupButton.setClickable(false);
                groupButton.setFocusable(false);
                groupButton.setBackgroundColor(Color.GRAY);
            }
            ItemMarks.add(ItemMarks.size(), false);
        }
        Buttons1.add(Buttons1.size(), groupButton);
        addingLayout.addView(itemName);
        addingLayout.addView(itemComment);
        addingLayout.addView(groupButton);
        addingFrameLayout.addView(addingLayout);
        listogramLayout.addView(addingFrameLayout);
        Items.add(Items.size(), itemId);
        NumberOfLines++;
    }
    protected void listsListMaker(String result) {
        int length = result.length();
        StringBuilder tempNameString = new StringBuilder();
        StringBuilder tempIdString = new StringBuilder();
        StringBuilder tempOwnerIdString = new StringBuilder();
        StringBuilder tempIsActiveString = new StringBuilder();
        boolean listActive = false;
        int listogramLineFlag = 0;
        for (int i = 0, j = 0; i < length; i++) {
            if (result.codePointAt(i) == INSIDE_DIVIDER && listogramLineFlag % 3 == 0) {
                tempNameString.setLength(0);
                tempNameString.append(result.substring(j, i));
                j = i + 1;
                listogramLineFlag++;
            } else if (result.codePointAt(i) == INSIDE_DIVIDER && listogramLineFlag % 3 == 1){
                tempIdString.setLength(0);
                tempIdString.append(result.substring(j, i));
                j = i + 1;
                listogramLineFlag++;
            } else if (result.codePointAt(i) == INSIDE_DIVIDER && listogramLineFlag % 3 == 2){
                tempOwnerIdString.setLength(0);
                tempOwnerIdString.append(result.substring(j, i));
                j = i + 1;
                listogramLineFlag++;
            } else if (result.codePointAt(i) == LISTS_DIVIDER) {
                tempIsActiveString.setLength(0);
                tempIsActiveString.append(result.substring(j, i));
                if(tempIsActiveString.toString().equals("t")){
                    listActive = true;
                }
                else{
                    listActive = false;
                }
                j = i + 1;
                listsLayoutDrawer(tempNameString.toString(), tempIdString.toString(), tempOwnerIdString.toString(), listActive);
            }
        }
    }
    private void onItemMarkButtonTouchedAction(Button button){
        int id = button.getId() - LISTOGRAM_BUTTON_BIG_NUMBER;
        int itemId = Items.get(id);
        LinearLayout itemLayout = (LinearLayout) button.getParent().getParent().getParent();
        int listId = itemLayout.getId() - LISTOGRAM_BIG_NUMBER;
        button.setClickable(false);
        button.setFocusable(false);
        button.setGravity(Gravity.END);
        itemMarkTask = new ItemMarkTask (userId, String.valueOf(itemId), String.valueOf(Lists.get(listId)), "itemmark", groupId);
        itemMarkTask.work();
    }

    protected void sendListogram(){
        Intent intent = new Intent(Group2Activity.this, CreateListogramActivity.class);
        intent.putExtra("name", groupName.toString());
        intent.putExtra("groupid", groupId.toString());
        intent.putExtra("userid", userId.toString());
        startActivity(intent);
    }

    protected class ListogramsGetter extends FirstLoginAttemptTask{
        ListogramsGetter(String groupId,  String action){
            super(groupId, action, "3", "0");
        }
    }
    protected class ItemMarkTask extends FirstLoginAttemptTask {
        ItemMarkTask(String userId, String itemIdAndGroupId,  String third, String action, String groupId){
            super(userId, itemIdAndGroupId, third, action, "3", "1", groupId);
        }
    }
    protected class DisactivateListTask extends FirstLoginAttemptTask {
        DisactivateListTask(String groupId, String listId){
            super(groupId, listId, userId, "disactivatelist", "3", "2");
        }
    }
}
