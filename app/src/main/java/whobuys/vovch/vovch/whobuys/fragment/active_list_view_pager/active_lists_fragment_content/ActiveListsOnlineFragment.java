package whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.active_lists_fragment_content;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vovch.listogram_20.R;
import whobuys.vovch.vovch.whobuys.activities.complex.ActiveListsActivity;
import whobuys.vovch.vovch.whobuys.data_types.GroupButton;
import whobuys.vovch.vovch.whobuys.data_types.ListInformer;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;
import whobuys.vovch.vovch.whobuys.fragment.active_list_view_pager.ActiveListsFragment;

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
    public void checkRootView(ViewGroup container, LayoutInflater inflater){
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
        if(activity != null) {
            activity.activeListsOnlineFragmentStart();
        }
        return rootView;
    }
    public void listsListMaker(ListInformer[] result) {
        if(rootView != null && getActivity() != null) {
            int i = 0;
            int length = result.length;
            for (i = 0; i < length; i++) {
                activeListLayoutDrawer(result[i]);
            }
        }
    }
    private void activeListLayoutDrawer(ListInformer informer){
        if(getActivity() != null) {
            LinearLayout basicLayout = (LinearLayout) rootView.findViewById(R.id.activelistslayout);
            CardView cardView = (CardView) LayoutInflater.from(basicLayout.getContext()).inflate(R.layout.list_card, basicLayout, false);
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(cardView.getContext()).inflate(R.layout.active_activity_frame_layout, cardView, false);

            LinearLayout listogramLayout = (LinearLayout) LayoutInflater.from(frameLayout.getContext()).inflate(R.layout.active_lists_linear_layout, frameLayout, false);

            LinearLayout textPartLayout = (LinearLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.active_lists_listinformer_linearlayout, listogramLayout, false);
            TextView groupNameTextView = (TextView) LayoutInflater.from(textPartLayout.getContext()).inflate(R.layout.active_actvity_text_view_1, textPartLayout, false);
            groupNameTextView.setText(informer.getName());


            FrameLayout attentionButtonFrame;
            if (informer.getGroup().getState().equals(UserGroup.DEFAULT_GROUP_STATE_UNWATCHED)) {
                attentionButtonFrame = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.list_header_imagebutton_frame, listogramLayout, false);
                if (informer.isActive()) {
                    ImageButton attentionButton = (ImageButton) LayoutInflater.from(attentionButtonFrame.getContext()).inflate(R.layout.done_button, attentionButtonFrame, false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        attentionButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
                    } else {
                        attentionButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
                    }
                    Uri uri = Uri.parse("android.resource://whobuys.vovch.vovch.whobuys/mipmap/alarm_custom_green_white");
                    attentionButton.setImageURI(uri);
                    attentionButtonFrame.addView(attentionButton);
                }
            } else {
                attentionButtonFrame = (FrameLayout) LayoutInflater.from(listogramLayout.getContext()).inflate(R.layout.small_attention_imagebutton_frame, listogramLayout, false);
                if (informer.isActive()) {
                    ImageButton attentionButton = (ImageButton) LayoutInflater.from(attentionButtonFrame.getContext()).inflate(R.layout.done_button, attentionButtonFrame, false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        attentionButton.setImageDrawable(getActivity().getDrawable(R.drawable.done_button_drawble));
                    } else {
                        attentionButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button_drawble));
                    }
                    Uri uri = Uri.parse("android.resource://whobuys.vovch.vovch.whobuys/mipmap/small_alarm_green");
                    attentionButton.setImageURI(uri);
                    attentionButtonFrame.addView(attentionButton);
                }
            }
            LinearLayout listPartLayout = (LinearLayout) LayoutInflater.from(textPartLayout.getContext()).inflate(R.layout.active_lists_linear_layout, textPartLayout, false);

            TextView lastListNameTextView = (TextView) LayoutInflater.from(listPartLayout.getContext()).inflate(R.layout.listinformer_list_name_textview, listPartLayout, false);
            lastListNameTextView.setText(informer.getLastListName());
            TextView lastListTimeTextView = (TextView) LayoutInflater.from(listPartLayout.getContext()).inflate(R.layout.listinformer_list_time_textview, listPartLayout, false);
            lastListTimeTextView.setText(informer.getLastListTime());

            listPartLayout.addView(lastListNameTextView);
            listPartLayout.addView(lastListTimeTextView);

            textPartLayout.addView(groupNameTextView);
            textPartLayout.addView(listPartLayout);

            listogramLayout.addView(textPartLayout);
            listogramLayout.addView(attentionButtonFrame);
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
    }
    @Override
    public void fragmentShowGood(ListInformer[] result){
        if(rootView != null) {
            cleaner();
            if (result.length > 0) {
                listsListMaker(result);
            } else {
                LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.activelistslayout);
                TextView messageTextView = (TextView) LayoutInflater.from(layout.getContext()).inflate(R.layout.no_listograms_text_view, layout, false);
                messageTextView.setText(getString(R.string.no_groups));
                layout.addView(messageTextView);
            }
        }

    }
    @Override
    public void fragmentShowBad(ListInformer[] result){
        if(rootView != null) {
            cleaner();
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.activelistslayout);
            TextView messageTextView = (TextView) LayoutInflater.from(layout.getContext()).inflate(R.layout.no_listograms_text_view, layout, false);
            messageTextView.setText(getString(R.string.some_error));
            layout.addView(messageTextView);
        }
    }
    private void goToGroup(UserGroup group){
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        if(activity != null && group != null) {
            activity.goToGroup(group);
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
            activity.refreshActiveLists();
        }
    }
    public void setRefresherRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        if(refresher != null) {
            refresher.setRefreshing(true);
        }
    }
    public void setRefresherNotRefreshing(){
        SwipeRefreshLayout refresher = getRefresher();
        if(refresher != null) {
            refresher.setRefreshing(false);
        }
    }
    public SwipeRefreshLayout getRefresher(){
        SwipeRefreshLayout layout = null;
        if(rootView != null) {
            layout = (SwipeRefreshLayout) rootView.findViewById(R.id.active_lists_online_refresh);
            layout.setFocusable(true);
        }
        return  layout;
    }
    @Override
    public void cleaner(){
        if(rootView != null) {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.activelistslayout);
            layout.removeAllViewsInLayout();
        }
    }
}
