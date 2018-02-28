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
 * Created by vovch on 08.01.2018.
 */

public class LoginFragment extends ActiveListsFragment {
    private View rootView;

    TextView.OnEditorActionListener editorListenerOne = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEND|| actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                CreateListEditText passwordEditText = (CreateListEditText) rootView.findViewById(R.id.edittext200);
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

    public void checkRootView(ViewGroup container, LayoutInflater inflater) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.login_page_layout, container, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.login_page_layout, container, false);
        View.OnClickListener NewListenner1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToLoginFromForms();
            }
        };
        View.OnClickListener NewListenner2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegisterFragment();
            }
        };
        Button Btn1 = (Button) rootView.findViewById(R.id.button100);
        Btn1.setOnClickListener(NewListenner1);
        Button Btn2 = (Button) rootView.findViewById(R.id.button200);
        Btn2.setOnClickListener(NewListenner2);
        CreateListEditText loginEditText = (CreateListEditText) rootView.findViewById(R.id.edittext100);
        loginEditText.setOnEditorActionListener(editorListenerOne);
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        activity.loginFragmentStart();
        return rootView;
    }

    public void tryToLoginFromForms() {
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        TextView errorTextView = (TextView) rootView.findViewById(R.id.login_errors_textview);
        errorTextView.setText("");
        if (activity.getToken() != null) {
            EditText editText1 = (EditText) rootView.findViewById(R.id.edittext100);
            EditText editText2 = (EditText) rootView.findViewById(R.id.edittext200);
            String uName = editText1.getText().toString();
            String uPassword = editText2.getText().toString();
            if (!uName.equals("") && !uPassword.equals("")) {
                activity.tryToLoginFromForms(uName, uPassword);
            } else {
                errorTextView.setText("Enter Missing Value");
            }
        } else {
            errorTextView.setText("Our Fail. Try to reinstall the app");
        }
    }

    public void setRegisterFragment() {
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        activity.loginToRegistrationFragmentChange();
    }

    public void badInform(String result) {
        TextView errorTextView = (TextView) rootView.findViewById(R.id.login_errors_textview);
        errorTextView.setText(result);
    }
}
