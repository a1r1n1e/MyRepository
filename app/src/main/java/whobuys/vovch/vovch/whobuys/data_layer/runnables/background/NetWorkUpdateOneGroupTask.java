package whobuys.vovch.vovch.whobuys.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_layer.DataExchanger;
import whobuys.vovch.vovch.whobuys.data_layer.WebCall;
import whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer.NetWorkUpdateOneGroupDETask;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class NetWorkUpdateOneGroupTask implements Runnable {
    private String groupId;
    public ActiveActivityProvider provider;

    public NetWorkUpdateOneGroupTask(String groupId, ActiveActivityProvider provider){
        this.groupId = groupId;
        this.provider = provider;
    }

    public void setProvider(ActiveActivityProvider provider) {
        this.provider = provider;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void run() {
        try {
            UserGroup result = null;

            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            String resultJsonString = webCall.callServer(groupId, DataExchanger.BLANK_WEBCALL_FIELD,
                    DataExchanger.BLANK_WEBCALL_FIELD, "check_group_updates",
                    jsonString, provider.userSessionData);
            if (resultJsonString != null && resultJsonString.length() > 2) {
                if (resultJsonString.substring(0, 3).equals("200")) {
                    result = WebCall.getGroupFromJSONString(resultJsonString.substring(3));

                    if(result != null) {
                        NetWorkUpdateOneGroupDETask runnable = new NetWorkUpdateOneGroupDETask(result, groupId, provider);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(runnable);
                    }
                }
            }

        } catch (Exception e){
            Log.d("WhoBuys", "NetWorkUpdateOneGroupTask");
        }
    }
}