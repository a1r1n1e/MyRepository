package whobuys.vovch.vovch.whobuys.data_types;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vovch on 23.12.2017.
 */

public class Item /*implements Parcelable*/ {
    private int id;
    private String name;
    private String comment;
    private SList list;
    private boolean state;
    private LinearLayout layout;
    private LinearLayout verticalLayout;
    private ItemButton button;
    private String owner;
    private String ownerName;
    private TextView ownerTextView;
    public Item(int newId, String newName, String newComment, boolean newState){
        id = newId;
        name = newName;
        comment = newComment;
        state = newState;
        owner = null;
        ownerName = null;
    }
    public Item(String newName, String newComment, boolean newState){
        id = 0;
        name = newName;
        comment = newComment;
        state = newState;
        owner = null;
        ownerName = null;
    }
    public Item(String newName, String newComment) {
        id = 0;
        state = true;
        name = newName;
        comment = newComment;
        owner = null;
        ownerName = null;
    }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public void clear(){
        layout = null;
        button = null;
    }
    public int getId(){
        return id;
    }
    public void setId(int newId){
        id = newId;
    }
    public boolean getState(){
        return  state;
    }
    public void setState(boolean newState){
        state = newState;
    }
    public String getName(){
        return name;
    }
    public void setName(String newName){
        name = newName;
    }
    public String getComment(){
        return comment;
    }
    public void setComment(String newComment){
        comment = newComment;
    }
    public void setList(SList newList){
        list = newList;
    }
    public SList getList(){
        return list;
    }
    public void setLayout(LinearLayout newLayout){
        layout = newLayout;
    }
    public LinearLayout getLayout(){
        return layout;
    }
    public void setButton(ItemButton newButton){
        button = newButton;
    }
    public ItemButton getButton(){
        return button;
    }
    public void setOwner(String newOwner){
        owner = newOwner;
    }
    public String getOwner(){
        return owner;
    }
    public void setOwnerName(String newOwnerName){
        ownerName = newOwnerName;
    }
    public String getOwnerName(){
        return ownerName;
    }
    public void setOwnerTextView(TextView newTextView){
        ownerTextView = newTextView;
    }
    public TextView getOwnerTextView(){
        return ownerTextView;
    }
    public void setVerticalLayout(LinearLayout newLayout){
        verticalLayout = newLayout;
    }
    public LinearLayout getVerticalLayout(){
        return verticalLayout;
    }
    /*protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        comment = in.readString();
        list = (SList) in.readValue(SList.class.getClassLoader());
        state = in.readByte() != 0x00;
        //layout = (LinearLayout) in.readValue(LinearLayout.class.getClassLoader());
        //button = (ItemButton) in.readValue(ItemButton.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        clear();
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(comment);
        dest.writeValue(list);
        dest.writeByte((byte) (state ? 0x01 : 0x00));
        //dest.writeValue(layout);
        //dest.writeValue(button);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };*/
}
