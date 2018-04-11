package whobuys.vovch.vovch.whobuys.data_types;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by vovch on 18.01.2018.
 */

public class UserButton extends AppCompatButton {
    private AddingUser user;

    public UserButton(Context context) {
        super(context);
        user = null;
    }

    public UserButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        user = null;
    }

    public UserButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        user = null;
    }
    public void setUser(AddingUser newUser){
        user = newUser;
    }
    public AddingUser getUser(){
        return user;
    }
}
