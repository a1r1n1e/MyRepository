package com.example.vovch.listogram_20.data_layer;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vovch.listogram_20.data_layer.async_tasks.LogouterTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vovch on 05.01.2018.
 */

public class UserSessionData {
    private Context context;
    private static UserSessionData instance;
    public static final String APP_PREFERENCES = "autentification";
    private static final String APP_PREFERENCES_TOKEN = "token";
    private static final String APP_PREFERENCES_USERID = "user_id";
    private static final String APP_PREFERENCES_LOGIN = "login";
    private static final String APP_PREFERENCES_PASSWORD = "password";
    private static final String APP_PREFERENCES_SESSION = "session_id";
    private SharedPreferences preferences;
    private String id;
    private String name;
    private String password;
    private String login;
    private String token;
    private String session;
    private static final String CLIENT_VERSION = "98";
    private static final String CLIENT_TYPE = "1";
    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_LOGOUT = "logout";
    private static final String ACTION_CHECK = "check_session";
    private static final String DEFAULT_NOT_EXISTING_SESSION_VALUE = "-100";

    private UserSessionData(Context newContext) {
        context = newContext;
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        login = preferences.getString(APP_PREFERENCES_LOGIN, null);
        password = preferences.getString(APP_PREFERENCES_PASSWORD, null);
        token = preferences.getString(APP_PREFERENCES_TOKEN, null);
        id = preferences.getString(APP_PREFERENCES_USERID, null);
        session = preferences.getString(APP_PREFERENCES_SESSION, null);
    }

    public static UserSessionData getInstance(Context newContext) {
        if (instance == null) {
            instance = new UserSessionData(newContext);
        }
        return instance;
    }

    public boolean isLoginned() {
        boolean result = false;
        if (id != null && login != null && password != null && token != null && isSession()) {
            result = true;
        }
        return result;
    }

    public boolean isSession(){
        boolean result = false;
        if(session != null){
            result = true;
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String newToken) {
        token = newToken;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_TOKEN, token);
        editor.apply();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setUserData(String newId, String newName, String newLogin, String newPassword) {
        id = newId;
        name = newName;
        login = newLogin;
        password = newPassword;
    }

    public void checkUserData(String newId, String newLogin, String newPassword, String newSession) {
        id = newId;
        login = newLogin;
        password = newPassword;
        session = newSession;
        savePrefs(newId, newLogin, newPassword, newSession);
    }

    public boolean isAnyPrefsData() {
        boolean result = false;
        if (preferences != null &&
                preferences.getString(APP_PREFERENCES_TOKEN, null) != null &&
                preferences.getString(APP_PREFERENCES_LOGIN, null) != null &&
                preferences.getString(APP_PREFERENCES_PASSWORD, null) != null &&
                preferences.getString(APP_PREFERENCES_SESSION, null) != null
                ) {
            result = true;
        }
        return result;
    }

    public String getClientVersion(){
        return CLIENT_VERSION;
    }

    public String getClientType(){
        return CLIENT_TYPE;
    }

    public void savePrefs(final String userId, final String userName, final String userPassword, final String userSession) {    // Should be used not from UI thread
        if (userId != null && userName != null && userPassword != null && userSession != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_PREFERENCES_LOGIN, userName);
            editor.putString(APP_PREFERENCES_USERID, userId);
            editor.putString(APP_PREFERENCES_PASSWORD, userPassword);
            editor.putString(APP_PREFERENCES_SESSION, userSession);
            editor.apply();
        }
    }

    public void exit() {
        LogouterTask logouterTask = new LogouterTask();
        logouterTask.execute(context);
    }

    public void showExitGood(){
        id = null;
        name = null;
        password = null;
        savePrefs(null, null, null, null);
    }

    public void showExitBad(){                                                                          //TODO

    }

    public void registerForPushes() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isAnyPrefsData()) {
                    sendTokenToServer(preferences.getString(APP_PREFERENCES_TOKEN, null), preferences.getString(APP_PREFERENCES_USERID, null), preferences.getString(APP_PREFERENCES_PASSWORD, null));
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    protected String getSession(){
        return session;
    }

    protected void setSession(String newSession){
        session = newSession;
    }

    public String startSession(String newLogin, String newPassword){                                                                       //should be used not from UI Thread
        StringBuilder result = null;
        String tempResult = doSMTHWithSession(DEFAULT_NOT_EXISTING_SESSION_VALUE, ACTION_LOGIN, newLogin, newPassword);
        String prefixString = tempResult.substring(0, 3);
        String postfixString = tempResult.substring(3);
        if (prefixString.equals("200")) {
            result = new StringBuilder("");
            WebCall webCall = new WebCall();
            result.append(prefixString);
            String newId = webCall.getStringFromJsonString(postfixString, "id");
            result.append(newId);
            String sessionValue = webCall.getStringFromJsonString(postfixString, "session_id");
            setSession(sessionValue);
            checkUserData(newId, newLogin, newPassword, sessionValue);
            return result.toString();
        }
        else{
            return prefixString;
        }
    }

    public String endSession(){                                                                         //should be used not from UI Thread
        return  doSMTHWithSession(session, ACTION_LOGOUT, login, password);
    }

    public Boolean checkSession(){                                                                      //should be used not from UI Thread
        boolean result = false;
        String tempResult = doSMTHWithSession(session, ACTION_CHECK, login, password);
        if(tempResult.substring(0, 3).equals("200")){
            result = true;
        }
        return result;
    }

    private String doSMTHWithSession(String session_id, String action, String incomingLogin, String incomingPassword){                                 //should be used not from UI Thread
        String response = null;
        if (token != null && incomingPassword != null && incomingLogin != null && session_id != null && action != null ) {
            try {
                URL url;
                HashMap<String, String> postDataParams = new HashMap<String, String>();
                if(!action.equals(ACTION_CHECK)) {
                    url = new URL("http://217.10.35.250/who_buys_sessioner.php");
                    postDataParams.put("uname", incomingLogin);
                    postDataParams.put("upassword", incomingPassword);
                }
                else{
                    url = new URL("http://217.10.35.250/who_buys_controller.php");
                    JSONArray object = new JSONArray();
                    String jsonString = object.toString();
                    postDataParams.put("data_json", jsonString);
                    postDataParams.put("third", "rrr");
                    postDataParams.put("uname", "hhh");
                    postDataParams.put("upassword", "nnn");
                }
                postDataParams.put("session_id", session_id);
                postDataParams.put("action", action);
                postDataParams.put("token", token);
                postDataParams.put("version", CLIENT_VERSION);
                postDataParams.put("client_type", CLIENT_TYPE);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);                                                 // Enable POST stream
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                response = "";

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    response += String.valueOf(responseCode);
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response += String.valueOf(responseCode);
                }

                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private void sendTokenToServer(String token, String userId, String password) {                 //Should be used not from UI thread
        if (token != null && userId != null && password != null) {
            try {
                URL url = new URL("http://217.10.35.250/java_token_refresh.php");
                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put("newusertoken", token);
                postDataParams.put("userid", userId);
                postDataParams.put("userpassword", password);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
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
