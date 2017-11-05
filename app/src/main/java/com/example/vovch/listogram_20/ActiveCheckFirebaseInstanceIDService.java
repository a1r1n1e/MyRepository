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
        tokenPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenPreferences.edit();
        editor.putString(APP_PREFERENCES_TOKEN, refreshedToken);
        editor.apply();

        if(tokenPreferences.contains(APP_PREFERENCES_USERID) && tokenPreferences.contains(APP_PREFERENCES_PASSWORD)) {
            String userId = tokenPreferences.getString(APP_PREFERENCES_USERID, null);
            String userPassword = tokenPreferences.getString(APP_PREFERENCES_PASSWORD, null);
            sendRegistrationToServer(refreshedToken, userId, userPassword);
        }
        //Log the token

        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    private void sendRegistrationToServer(String token, String user, String password) {
        try {
            URL url = new URL("http://217.10.35.250/java_token_refresh.php");
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("newusertoken", token);
            postDataParams.put("userid", user);
            postDataParams.put("userpassword", password);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);                                                 // Enable POST stream
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
