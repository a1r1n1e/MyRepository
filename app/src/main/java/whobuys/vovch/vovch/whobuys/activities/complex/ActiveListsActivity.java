package whobuys.vovch.vovch.whobuys.activities.complex;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.simple.CreateListogramActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.GroupList2Activity;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.NewGroup;
import whobuys.vovch.vovch.whobuys.activities.simple.ProfileActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.SendBugActivity;
import whobuys.vovch.vovch.whobuys.data_types.ListImageButton;
import whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.ActiveFragmentHistory;
import whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.ActiveFragmentOffline;
import whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.ActiveListsFragment;
import whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.active_lists_fragment_content.ActiveListsOnlineFragment;
import whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.active_lists_fragment_content.LoginFragment;
import com.example.vovch.listogram_20.R;
import whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.active_lists_fragment_content.RegistrationFragment;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.ListInformer;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

import java.util.ArrayList;
import java.util.List;

public class ActiveListsActivity extends WithLoginActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String INTENT_LOAD_TYPE = "loadtype";
    private static final String FRAGMENT_TRANSACTION_DIALOG = "dialog";

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
            Intent intent = new Intent(ActiveListsActivity.this, NewGroup.class);
            intent.putExtra(INTENT_LOAD_TYPE, 0);
            startActivity(intent);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.active_lists_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);


        TabLayout tabs = (TabLayout) findViewById(R.id.active_lists_tabs);
        tabs.removeAllTabs();
        tabs.addTab(tabs.newTab().setText(getString(R.string.online)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.offline)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.history)));
        tabs.setupWithViewPager(viewPager);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton settingsButton = (ImageButton) findViewById(R.id.active_lists_menu_button);
        Uri uri = Uri.parse("android.resource://whobuys.vovch.vovch.whobuys/mipmap/settings_more");
        settingsButton.setImageURI(uri);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer != null) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.active_lists_fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setElevation(24);
        }

        viewPager.addOnPageChangeListener(pageChangeListener);

        adapter.startUpdate(viewPager);
        offlineFragment = (ActiveFragmentOffline) adapter.instantiateItem(viewPager, 1);
        historyFragment = (ActiveFragmentHistory) adapter.instantiateItem(viewPager, 2);
        activeFragment = (ActiveListsFragment) adapter.instantiateItem(viewPager, 0);
        adapter.finishUpdate(viewPager);

        loginFragment = new LoginFragment();
        registrationFragment = new RegistrationFragment();
        activeListsOnlineFragment = new ActiveListsOnlineFragment();

        if( -1 < loadType && loadType < 3) {                                                           //setting current page of viewPager
            viewPager.setCurrentItem(loadType);
        }
        fabActionOne(fab);

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
        //provider.synchronizeDB();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (provider.getActiveActivityNumber() == 2) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (activeFragment != null && registrationFragment != null && activeFragment.getChildFragmentManager() != null) {
                if(activeFragment.getChildFragmentManager().findFragmentByTag("One").equals(registrationFragment)) {
                    registrationToLoginFragmentChange();
                } else{
                    super.onBackPressed();
                }
            } else{
                super.onBackPressed();
            }
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.active_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(ActiveListsActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            loginFailed = getString(R.string.login_failed);
            ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
            exitDialogFragment.setActiveActivityProvider(provider);
            exitDialogFragment.setActiveListsActivity(ActiveListsActivity.this);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            exitDialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
        } else if (id == R.id.nav_copy_id) {
            ClipboardManager clipboard = (ClipboardManager) ActiveListsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard != null) {
                ClipData clip = ClipData.newPlainText("", provider.userSessionData.getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ActiveListsActivity.this, R.string.id_copied_informer, Toast.LENGTH_LONG)
                        .show();
            }
        } else if (id == R.id.nav_troubleshot) {
            Intent intent = new Intent(ActiveListsActivity.this, SendBugActivity.class);
            intent.putExtra(INTENT_LOAD_TYPE, 0);
            startActivity(intent);
        } else if(id == R.id.nav_drop_history) {
            DropHistoryDialogFragment dialogFragment = new DropHistoryDialogFragment();
            dialogFragment.setActiveActivityProvider(provider);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class DropHistoryDialogFragment extends DialogFragment {
        private ActiveActivityProvider activeActivityProvider;
        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider){
            activeActivityProvider = newActiveActivityProvider;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = getString(R.string.dialog_drop_history_question);
            String button1String = getString(R.string.Yes);
            String button2String = getString(R.string.No);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened), Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activeActivityProvider.dropHistory();
                }
            });
            builder.setCancelable(true);
            return builder.create();
        }
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
            String message = getString(R.string.dialog_logout_question);
            String button1String = getString(R.string.Yes);
            String button2String = getString(R.string.No);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened), Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activeActivityProvider.userSessionData.exit();
                }
            });
            builder.setCancelable(true);
            return builder.create();
        }
    }

    public void refreshActiveLists() {
        try {
            update();

        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void refreshOfflineLists() {
        try {
            if (offlineFragment != null) {
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                offlineFragment.setRefresher();
                offlineUpdate(true);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void refreshOfflineHistory() {
        try {
            if (historyFragment != null) {
                historyFragment.checkRootView(viewPager, getLayoutInflater());
                historyFragment.setRefresher();
                offlineUpdate(false);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void onOfflineFragmentStart(ActiveFragmentOffline fragment) {
        try {
            if (fragment != null) {
                offlineFragment = fragment;
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                refreshOfflineLists();
            }
        } catch(Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void onHistoryFragmentStart(ActiveFragmentHistory fragment) {
        try {
            if (fragment != null) {
                historyFragment = fragment;
                historyFragment.checkRootView(viewPager, getLayoutInflater());
                refreshOfflineHistory();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }


    public void tryToLoginFromPrefs() {
        try {
            provider.tryToLoginFromPrefs();
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void tryToLoginFromForms(String login, String password) {
        try {
            provider.tryToLoginFromForms(login, password);
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public String getToken() {
        return provider.userSessionData.getToken();
    }

    public void loginFragmentStart() {
        try {
            if (loginFailed == null) {
                tryToLoginFromPrefs();
            }
            checkLoginBadInformNeeded();
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void onLoginFailed(String result) {                                                        //TODO cases
        try {
            activeToLoginFragmentChange();
            loginFailed = result;
            checkLoginBadInformNeeded();
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void checkLoginBadInformNeeded() {
        try {
            if (loginFailed != null) {
                loginFragment.checkRootView(viewPager, getLayoutInflater());
                loginFragment.badInform(loginFailed);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void loginToActiveFragmentChange() {
        try {
            if (activeFragment != null && activeListsOnlineFragment != null) {
                Fragment fragment = activeFragment.getChildFragmentManager().findFragmentByTag("One");
                if (fragment != null) {
                    FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                    transaction.remove(fragment);
                    transaction.commitNow();
                }

                FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment, "One");
                transaction.commit();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void activeListsOnlineFragmentStart() {
        try {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            if (viewPager.getCurrentItem() == 0) {
                fabActionZero(fab);
            }
            if (activeListsOnlineFragment != null) {
                activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
                activeListsOnlineFragment.setRefresher();
            }
            update();
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void loginToRegistrationFragmentChange() {
        try {
            if (activeFragment != null && registrationFragment!= null) {
                Fragment fragment = activeFragment.getChildFragmentManager().findFragmentByTag("One");
                if (fragment != null) {
                    FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                    transaction.remove(fragment);
                    transaction.commitNow();
                }

                FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                transaction.add(R.id.active_lists_page_one, registrationFragment, "One");
                transaction.commit();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void tryToRegister(String login, String password) {
        try {
            provider.registrationTry(login, password);
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void registrationToActiveListsOnlineFragmentChange() {
        try {
            if (activeFragment != null && activeListsOnlineFragment != null) {
                Fragment fragment = activeFragment.getChildFragmentManager().findFragmentByTag("One");
                if (fragment != null) {
                    FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                    transaction.remove(fragment);
                    transaction.commitNow();
                }

                FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                transaction.add(R.id.active_lists_page_one, activeListsOnlineFragment, "One");
                transaction.commit();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void badRegistrationTry(String result) {
        try {
            if (registrationFragment != null) {
                registrationFragment.checkRootView(viewPager, getLayoutInflater());
                registrationFragment.badInform(result);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void activeToLoginFragmentChange() {
        try {
            if (activeFragment != null &&loginFragment != null) {
                Fragment fragment = activeFragment.getChildFragmentManager().findFragmentByTag("One");
                if (fragment != null) {
                    FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                    transaction.remove(fragment);
                    transaction.commitNow();
                }

                FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                transaction.add(R.id.active_lists_page_one, loginFragment, "One");
                transaction.commit();

                if (viewPager.getCurrentItem() == 0) {
                    fabActionZero(fab);
                }
                if (drawer != null) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void registrationToLoginFragmentChange() {
        try {
            if (activeFragment != null && loginFragment != null) {
                Fragment fragment = activeFragment.getChildFragmentManager().findFragmentByTag("One");
                if (fragment != null) {
                    FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                    transaction.remove(fragment);
                    transaction.commitNow();
                }

                FragmentTransaction transaction = activeFragment.getChildFragmentManager().beginTransaction();
                transaction.add(R.id.active_lists_page_one, loginFragment, "One");
                transaction.commit();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void noInternet() {
        try {
            if (activeFragment != null) {
                activeFragment.checkRootView(viewPager, getLayoutInflater());
                activeFragment.noInternet();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void update() {
        try {
            provider.getActiveActivityActiveLists();
            if (activeListsOnlineFragment != null) {
                activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
                if (activeListsOnlineFragment.getRefresher() != null) {
                    activeListsOnlineFragment.setRefresherRefreshing();
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void offlineUpdate(boolean type) {
        try {
            provider.startOfflineGetterDatabaseTask(type);
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void disactivateList(ListImageButton button) {
        try {
            SList list = button.getList();
            if (list != null) {
                DisactivateDialogFragment dialogFragment = new DisactivateDialogFragment();
                dialogFragment.setList(list);
                dialogFragment.setActiveActivityProvider(provider);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                dialogFragment.show(transaction, "dialog");
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
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
            String message = getString(R.string.dialog_kill_question);
            String button2String = getString(R.string.No);
            String button1String = getString(R.string.Yes);


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened), Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activeActivityProvider.activeActivityDisactivateList(list);
                    Toast.makeText(getActivity(), getString(R.string.dialog_kill_action_processing),
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
        try {
            if (list != null) {
                RedactDialogFragment dialogFragment = new RedactDialogFragment();
                dialogFragment.setList(list);
                dialogFragment.setActiveActivityProvider(provider);
                dialogFragment.setActivity(ActiveListsActivity.this);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                dialogFragment.show(transaction, "dialog");
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
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
            String message = getString(R.string.dialog_redact_question);
            String button1String = getString(R.string.Yes);
            String button2String = getString(R.string.No);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_redact_action_procesing), Toast.LENGTH_LONG)
                            .show();
                    activeActivityProvider.setResendingList(list);
                    activeActivityProvider.saveTempItems(activeActivityProvider.dataExchanger.makeTempItemsFromItems(list.getItems()));
                    Intent intent = new Intent(activity, CreateListogramActivity.class);
                    intent.putExtra(INTENT_LOAD_TYPE, 2);
                    startActivity(intent);
                }
            });
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened),
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
        try {
            if (list != null) {
                ResendDialogFragment dialogFragment = new ResendDialogFragment();
                dialogFragment.setList(list);
                dialogFragment.setActiveActivityProvider(provider);
                dialogFragment.setActivity(ActiveListsActivity.this);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
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
            String message = getString(R.string.dialog_resend_question);
            String button1String = getString(R.string.Resend_List);
            String button2String = getString(R.string.Copy);
            String button3String = getString(R.string.Cancel);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.list_copied_informer), Toast.LENGTH_LONG)
                            .show();
                    activeActivityProvider.createListogramOffline(list.getItems());
                }
            });
            if(activeActivityProvider.userSessionData.isLoginned()) {
                builder.setNeutralButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), getString(R.string.resend_where_informer),
                                Toast.LENGTH_LONG).show();
                        activeActivityProvider.setResendingList(list);
                        Intent intent = new Intent(activity, GroupList2Activity.class);
                        intent.putExtra(INTENT_LOAD_TYPE, 1);
                        startActivity(intent);
                    }
                });
            }
            builder.setPositiveButton(button3String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened),
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
        try {
            provider.activeListsItemmark(item);
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showGood(ListInformer[] result) {
        try {
            if (activeListsOnlineFragment != null) {
                activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
                activeListsOnlineFragment.fragmentShowGood(result);
                if (activeListsOnlineFragment.getRefresher() != null) {
                    activeListsOnlineFragment.setRefresherNotRefreshing();
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showBad(ListInformer[] result) {
        try {
            if (activeFragment != null) {
                activeListsOnlineFragment.checkRootView(viewPager, getLayoutInflater());
                activeListsOnlineFragment.fragmentShowBad(result);
                if (activeListsOnlineFragment.getRefresher() != null) {
                    activeListsOnlineFragment.setRefresherNotRefreshing();
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void goToGroup(UserGroup group) {
        try {
            if (group != null) {
                if (group.getId() != null && group.getName() != null) {
                    provider.setActiveGroup(group);
                    if (group.getOwner().equals(provider.userSessionData.getId())) {
                        provider.makeAllMembersPossible(group);
                    }
                    Intent intent = new Intent(ActiveListsActivity.this, Group2Activity.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showActiveOfflineGood(SList[] lists) {
        try {
            if (offlineFragment != null) {
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                offlineFragment.fragmentShowGood(lists);
                if (offlineFragment.getRefresher() != null) {
                    offlineFragment.unsetRefresher();
                }
            }
        }catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showActiveOfflineBad(SList[] lists) {
        try {
            if (offlineFragment != null) {
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                offlineFragment.fragmentShowBad(lists);
                if (offlineFragment.getRefresher() != null) {
                    offlineFragment.unsetRefresher();
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showHistoryOfflineGood(SList[] lists) {
        try {
            if (historyFragment != null) {
                historyFragment.checkRootView(viewPager, getLayoutInflater());
                historyFragment.fragmentShowGood(lists);
                if (historyFragment.getRefresher() != null) {
                    historyFragment.unsetRefresher();
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showHistoryOfflineBad(SList[] lists) {
        try {
            if (historyFragment != null) {
                historyFragment.checkRootView(viewPager, getLayoutInflater());
                historyFragment.fragmentShowBad(lists);
                if (historyFragment.getRefresher() != null) {
                    historyFragment.unsetRefresher();
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showDisactivateOfflineGood(SList result) {
        try {
            if (offlineFragment != null) {
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                offlineFragment.fragmentDisactivateGood(result);
                refreshOfflineHistory();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showDisactivateOfflineBad(SList result) {
        try {
            if (offlineFragment != null) {
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                offlineFragment.fragmentDisactivateBad(result);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showItemmarkOfflineGood(Item result) {
        try {
            if (offlineFragment != null) {
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                offlineFragment.fragmentShowSecondGood(result);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showItemmarkOfflineBad(Item result) {
        try {
            if (offlineFragment != null) {
                offlineFragment.checkRootView(viewPager, getLayoutInflater());
                offlineFragment.fragmentShowSecondBad(result);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showDropHistoryGood(){
        try {
            ;
            refreshOfflineHistory();
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    public void showDropHistoryBad(){

    }

    private void fabActionZero(FloatingActionButton fab) {
        try {
            ;
            if (fab != null) {
                if (provider.userSessionData.isLoginned() && provider.userSessionData.isSession()) {
                    fab.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white, getTheme()));
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white));
                    }
                    fab.setOnClickListener(fabOnClickListener);
                } else {
                    fab.hide();
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    private void fabActionOne(FloatingActionButton fab) {
        try {
            if (fab != null) {
                fab.show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white, getTheme()));
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white));
                }
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ActiveListsActivity.this, CreateListogramActivity.class);
                        intent.putExtra(INTENT_LOAD_TYPE, false);
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    private void fabActionTwo(FloatingActionButton fab) {
        try {
            if (fab != null) {
                fab.hide();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        try {
            if (viewPager != null) {
                adapter = new Adapter(getSupportFragmentManager());
                adapter.clean();
                adapter.addFragment(new ActiveListsFragment(), getString(R.string.online));
                adapter.addFragment(new ActiveFragmentOffline(), getString(R.string.offline));
                adapter.addFragment(new ActiveFragmentHistory(), getString(R.string.history));
                viewPager.setAdapter(adapter);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ALA");
        }
    }

    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private Adapter(FragmentManager manager) {
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

        private void addFragment(Fragment fragment, String title) {
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
