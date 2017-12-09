package com.example.vovch.listogram_20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

/**
 * Created by vovch on 30.11.2017.
 */

public class GroupFragmentActive extends Fragment {
    private LinearLayout listsLayout;
    private View rootView = null;
    private Inflater insideInflater;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_fragment_active, container, false);
        Button groupDownButton = (Button) rootView.findViewById(R.id.groupdownbutton);
        groupDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group2Activity groupActivity = (Group2Activity) getActivity();
                groupActivity.sendListogram();
            }
        });
        return rootView;
    }
}
