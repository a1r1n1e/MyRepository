package whobuys.vovch.vovch.whobuys.data_layer.async_tasks;

import android.os.AsyncTask;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;

/**
 * Created by vovch on 09.01.2018.
 */

public class RegistrationTask extends AsyncTask<Object, Void, String> {
    private ActiveActivityProvider activeActivityProvider;
    private String userLogin;
    private String userPassword;

    @Override
    public String doInBackground(Object... loginPair) {
        String result;
        userLogin = (String) loginPair[0];
        userPassword = (String) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.userSessionData.registration(userLogin, userPassword);
        return result;
    }

    @Override
    public void onPostExecute(String result) {
        if ( result != null && result.substring(0, 3).equals("200")) {
            activeActivityProvider.goodRegistrationTry(result.substring(3));
        } else {
            if ( result != null && result.substring(0, 3).equals("403")) {
                result = "Username Is Already Used";
            } else {
                result = "Something Went Wrong";
            }
            activeActivityProvider.badRegistrationTry(result);
        }
        activeActivityProvider = null;
    }
}
