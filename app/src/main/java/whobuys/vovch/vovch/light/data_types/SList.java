package whobuys.vovch.vovch.light.data_types;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vovch on 23.12.2017.
 */

public class SList /*implements Parcelable*/ {
    private int id;
    private boolean state;
    private UserGroup group;
    private boolean type;
    private Item[] items;
    private final String creationTime;
    private String humanCreationTime;
    private int owner;
    private String ownerName;
    private String storeName;
    private String storeTime;

    private CardView cardView;
    private ImageButton disButton;
    private ImageButton resendButton;
    private ImageButton redactButton;

    public SList(Item[] newItems){
        items = newItems;
        id = 0;
        group = null;
        owner = -1;
        type = false;
        state = true;
        storeName = "";                             //should always be filled, doing this just to be sure
        storeTime = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        creationTime = format.format(Calendar.getInstance().getTime());
        setHumanCreationTime();
    }
    public SList(Item[] newItems, int newId, UserGroup newGroup, boolean newType, boolean newState, int newOwner, String newOwnerName, String newCreationTime){
        setItems(newItems);
        setId(newId);
        setGroup(newGroup);
        setType(newType);
        setState(newState);
        setOwner(newOwner);
        setOwnerName(newOwnerName);
        setStoreName("");                           //should always be filled, doing this just to be sure
        setStoreTime("");
        creationTime = newCreationTime;
        setHumanCreationTime();
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public String getStoreName(){
        return storeName;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime = storeTime;
    }

    public String getStoreTime() {
        return storeTime;
    }

    private void setHumanCreationTime(){
        if (creationTime != null) {
            if(type) {
                try {
                    SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateFormatParser.parse(creationTime);
                    SimpleDateFormat dateFormatFormater = new SimpleDateFormat("dd-MM-yy HH:mm");
                    humanCreationTime = dateFormatFormater.format(date);
                } catch (ParseException e) {
                    Log.d("WhoBuys", "SLIST");
                }
            }
            else {
                try {
                    SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateFormatParser.parse(creationTime);
                    SimpleDateFormat dateFormatFormater = new SimpleDateFormat("dd-MM-yy HH:mm");
                    humanCreationTime = dateFormatFormater.format(date);
                } catch (ParseException e) {
                    Log.d("WhoBuys", "SLIST");
                }
            }
        }
    }
    public void clear(){
        cardView = null;
    }

    public ImageButton getRedactButton(){
        return redactButton;
    }
    public void setRedactButton(ImageButton newRedactButton){
        redactButton = newRedactButton;
    }
    public void setDisButton(ImageButton newDisButton){
        disButton = newDisButton;
    }
    public ImageButton getDisButton(){
        return disButton;
    }
    public void setResendButton(ImageButton newResendButton){
        resendButton = newResendButton;
    }
    public ImageButton getResendButton(){
        return resendButton;
    }
    public int getOwner(){
        return owner;
    }
    public void setOwner(int newOwner){
        owner = newOwner;
    }
    public String getOwnerName(){
        return ownerName;
    }
    public void  setOwnerName(String newOwnerName){
        ownerName = newOwnerName;
    }
    public int getId(){
        return id;
    }
    public void setId(int newId){
        id = newId;
    }
    public UserGroup getGroup(){
        return group;
    }
    public void setGroup(UserGroup newGroup){
        group = newGroup;
    }
    public boolean getType(){
        return type;
    }
    public void setType(boolean newType){
        type = newType;
    }
    public boolean getState(){
        return  state;
    }
    public void setState(boolean newState){
        state = newState;
    }
    public Item[] getItems(){
        return items;
    }
    public void setItems(Item[] newItems){
        items = newItems;
    }
    public String getCreationTime(){
        return creationTime;
    }
    public String getHumanCreationTime(){
        return humanCreationTime;
    }

    public void disactivate(){
        setState(false);
    }
    public CardView getCardView(){
        return cardView;
    }
    public void setCardView(CardView newCardView){
        cardView = newCardView;
    }
}