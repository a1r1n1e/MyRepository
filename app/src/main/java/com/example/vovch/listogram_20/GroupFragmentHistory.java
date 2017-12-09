package com.example.vovch.listogram_20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_fragment_history, container, false);
        Button historyLoadButton = (Button) rootView.findViewById(R.id.loadhistorybutton);
        historyLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group2Activity groupActivity = (Group2Activity) getActivity();
                groupActivity.historyLoad();
            }
        });
        return rootView;
    }
}
