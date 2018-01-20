package com.example.vovch.listogram_20.fragment.active_list_view_pager.active_lists_fragment_content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.ActiveListsFragment;

/**
 * Created by vovch on 09.01.2018.
 */

public class RegistrationFragment extends ActiveListsFragment {
    private View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    public void checkRootView(ViewGroup container, LayoutInflater inflater){
        if(rootView == null){
            rootView = inflater.inflate(R.layout.activity_registration, container, false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_registration, container, false);
        View.OnClickListener NewListenner1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldCheck()) {
                    EditText loginTextView = (EditText) rootView.findViewById(R.id.registration_login);
                    EditText passwordTextView = (EditText) rootView.findViewById(R.id.registration_pasword);
                    tryToRegister(loginTextView.getText().toString(), passwordTextView.getText().toString());
                }
                else{
                    TextView errorInformer = (TextView) rootView.findViewById(R.id.registration_error_textview);
                    errorInformer.setText("Values Should Be Less Than 32 At Least 4 Characters Long:(");
                }
            }
        };
        Button Btn1 = (Button) rootView.findViewById(R.id.registration_button);
        Btn1.setOnClickListener(NewListenner1);
        return rootView;
    }
    public boolean fieldCheck(){
        boolean result = false;
        EditText loginTextView = (EditText) rootView.findViewById(R.id.registration_login);
        EditText passwordTextView = (EditText) rootView.findViewById(R.id.registration_pasword);
        String login = loginTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        if(login.length() > 3 && password.length() > 3 && login.length() < 33 && password.length() < 33){
            result = true;
        }
        return result;
    }
    public void tryToRegister(String login, String password){
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        activity.tryToRegister(login, password);
    }
    public void badInform(String result){
        TextView errorTextView = (TextView) rootView.findViewById(R.id.registration_error_textview);
        errorTextView.setText(result);
    }
}