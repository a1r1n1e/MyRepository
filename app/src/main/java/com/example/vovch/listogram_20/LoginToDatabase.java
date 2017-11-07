package com.example.vovch.listogram_20;

import android.content.Context;
import android.os.AsyncTask;
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

/**
 * Created by vovch on 20.08.2017.
 */
 class LoginToDatabase extends AsyncTask<String, Void, String> {
    private Boolean succesess;
    private Context contextFrom;
    protected void setContextFrom(Context ctf){
        contextFrom = ctf;
    }
    @Override
    protected String doInBackground(String... loginPair) {
        String response = "";
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
        if(loginPair[2].equals("login")) {
            if (response.codePointAt(0) != 'd' && !response.equals("fill fields")) {                            //хз что происходит
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
        WithLoginActivity InvokerActivity = (WithLoginActivity) contextFrom;
            if (succesess) {
                InvokerActivity.getFirstLoginAttemptTask().onGoodResult(result);
            } else {
                InvokerActivity.getFirstLoginAttemptTask().onBedResult(result);
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
