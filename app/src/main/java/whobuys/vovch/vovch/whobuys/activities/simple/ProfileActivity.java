package whobuys.vovch.vovch.whobuys.activities.simple;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.vovch.listogram_20.R;

import org.w3c.dom.Text;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;

public class ProfileActivity extends AppCompatActivity {
    private ActiveActivityProvider provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(9, ProfileActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);

        TextView loginTextView = (TextView) findViewById(R.id.profile_login);
        loginTextView.setText(provider.userSessionData.getLogin());
        TextView passwordTextView = (TextView) findViewById(R.id.profile_password);
        passwordTextView.setText(provider.userSessionData.getPassword());
        TextView idTextView = (TextView) findViewById(R.id.profile_id);
        idTextView.setText(provider.userSessionData.getId());
    }
    @Override
    protected void onStart(){
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(9, ProfileActivity.this);
    }

    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 9) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        if(provider.getActiveActivityNumber() == 9) {
            provider.nullActiveActivity();
        }
        provider.setActiveListsActivityLoadType(1);
        super.onBackPressed();
    }
}
