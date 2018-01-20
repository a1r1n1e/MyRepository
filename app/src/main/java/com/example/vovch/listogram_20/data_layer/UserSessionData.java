package com.example.vovch.listogram_20.data_layer;

import android.content.Context;
import android.content.SharedPreferences;

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

/**
 * Created by vovch on 05.01.2018.
 */

public class UserSessionData {
    private Context context;
    private static UserSessionData instance;
    public static final String APP_PREFERENCES = "autentification";
    private static final String  APP_PREFERENCES_TOKEN= "token";
    private static final String  APP_PREFERENCES_USERID= "user_id";
    private static final String APP_PREFERENCES_LOGIN = "login";
    private static final String APP_PREFERENCES_PASSWORD = "password";
    private SharedPreferences preferences;
    private String id;
    private String name;
    private String password;
    private String login;
    private String token;

    private UserSessionData(Context newContext){
        context = newContext;
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        login = preferences.getString(APP_PREFERENCES_LOGIN, null);
        password = preferences.getString(APP_PREFERENCES_PASSWORD, null);
        token = preferences.getString(APP_PREFERENCES_TOKEN, null);
        id = preferences.getString(APP_PREFERENCES_USERID, null);
    }
    public static UserSessionData getInstance(Context newContext){
        if(instance == null){
            instance = new UserSessionData(newContext);
        }
        return instance;
    }
    public String getId(){
        return id;
    }
    public String getToken(){
        return token;
    }
    public void  setToken(String newToken){
        token = newToken;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_TOKEN, token);
        editor.apply();
    }
    public String getLogin(){return login;}
    public String getPassword(){return password;}
    public void setUserData(String newId, String newName, String newLogin, String newPassword){
        id = newId;
        name = newName;
        login = newLogin;
        password = newPassword;
    }
    public void checkUserData(String newId, String newLogin, String newPassword){
        if(id == null || !newId.equals(id)){
            id = newId;
        }
        if(login == null){
            login = newLogin;
        }
        if(password == null){
            password = newPassword;
        }
        savePrefs(newId, newLogin, newPassword);
    }
    public boolean isAnyPrefsData(){
        boolean result = false;
        if(     preferences != null &&
                preferences.getString(APP_PREFERENCES_TOKEN, null) != null &&
                preferences.getString(APP_PREFERENCES_LOGIN, null) != null &&
                preferences.getString(APP_PREFERENCES_PASSWORD, null) != null
                ){
            result = true;
        }
        return result;
    }
    public void savePrefs(final String userId, final String userName, final String userPassword){    // Should be used not from UI thread
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_LOGIN, userName);
        editor.putString(APP_PREFERENCES_USERID, userId);
        editor.putString(APP_PREFERENCES_PASSWORD, userPassword);
        editor.apply();
    }
    public void exit(){
        id = null;
        name = null;
        password = null;
        savePrefs(id, name, password);
    }
    public void registerForPushes(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(isAnyPrefsData()){
                    sendRegistrationToServer(preferences.getString(APP_PREFERENCES_TOKEN, null), preferences.getString(APP_PREFERENCES_USERID, null), preferences.getString(APP_PREFERENCES_PASSWORD, null));
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private void sendRegistrationToServer(String token, String user, String password) {                 //Should be used not from UI thread
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
}
