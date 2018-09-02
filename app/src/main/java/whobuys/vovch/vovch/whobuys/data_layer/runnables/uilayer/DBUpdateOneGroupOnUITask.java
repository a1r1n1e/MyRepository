package whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;
public class DBUpdateOneGroupOnUITask implements Runnable {
    private UserGroup result;
    private ActiveActivityProvider provider;
    private static final String DEFAULT_NOTIFICATION_MESSAGE = "Something new";

    public DBUpdateOneGroupOnUITask(UserGroup group, ActiveActivityProvider provider){
        this.result = group;
        this.provider = provider;
    }

    @Override
    public void run() {
        if(result != null){
            provider.showGroupChangeOutside(result);

            NotificationsTask notificationsTask = new NotificationsTask(provider, DEFAULT_NOTIFICATION_MESSAGE, false);
            provider.executor.execute(notificationsTask);

        }
    }
}