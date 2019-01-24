package whobuys.vovch.vovch.light.data_layer.runnables.uilayer;

import whobuys.vovch.vovch.light.ActiveActivityProvider;
import whobuys.vovch.vovch.light.data_types.Item;
import whobuys.vovch.vovch.light.data_types.UserGroup;

public class SuccessesItemmarkOnlinePublisherRunnable implements Runnable{
    private UserGroup group;
    private  UserGroup result;
    private ActiveActivityProvider provider;
    private Item item;

    public SuccessesItemmarkOnlinePublisherRunnable(UserGroup group, UserGroup result, ActiveActivityProvider provider, Item item){
        this.group = group;
        this.result = result;
        this.provider = provider;
        this.item = item;
    }

    @Override
    public void run() {
        if(result != null){
            if(result.equals(group)){
                provider.showOnlineItemmarkedGoodLight(item);
            } else {
                provider.showOnlineItemmarkedGood(result);
            }
        }
        else{
            provider.showOnlineItemmarkedBad(group);
        }
    }
}