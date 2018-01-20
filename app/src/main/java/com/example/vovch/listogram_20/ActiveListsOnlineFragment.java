package com.example.vovch.listogram_20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by vovch on 08.01.2018.
 */

public class ActiveListsOnlineFragment extends ActiveListsFragment {
    private View rootView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    protected void checkRootView(ViewGroup container, LayoutInflater inflater){
        if(rootView == null){
            rootView = inflater.inflate(R.layout.active_lists_online_fragment, container, false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.active_lists_online_fragment, container, false);
        SwipeRefreshLayout refreshLayout = getRefresher();
        refreshLayout.setEnabled(true);
        refreshLayout.setFocusable(false);
        refreshLayout.setRefreshing(false);
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        activity.activeListsOnlineFragmentStart();
        return rootView;
    }
    protected void listsListMaker(ListInformer[] result) {
        int i = 0;
        int length = result.length;
        for(i = 0; i < length; i++){
            activeListLayoutDrawer(result[i]);
        }
    }
    protected void activeListLayoutDrawer(ListInformer informer){
        LinearLayout basicLayout = (LinearLayout) rootView.findViewById(R.id.activelistslayout);
        CardView cardView = (CardView) LayoutInflater.from(basicLayout.getContext()).inflate(R.layout.list_card, basicLayout, false);
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(cardView.getContext()).inflate(R.layout.active_activity_frame_layout, cardView, false);
        LinearLayout listogramLayout = (LinearLayout) LayoutInflater.from(frameLayout.getContext()).inflate(R.layout.active_lists_linear_layout, frameLayout, false);
        TextView groupNameTextView = (TextView) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.active_actvity_text_view_1, listogramLayout,false);
        groupNameTextView.setText(informer.getName());
        TextView listOwnerTextView = (TextView) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.active_activity_text_view_2, listogramLayout,false);

        String  tempString = "New list";                                                                //strings TODO
        listOwnerTextView.setText(tempString);

        listogramLayout.addView(groupNameTextView);
        listogramLayout.addView(listOwnerTextView);
        GroupButton frameButton = (GroupButton) LayoutInflater.from(frameLayout.getContext()).inflate(R.layout.group_button, frameLayout, false);

        frameButton.setGroup(informer.getGroup());

        View.OnClickListener frameButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupButton button = (GroupButton) v;
                goToGroup(button.getGroup());
            }
        };
        frameButton.setOnClickListener(frameButtonListenner);
        informer.setButton(frameButton);
        frameLayout.addView(listogramLayout);
        frameLayout.addView(frameButton);
        cardView.addView(frameLayout);
        basicLayout.addView(cardView);
    }
    @Override
    protected void fragmentShowGood(ListInformer[] result){
        cleaner();
        listsListMaker(result);
    }
    @Override
    protected void fragmentShowBad(ListInformer[] result){
        cleaner();
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.activelistslayout);
        TextView messageTextView = (TextView) LayoutInflater.from(layout.getContext()).inflate(R.layout.no_listograms_text_view, layout, false);
        layout.addView(messageTextView);
    }
    private void goToGroup(UserGroup group){
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        activity.goToGroup(group);
    }
    protected void setRefresher(){
        SwipeRefreshLayout refreshLayout = getRefresher();
        refreshLayout.setFocusable(true);
        refreshLayout.setRefreshing(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    protected void unsetRefresher(){
        SwipeRefreshLayout refreshLayout = getRefresher();
        refreshLayout.setFocusable(false);
        refreshLayout.setRefreshing(false);
    }
    protected void refresh(){
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        activity.refreshActiveLists();
    }
    protected void setRefresherRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(true);
    }
    protected void setRefresherNotRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(false);
    }
    protected SwipeRefreshLayout getRefresher(){
        SwipeRefreshLayout layout = (SwipeRefreshLayout) rootView.findViewById(R.id.active_lists_online_refresh);
        layout.setFocusable(true);
        return  layout;
    }
    private void cleaner(){
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.activelistslayout);
        layout.removeAllViewsInLayout();
    }
}
