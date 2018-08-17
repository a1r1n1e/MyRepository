package whobuys.vovch.vovch.whobuys.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_layer.DataBaseTask2;
import whobuys.vovch.vovch.whobuys.data_layer.DataExchanger;
import whobuys.vovch.vovch.whobuys.data_layer.WebCall;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.DBUpdateOneGroupOnUITask;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.ListInformersCreatorTask;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class GroupsUpdaterTask implements Runnable {
    private ActiveActivityProvider provider;

    public GroupsUpdaterTask(ActiveActivityProvider provider){
        this.provider = provider;
    }

    @Override
    public void run() {
        try {
            UserGroup[] groups;
            DataBaseTask2 dataBaseTask2 = new DataBaseTask2(provider);
            groups = dataBaseTask2.getGroups();

            ListInformersCreatorTask runnable = new ListInformersCreatorTask(groups, provider, false);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(runnable);

            UserGroup[] result = null;

            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();

            String resultJsonString = webCall.callServer( provider.userSessionData.getId(), DataExchanger.BLANK_WEBCALL_FIELD,
                                                                    DataExchanger.BLANK_WEBCALL_FIELD, "update_groups",
                                                                    jsonString, provider.userSessionData);

            if(resultJsonString != null && resultJsonString.length() > 2){
                if(resultJsonString.substring(0, 3).equals("200")) {
                    groups = webCall.getGroupsFromJsonString(resultJsonString.substring(3));
                    result = dataBaseTask2.resetGroups(groups);
                } else if(resultJsonString.substring(0, 3).equals("502")) {
                    provider.userSessionData.setNotLoggedIn();
                } else {
                    result = groups;
                }
            }

            ListInformersCreatorTask task = new ListInformersCreatorTask(result, provider, true);
            //Handler handler = new Handler(Looper.getMainLooper());
            handler.post(task);

        } catch (Exception e){
            Log.d("WhoBuys", "GroupsUpdaterTask");
        }
    }
}
