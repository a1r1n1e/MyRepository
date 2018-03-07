package com.example.vovch.listogram_20.fragment.active_list_view_pager.active_lists_fragment_content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vovch.listogram_20.R;
import com.example.vovch.listogram_20.activities.complex.ActiveListsActivity;
import com.example.vovch.listogram_20.data_types.CreateListEditText;
import com.example.vovch.listogram_20.fragment.active_list_view_pager.ActiveListsFragment;

/**
 * Created by vovch on 09.01.2018.
 */

public class RegistrationFragment extends ActiveListsFragment {
    private View rootView;
    TextView.OnEditorActionListener editorListenerOne = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND|| actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                CreateListEditText passwordEditText = (CreateListEditText) rootView.findViewById(R.id.registration_pasword);
                Selection.setSelection(passwordEditText.getText(), passwordEditText.getSelectionStart());
                passwordEditText.requestFocus();
                return true;
            }
            else {
                return true;
            }
        }
    };
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
                    errorInformer.setText(getString(R.string.values_format));
                }
            }
        };
        CreateListEditText registrationLogin = (CreateListEditText) rootView.findViewById(R.id.registration_login);
        registrationLogin.setOnEditorActionListener(editorListenerOne);
        Button Btn1 = (Button) rootView.findViewById(R.id.registration_button);
        Btn1.setOnClickListener(NewListenner1);
        return rootView;
    }
    public boolean fieldCheck(){
        boolean result = false;
        if(rootView != null) {
            EditText loginTextView = (EditText) rootView.findViewById(R.id.registration_login);
            EditText passwordTextView = (EditText) rootView.findViewById(R.id.registration_pasword);
            String login = loginTextView.getText().toString();
            String password = passwordTextView.getText().toString();
            if (login.length() > 3 && password.length() > 3 && login.length() < 33 && password.length() < 33) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void cleaner(){
        if(rootView != null) {
            rootView.setVisibility(View.GONE);
        }
    }

    public void tryToRegister(String login, String password){
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        if(activity != null) {
            activity.tryToRegister(login, password);
        }
    }
    public void badInform(String result){
        if(rootView != null) {
            TextView errorTextView = (TextView) rootView.findViewById(R.id.registration_error_textview);
            errorTextView.setText(result);
        }
    }
}