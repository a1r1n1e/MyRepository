package whobuys.vovch.vovch.light.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_layer.DataBaseTask2;
import whobuys.vovch.vovch.light.data_layer.runnables.uilayer.DBUpdateOneGroupOnUITask;
import whobuys.vovch.vovch.light.data_types.UserGroup;

public class DBUpdateOneGroupTask implements Runnable {
    private UserGroup group;
    public ActiveActivityProvider provider;

    public DBUpdateOneGroupTask(UserGroup group, ActiveActivityProvider provider){
        this.provider = provider;
        this.group = group;
    }

    @Override
    public void run() {
        UserGroup result = null;
        DataBaseTask2 dataBaseTask2 = new DataBaseTask2(provider);
        result = dataBaseTask2.addGroup(group);

        DBUpdateOneGroupOnUITask runnable = new DBUpdateOneGroupOnUITask(result, provider);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}
