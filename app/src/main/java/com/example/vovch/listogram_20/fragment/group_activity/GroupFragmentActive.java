package com.example.vovch.listogram_20.fragment.group_activity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.ListImageButton;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ItemButton;
import com.example.vovch.listogram_20.data_types.SList;

/**
 * Created by vovch on 30.11.2017.
 */

public class GroupFragmentActive extends Fragment {
    private View rootView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    public void checkRootView(ViewGroup container, LayoutInflater inflater){
        if(rootView == null){
            rootView = inflater.inflate(R.layout.group_fragment_active, container, false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_fragment_active, container, false);
        unsetRefresher();
        Group2Activity activity = (Group2Activity) getActivity();
        activity.onActiveReady();
        return rootView;
    }
    private void disactivateGroupList(ListImageButton button){
        if(button.getList() != null) {
            Group2Activity activity = (Group2Activity) getActivity();
            activity.disactivateGroupList(button);
        }
    }
    private void onItemMarkButtonTouchedAction(ItemButton button){
        if(button.getItem() != null) {
            button.setFocusable(false);
            button.setClickable(false);
            Group2Activity groupActivity = (Group2Activity) getActivity();
            groupActivity.itemmark(button.getItem());
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
    public void listsListMaker(SList[] result) {
        int listsNumber = result.length;
        for(int i = 0; i < listsNumber; i++){
            listsLayoutDrawer(result[i]);
        }
    }
    public void listsLayoutDrawer(SList list){
        LinearLayout basicLayout;
        basicLayout = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
        CardView listCard = (CardView) LayoutInflater.from(basicLayout.getContext()).inflate(R.layout.list_card, basicLayout, false);
        list.setCardView(listCard);
        LinearLayout listogramLayout = (LinearLayout) LayoutInflater.from(listCard.getContext()).inflate(R.layout.list_layout, listCard, false);
        LinearLayout headerLayout = (LinearLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.list_header_layout, listogramLayout, false);
        LinearLayout leftHeaderLayout = (LinearLayout) LayoutInflater.from(headerLayout.getContext()).inflate(R.layout.list_header_left_layout, headerLayout, false);
        TextView listNameTextView = (TextView) LayoutInflater.from(leftHeaderLayout.getContext()).inflate(R.layout.list_header_left_textview, leftHeaderLayout, false);
        if(list.getOwner() != -1) {
            listNameTextView.setText("From: " + list.getOwnerName());
        }
        TextView listCreationTimeTextView = (TextView) LayoutInflater.from(leftHeaderLayout.getContext()).inflate(R.layout.list_header_left_textview, leftHeaderLayout, false);
        if(list.getCreationTime() != null){
            listCreationTimeTextView.setTextSize(10);
            listCreationTimeTextView.setText(list.getHumanCreationTime());
        }
        leftHeaderLayout.addView(listNameTextView);
        leftHeaderLayout.addView(listCreationTimeTextView);
        headerLayout.addView(leftHeaderLayout);
        FrameLayout imageButtonFrame = (FrameLayout) LayoutInflater.from(headerLayout.getContext()).inflate(R.layout.list_header_imagebutton_frame, headerLayout, false);
        headerLayout.addView(imageButtonFrame);
        listogramLayout.addView(headerLayout);
        Item[] items = list.getItems();
        int length = items.length;
        for (int i = 0; i < length; i++) {
            makeListogramLine(items[i], listogramLayout);
        }
        FrameLayout disButtonFrameLayout = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.dis_button_frame_layout, listogramLayout, false);
        ListImageButton disactivateListButton = (ListImageButton) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.done_button, disButtonFrameLayout, false);
        View.OnClickListener disactivateListButtonOnClickListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListImageButton button = (ListImageButton) v;
                disactivateGroupList(button);
            }
        };
        list.setDisButton(disactivateListButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            disactivateListButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
        } else {
            disactivateListButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
        }
        Uri uri = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/confirm_button_64");
        disactivateListButton.setImageURI(uri);
        disactivateListButton.setOnClickListener(disactivateListButtonOnClickListenner);
        disactivateListButton.setList(list);
        disButtonFrameLayout.addView(disactivateListButton);
        listogramLayout.addView(disButtonFrameLayout);
        listCard.addView(listogramLayout);
        basicLayout.addView(listCard);
    }
    public void activeFragmentCleaner(){
        LinearLayout layoutScrollingActiveListsContainer = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
        layoutScrollingActiveListsContainer.removeAllViews();
    }
    public void fragmentShowGood(SList[] result){
        listsListMaker(result);
    }
    public void fragmentShowBad(SList[] result){
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout parentLayout = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
        TextView emptyInformer = (TextView) LayoutInflater.from(parentLayout.getContext()).inflate(R.layout.no_listograms_text_view, parentLayout, false);
        parentLayout.addView(emptyInformer);
    }

    public void fragmentShowSecondGood(SList result){
        CardView cardView = result.getCardView();
        cardView.setVisibility(View.GONE);
    }
    public void fragmentShowSecondBad(SList result){                                                //TODO remember result is always null
        if(result != null){
            ImageButton button = result.getDisButton();
            if(button != null){
                button.setFocusable(true);
                button.setClickable(true);
            }
        }
    }
    public void fragmentShowThirdGood(Item item){
        LinearLayout itemMarkLayout = item.getLayout();
        itemMarkLayout.setAlpha(1f);
        ItemButton itemMarkTouchedButton = item.getButton();
        itemMarkTouchedButton.setFocusable(true);
        itemMarkTouchedButton.setClickable(true);
        if(item.getState()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemMarkLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_1));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                itemMarkLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            } else{
                itemMarkLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            }
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemMarkLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_2));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                itemMarkLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            } else{
                itemMarkLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            }
        }
    }
    public void fragmentShowThirdBad(Item item){
        if(item != null) {
            LinearLayout layout = item.getLayout();
            if(layout != null) {
                layout.setAlpha(1);
            }
            ItemButton itemMarkButton = item.getButton();
            if(itemMarkButton != null) {
                itemMarkButton.setClickable(true);
                itemMarkButton.setFocusable(true);
                itemMarkButton.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }
    public void fragmentShowProcessing(Item item){
        LinearLayout layout = item.getLayout();
        layout.setAlpha(0.5f);
    }

    public void setRefresher(){
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
    public void unsetRefresher(){
        SwipeRefreshLayout refreshLayout = getRefresher();
        if(refreshLayout != null) {
            refreshLayout.setFocusable(false);
            refreshLayout.setRefreshing(false);
        }
    }
    public void setRefresherRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(true);
    }
    public void setRefresherNotRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(false);
    }
    public void refresh(){
        Group2Activity activity = (Group2Activity) getActivity();
        activity.refreshActiveLists();
    }
    public SwipeRefreshLayout getRefresher(){
        return  (SwipeRefreshLayout) rootView.findViewById(R.id.group_active_fragment_refresher);
    }
}
