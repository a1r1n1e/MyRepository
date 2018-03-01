package com.example.vovch.listogram_20.fragment.group_activity;

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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.activities.complex.Group2Activity;
import com.example.vovch.listogram_20.data_types.HistoryScrollView;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.ItemButton;
import com.example.vovch.listogram_20.data_types.ListImageButton;
import com.example.vovch.listogram_20.data_types.SList;

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

    public void checkRootView(ViewGroup container, LayoutInflater inflater) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.group_fragment_history, container, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_fragment_history, container, false);
        unsetRefresher();
        Group2Activity activity = (Group2Activity) getActivity();
        activity.onHistoryReady(GroupFragmentHistory.this);
        setRefresher();
        return rootView;
    }

    public void listsLayoutDrawer(SList list) {
        LinearLayout basicLayout;
        basicLayout = (LinearLayout) rootView.findViewById(R.id.passedlistogramslayout);
        CardView listCard = (CardView) LayoutInflater.from(basicLayout.getContext()).inflate(R.layout.list_card, basicLayout, false);
        list.setCardView(listCard);
        LinearLayout listogramLayout = (LinearLayout) LayoutInflater.from(listCard.getContext()).inflate(R.layout.list_layout, listCard, false);
        LinearLayout headerLayout = (LinearLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.list_header_layout, listogramLayout, false);
        LinearLayout leftHeaderLayout = (LinearLayout) LayoutInflater.from(headerLayout.getContext()).inflate(R.layout.list_header_left_layout, headerLayout, false);
        TextView listNameTextView = (TextView) LayoutInflater.from(leftHeaderLayout.getContext()).inflate(R.layout.list_header_left_textview, leftHeaderLayout, false);
        listNameTextView.setAlpha(0.5f);
        String listOwner;
        if(list.getOwner() > 0) {
            listOwner = "From: " + list.getOwnerName();
        }
        else{
            listOwner = "Your List";
        }
        listNameTextView.setText(listOwner);

        TextView listCreationTimeTextView = (TextView) LayoutInflater.from(leftHeaderLayout.getContext()).inflate(R.layout.list_header_left_textview, leftHeaderLayout, false);
        listCreationTimeTextView.setAlpha(0.5f);
        if(list.getCreationTime() != null){
            listCreationTimeTextView.setTextSize(10);
            listCreationTimeTextView.setText(list.getHumanCreationTime());
        }
        leftHeaderLayout.addView(listNameTextView);
        leftHeaderLayout.addView(listCreationTimeTextView);
        headerLayout.addView(leftHeaderLayout);
        FrameLayout imageButtonFrame = (FrameLayout) LayoutInflater.from(headerLayout.getContext()).inflate(R.layout.list_header_imagebutton_frame, headerLayout, false);
        ListImageButton resendButton = (ListImageButton) LayoutInflater.from(imageButtonFrame.getContext()).inflate(R.layout.done_button, imageButtonFrame, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resendButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
        } else {
            resendButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
        }
        Uri uri3 = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/resend_48");
        resendButton.setImageURI(uri3);
        resendButton.setAlpha(0.5f);
        resendButton.setList(list);

        imageButtonFrame.addView(resendButton);
        headerLayout.addView(imageButtonFrame);
        listogramLayout.addView(headerLayout);
        Item[] items = list.getItems();
        int length = items.length;
        list.setResendButton(resendButton);
        resendButton.setList(list);
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend((ListImageButton) v);
            }
        });
        for (int i = 0; i < length; i++) {
            makeListogramLine(items[i], listogramLayout);
        }

        LinearLayout footerLayout = (LinearLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.list_footer_layout, listogramLayout, false);
        LinearLayout leftFooterLayout = (LinearLayout) LayoutInflater.from(footerLayout.getContext()).inflate(R.layout.list_footer_half_layout, footerLayout, false);
        leftFooterLayout.setGravity(Gravity.START);
        FrameLayout disButtonFrameLayout = (FrameLayout) LayoutInflater.from(leftFooterLayout.getContext()).inflate(R.layout.dis_button_frame_layout, leftFooterLayout, false);
        ListImageButton disactivateListButton = (ListImageButton) LayoutInflater.from(disButtonFrameLayout.getContext()).inflate(R.layout.done_button, disButtonFrameLayout, false);
        list.setDisButton(disactivateListButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            disactivateListButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
        } else {
            disactivateListButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
        }
        Uri uri = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/check_48");
        disactivateListButton.setImageURI(uri);
        disactivateListButton.setAlpha(0.5f);
        disactivateListButton.setList(list);

        disButtonFrameLayout.addView(disactivateListButton);
        leftFooterLayout.addView(disButtonFrameLayout);
        LinearLayout rightFooterLayout = (LinearLayout) LayoutInflater.from(footerLayout.getContext()).inflate(R.layout.list_footer_half_layout, footerLayout, false);
        rightFooterLayout.setGravity(Gravity.END);
        FrameLayout redactButtonFrameLayout = (FrameLayout) LayoutInflater.from(rightFooterLayout.getContext()).inflate(R.layout.dis_button_frame_layout, rightFooterLayout, false);
        ListImageButton redactButton = (ListImageButton) LayoutInflater.from(redactButtonFrameLayout.getContext()).inflate(R.layout.done_button, redactButtonFrameLayout, false);

        View.OnClickListener redactListButtonOnClickListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redactList((ListImageButton) v);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            redactButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
        } else {
            redactButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
        }
        Uri uri2 = Uri.parse("android.resource://com.example.vovch.listogram_20/drawable/redact_document_48");
        redactButton.setImageURI(uri2);
        redactButton.setAlpha(0.5f);
        redactButton.setList(list);
        redactButton.setOnClickListener(redactListButtonOnClickListenner);

        list.setRedactButton(redactButton);
        redactButtonFrameLayout.addView(redactButton);
        rightFooterLayout.addView(redactButtonFrameLayout);
        footerLayout.addView(leftFooterLayout);
        footerLayout.addView(rightFooterLayout);
        listogramLayout.addView(footerLayout);
        listCard.addView(listogramLayout);
        basicLayout.addView(listCard);
    }

    private void makeListogramLine(Item item, LinearLayout listogramLayout) {
        FrameLayout addingFrameLayout = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.list_element_frame_layout, listogramLayout, false);
        LinearLayout addingVerticalLayout = (LinearLayout) LayoutInflater.from(addingFrameLayout.getContext()).inflate(R.layout.list_element_vertical_layout, addingFrameLayout, false);
        LinearLayout addingLayout = (LinearLayout) LayoutInflater.from(addingVerticalLayout.getContext()).inflate(R.layout.list_element_linear_layout, addingVerticalLayout, false);
        addingLayout.setAlpha(0.5f);

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
        addingLayout.addView(itemName);
        addingLayout.addView(itemComment);
        item.setLayout(addingLayout);
        item.setButton(groupButton);
        addingVerticalLayout.addView(addingLayout);
        item.setVerticalLayout(addingVerticalLayout);
        if(item.getOwnerName() != null && item.getOwner() != null){
            TextView itemOwnerTextView = (TextView) LayoutInflater.from(addingVerticalLayout.getContext()).inflate(R.layout.list_element_item_owner_textview, addingVerticalLayout, false);
            itemOwnerTextView.setText( "By: " + item.getOwnerName());
            addingVerticalLayout.addView(itemOwnerTextView);
            item.setOwnerTextView(itemOwnerTextView);
        }
        addingFrameLayout.addView(addingVerticalLayout);
        addingFrameLayout.addView(groupButton);
        listogramLayout.addView(addingFrameLayout);
    }

    private void resend(ListImageButton button){
        if(button.getList() != null){
            Group2Activity activity = (Group2Activity) getActivity();
            button.setFocusable(false);
            button.setClickable(false);
            activity.resendList(button);
        }
    }

    private void redactList(ListImageButton button) {
        if (button.getList() != null) {
            Group2Activity activity = (Group2Activity) getActivity();
            button.setFocusable(false);
            button.setClickable(false);
            activity.redactList(button.getList());
        }
    }

    public void listsListMaker(SList[] result) {
        int listsNumber = result.length;
        for (int i = 0; i < listsNumber; i++) {
            listsLayoutDrawer(result[i]);
        }
    }

    public void historyFragmentCleaner() {
        LinearLayout layoutScrollingActiveListsContainer = (LinearLayout) rootView.findViewById(R.id.passedlistogramslayout);
        layoutScrollingActiveListsContainer.removeAllViews();
    }

    public void fragmentShowGood(SList[] result) {
        historyFragmentCleaner();
        listsListMaker(result);
    }
    public HistoryScrollView getScrollView(){
        return  (HistoryScrollView) rootView.findViewById(R.id.grouphistoryscroll);
    }
    public void fragmentShowBad(String result) {
        historyFragmentCleaner();
        LinearLayout parentLayout = (LinearLayout) rootView.findViewById(R.id.passedlistogramslayout);
        TextView emptyInformer = (TextView) LayoutInflater.from(parentLayout.getContext()).inflate(R.layout.no_listograms_text_view, parentLayout, false);
        if (!(result.equals(""))) {
            emptyInformer.setText("We Failed");
        }
        parentLayout.addView(emptyInformer);
    }

    public void setRefresher() {
        SwipeRefreshLayout refreshLayout = getRefresher();
        if (refreshLayout != null) {
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

    public void unsetRefresher() {
        SwipeRefreshLayout refreshLayout = getRefresher();
        if (refreshLayout != null) {
            refreshLayout.setFocusable(false);
            refreshLayout.setRefreshing(false);
        }
    }

    public void setRefresherRefreshing() {
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(true);
    }

    public void setRefresherNotRefreshing() {
        SwipeRefreshLayout refresher = getRefresher();
        refresher.setRefreshing(false);
    }

    public void refresh() {
        Group2Activity activity = (Group2Activity) getActivity();
        activity.refreshHistoryLists();
    }

    public SwipeRefreshLayout getRefresher() {
        return (SwipeRefreshLayout) rootView.findViewById(R.id.group_history_fragment_refresher);
    }
}
