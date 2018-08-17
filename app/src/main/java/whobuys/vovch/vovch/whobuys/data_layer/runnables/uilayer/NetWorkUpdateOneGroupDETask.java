package whobuys.vovch.vovch.whobuys.data_layer.runnables.uilayer;

import whobuys.vovch.vovch.whobuys.ActiveActivityProvider;
import whobuys.vovch.vovch.whobuys.data_types.UserGroup;

public class NetWorkUpdateOneGroupDETask implements Runnable {
    public String groupId;
    public UserGroup group;
    public ActiveActivityProvider provider;

    public NetWorkUpdateOneGroupDETask(UserGroup group, String groupId, ActiveActivityProvider provider){
        this.group = group;
        this.groupId = groupId;
        this.provider = provider;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public void setProvider(ActiveActivityProvider provider) {
        this.provider = provider;
    }

    @Override
    public void run() {
        provider.dataExchanger.updateOneGroupDataNew(groupId, group);
    }
}