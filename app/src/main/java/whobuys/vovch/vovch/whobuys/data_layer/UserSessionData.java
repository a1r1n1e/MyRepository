package whobuys.vovch.vovch.whobuys.data_layer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import com.example.vovch.listogram_20.R;
import whobuys.vovch.vovch.whobuys.data_layer.async_tasks.LogouterTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
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
    private static final String CLIENT_VERSION = "110";
    private static final String CLIENT_TYPE = "1";
    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_LOGOUT = "logout";
    private static final String ACTION_CHECK = "check_session";
    private static final String DEFAULT_NOT_EXISTING_SESSION_VALUE = "-100";

    private UserSessionData(Context newContext) {
        try {
            context = newContext;
            if (context != null) {
                preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                if (preferences != null) {
                    login = preferences.getString(APP_PREFERENCES_LOGIN, null);
                    password = preferences.getString(APP_PREFERENCES_PASSWORD, null);
                    token = preferences.getString(APP_PREFERENCES_TOKEN, null);
                    id = preferences.getString(APP_PREFERENCES_USERID, null);
                    session = preferences.getString(APP_PREFERENCES_SESSION, DEFAULT_NOT_EXISTING_SESSION_VALUE);
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
    }

    public static UserSessionData getInstance(Context newContext) {
        if (instance == null) {
            instance = new UserSessionData(newContext);
        }
        return instance;
    }

    public Context getContext(){
        return context;
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
        if(session != null && !session.equals(DEFAULT_NOT_EXISTING_SESSION_VALUE)){
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
        try {
            token = newToken;
            if (token != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(APP_PREFERENCES_TOKEN, token);
                editor.apply();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
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
        try {
            id = newId;
            login = newLogin;
            password = newPassword;
            if (newSession != null) {
                session = newSession;
            } else {
                session = DEFAULT_NOT_EXISTING_SESSION_VALUE;
            }
            savePrefs(newId, newLogin, newPassword, newSession);
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
    }

    public boolean isAnyPrefsData() {
        boolean result = false;
        try {
            if (preferences != null &&
                    preferences.getString(APP_PREFERENCES_TOKEN, null) != null &&
                    preferences.getString(APP_PREFERENCES_LOGIN, null) != null &&
                    preferences.getString(APP_PREFERENCES_PASSWORD, null) != null &&
                    !preferences.getString(APP_PREFERENCES_SESSION, DEFAULT_NOT_EXISTING_SESSION_VALUE).equals(DEFAULT_NOT_EXISTING_SESSION_VALUE)
                    ) {
                result = true;
            }
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
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
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_PREFERENCES_LOGIN, userName);
            editor.putString(APP_PREFERENCES_USERID, userId);
            editor.putString(APP_PREFERENCES_PASSWORD, userPassword);
            editor.putString(APP_PREFERENCES_SESSION, userSession);
            editor.apply();
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
    }

    public void exit() {
        try {
            if (context != null) {
                LogouterTask logouterTask = new LogouterTask();
                logouterTask.execute(context);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
    }

    public void showExitGood(){
        try {
            id = null;
            name = null;
            password = null;
            session = DEFAULT_NOT_EXISTING_SESSION_VALUE;
            savePrefs(null, null, null, null);
            if (context != null) {
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                provider.dataExchanger.clearStorage();
                provider.showExitGood();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
    }

    public void showExitBad(){
        try {
            if (context != null) {
                ActiveActivityProvider provider = (ActiveActivityProvider) context;
                provider.showExitBad();
            }
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
    }

    protected String getSession(){
        return session;
    }

    protected void setSession(String newSession){
        if(newSession != null) {
            session = newSession;
        } else {
            session = DEFAULT_NOT_EXISTING_SESSION_VALUE;
        }
    }

    public String startSession(String newLogin, String newPassword){                                                                       //should be used not from UI Thread
        try {
            StringBuilder result = null;
            String tempResult = doSMTHWithSession(DEFAULT_NOT_EXISTING_SESSION_VALUE, ACTION_LOGIN, newLogin, newPassword);
            String prefixString = null;
            String postfixString = null;
            if(tempResult != null && tempResult.length() > 2) {
                prefixString = tempResult.substring(0, 3);
                postfixString = tempResult.substring(3);
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
                } else {
                    return prefixString;
                }
            } else {
                return  null;
            }
        } catch (Exception e){
            return  "500";
        }
    }

    public String endSession(){                                                                         //should be used not from UI Thread
        String result = null;
        try {
            result = doSMTHWithSession(session, ACTION_LOGOUT, login, password);
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
        return  result;
    }

    public int checkSession(){                                                                      //should be used not from UI Thread
        int result = 0;
        try {
            String tempResult = doSMTHWithSession(session, ACTION_CHECK, login, password);
            if (tempResult != null && tempResult.length() > 2) {
                if (tempResult.substring(0, 3).equals("200")) {
                    result = 1;
                } else if (tempResult.substring(0, 3).equals("500")) {
                    result = 5;
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
        }
        return result;
    }

    private String doSMTHWithSession(String session_id, String action, String incomingLogin, String incomingPassword){                                 //should be used not from UI Thread
        String response = null;
        if (token != null && incomingPassword != null && incomingLogin != null && action != null) {
            if((session_id != null && !session_id.equals(DEFAULT_NOT_EXISTING_SESSION_VALUE)) || action.equals(ACTION_LOGIN)){
                try {
                    URL url;
                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                    if (!action.equals(ACTION_CHECK)) {
                        url = new URL(context.getString(R.string.session_page));
                        postDataParams.put("uname", incomingLogin);
                        postDataParams.put("upassword", incomingPassword);
                    } else {
                        url = new URL(context.getString(R.string.controller_page));
                        JSONArray object = new JSONArray();
                        String jsonString = object.toString();
                        postDataParams.put("data_json", jsonString);
                        postDataParams.put("third", "rrrr");
                        postDataParams.put("uname", "hhhh");
                        postDataParams.put("upassword", "nnnn");
                    }
                    postDataParams.put("session_id", session_id);
                    postDataParams.put("action", action);
                    postDataParams.put("token", token);
                    postDataParams.put("version", CLIENT_VERSION);
                    postDataParams.put("client_type", CLIENT_TYPE);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
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
                } catch (Exception e) {
                    e.printStackTrace();
                    if(e instanceof SocketTimeoutException) {
                        response = "500";
                    }
                }
            }
        } else if(token == null){
            response = "600";
        }
        return response;
    }

    public String registration(String newLogin, String newPassword){
        try {
            StringBuilder loginResult = null;
            String tempResult;
            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            if (token != null && newLogin != null && newPassword != null) {
                tempResult = webCall.callServer(newLogin, newPassword, DEFAULT_NOT_EXISTING_SESSION_VALUE, "registration", jsonString, UserSessionData.this);
                if (tempResult != null && tempResult.length() > 2) {
                    String prefixString = tempResult.substring(0, 3);
                    String postfixString = tempResult.substring(3);
                    loginResult = new StringBuilder(prefixString);
                    if (prefixString.equals("200")) {
                        String newSessionId = webCall.getStringFromJsonString(postfixString, "session_id");
                        setSession(newSessionId);
                        String newId = webCall.getStringFromJsonString(postfixString, "id");
                        checkUserData(newId, newLogin, newPassword, newSessionId);
                        loginResult.append(newId);
                    }
                }
            }
            if (loginResult != null) {
                return loginResult.toString();
            } else {
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        try {
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
        } catch (Exception e){
            Log.d("WhoBuys", "USD");
            return "";
        }
    }
}
