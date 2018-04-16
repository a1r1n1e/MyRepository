package whobuys.vovch.vovch.whobuys.data_types;

import android.widget.Button;

/**
 * Created by vovch on 07.01.2018.
 */

public class ListInformer {
    private String id;
    private String name;
    private String active;
    private UserGroup group;
    private Button button;
    public ListInformer(String groupId, String groupName, String newActive){
        id = groupId;
        name = groupName;
        active = newActive;
        button = null;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public boolean isActive(){
        return active != null && active.equals("t");
    }
    public void setGroup(UserGroup newGroup){
        group = newGroup;
    }
    public UserGroup getGroup(){
        return group;
    }
    public void setButton(Button newButoon){
        button = newButoon;
    }
    public Button getButton(){
        return button;
    }
}
