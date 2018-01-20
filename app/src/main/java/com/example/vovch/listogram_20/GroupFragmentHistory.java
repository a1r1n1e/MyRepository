package com.example.vovch.listogram_20;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vovch on 30.11.2017.
 */

public class GroupFragmentHistory extends Fragment {
    private View rootView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    protected void checkRootView(ViewGroup container, LayoutInflater inflater){
        if(rootView == null){
            rootView = inflater.inflate(R.layout.group_fragment_history, container, false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_fragment_history, container, false);
        unsetRefresher();
        Group2Activity activity = (Group2Activity) getActivity();
        activity.onHistoryReady();
        return rootView;
    }
    protected void listsLayoutDrawer(SList list){
        LinearLayout basicLayout;
        basicLayout = (LinearLayout) rootView.findViewById(R.id.passedlistogramslayout);
        CardView listCard = (CardView) LayoutInflater.from(basicLayout.getContext()).inflate(R.layout.list_card, basicLayout, false);
        list.setCardView(listCard);
        LinearLayout listogramLayout = (LinearLayout) LayoutInflater.from(listCard.getContext()).inflate(R.layout.list_layout, listCard, false);
        Item[] items = list.getItems();
        int length = items.length;
        for (int i = 0; i < length; i++) {
            makeListogramLine(items[i], listogramLayout);
        }
        FrameLayout disButtonFrameLayout = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.dis_button_frame_layout, listogramLayout, false);
        final ImageButton disactivateListButton = (ImageButton) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.done_button, disButtonFrameLayout, false);
        list.setDisButton(disactivateListButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            disactivateListButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
        } else {
            disactivateListButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
        }
        Uri uri = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/confirm_button_64");
        disactivateListButton.setImageURI(uri);
        disactivateListButton.setAlpha(0.5f);
        disButtonFrameLayout.addView(disactivateListButton);
        listogramLayout.addView(disButtonFrameLayout);
        listCard.addView(listogramLayout);
        basicLayout.addView(listCard);
    }
    private void makeListogramLine(Item item, LinearLayout listogramLayout){
        FrameLayout addingFrameLayout = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.list_element_frame_layout, listogramLayout, false);
        LinearLayout addingLayout = (LinearLayout) LayoutInflater.from(addingFrameLayout.getContext()).inflate(R.layout.list_element_linear_layout, addingFrameLayout, false);

        TextView itemName = (TextView) LayoutInflater.from(addingLayout.getContext()).inflate(R.layout.list_element_text_view, addingLayout, false);
        itemName.setText(item.getName());
        TextView itemComment = (TextView) LayoutInflater.from(addingLayout.getContext()).inflate(R.layout.list_element_text_view, addingLayout, false);
        itemComment.setText(item.getComment());
        ItemButton groupButton = (ItemButton) LayoutInflater.from(addingLayout.getContext()).inflate(R.layout.list_element_button, addingFrameLayout, false);
        groupButton.setItem(item);
        if (item.getState()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addingLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_1));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addingLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            } else {
                addingLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addingLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_2));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addingLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            } else {
                addingLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            }
        }
        addingLayout.setAlpha(0.5f);
        groupButton.setFocusable(false);
        groupButton.setClickable(false);
        addingLayout.addView(itemName);
        addingLayout.addView(itemComment);
        item.setLayout(addingLayout);
        item.setButton(groupButton);
        addingFrameLayout.addView(addingLayout);
        addingFrameLayout.addView(groupButton);
        listogramLayout.addView(addingFrameLayout);
    }
    protected void listsListMaker(SList[] result) {
        int listsNumber = result.length;
        for(int i = 0; i < listsNumber; i++){
            listsLayoutDrawer(result[i]);
        }
    }
    protected void historyFragmentCleaner(){
        LinearLayout layoutScrollingActiveListsContainer = (LinearLayout) rootView.findViewById(R.id.passedlistogramslayout);
        layoutScrollingActiveListsContainer.removeAllViews();
    }
    protected void fragmentShowGood(SList[] result){
        listsListMaker(result);
    }
    protected void fragmentShowBad(String result) {
        historyFragmentCleaner();
        LinearLayout parentLayout = (LinearLayout) rootView.findViewById(R.id.passedlistogramslayout);
        TextView emptyInformer = (TextView) LayoutInflater.from(parentLayout.getContext()).inflate(R.layout.no_listograms_text_view, parentLayout, false);
        if (!(result.equals(""))) {
            emptyInformer.setText("We Failed");
        }
        parentLayout.addView(emptyInformer);
    }
    protected void setRefresher(){
        SwipeRefreshLayout refreshLayout = getRefresher();
        if(refreshLayout != null) {
            refreshLayout.setFocusable(true);
            refreshLayout.setRefreshing(false);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh();
                }
            });
        }
    }
    protected void unsetRefresher(){
        SwipeRefreshLayout refreshLayout = getRefresher();
        if(refreshLayout != null) {
            refreshLayout.setFocusable(false);
            refreshLayout.setRefreshing(false);
        }
    }
    protected void setRefresherRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(true);
    }
    protected void setRefresherNotRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(false);
    }
    protected void refresh(){
        Group2Activity activity = (Group2Activity) getActivity();
        activity.refreshHistoryLists();
    }
    protected SwipeRefreshLayout getRefresher(){
        return  (SwipeRefreshLayout) rootView.findViewById(R.id.group_history_fragment_refresher);
    }
}
