package whobuys.vovch.vovch.whobuys.data_types;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;

/**
 * Created by vovch on 07.01.2018.
 */

public class AddingUser /*implements Parcelable*/ {
    private String userName;
    private String userId;
    private CardView cardView;
    public AddingUser(){
        userId = null;
        userName = null;
        cardView = null;
    }
    public void setData(String newName, String newId){
        userId = newId;
        userName = newName;
    }
    public String getUserId(){
        return userId;
    }
    public String getUserName(){
        return userName;
    }
    public CardView getCardView(){
        return  cardView;
    }
    public void setCardView(CardView newCardView){
        cardView = newCardView;
    }

    /*protected AddingUser(Parcel in) {
        userName = in.readString();
        userId = in.readString();
        //button = (UserButton) in.readValue(UserButton.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
        //dest.writeValue(button);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AddingUser> CREATOR = new Parcelable.Creator<AddingUser>() {
        @Override
        public AddingUser createFromParcel(Parcel in) {
            return new AddingUser(in);
        }

        @Override
        public AddingUser[] newArray(int size) {
            return new AddingUser[size];
        }
    };*/
}
