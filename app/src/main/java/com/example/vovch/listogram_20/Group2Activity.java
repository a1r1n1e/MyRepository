package com.example.vovch.listogram_20;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Group2Activity extends WithLoginActivity {
    private String groupName;
    private String groupId;
    protected FragmentManager fragmentManager;
    private ActiveActivityProvider provider;
    private GroupFragmentActive activeFragment;
    private GroupFragmentHistory historyFragment;
    private Adapter adapter;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private boolean activeReady;
    private boolean historyReady;

    View.OnClickListener groupSettingsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Group2Activity.this, GroupSettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            if(provider.getActiveActivityNumber() == 3) {
                provider.nullActiveActivity();
            }
            Group2Activity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activeReady = false;
        historyReady = false;

        setContentView(R.layout.activity_group3);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(3, Group2Activity.this);

        UserGroup activeGroup = provider.getActiveGroup();
        groupId = activeGroup.getId();
        groupName = activeGroup.getName();

        fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.group_viewpager);
        setupViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.group_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);

        TextView groupNameTextView = (TextView) findViewById(R.id.group_name_textview);
        groupNameTextView.setText(groupName);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.group_settings_imagebutton);
        Uri uri = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/ic_more_vertical_32");
        settingsButton.setImageURI(uri);
        settingsButton.setOnClickListener(groupSettingsButtonListener);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_group);
        tabs.removeAllTabs();
        tabs.addTab(tabs.newTab().setText("Active"));
        tabs.addTab(tabs.newTab().setText("History"));
        tabs.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.group_fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setElevation(24);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    fab.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send, getTheme()));
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send));
                    }
                }
                else if(position == 1){
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        adapter.startUpdate(viewPager);
        activeFragment = (GroupFragmentActive) adapter.instantiateItem(viewPager, 0);
        historyFragment = (GroupFragmentHistory) adapter.instantiateItem(viewPager, 1);
        adapter.finishUpdate(viewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                if (currentFragment instanceof GroupFragmentActive) {
                    sendListogram();
                } else if (currentFragment instanceof GroupFragmentHistory) {
                    historyLoad();
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    public void onStart(){
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(3, Group2Activity.this);
    }
    @Override
    public void onBackPressed(){
        cleaner();
        if(provider.getActiveActivityNumber() == 3) {
            provider.nullActiveActivity();
        }
        provider.setActiveGroup(null);
        Intent intentGroupList = new Intent(Group2Activity.this, ActiveListsActivity.class);
        intentGroupList.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intentGroupList);
        this.finish();
    }
    @Override
    protected void onDestroy(){
        cleaner();
        super.onDestroy();
    }
    private void cleaner(){
        if(activeFragment != null) {
            if(activeReady) {
                activeFragment.activeFragmentCleaner();
            }
        }
        if(historyFragment != null) {
            if(historyReady) {
                historyFragment.historyFragmentCleaner();
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onStop(){
        if(provider.getActiveActivityNumber() == 3) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }
    protected void update(){
        if(activeFragment != null){
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.setRefresherRefreshing();
        }
        provider.getGroupActiveLists(groupId);
    }
    protected void refreshActiveLists(){
        if(activeFragment != null){
            if(activeReady) {
                activeFragment.checkRootView(viewPager, getLayoutInflater());
                activeFragment.setRefresherRefreshing();
                update();
            }
        }
    }
    protected  void onActiveReady(){
        activeReady = true;
        if(activeFragment != null) {
            activeFragment.setRefresher();
        }
        refreshActiveLists();
    }
    protected void refreshHistoryLists(){
        if(historyFragment != null){
            if(historyReady) {
                historyFragment.checkRootView(viewPager, getLayoutInflater());
                historyFragment.setRefresherRefreshing();
                historyLoad();
            }
        }
    }
    protected void onHistoryReady(){
        if(historyFragment != null) {
            historyReady = true;
            historyFragment.setRefresher();
        }
    }
    protected void historyLoad(){
        provider.getGroupHistoryLists(groupId);
    }
    protected void itemmark(Item item){
        provider.itemmark(item);
    }
    protected void disactivateGroupList(SList list){
        if(list != null) {
            provider.disactivateGroupList(list);
        }
    }
    protected void showGroupDataSettledGood(){

    }
    protected void showGroupDataSettledBad(){

    }


    protected void historyLoadOnGood(SList[] result){
        if(historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            onHistoryReady();                                                                        //strange solution
            historyFragment.historyFragmentCleaner();
            historyFragment.setRefresherNotRefreshing();
            historyFragment.fragmentShowGood(result);
        }
    }
    protected void historyLoadOnBad(String result){
        if(historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            onHistoryReady();                                                                        //strange solution
            historyFragment.setRefresherNotRefreshing();
            historyFragment.fragmentShowBad(result);
        }
    }
    protected void showGood(SList[] result){
        if(activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.activeFragmentCleaner();
            activeFragment.fragmentShowGood(result);
            activeFragment.setRefresherNotRefreshing();
        }
    }
    protected void showBad(SList[] result){
        if(activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.activeFragmentCleaner();
            activeFragment.fragmentShowBad(result);
            activeFragment.setRefresherNotRefreshing();
        }
    }
    protected void showSecondGood(SList result){
        if(activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowSecondGood(result);
        }
    }
    protected void showSecondBad(SList result){
        if(activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowSecondBad(result);
        }
    }
    protected void showThirdGood(Item result){
        if(activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowThirdGood(result);
        }
    }
    protected void showThirdBad(Item result){
        if(activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowThirdBad(result);
        }
    }
    protected void showItemmarkProcessing(Item item){
        if(activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowProcessing(item);
        }
    }

    public void sendListogram(){
        Intent intent = new Intent(Group2Activity.this, CreateListogramActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("loadtype", true);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.clean();
        adapter.addFragment(new GroupFragmentActive(), "Active");
        adapter.addFragment(new GroupFragmentHistory(), "History");
        viewPager.setAdapter(adapter);
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        protected Adapter(FragmentManager manager) {
            super(manager);
        }
        private void clean(){
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        protected void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
