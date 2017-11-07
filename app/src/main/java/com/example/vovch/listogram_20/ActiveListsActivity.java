package com.example.vovch.listogram_20;

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
    private ServiceConnection serviceConn;
    private CurrentActivityProvider currActivityProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersId = getIntent().getExtras().getString("userId");
        update();
        setContentView(R.layout.activity_active_lists);
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
    protected void onPause(){
        super.onPause();
        serviceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                currActivityProvider = ((CurrentActivityProvider.MBinder)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        currActivityProvider.updateActivityContext(2, null);
    }
    @Override
    protected void onResume(){
        super.onResume();
        serviceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                currActivityProvider = ((CurrentActivityProvider.MBinder)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        currActivityProvider.updateActivityContext(2, ActiveListsActivity.this);
    }
    private void finisher(){
        this.finish();
    }
    public WithLoginActivity.FirstLoginAttemptTask getFirstLoginAttemptTask(){
        return actTask;
    }
    @Override
    public void onBackPressed() {
        GroupNames.clear();
        GroupIds.clear();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void update(){
        actTask = new ActiveListSearchTask(usersId, "checkactives");
        actTask.work();
    }
    protected class ActiveListSearchTask extends FirstLoginAttemptTask {
        private int LISTS_DIVIDER = 10301;
        private int INSIDE_DIVIDER = 10253;
        ActiveListSearchTask(String userId, String action){
            super(userId, action);
        }
        @Override
        protected void onGoodResult(String result){
            listsListMaker(result);
        }
        @Override
        protected void onBedResult(String result){
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
                if (result.codePointAt(i) == INSIDE_DIVIDER && flag % 2 == 0) {
                    tempGroupName.setLength(0);
                    tempGroupName.append(result.substring(j, i));
                    j = i + 1;
                    flag++;
                }
                else if(result.codePointAt(i) == INSIDE_DIVIDER && flag % 2 == 1) {
                    tempGroupId.setLength(0);
                    tempGroupId.append(result.substring(j, i));
                    j = i + 1;
                    flag++;
                }else if (result.codePointAt(i) == LISTS_DIVIDER) {
                    tempOwner.setLength(0);
                    tempOwner.append(result.substring(j, i));
                    activeListLayoutDrawer(tempGroupName.toString(), tempOwner.toString(), tempGroupId.toString());
                    j = i + 1;
                }
            }
        }
        protected void activeListLayoutDrawer(final String groupName, String listOwner, final String groupId){
            int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
            int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(matchParent, wrapContent);
            LinearLayout.LayoutParams fullParameters = new LinearLayout.LayoutParams(matchParent, matchParent);
            LinearLayout.LayoutParams groupNameButtonParameters = new LinearLayout.LayoutParams(400, wrapContent);
            LinearLayout.LayoutParams listOwnerButtonParameters = new LinearLayout.LayoutParams(600, wrapContent);
            FrameLayout frameLayout = new FrameLayout(findViewById(R.id.activelistslayout).getContext());
            frameLayout.setLayoutParams(parameters);
            LinearLayout listogramLayout = new LinearLayout(frameLayout.getContext());
            listogramLayout.setOrientation(LinearLayout.HORIZONTAL);
            listogramLayout.setLayoutParams(parameters);
            listogramLayout.setId(LISTOGRAM_ACTIVES_BIG_NUMBER + NumberOfActiveLists);
            listogramLayout.setBaselineAligned(false);
            //listogramLayout.setBackgroundColor(Color.GREEN);
            //listogramLayout.setPadding(0, 5, 0, 0);
            Button groupNameButton = new Button(listogramLayout.getContext());
            groupNameButton.setLayoutParams(groupNameButtonParameters);
            groupNameButton.setClickable(false);
            groupNameButton.setText(groupName);
            groupNameButton.setBackgroundColor(Color.TRANSPARENT);
            Button listOwnerButton = new Button(listogramLayout.getContext());
            listOwnerButton.setLayoutParams(listOwnerButtonParameters);
            listOwnerButton.setClickable(false);
            listOwnerButton.setBackgroundColor(Color.TRANSPARENT);
            StringBuilder tempString = new StringBuilder("New list from ");
            tempString.append(listOwner);
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
    }
}
