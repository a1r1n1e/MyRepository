package whobuys.vovch.vovch.whobuys.activities.complex;

import android.app.Dialog;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.activities.simple.CreateListogramActivity;
import whobuys.vovch.vovch.whobuys.activities.simple.GroupList2Activity;
import whobuys.vovch.vovch.whobuys.activities.simple.GroupSettingsActivity;
import whobuys.vovch.vovch.whobuys.activities.WithLoginActivity;
import whobuys.vovch.vovch.whobuys.data_types.ListImageButton;
import whobuys.vovch.vovch.whobuys.fragment.group_activity.GroupFragmentActive;
import whobuys.vovch.vovch.whobuys.fragment.group_activity.GroupFragmentHistory;
import com.example.vovch.listogram_20.R;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.SList;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

import java.util.ArrayList;
import java.util.List;

public class Group2Activity extends WithLoginActivity {

    private static final String INTENT_LOAD_TYPE = "loadtype";
    private static final String TAB_LAYOUT_PAGE_0 = "Active";
    private static final String TAB_LAYOUT_PAGE_1 = "History";
    private static final String FRAGMENT_TRANSACTION_DIALOG = "dialog";

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
            startActivity(intent);
            if (provider.getActiveActivityNumber() == 3) {
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
        Uri uri = Uri.parse("android.resource://whobuys.vovch.vovch.whobuys/mipmap/settings_more");
        settingsButton.setImageURI(uri);
        settingsButton.setOnClickListener(groupSettingsButtonListener);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_group);
        tabs.removeAllTabs();
        tabs.addTab(tabs.newTab().setText(getString(R.string.active)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.history)));
        tabs.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.group_fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setElevation(24);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white, getTheme()));
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white));
                    }
                } else if (position == 1) {
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        adapter.startUpdate(viewPager);
        activeFragment = (GroupFragmentActive) adapter.instantiateItem(viewPager, 0);
        historyFragment = (GroupFragmentHistory) adapter.instantiateItem(viewPager, 1);
        adapter.finishUpdate(viewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.mipmap.add_plus_custom_green_white));
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(3, Group2Activity.this);
        if(provider.getActiveGroup() != null && provider.getActiveGroup().getUpdateNeededFlag()){
            refreshActiveLists();
        }
    }

    @Override
    public void onBackPressed() {
        if (provider.getActiveActivityNumber() == 3) {
            provider.nullActiveActivity();
        }
        provider.setActiveGroup(null);
        provider.setActiveListsActivityLoadType(0);
        provider.clearNewGroupPossibleMembers();
        Intent intentGroupList = new Intent(Group2Activity.this, ActiveListsActivity.class);
        intentGroupList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentGroupList);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        if (provider.getActiveActivityNumber() == 3) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }

    public void update() {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.setRefresherRefreshing();
        }
        provider.getGroupActiveLists(provider.getActiveGroup());
    }

    public void refreshActiveLists() {
        if (activeFragment != null) {
            if (activeReady) {
                activeFragment.checkRootView(viewPager, getLayoutInflater());
                activeFragment.setRefresherRefreshing();
                update();
            }
        }
    }

    public void onActiveReady(GroupFragmentActive fragment) {
        activeReady = true;
        if (fragment != null) {
            activeFragment = fragment;
            refreshActiveLists();
        }
    }

    public void refreshHistoryLists() {
        if (historyFragment != null) {
            if (historyReady) {
                historyFragment.checkRootView(viewPager, getLayoutInflater());
                historyFragment.setRefresherRefreshing();
                historyLoad();
            }
        }
    }

    public void onHistoryReady(GroupFragmentHistory fragment) {
        if (fragment != null) {
            historyReady = true;
            historyFragment = fragment;
            refreshHistoryLists();
        }
    }

    public void historyLoad() {
        provider.getGroupHistoryLists(provider.getActiveGroup());
    }

    public void itemmark(Item item) {
        provider.itemmark(item);
    }

    public void disactivateGroupList(ListImageButton button) {
        SList list = button.getList();
        if (list != null) {
            DisactivateDialogFragment dialogFragment = new DisactivateDialogFragment();
            dialogFragment.setList(list);
            dialogFragment.setActiveActivityProvider(provider);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
        }
    }

    public static class DisactivateDialogFragment extends DialogFragment {
        private SList list;
        private ActiveActivityProvider activeActivityProvider;

        protected void setList(SList newList) {
            list = newList;
        }

        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider) {
            activeActivityProvider = newActiveActivityProvider;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String message = getString(R.string.dialog_kill_question);
            String button2String = getString(R.string.Yes);
            String button1String = getString(R.string.No);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened), Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activeActivityProvider.disactivateGroupList(list);
                    Toast.makeText(getActivity(), getString(R.string.dialog_kill_action_processing),
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.setCancelable(true);
            if(list != null && list.getDisButton() != null) {
                list.getDisButton().setFocusable(true);
                list.getDisButton().setClickable(true);
            }
            return builder.create();
        }
    }

    public void showGroupDataSettledGood() {

    }

    public void showGroupDataSettledBad() {

    }

    public void resendList(ListImageButton button) {
        SList list = button.getList();
        if (list != null) {
            ResendDialogFragment dialogFragment = new ResendDialogFragment();
            dialogFragment.setList(list);
            dialogFragment.setActiveActivityProvider(provider);
            dialogFragment.setActivity(Group2Activity.this);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
        }
    }

    public static class ResendDialogFragment extends DialogFragment {
        private SList list;
        private ActiveActivityProvider activeActivityProvider;
        private Group2Activity activity;

        protected void setActivity(Group2Activity newActivity) {
            activity = newActivity;
        }

        protected void setList(SList newList) {
            list = newList;
        }

        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider) {
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
                    Toast.makeText(getActivity(), getString(R.string.list_downloaded_informer), Toast.LENGTH_LONG)
                            .show();
                    activeActivityProvider.createListogramOffline(list.getItems());
                }
            });
            builder.setNeutralButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.resend_where_informer),
                            Toast.LENGTH_LONG).show();
                    activeActivityProvider.setResendingList(list);
                    Intent intent = new Intent(activity, GroupList2Activity.class);
                    intent.putExtra(INTENT_LOAD_TYPE, 2);
                    startActivity(intent);
                    activity.finish();
                }
            });
            builder.setPositiveButton(button3String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened),
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.setCancelable(true);
            if(list != null && list.getDisButton() != null) {
                list.getResendButton().setFocusable(true);
                list.getResendButton().setClickable(true);
            }
            return builder.create();
        }
    }

    public void redactList(SList list) {
        if (list != null) {
            RedactDialogFragment dialogFragment = new RedactDialogFragment();
            dialogFragment.setList(list);
            dialogFragment.setActiveActivityProvider(provider);
            dialogFragment.setActivity(Group2Activity.this);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
        }
    }

    public static class RedactDialogFragment extends DialogFragment {
        private SList list;
        private ActiveActivityProvider activeActivityProvider;
        private Group2Activity activity;

        protected void setActivity(Group2Activity newActivity) {
            activity = newActivity;
        }

        protected void setList(SList newList) {
            list = newList;
        }

        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider) {
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
                    intent.putExtra(INTENT_LOAD_TYPE, 3);
                    startActivity(intent);
                    activity.finish();
                }
            });
            builder.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened),
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.setCancelable(true);
            if(list != null && list.getDisButton()!= null) {
                list.getRedactButton().setFocusable(true);
                list.getRedactButton().setClickable(true);
            }
            return builder.create();
        }
    }


    public void historyLoadOnGood(SList[] result) {
        if (historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            historyFragment.historyFragmentCleaner();
            if(historyFragment.getRefresher() != null) {
                historyFragment.setRefresherNotRefreshing();
            }
            historyFragment.fragmentShowGood(result);
        }
    }

    public void historyLoadOnBad(SList[] lists) {
        if (historyFragment != null) {
            historyFragment.checkRootView(viewPager, getLayoutInflater());
            if(historyFragment.getRefresher() != null) {
                historyFragment.setRefresherNotRefreshing();
            }
            historyFragment.fragmentShowBad(lists);
        }
    }

    public void updateNeededFlagTurnOff(){
        if(provider.getActiveGroup() != null && provider.getActiveGroup().getUpdateNeededFlag()){
            provider.getActiveGroup().changeUpdateNeededFlag();
        }
    }

    public void showGood(SList[] result) {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.activeFragmentCleaner();
            activeFragment.fragmentShowGood(result);
            if(activeFragment.getRefresher() != null) {
                activeFragment.setRefresherNotRefreshing();
            }
            updateNeededFlagTurnOff();
        }
    }

    public void showBad(SList[] result) {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.activeFragmentCleaner();
            activeFragment.fragmentShowBad(result);
            if(activeFragment.getRefresher() != null) {
                activeFragment.setRefresherNotRefreshing();
            }
            updateNeededFlagTurnOff();
        }
    }

    public void showSecondGood(SList result) {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowSecondGood(result);
            historyLoad();
            //updateNeededFlagTurnOff();
        }
    }

    public void showSecondBad(SList result) {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowSecondBad(result);
            //updateNeededFlagTurnOff();
        }
    }

    public void showThirdGood(Item result) {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowThirdGood(result);
            //updateNeededFlagTurnOff();
        }
    }

    public void showThirdBad(Item result) {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowThirdBad(result);
            //updateNeededFlagTurnOff();
        }
    }

    public void showItemmarkProcessing(Item item) {
        if (activeFragment != null) {
            activeFragment.checkRootView(viewPager, getLayoutInflater());
            activeFragment.fragmentShowProcessing(item);
        }
    }

    public void sendListogram() {
        Intent intent = new Intent(Group2Activity.this, CreateListogramActivity.class);
        intent.putExtra(INTENT_LOAD_TYPE, 1);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.clean();
        adapter.addFragment(new GroupFragmentActive(), getString(R.string.active));
        adapter.addFragment(new GroupFragmentHistory(), getString(R.string.history));
        viewPager.setAdapter(adapter);
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
