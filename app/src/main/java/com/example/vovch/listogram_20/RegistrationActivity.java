package com.example.vovch.listogram_20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistrationActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "autentification";
    public static final String APP_PREFERENCES_LOGIN = "login";
    public static final String APP_PREFERENCES_PASSWORD = "password";
    public static final String APP_PREFERENCES_USERID = "userid";
    public static final String APP_PREFERENCES_TOKEN= "token";
    private SharedPreferences loginPasswordPair;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loginPasswordPair = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        token = loginPasswordPair.getString(APP_PREFERENCES_TOKEN, null);
        View.OnClickListener NewListenner3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id == R.id.button3){
                    EditText editText3 = (EditText)findViewById(R.id.edittext3);
                    EditText editText4 = (EditText)findViewById(R.id.edittext4);
                    String uNameRegister = editText3.getText().toString();
                    String uPasswordRegister = editText4.getText().toString();

                    RegistrationTask rTask = new RegistrationTask();
                    rTask.execute(uNameRegister, uPasswordRegister, token);
                }
            }
        };
        Button Btn3 = (Button)findViewById(R.id.button3);
        Btn3.setOnClickListener(NewListenner3);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    private void finisher(){
        this.finish();
    }
    private class RegistrationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... loginPair) {
            String response = "";
            try {
                URL url = new URL("http://217.10.35.250/java_registration_2.php");
                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put("newuser", loginPair[0]);
                postDataParams.put("newpassword", loginPair[1]);
                postDataParams.put("token", loginPair[2]);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                    if(response.equals("Registration complete")){
                        loginPasswordPair = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginPasswordPair.edit();
                        editor.putString(APP_PREFERENCES_LOGIN, loginPair[0]);
                        editor.putString(APP_PREFERENCES_PASSWORD, loginPair[1]);
                        editor.putString(APP_PREFERENCES_USERID, response);
                        editor.apply();
                    }
                }

                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            if(result.charAt(0)=='%'){
                Intent intent = new Intent(RegistrationActivity.this, ActiveListsActivity.class);
                intent.putExtra("userId", result.substring(1).toString());
                startActivity(intent);
                finisher();
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
}
