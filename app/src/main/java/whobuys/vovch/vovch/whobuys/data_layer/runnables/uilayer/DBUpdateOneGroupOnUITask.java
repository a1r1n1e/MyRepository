package whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;
public class DBUpdateOneGroupOnUITask implements Runnable {
    private UserGroup result;
    public ActiveActivityProvider provider;

    public DBUpdateOneGroupOnUITask(UserGroup group, ActiveActivityProvider provider){
        this.result = group;
        this.provider = provider;
    }

    @Override
    public void run() {
        if(result != null){
            provider.showGroupChangeOutside(result);
        }
    }
}