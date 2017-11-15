package com.example.vovch.listogram_20;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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

import static java.security.AccessController.getContext;

/**
 * Created by vovch on 20.08.2017.
 */
 class LoginToDatabase extends AsyncTask<String, Void, String> {
    private Boolean succesess;
    private Context applicationContext;
    private int whichActivity;
    private String firstString;
    private String secondString;
    private int taskType;
    private ActiveActivityProvider provider;


    protected void setApplicationContext(Context ctf){
        applicationContext = ctf;
    }
    @Override
    protected String doInBackground(String... loginPair) {
        String response = "";
        whichActivity = Integer.parseInt(loginPair[4]);
        firstString = loginPair[0];
        secondString = loginPair[1];
        taskType = Integer.parseInt(loginPair[5]);

        try {
            URL url = new URL("http://217.10.35.250/java_listenner_2.php");
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("uname", loginPair[0]);
            postDataParams.put("upassword", loginPair[1]);
            postDataParams.put("third", loginPair[2]);
            postDataParams.put("action", loginPair[3]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);                                                 // Enable POST stream
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            InputStream is = null;
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }                                                       //придумать как уменьшить трафик
        if(loginPair[3].equals("login")) {
            if (response.codePointAt(0) != 'd') {
                succesess = true;
            }
            else{
                succesess = false;
            }

        }
        else if(loginPair[2].equals("checkuser")){
            if(response.equals("No Such User(")){
                succesess = false;
            }
            else{
                succesess = true;
            }
        }
        else{
            if (!response.equals("")) {
                succesess = true;
            } else {
                succesess = false;
            }
        }
        return response;
    }
    @Override
    protected void onPostExecute(String result) {
        /*WithLoginActivity InvokerActivity = (WithLoginActivity) applicationContext;
            if (succesess) {
                InvokerActivity.getFirstLoginAttemptTask().onGoodResult(result);
            } else {
                InvokerActivity.getFirstLoginAttemptTask().onBedResult(result);
            }*/

        provider = (ActiveActivityProvider) applicationContext;
        switch(whichActivity){
            case 1:
                if(provider.getActiveActivity() != null) {
                    MainActivity mainContext = (MainActivity) provider.getActiveActivity();
                    if (succesess) {
                        mainContext.showGood(result, firstString, secondString);
                    } else {
                        mainContext.showBad(result);
                    }
                }
                break;
            case 2:
                if(provider.getActiveActivity() != null) {
                    ActiveListsActivity activeContext = (ActiveListsActivity) provider.getActiveActivity();
                    if (succesess) {
                        activeContext.showGood(result);
                    } else {
                        activeContext.showBad();
                    }
                }
                break;
            case 3:
                if(provider.getActiveActivity() != null) {
                    Group2Activity groupContext = (Group2Activity) provider.getActiveActivity();
                    if (succesess) {
                        if (taskType == 0) {
                            groupContext.showGood(result);
                        } else if (taskType == 1) {
                            groupContext.showSecondGood(result);
                        } else {
                            groupContext.showThirdGood(result);
                        }
                    } else {
                        if (taskType == 0) {
                            groupContext.showBad(result);
                        } else if (taskType == 1) {
                            groupContext.showSecondBad(result);
                        } else {
                            groupContext.showThirdBad(result);
                        }
                    }
                }
                break;
            case 4:
                if(provider.getActiveActivity() != null) {
                    GroupList2Activity groupListContext = (GroupList2Activity) provider.getActiveActivity();
                    if (succesess) {
                        groupListContext.showGood(result);
                    } else {
                        groupListContext.showBad();
                    }
                }
                break;
            case 5:
                if(provider.getActiveActivity()!= null) {
                    NewGroup newGroupContext = (NewGroup) provider.getActiveActivity();
                    if (succesess) {
                        if (taskType == 0) {
                            newGroupContext.showUserCheckGood(result);
                        } else {
                            newGroupContext.showGood(result);
                        }
                    } else {
                        if (taskType == 0) {
                            newGroupContext.showUserCheckBad(result);
                        } else {
                            newGroupContext.showBad(result);
                        }
                    }
                }
                break;
            case 6:
                if(provider.getActiveActivity() != null) {
                    CreateListogramActivity createContext = (CreateListogramActivity) provider.getActiveActivity();
                    if (succesess) {
                        createContext.showGood();
                    } else {
                        createContext.showBad(result);
                    }
                }
                break;
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
