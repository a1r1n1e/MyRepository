package com.example.vovch.listogram_20.activities.complex;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vovch.listogram_20.ActiveActivityProvider;
import com.example.vovch.listogram_20.activities.simple.CreateListogramActivity;
import com.example.vovch.listogram_20.activities.simple.GroupList2Activity;
import com.example.vovch.listogram_20.activities.WithLoginActivity;
import com.example.vovch.listogram_20.data_types.ListImageButton;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.ActiveFragmentHistory;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.ActiveFragmentOffline;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.ActiveListsFragment;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.active_lists_fragment_content.ActiveListsOnlineFragment;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.active_lists_fragment_content.LoginFragment;
import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.active_lists_fragment_content.RegistrationFragment;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ListInformer;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.UserGroup;

import java.util.ArrayList;
import java.util.List;

public class ActiveListsActivity extends WithLoginActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ActiveActivityProvider provider;
    private int loadType = -1;

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
            intent.putExtra("loadtype", 0);
            startActivity(intent);
            ActiveListsActivity.this.finish();
        }
    };
    final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 1) {
                fabActionOne(fab);
            } else if (position == 2) {
                fabActionTwo(fab);
            } else if (position == 0) {
                fabActionZero(fab);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_active_lists);

        loginFailed = null;

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(2, ActiveListsActivity.this);

        loadType = provider.getActiveListsActivityLoadType();

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
        offlineFragment = (ActiveFragmentOffline) adapter.instantiateItem(viewPager, 1);
        historyFragment = (ActiveFragmentHistory) adapter.instantiateItem(viewPager, 2);
        activeFragment = (ActiveListsFragment) adapter.instantiateItem(viewPager, 0);
        adapter.finishUpdate(viewPager);

        if( -1 < loadType && loadType < 3) {                                                           //setting current page of viewPager
            viewPager.setCurrentItem(loadType);
        }
        fabActionOne(fab);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(2, ActiveListsActivity.this);
        loadType = provider.getActiveListsActivityLoadType();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (provider.getActiveActivityNumber() == 2) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }

    private void finisher() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (registrationFragment != null) {
            registrationToLoginFragmentChange();
        } else {
            if (provider.getActiveActivityNumber() == 2) {
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
            intent.putExtra("loadtype", 0);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_share) {
            loginFailed = "You Logged Out";
            ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
            exitDialogFragment.setActiveActivityProvider(provider);
            exitDialogFragment.setActiveListsActivity(ActiveListsActivity.this);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            exitDialogFragment.show(transaction, "dialog");
        } else if (id == R.id.nav_copy_id) {
            //ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            //ClipData clip = ClipData.newPlainText("", usersId);
            //clipboard.setPrimaryClip(clip);
            ClipboardManager clipboard = (ClipboardManager) ActiveListsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", provider.userSessionData.getId());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ActiveListsActivity.this, "Your Id Copied", Toast.LENGTH_LONG)
                    .show();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class ExitDialogFragment extends DialogFragment {
        private ActiveActivityProvider activeActivityProvider;
        private ActiveListsActivity activeListsActivity;
        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider){
            activeActivityProvider = newActiveActivityProvider;
        }
        protected void setActiveListsActivity(ActiveListsActivity newActivity){
            activeListsActivity = newActivity;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = "Want To Logout?";
            String button1String = "Yes";
            String button2String = "No";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Nothing Happened", Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activeActivityProvider.userSessionData.exit();
                    activeListsActivity.activeToLoginFragmentChange();
                }
            });
            builder.setCancelable(true);
            return builder.create();
        }
    }

    public void refreshActiveLists() {
        update();
    }

    public void refreshOfflineLists() {
        if (offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.setRefresher();
            offlineUpdate(true);
        }
    }

    public void refreshOfflineHistory() {
        if (historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            historyFragment.setRefresher();
            offlineUpdate(false);
        }
    }

    public void onOfflineFragmentStart(ActiveFragmentOffline fragment) {
        if(fragment != null){
            offlineFragment = fragment;
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            refreshOfflineLists();
        }
    }

    public void onHistoryFragmentStart(ActiveFragmentHistory fragment) {
        if (fragment != null) {
            historyFragment = fragment;
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            refreshOfflineHistory();
        }
    }


    public void tryToLoginFromPrefs() {
        provider.tryToLoginFromPrefs();
    }

    public void tryToLoginFromForms(String login, String password) {
        provider.tryToLoginFromForms(login, password);
    }

    public String getToken() {
        return provider.userSessionData.getToken();
    }

    public void loginFragmentStart() {
        if(loginFailed == null) {
            tryToLoginFromPrefs();
        }
        checkLoginBadInformNeeded();
    }

    public void onLoginFailed(String result) {                                                        //TODO cases
        activeToLoginFragmentChange();
        loginFailed = result;
        checkLoginBadInformNeeded();
    }

    public void checkLoginBadInformNeeded() {
        if (loginFailed != null) {
            loginFragment.checkRootView(viewPager, getLayoutInflater());
            loginFragment.badInform(loginFailed);
        }
    }

    public void loginToActiveFragmentChange() {
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        activeFragment.unsetRefresher();
        if (loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null) {
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        } else if (loginFragment != null) {
            transaction.remove(loginFragment);
            loginFragment.onDestroy();
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        }
        registrationFragment = null;
        loginFragment = null;
        loginFailed = null;
        transaction.commit();
    }

    public void activeListsOnlineFragmentStart() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (viewPager.getCurrentItem() == 0) {
            fabActionZero(fab);
        }
        if (activeListsOnlineFragment != null) {
            activeListsOnlineFragment.setRefresher();
        }
        update();
    }

    public void loginToRegistrationFragmentChange() {
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        activeFragment.unsetRefresher();
        if (loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null) {
            registrationFragment = new RegistrationFragment();
            transaction.add(R.id.active_lists_page_one, registrationFragment);
        } else if (loginFragment != null) {
            transaction.remove(loginFragment);
            loginFragment.onDestroy();
            registrationFragment = new RegistrationFragment();
            transaction.add(R.id.active_lists_page_one, registrationFragment);
        }
        activeListsOnlineFragment = null;
        loginFragment = null;
        transaction.commit();
    }

    public void tryToRegister(String login, String password) {
        provider.registrationTry(login, password);
    }

    public void registrationToActiveListsOnlineFragmentChange() {
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        activeFragment.unsetRefresher();
        if (loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null) {
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        } else if (registrationFragment != null) {
            transaction.remove(registrationFragment);
            registrationFragment.onDestroy();
            activeListsOnlineFragment = new ActiveListsOnlineFragment();
            transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment);
        }
        registrationFragment = null;
        loginFragment = null;
        transaction.commit();
    }

    public void badRegistrationTry(String result) {
        if (registrationFragment != null) {
            registrationFragment.checkRootView(viewPager, getLayoutInflater());
            registrationFragment.badInform(result);
        }
    }

    public void activeToLoginFragmentChange() {
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        activeFragment.unsetRefresher();
        if (activeListsOnlineFragment == null && registrationFragment == null) {
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        } else if (activeListsOnlineFragment != null) {
            transaction.remove(activeListsOnlineFragment);
            activeListsOnlineFragment.unsetRefresher();
            activeListsOnlineFragment.onDestroy();
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        }
        registrationFragment = null;
        activeListsOnlineFragment = null;
        transaction.commit();
        if (viewPager.getCurrentItem() == 0) {
            fabActionZero(fab);
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void registrationToLoginFragmentChange() {
        FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
        activeFragment.unsetRefresher();
        if (loginFragment == null && activeListsOnlineFragment == null && registrationFragment == null) {
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        } else if (registrationFragment != null) {
            transaction.remove(registrationFragment);
            registrationFragment.onDestroy();
            loginFragment = new LoginFragment();
            transaction.add(R.id.active_lists_page_one, loginFragment);
        }
        registrationFragment = null;
        activeListsOnlineFragment = null;
        transaction.commit();
    }

    public void noInternet() {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.noInternet();
        }
    }

    public void update() {
        provider.getActiveActivityActiveLists();
        if (activeListsOnlineFragment != null) {
            activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
            if (activeListsOnlineFragment.getRefresher() != null) {
                activeListsOnlineFragment.setRefresherRefreshing();
            }
        }
    }

    public void offlineUpdate(boolean type) {
        provider.startOfflineGetterDatabaseTask(type);
    }

    public void disactivateList(ListImageButton button) {
        SList list = button.getList();
        if(list != null) {
            DisactivateDialogFragment dialogFragment = new DisactivateDialogFragment();
            dialogFragment.setList(list);
            dialogFragment.setActiveActivityProvider(provider);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, "dialog");
        }
    }

    public static class DisactivateDialogFragment extends DialogFragment {
        private SList list;
        private ActiveActivityProvider activeActivityProvider;
        protected void setList(SList newList){
            list = newList;
        }
        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider){
            activeActivityProvider = newActiveActivityProvider;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = "Want To Kill List?";
            String button1String = "Yes";
            String button2String = "No";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Nothing Happened", Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activeActivityProvider.activeActivityDisactivateList(list);
                    Toast.makeText(getActivity(), "Processing",
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.setCancelable(true);
            list.getDisButton().setFocusable(true);
            list.getDisButton().setClickable(true);
            return builder.create();
        }
    }

    public void redactList(SList list){
        if(list != null){
            RedactDialogFragment dialogFragment = new RedactDialogFragment();
            dialogFragment.setList(list);
            dialogFragment.setActiveActivityProvider(provider);
            dialogFragment.setActivity(ActiveListsActivity.this);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, "dialog");
        }
    }

    public static class RedactDialogFragment extends DialogFragment {
        private SList list;
        private ActiveActivityProvider activeActivityProvider;
        private ActiveListsActivity activity;
        protected void setActivity(ActiveListsActivity newActivity){
            activity = newActivity;
        }
        protected void setList(SList newList){
            list = newList;
        }
        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider){
            activeActivityProvider = newActiveActivityProvider;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = "Whant To Redact List?";
            String button1String = "Yes";
            String button2String = "No";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Redacting", Toast.LENGTH_LONG)
                            .show();
                    activeActivityProvider.setResendingList(list);
                    activeActivityProvider.saveTempItems(activeActivityProvider.dataExchanger.makeTempItemsFromItems(list.getItems()));
                    Intent intent = new Intent(activity, CreateListogramActivity.class);
                    intent.putExtra("loadtype", 2);
                    startActivity(intent);
                    activity.finish();
                }
            });
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Nothing Happened:)",
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.setCancelable(true);
            list.getRedactButton().setFocusable(true);
            list.getRedactButton().setClickable(true);
            return builder.create();
        }
    }

    public void resendList(SList list){
        if(list != null){
            ResendDialogFragment dialogFragment = new ResendDialogFragment();
            dialogFragment.setList(list);
            dialogFragment.setActiveActivityProvider(provider);
            dialogFragment.setActivity(ActiveListsActivity.this);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, "dialog");
        }
    }

    public static class ResendDialogFragment extends DialogFragment {
        private SList list;
        private ActiveActivityProvider activeActivityProvider;
        private ActiveListsActivity activity;
        protected void setActivity(ActiveListsActivity newActivity){
            activity = newActivity;
        }
        protected void setList(SList newList){
            list = newList;
        }
        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider){
            activeActivityProvider = newActiveActivityProvider;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = "What To Do?";
            String button1String = "Send To Group";
            String button2String = "Copy";
            String button3String = "Cancel";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "List Copied", Toast.LENGTH_LONG)
                            .show();
                    activeActivityProvider.createListogramOffline(list.getItems(), activity);
                }
            });
            if(activeActivityProvider.userSessionData.isLoginned()) {
                builder.setNeutralButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Chose Where To :)",
                                Toast.LENGTH_LONG).show();
                        activeActivityProvider.setResendingList(list);
                        Intent intent = new Intent(activity, GroupList2Activity.class);
                        intent.putExtra("loadtype", 1);
                        startActivity(intent);
                        activity.finish();
                    }
                });
            }
            builder.setPositiveButton(button3String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Nothing Happened:)",
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.setCancelable(true);
            list.getResendButton().setFocusable(true);
            list.getResendButton().setClickable(true);
            return builder.create();
        }
    }

    public void itemmark(Item item) {
        provider.activeListsItemmark(item);
    }

    public void showGood(ListInformer[] result) {
        if (activeListsOnlineFragment != null) {
            activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
            activeListsOnlineFragment.fragmentShowGood(result);
            if (activeListsOnlineFragment.getRefresher() != null) {
                activeListsOnlineFragment.setRefresherNotRefreshing();
            }
        }
    }

    public void showBad(ListInformer[] result) {
        if (activeListsOnlineFragment != null) {
            activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
            activeListsOnlineFragment.fragmentShowBad(result);
            if (activeListsOnlineFragment.getRefresher() != null) {
                activeListsOnlineFragment.setRefresherNotRefreshing();
            }
        }
    }

    public void goToGroup(UserGroup group) {
        if (group != null) {
            if (group.getId() != null && group.getName() != null) {
                provider.setActiveGroup(group);
                provider.makeAllMembersPossible(group);
                Intent intent = new Intent(ActiveListsActivity.this, Group2Activity.class);
                startActivity(intent);
                finisher();
            }
        }
    }

    public void showActiveFragmentGood(){
        if(activeFragment != null && viewPager != null){
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowGood(null);
        }
    }

    public void showActiveFragmentBad(){
        if(activeFragment != null && viewPager != null){
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowBad(null);
        }
    }

    public void showActiveOfflineGood(SList[] lists) {
        if (offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowGood(lists);
            if (offlineFragment.getRefresher() != null) {
                offlineFragment.unsetRefresher();
            }
        }
    }

    public void showActiveOfflineBad(SList[] lists) {
        if (offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowBad(lists);
            if (offlineFragment.getRefresher() != null) {
                offlineFragment.unsetRefresher();
            }
        }
    }

    public void showHistoryOfflineGood(SList[] lists) {
        if (historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            historyFragment.fragmentShowGood(lists);
            if (historyFragment.getRefresher() != null) {
                historyFragment.unsetRefresher();
            }
        }
    }

    public void showHistoryOfflineBad(SList[] lists) {
        if (historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            historyFragment.fragmentShowBad(lists);
            if (historyFragment.getRefresher() != null) {
                historyFragment.unsetRefresher();
            }
        }
    }

    public void showDisactivateOfflineGood(SList result) {
        if (offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentDisactivateGood(result);
            refreshOfflineHistory();
        }
    }

    public void showDisactivateOfflineBad(SList result) {
        if (offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentDisactivateBad(result);
        }
    }

    public void showItemmarkOfflineGood(Item result) {
        if (offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowSecondGood(result);
        }
    }

    public void showItemmarkOfflineBad(Item result) {
        if (offlineFragment != null) {
            offlineFragment.checkRootView(viewPager, getLayoutInflater());
            offlineFragment.fragmentShowSecondBad(result);
        }
    }


    private void fabActionZero(FloatingActionButton fab) {
        if (activeListsOnlineFragment != null) {
            fab.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.square_arrow_48, getTheme()));
            } else {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.square_arrow_48));
            }
            fab.setOnClickListener(fabOnClickListener);
        } else {
            fab.hide();
        }
    }

    private void fabActionOne(FloatingActionButton fab) {
        fab.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.add_48, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.add_48));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActiveListsActivity.this, CreateListogramActivity.class);
                intent.putExtra("loadtype", false);
                startActivity(intent);
            }
        });
    }

    private void fabActionTwo(FloatingActionButton fab) {
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

        private void clean() {
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

        protected void replace0FragmentByNewOne(Fragment oldOne, Fragment newOne) {
            mFragmentList.add(0, newOne);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
