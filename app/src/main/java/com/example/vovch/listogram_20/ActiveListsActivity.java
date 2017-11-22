package com.example.vovch.listogram_20;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ActiveListsActivity extends WithLoginActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected ActiveListSearchTask actTask;
    protected static SharedPreferences loginPasswordPair;
    private int NumberOfActiveLists = 0;
    private int LISTOGRAM_ACTIVES_BIG_NUMBER = 90000000;
    private int LISTOGRAM_FRAME_LAYOUTS_BIG_NUMBER =10000;
    private int LISTOGRAM_ACTIVE_BUTTONS_BIG_NUMBER =20000;
    private static ArrayList<Integer> GroupIds = new ArrayList<>();
    private static ArrayList<String> GroupNames= new ArrayList<>();
    private String usersId;
    private static final String  APP_PREFERENCES_USERID= "userid";
    private static final String APP_PREFERENCES_LOGIN = "login";
    private static final String APP_PREFERENCES_PASSWORD = "password";
    public static final String APP_PREFERENCES = "autentification";
    private ActiveActivityProvider provider;

    private int LISTS_DIVIDER = 10301;
    private int INSIDE_DIVIDER = 10253;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_active_lists);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(2, ActiveListsActivity.this);

        usersId = getIntent().getExtras().getString("userId");

        //update();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(2, ActiveListsActivity.this);
        update();
    }
    @Override
    protected void onPause(){
        provider.nullActiveActivity();
        cleaner();
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        provider.nullActiveActivity();
        super.onDestroy();
    }
    private void finisher(){
        this.finish();
    }
    public WithLoginActivity.FirstLoginAttemptTask getFirstLoginAttemptTask(){
        return actTask;
    }
    @Override
    public void onBackPressed() {
        cleaner();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //provider.nullActiveActivity();
            ActiveListsActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.active_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(ActiveListsActivity.this, GroupList2Activity.class);
            intent.putExtra("userId", usersId);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_share) {
            loginPasswordPair = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = loginPasswordPair.edit();
            editor.putString(APP_PREFERENCES_LOGIN, null);
            editor.putString(APP_PREFERENCES_PASSWORD, null);
            editor.putString(APP_PREFERENCES_USERID, null);
            editor.apply();
            Intent intent = new Intent(ActiveListsActivity.this, MainActivity.class);
            startActivity(intent);
            finisher();
        } else if (id == R.id.nav_copy_id){
            ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", usersId);
            clipboard.setPrimaryClip(clip);

            dialog = new Dialog(ActiveListsActivity.this);
            dialog.setTitle(usersId);
            dialog.setContentView(R.layout.dialog_view);
            TextView text = (TextView) dialog.findViewById(R.id.dialogTextView);
            text.setText("Your Id Copied");
            text.setBackgroundColor(Color.GRAY);
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void update(){
        actTask = new ActiveListSearchTask(usersId, "checkactives");
        actTask.work();
    }
    private void cleaner(){
        NumberOfActiveLists = 0;
        GroupIds.clear();
        GroupNames.clear();
        LinearLayout layout = (LinearLayout) findViewById(R.id.activelistslayout);
        layout.removeAllViews();
    }
    protected void showGood(String result){
        cleaner();
        listsListMaker(result);
    }
    protected void showBad(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.activelistslayout);
        TextView messageTextView = new TextView(layout.getContext());
        LinearLayout.LayoutParams layoutParameters= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        messageTextView.setLayoutParams(layoutParameters);
        messageTextView.setText("No Active listograms(");
        layout.addView(messageTextView);
    }
    protected void listsListMaker(String result) {
        int length = result.length();
        StringBuilder tempGroupName = new StringBuilder();
        StringBuilder tempOwner = new StringBuilder();
        StringBuilder tempGroupId = new StringBuilder();
        int flag = 0;
        for (int i = 0, j = 0; i < length; i++) {
            if (result.codePointAt(i) == INSIDE_DIVIDER) {
                tempGroupName.setLength(0);
                tempGroupName.append(result.substring(j, i));
                j = i + 1;
            }
            else if(result.codePointAt(i) == LISTS_DIVIDER) {
                tempGroupId.setLength(0);
                tempGroupId.append(result.substring(j, i));
                j = i + 1;
                activeListLayoutDrawer(tempGroupName.toString(), tempGroupId.toString());
            }
        }
    }
    protected void activeListLayoutDrawer(final String groupName, final String groupId){
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(matchParent, wrapContent);
        LinearLayout.LayoutParams fullParameters = new LinearLayout.LayoutParams(matchParent, matchParent);
        LinearLayout.LayoutParams groupNameButtonParameters = new LinearLayout.LayoutParams(0, wrapContent, 0.4f);
        LinearLayout.LayoutParams listOwnerButtonParameters = new LinearLayout.LayoutParams(0, wrapContent, 0.6f);
        FrameLayout frameLayout = new FrameLayout(findViewById(R.id.activelistslayout).getContext());
        frameLayout.setLayoutParams(parameters);
        LinearLayout listogramLayout = new LinearLayout(frameLayout.getContext());
        listogramLayout.setOrientation(LinearLayout.HORIZONTAL);
        listogramLayout.setLayoutParams(parameters);
        listogramLayout.setId(LISTOGRAM_ACTIVES_BIG_NUMBER + NumberOfActiveLists);
        listogramLayout.setBaselineAligned(false);
        Button groupNameButton = new Button(listogramLayout.getContext());
        groupNameButton.setLayoutParams(groupNameButtonParameters);
        groupNameButton.setClickable(false);
        groupNameButton.setText(groupName);
        groupNameButton.setBackgroundColor(Color.TRANSPARENT);
        Button listOwnerButton = new Button(listogramLayout.getContext());
        listOwnerButton.setLayoutParams(listOwnerButtonParameters);
        listOwnerButton.setClickable(false);
        listOwnerButton.setBackgroundColor(Color.TRANSPARENT);
        StringBuilder tempString = new StringBuilder("New list");
        listOwnerButton.setText(tempString.toString());
        listogramLayout.addView(groupNameButton);
        listogramLayout.addView(listOwnerButton);
        listogramLayout.setBackgroundColor(Color.GREEN);
        frameLayout.setPadding(0, 80, 0, 80);
        Button frameButton = new Button(frameLayout.getContext());
        frameButton.setLayoutParams(fullParameters);
        frameButton.setId(LISTOGRAM_FRAME_LAYOUTS_BIG_NUMBER + NumberOfActiveLists);
        GroupIds.add(GroupIds.size(), Integer.parseInt(groupId));
        GroupNames.add(GroupNames.size(), groupName);
        frameButton.setBackgroundColor(Color.TRANSPARENT);
        View.OnClickListener frameButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId() - LISTOGRAM_FRAME_LAYOUTS_BIG_NUMBER;
                Button button = (Button) v;
                String text = button.getText().toString();
                Intent intent = new Intent(ActiveListsActivity.this, Group2Activity.class);
                intent.putExtra("userid", usersId);
                int grid = GroupIds.get(id);
                intent.putExtra("groupid", String.valueOf(grid));
                intent.putExtra("name", GroupNames.get(id));
                startActivity(intent);
                finisher();
            }
        };
        frameButton.setClickable(true);
        frameButton.setOnClickListener(frameButtonListenner);
        LinearLayout basicLayout = (LinearLayout) findViewById(R.id.activelistslayout);
        frameLayout.addView(listogramLayout);
        frameLayout.addView(frameButton);
        basicLayout.addView(frameLayout);
        NumberOfActiveLists++;
    }
    protected class ActiveListSearchTask extends FirstLoginAttemptTask {
        ActiveListSearchTask(String userId, String action){
            super(userId, action, "2", "0");
        }
    }
}
