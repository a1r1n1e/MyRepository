package com.example.vovch.listogram_20;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vovch on 30.10.2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class ActiveCheckFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";
    public static final String APP_PREFERENCES = "autentification";
    private static final String  APP_PREFERENCES_TOKEN= "token";
    private static final String  APP_PREFERENCES_USERID= "userid";
    public static final String APP_PREFERENCES_PASSWORD = "password";
    private SharedPreferences tokenPreferences;
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
        provider.userSessionData.setToken(refreshedToken);
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        provider.userSessionData.registerForPushes();
    }
}
