package com.example.vovch.listogram_20;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActiveListsActivity extends WithLoginActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ActiveActivityProvider provider;

    private Dialog dialog;

    protected FragmentManager fragmentManager;
    private ActiveFragmentOffline offlineFragment;
    private ActiveFragmentHistory historyFragment;
    private ActiveListsFragment activeFragment;
    private LoginFragment loginFragment;
    private RegistrationFragment registrationFragment;
    private ActiveListsOnlineFragment activeListsOnlineFragment;
    private ActiveListsActivity.Adapter adapter = null;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private DrawerLayout drawer;

    private String loginFailed;

    final View.OnClickListener fabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActiveListsActivity.this, GroupList2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            ActiveListsActivity.this.finish();
        }
    };
    final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {
                if(position == 1){
                    fabActionOne(fab);
                }
                else if(position == 2){
                    fabActionTwo(fab);
                }
                else if(position == 0){
                    fabActionZero(fab);
                }
        }
        @Override
        public void onPageScrollStateChanged(int state) {}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_active_lists);

        loginFailed = null;

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(2, ActiveListsActivity.this);

        fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.active_lists_viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        TabLayout tabs = (TabLayout) findViewById(R.id.active_lists_tabs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabs.setElevation(24);
        }
        tabs.removeAllTabs();
        tabs.addTab(tabs.newTab().setText("Online"));
        tabs.addTab(tabs.newTab().setText("Offline"));
        tabs.addTab(tabs.newTab().setText("History"));
        tabs.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.active_lists_fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setElevation(24);
        }

        viewPager.addOnPageChangeListener(pageChangeListener);

        activeListsOnlineFragment = null;
        loginFragment = null;
        registrationFragment = null;

        adapter.startUpdate(viewPager);
        activeFragment = (ActiveListsFragment) adapter.instantiateItem(viewPager, 0);
        offlineFragment = (ActiveFragmentOffline) adapter.instantiateItem(viewPager, 1);
        historyFragment = (ActiveFragmentHistory) adapter.instantiateItem(viewPager, 2);
        adapter.finishUpdate(viewPager);

        viewPager.setCurrentItem(1);
        fabActionOne(fab);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onStart(){
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(2, ActiveListsActivity.this);
        refreshOfflineLists();
        refreshOfflineHistory();
        loginFragmentStart();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 2) {
            provider.nullActiveActivity();
        }
        offlineFragment.activeFragmentCleaner();
        super.onStop();
    }
    private void finisher(){
        this.finish();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(registrationFragment != null) {
            registrationToLoginFragmentChange();
        } else {
            if(provider.getActiveActivityNumber() == 2) {
                provider.nullActiveActivity();
            }
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
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(ActiveListsActivity.this, GroupList2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_share) {
            provider.userSessionData.exit();
            activeToLoginFragmentChange();
        } else if (id == R.id.nav_copy_id){
            //ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            //ClipData clip = ClipData.newPlainText("", usersId);
            //clipboard.setPrimaryClip(clip);
            ClipboardManager clipboard = (ClipboardManager) ActiveListsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", provider.userSessionData.getId());
            clipboard.setPrimaryClip(clip);
            dialog = new Dialog(ActiveListsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_view);
            TextView text = (TextView) dialog.findViewById(R.id.dialogTextView);
            text.setText("Your Id Copied");
            text.setBackgroundColor(Color.GRAY);
            dialog.show();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void refreshActiveLists(){
            update();
    }
    protected void refreshOfflineLists(){
        if(offlineFragment != null){
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.setRefresher();
            offlineUpdate(true);
        }
    }
    protected void refreshOfflineHistory(){
        if(historyFragment != null){
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            historyFragment.setRefresher();
            offlineUpdate(false);
        }
    }
    protected void onOfflineFragmentStart(){
        if(offlineFragment != null){
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            refreshOfflineLists();
        }
    }
    protected void onHistoryFragmentStart(){
        if(historyFragment != null){
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            refreshOfflineHistory();
        }
    }





    protected void tryToLoginFromPrefs(){
        provider.tryToLoginFromPrefs();
    }
    protected void tryToLoginFromForms(String login, String password){
        provider.tryToLoginFromForms(login, password);
    }
    protected String getToken(){
        return provider.userSessionData.getToken();
    }
    protected void loginFragmentStart(){
        tryToLoginFromPrefs();
    }
    protected void onLoginFailed(String result){                                                        //TODO cases
        activeToLoginFragmentChange();
        loginFailed = result;
        checkLoginBadInformNeeded();
    }
    protected void checkLoginBadInformNeeded(){
        if(loginFailed != null){
            loginFragment.checkRootView(viewPager, getLayoutInflater());
            loginFragment.badInform(loginFailed);
        }
    }
    protected void loginFragmentOnStart(){
        checkLoginBadInformNeeded();
    }
    protected void loginToActiveFragmentChange() {                                                      //TODO same class for moving to activeListsOnlineFragment
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        if(loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null){
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        } else if(loginFragment != null){
            transaction.remove(loginFragment);
            loginFragment.onDestroy();
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        }
        registrationFragment = null;
        loginFragment = null;
        transaction.commit();
    }
    protected void activeListsOnlineFragmentStart(){
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if(viewPager.getCurrentItem() == 0) {
            fabActionZero(fab);
        }
        if(activeListsOnlineFragment != null) {
            activeListsOnlineFragment.setRefresher();
        }
        update();
    }
    protected void loginToRegistrationFragmentChange(){
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        if(loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null){
            registrationFragment = new RegistrationFragment();
            transaction.add(R.id.active_lists_page_one, registrationFragment);
        } else if(loginFragment != null){
            transaction.remove(loginFragment);
            loginFragment.onDestroy();
            registrationFragment = new RegistrationFragment();
            transaction.add(R.id.active_lists_page_one, registrationFragment);
        }
        activeListsOnlineFragment = null;
        loginFragment = null;
        transaction.commit();
    }
    protected void tryToRegister(String login, String password){
        provider.registrationTry(login, password);
    }
    protected void registrationToActiveListsOnlineFragmentChange(){
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        if(loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null){
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        } else if(registrationFragment != null){
            transaction.remove(registrationFragment);
            registrationFragment.onDestroy();
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        }
        registrationFragment = null;
        loginFragment = null;
        transaction.commit();
    }
    protected void badRegistrationTry(String result){
        if(registrationFragment != null) {
            registrationFragment.checkRootView(viewPager, getLayoutInflater());
            registrationFragment.badInform(result);
        }
    }
    protected void activeToLoginFragmentChange(){
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        if(loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null){
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        } else if(activeListsOnlineFragment != null){
            transaction.remove(activeListsOnlineFragment);
            activeListsOnlineFragment.unsetRefresher();
            activeListsOnlineFragment.onDestroy();
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        }
        registrationFragment = null;
        activeListsOnlineFragment = null;
        transaction.commit();
        if(viewPager.getCurrentItem() == 0) {
            fabActionZero(fab);
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    protected void registrationToLoginFragmentChange(){
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        if(loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null){
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        } else if(registrationFragment != null){
            transaction.remove(registrationFragment);
            registrationFragment.onDestroy();
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        }
        registrationFragment = null;
        activeListsOnlineFragment = null;
        transaction.commit();
    }
    protected void noInternet(){
        if(activeFragment != null){
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.noInternet();
        }
    }





    protected void update(){
        provider.getActiveActivityActiveLists();
        if(activeListsOnlineFragment != null){
            activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
            if(activeListsOnlineFragment.getRefresher() != null) {
                activeListsOnlineFragment.setRefresherRefreshing();
            }
        }
    }
    protected void offlineUpdate(boolean type){
        provider.startOfflineGetterDatabaseTask(type);
    }
    protected void disactivateList(SList list){
        provider.activeActivityDisactivateList(list);
    }
    protected void itemmark(Item item){
        provider.activeListsItemmark(item);
    }
    protected void showGood(ListInformer[] result){
        if(activeListsOnlineFragment != null) {
            activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
            activeListsOnlineFragment.fragmentShowGood(result);
            if(activeListsOnlineFragment.getRefresher() != null) {
                activeListsOnlineFragment.setRefresherNotRefreshing();
            }
        }
    }
    protected void showBad(ListInformer[] result){
        if(activeListsOnlineFragment != null) {
            activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
            activeListsOnlineFragment.fragmentShowBad(result);
            if(activeListsOnlineFragment.getRefresher() != null) {
                activeListsOnlineFragment.setRefresherNotRefreshing();
            }
        }
    }
    protected void goToGroup(UserGroup group){
        if(group != null) {
            if (group.getId() != null && group.getName() != null) {
                provider.setActiveGroup(group);
                Intent intent = new Intent(ActiveListsActivity.this, Group2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finisher();
            }
        }
    }

    protected void showActiveOfflineGood(SList[] lists){
        if(offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowGood(lists);
            if(offlineFragment.getRefresher() != null) {
                offlineFragment.unsetRefresher();
            }
        }
    }
    protected void showActiveOfflineBad(SList[] lists){
        if(offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowBad(lists);
            if(offlineFragment.getRefresher() != null) {
                offlineFragment.unsetRefresher();
            }
        }
    }
    protected void showHistoryOfflineGood(SList[] lists){
        if(historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            historyFragment.fragmentShowGood(lists);
            if(historyFragment.getRefresher() != null){
                historyFragment.unsetRefresher();
            }
        }
    }
    protected void showHistoryOfflineBad(SList[] lists){
        if(historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            historyFragment.fragmentShowBad(lists);
            if(historyFragment.getRefresher() != null){
                historyFragment.unsetRefresher();
            }
        }
    }
    protected void showDisactivateOfflineGood(SList result){
        if(offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentDisactivateGood(result);
        }
    }
    protected void showDisactivateOfflineBad(SList result){
        if(offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentDisactivateBad(result);
        }
    }
    protected void showItemmarkOfflineGood(Item result){
        if(offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowSecondGood(result);
        }
    }
    protected void showItemmarkOfflineBad(Item result){
        if(offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowSecondBad(result);
        }
    }


    private void fabActionZero(FloatingActionButton fab){
        if(activeListsOnlineFragment != null) {
            fab.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add, getTheme()));
            } else {
                fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add));
            }
            fab.setOnClickListener(fabOnClickListener);
        }
        else{
            fab.hide();
        }
    }
    private void fabActionOne(FloatingActionButton fab){
        fab.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_send));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActiveListsActivity.this, CreateListogramActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("name", "");
                intent.putExtra("groupid", "");
                intent.putExtra("loadtype", false);
                startActivity(intent);
            }
        });
    }
    private void fabActionTwo(FloatingActionButton fab){
        fab.hide();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.clean();
        adapter.addFragment(new ActiveListsFragment(), "Online");
        adapter.addFragment(new ActiveFragmentOffline(), "Offline");
        adapter.addFragment(new ActiveFragmentHistory(), "History");
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
        protected void replace0FragmentByNewOne(Fragment oldOne, Fragment newOne){
            mFragmentList.add(0, newOne);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
