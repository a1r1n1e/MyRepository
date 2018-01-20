package com.example.vovch.listogram_20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vovch on 14.12.2017.
 */

public class ActiveListsFragment extends Fragment {
    private View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    protected void checkRootView(ViewGroup container, LayoutInflater inflater){
        if(rootView == null){
            rootView = inflater.inflate(R.layout.active_lists_page_one_container, container, false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.active_lists_page_one_container, container, false);
        return rootView;
    }
    protected void fragmentShowGood(ListInformer[] result){}
    protected void fragmentShowBad(ListInformer[] result){}
    private void cleaner(){
        FrameLayout layout = (FrameLayout) rootView.findViewById(R.id.active_lists_page_one);
        layout.removeAllViewsInLayout();
    }
    protected void noInternet(){
        cleaner();
        FrameLayout layout = (FrameLayout) rootView.findViewById(R.id.active_lists_page_one);
        TextView messageTextView = (TextView) LayoutInflater.from(layout.getContext()).inflate(R.layout.no_internet_informer_text_view, layout, false);
        layout.addView(messageTextView);
    }
}
