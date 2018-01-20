package com.example.vovch.listogram_20;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by vovch on 14.12.2017.
 */

public class ActiveFragmentOffline extends Fragment {
    private View rootView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    protected void checkRootView(ViewGroup container, LayoutInflater inflater){
        if(rootView == null){
            rootView = inflater.inflate(R.layout.group_fragment_active, container, false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_fragment_active, container, false);
        SwipeRefreshLayout refreshLayout = getRefresher();
        refreshLayout.setFocusable(false);
        refreshLayout.setRefreshing(false);
        ActiveListsActivity activeListsActivity = (ActiveListsActivity) getActivity();
        activeListsActivity.onOfflineFragmentStart();
        return rootView;
    }
    protected void listsLayoutDrawer(SList list){
        LinearLayout basicLayout;
        basicLayout = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
        CardView listCard = (CardView) LayoutInflater.from(basicLayout.getContext()).inflate(R.layout.list_card, basicLayout, false);
        list.setCardView(listCard);
        LinearLayout listogramLayout = (LinearLayout) LayoutInflater.from(listCard.getContext()).inflate(R.layout.list_layout, listCard, false);
        Item[] items = list.getItems();
        int length = items.length;
        for (int i = 0; i < length; i++) {
            makeListogramLine(items[i], listogramLayout);
        }
        FrameLayout disButtonFrameLayout = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.dis_button_frame_layout, listogramLayout, false);
        DisactivateImageButton disactivateListButton = (DisactivateImageButton) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.done_button, disButtonFrameLayout, false);
        View.OnClickListener disactivateListButtonOnClickListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disactivateList((DisactivateImageButton) v);
            }
        };
        list.setDisButton(disactivateListButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            disactivateListButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
        } else {
            disactivateListButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
        }
        disactivateListButton.setOnClickListener(disactivateListButtonOnClickListenner);
        Uri uri = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/confirm_button_64");
        disactivateListButton.setImageURI(uri);
        disactivateListButton.setList(list);
        disButtonFrameLayout.addView(disactivateListButton);
        listogramLayout.addView(disButtonFrameLayout);
        listCard.addView(listogramLayout);
        basicLayout.addView(listCard);
    }
    private void disactivateList(DisactivateImageButton button){
        if(button.getList() != null) {
            button.setFocusable(false);
            button.setClickable(false);
            ActiveListsActivity activity = (ActiveListsActivity) getActivity();
            activity.disactivateList(button.getList());
        }
    }
    private void onItemMarkButtonTouchedAction(ItemButton button){
        if(button.getItem() != null) {
            button.setFocusable(false);
            button.setClickable(false);
            ActiveListsActivity activity = (ActiveListsActivity) getActivity();
            activity.itemmark(button.getItem());
        }
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
        View.OnClickListener ItemMarkButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemMarkButtonTouchedAction((ItemButton) v);
            }
        };
        if (item.getState()) {
            groupButton.setFocusable(true);
            groupButton.setClickable(true);
            groupButton.setOnClickListener(ItemMarkButtonListenner);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addingLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_1));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addingLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            } else {
                addingLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            }
        } else {
            groupButton.setFocusable(true);
            groupButton.setClickable(true);
            groupButton.setOnClickListener(ItemMarkButtonListenner);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addingLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_2));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addingLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            } else {
                addingLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            }
        }
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
    protected void activeFragmentCleaner(){
        LinearLayout layoutScrollingActiveListsContainer = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
        layoutScrollingActiveListsContainer.removeAllViews();
    }
    protected void fragmentShowGood(SList[] result){
        activeFragmentCleaner();
        listsListMaker(result);
    }
    protected void fragmentShowBad(SList[] result){
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout parentLayout = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
        parentLayout.removeAllViewsInLayout();
        TextView emptyInformer = (TextView) LayoutInflater.from(parentLayout.getContext()).inflate(R.layout.no_listograms_text_view, parentLayout, false);
        if(!(result == null || result.length == 0)) {
            emptyInformer.setText("Our Fail");
        }
        parentLayout.addView(emptyInformer);
    }
    protected void fragmentShowSecondGood(Item result){
        LinearLayout itemMarkLayout = result.getLayout();
        Button itemMarkTouchedButton = result.getButton();
        if(result.getState()){
            itemMarkTouchedButton.setFocusable(true);
            itemMarkTouchedButton.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemMarkLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_1));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                itemMarkLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            } else{
                itemMarkLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            }
        }
        else{
            itemMarkTouchedButton.setFocusable(true);
            itemMarkTouchedButton.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemMarkLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_2));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                itemMarkLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            } else{
                itemMarkLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            }
        }
    }
    protected void fragmentShowSecondBad(Item result){
        Button itemMarkButton = result.getButton();
        itemMarkButton.setClickable(true);
        itemMarkButton.setFocusable(true);
        itemMarkButton.setGravity(Gravity.CENTER_HORIZONTAL);
    }
    protected void fragmentDisactivateGood(SList result){
        CardView cardView = result.getCardView();
        cardView.setVisibility(View.GONE);
    }
    protected void fragmentDisactivateBad(SList result){
        if(result != null) {
            result.getDisButton().setFocusable(true);
            result.getDisButton().setClickable(true);
        }
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
        activity.refreshOfflineLists();
    }
    protected SwipeRefreshLayout getRefresher(){
        return  (SwipeRefreshLayout) rootView.findViewById(R.id.group_active_fragment_refresher);
    }
}
