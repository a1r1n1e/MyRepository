package whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vovch.listogram_20.R;
import whobuys.vovch.vovch.whobuys.activities.complex.ActiveListsActivity;
import whobuys.vovch.vovch.whobuys.data_types.Item;
import whobuys.vovch.vovch.whobuys.data_types.ItemButton;
import whobuys.vovch.vovch.whobuys.data_types.ListImageButton;
import whobuys.vovch.vovch.whobuys.data_types.SList;

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
    public void checkRootView(ViewGroup container, LayoutInflater inflater){
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
        if(activeListsActivity != null) {
            activeListsActivity.onOfflineFragmentStart(ActiveFragmentOffline.this);
        }
        return rootView;
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
        String listOwner;
        if(list.getOwner() > 0) {
            listOwner = getString(R.string.from)+ " " + list.getOwnerName();
        }
        else{
            listOwner = getString(R.string.your_list);
        }
        listNameTextView.setText(listOwner);

        TextView listCreationTimeTextView = (TextView) LayoutInflater.from(leftHeaderLayout.getContext()).inflate(R.layout.list_header_left_textview, leftHeaderLayout, false);
        if(list.getCreationTime() != null){
            listCreationTimeTextView.setTextSize(10);
            listCreationTimeTextView.setText(list.getCreationTime());
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
        Uri uri3 = Uri.parse("android.resource://com.vovch.vovch.whobuys/mipmap/resend_custom_white_green");
        resendButton.setImageURI(uri3);
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
        View.OnClickListener disactivateListButtonOnClickListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disactivateList((ListImageButton) v);
            }
        };

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
        Uri uri = Uri.parse("android.resource://com.vovch.vovch.whobuys/mipmap/delete_custom_white_green");
        disactivateListButton.setImageURI(uri);
        disactivateListButton.setList(list);
        disactivateListButton.setOnClickListener(disactivateListButtonOnClickListenner);

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
        Uri uri2 = Uri.parse("android.resource://com.vovch.vovch.whobuys/mipmap/redact_custom_white_green");
        redactButton.setImageURI(uri2);
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
    private void disactivateList(ListImageButton button){
        if(button.getList() != null) {
            ActiveListsActivity activity = (ActiveListsActivity) getActivity();
            button.setFocusable(false);
            button.setClickable(false);
            if(activity != null) {
                activity.disactivateList(button);
            }
        }
    }
    private void redactList(ListImageButton button){
        if(button.getList() != null){
            ActiveListsActivity activity = (ActiveListsActivity) getActivity();
            button.setFocusable(false);
            button.setClickable(false);
            if(activity != null) {
                activity.redactList(button.getList());
            }
        }
    }
    private void resend(ListImageButton button){
        if(button.getList() != null){
            ActiveListsActivity activity = (ActiveListsActivity) getActivity();
            button.setFocusable(false);
            button.setClickable(false);
            if(activity != null) {
                activity.resendList(button.getList());
            }
        }
    }
    private void onItemMarkButtonTouchedAction(ItemButton button){
        if(button.getItem() != null) {
            button.setFocusable(false);
            button.setClickable(false);
            ActiveListsActivity activity = (ActiveListsActivity) getActivity();
            if(activity != null) {
                activity.itemmark(button.getItem());
            }
        }
    }
    private void makeListogramLine(Item item, LinearLayout listogramLayout){
        FrameLayout addingFrameLayout = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.list_element_frame_layout, listogramLayout, false);
        LinearLayout addingLayout = (LinearLayout) LayoutInflater.from(addingFrameLayout.getContext()).inflate(R.layout.list_element_linear_layout, addingFrameLayout, false);

        TextView itemName = (TextView) LayoutInflater.from(addingLayout.getContext()).inflate(R.layout.list_element_text_view, addingLayout, false);
        itemName.setText(item.getName());
        ItemButton groupButton = (ItemButton) LayoutInflater.from(addingLayout.getContext()).inflate(R.layout.list_element_button, addingFrameLayout, false);
        groupButton.setItem(item);
        groupButton.setFocusable(true);
        groupButton.setClickable(true);
        View.OnClickListener ItemMarkButtonListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemMarkButtonTouchedAction((ItemButton) v);
            }
        };
        if (item.getState()) {
            groupButton.setOnClickListener(ItemMarkButtonListenner);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addingLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_1));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addingLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            } else {
                addingLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
            }
        } else {
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
        item.setLayout(addingLayout);
        item.setButton(groupButton);
        addingFrameLayout.addView(addingLayout);
        addingFrameLayout.addView(groupButton);
        listogramLayout.addView(addingFrameLayout);
    }
    public void listsListMaker(SList[] result) {
        if(rootView != null && getActivity() != null) {
            int listsNumber = result.length;
            for (int i = 0; i < listsNumber; i++) {
                listsLayoutDrawer(result[i]);
            }
        }
    }
    public void activeFragmentCleaner(){
        if(rootView != null) {
            LinearLayout layoutScrollingActiveListsContainer = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
            layoutScrollingActiveListsContainer.removeAllViews();
        }
    }
    public void fragmentShowGood(SList[] result){
        activeFragmentCleaner();
        listsListMaker(result);
    }
    public void fragmentShowBad(SList[] result){
        if(rootView != null) {
            LinearLayout parentLayout = (LinearLayout) rootView.findViewById(R.id.listogramslayout);
            parentLayout.removeAllViewsInLayout();
            TextView emptyInformer = (TextView) LayoutInflater.from(parentLayout.getContext()).inflate(R.layout.no_listograms_text_view, parentLayout, false);
            if (!(result == null || result.length == 0)) {
                emptyInformer.setText(getString(R.string.some_error));
            }
            parentLayout.addView(emptyInformer);
        }
    }
    public void fragmentShowSecondGood(Item result){
        Button itemMarkTouchedButton = result.getButton();
        if(itemMarkTouchedButton != null) {
            itemMarkTouchedButton.setFocusable(true);
            itemMarkTouchedButton.setClickable(true);
        }
        LinearLayout itemMarkLayout = result.getLayout();
        if(itemMarkLayout != null && getActivity() != null) {
            if (result.getState()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemMarkLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_1));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemMarkLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
                } else {
                    itemMarkLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_1));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemMarkLayout.setBackground(getActivity().getDrawable(R.drawable.no_corners_layout_color_2));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    itemMarkLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
                } else {
                    itemMarkLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
                }
            }
        }
    }
    public void fragmentShowSecondBad(Item result){
        Button itemMarkButton = result.getButton();
        if(itemMarkButton != null) {
            itemMarkButton.setClickable(true);
            itemMarkButton.setFocusable(true);
            itemMarkButton.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }
    public void fragmentDisactivateGood(SList result){
        CardView cardView = result.getCardView();
        if(cardView != null) {
            cardView.setVisibility(View.GONE);
        }
    }
    public void fragmentDisactivateBad(SList result){
        if(result != null) {
            result.getDisButton().setFocusable(true);
            result.getDisButton().setClickable(true);
        }
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
    public void refresh(){
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        if(activity != null) {
            activity.refreshOfflineLists();
        }
    }
    public SwipeRefreshLayout getRefresher(){
        SwipeRefreshLayout result = null;
        if(rootView != null) {
            result = (SwipeRefreshLayout) rootView.findViewById(R.id.group_active_fragment_refresher);
        }
        return result;
    }
}
