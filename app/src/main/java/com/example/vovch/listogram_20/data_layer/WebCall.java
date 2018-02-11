package com.example.vovch.listogram_20.data_layer;

import com.example.vovch.listogram_20.data_types.ListInformer;
import com.example.vovch.listogram_20.data_types.AddingUser;
import com.example.vovch.listogram_20.data_types.Item;
import com.example.vovch.listogram_20.data_types.SList;
import com.example.vovch.listogram_20.data_types.UserGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vovch on 03.01.2018.
 */

public class WebCall {

    public WebCall() {
    }

    public String callServer(String... loginPair) {
        String response = "";

        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://217.10.35.250/java_listenner_2.php");
            conn = (HttpURLConnection) url.openConnection();
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("uname", loginPair[0]);
            postDataParams.put("upassword", loginPair[1]);
            postDataParams.put("third", loginPair[2]);
            postDataParams.put("action", loginPair[3]);
            postDataParams.put("itemmarkgroup", loginPair[6]);

            conn.setDoOutput(true);                                                 // Enable POST stream
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            InputStream is = null;
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                response += String.valueOf(responseCode);
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response += String.valueOf(responseCode);
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            if (loginPair[3].equals("itemmark")) {
                StringBuilder tempString = new StringBuilder("");
                tempString.append("400");
                tempString.append(loginPair[1]);
                response = tempString.toString();
            } else if (loginPair[3].equals("login")) {
                response = "400No Internet Acesess";
            }
        } catch (IOException e) {
            if (loginPair[3].equals("itemmark")) {
                StringBuilder tempString = new StringBuilder("");
                tempString.append("400");
                tempString.append(loginPair[1]);
                response = tempString.toString();
            } else if (loginPair[3].equals("login")) {
                response = "400No Internet Acesess";
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response;
    }

    public UserGroup[] getGroupsFromJsonString(String result) {
        UserGroup[] groups = null;
        try {
            JSONArray groupsArray = new JSONArray(result);
            int length = groupsArray.length();
            groups = new UserGroup[length];
            UserGroup tempGroup;
            JSONObject tempJsonObject;
            JSONArray tempMembersArray;
            String name;
            String id;
            AddingUser[] members;
            String owner;
            for (int i = 0; i < length; i++) {
                tempJsonObject = groupsArray.getJSONObject(i);
                name = tempJsonObject.getString("group_name");
                id = tempJsonObject.getString("group_id");
                tempMembersArray = tempJsonObject.getJSONArray("group_members");
                int membersNumber = tempMembersArray.length();
                members = new AddingUser[membersNumber];
                JSONObject tempMember;
                for (int j = 0; j < membersNumber; j++) {
                    tempMember = tempMembersArray.getJSONObject(j);
                    members[j] = new AddingUser();
                    members[j].setData(tempMember.getString("name"), tempMember.getString("id"));
                }
                owner = tempJsonObject.getString("group_owner");
                tempGroup = new UserGroup(name, id, members);
                tempGroup.setOwner(owner);
                groups[i] = tempGroup;
            }
        } catch (JSONException e) {
            //TODO
        }
        return groups;
    }

    protected String getStringFromJsonString(String jsonString, String value) {
        String result = null;
        try {
            JSONObject dataHolder = new JSONObject(jsonString);
            result = dataHolder.getString(value);
        } catch (JSONException e) {                                                                         //TODO

        }
        return result;
    }

    public SList[] getGroupListsFromJsonString(String result) {
        SList[] lists = null;
        try {
            JSONArray fromJsonLists = new JSONArray(result);
            int length = fromJsonLists.length();
            SList tempList;
            Item[] items;
            Item tempItem;
            JSONObject tempListObject;
            lists = new SList[length];
            for (int i = 0; i < length; i++) {
                try {
                    tempListObject = fromJsonLists.getJSONObject(i);
                    JSONArray encodedItems = tempListObject.getJSONArray("items_array");
                    int itemsLength = encodedItems.length();
                    items = new Item[itemsLength];
                    for (int j = 0; j < itemsLength; j++) {
                        String itemName = encodedItems.getJSONObject(j).getString("item_name");
                        String itemComment = encodedItems.getJSONObject(j).getString("item_comment");
                        int itemId = encodedItems.getJSONObject(j).getInt("item_id");
                        String itemStateString = encodedItems.getJSONObject(j).getString("item_state");
                        boolean itemState = false;
                        if (itemStateString.equals("t")) {
                            itemState = true;
                        }
                        tempItem = new Item(itemId, itemName, itemComment, itemState);
                        items[j] = tempItem;
                    }
                    int listId = tempListObject.getInt("list_id");
                    String listStateString = tempListObject.getString("list_state");
                    boolean listState = false;
                    if (listStateString.equals("t")) {
                        listState = true;
                    }
                    int listOwner = tempListObject.getInt("list_owner");
                    String listOwnerName = tempListObject.getString("list_owner_name");
                    int listGroupId = tempListObject.getInt("list_group");
                    String creation_time = tempListObject.getString("list_creation_time");
                    tempList = new SList(items, listId, listGroupId, false, listState, listOwner, listOwnerName, creation_time);
                    for (int k = 0; k < items.length; k++) {
                        items[k].setList(tempList);
                    }
                    lists[i] = tempList;
                } catch (JSONException e) {                                                                 //TODO
                    String res = "123";
                }
            }
        } catch (JSONException e) {
            // TODO
        }
        return lists;
    }

    public ListInformer[] getListInformersFromJsonString(String result) {
        ListInformer[] informers = null;
        try {
            JSONArray informersArray = new JSONArray(result);
            JSONObject tempObject = null;
            ListInformer tempInformer = null;
            UserGroup tempGroup;
            int i = 0;
            int length = informersArray.length();
            informers = new ListInformer[length];
            String groupName;
            String groupId;
            AddingUser[] tempMembers;
            JSONArray tempMembersArray;
            String owner;
            int tempMembersLength;
            for (i = 0; i < length; i++) {
                tempObject = informersArray.getJSONObject(i);
                groupId = tempObject.getString("group_id");
                groupName = tempObject.getString("group_name");
                owner = tempObject.getString("group_owner");
                tempMembersArray = tempObject.getJSONArray("group_members");
                tempMembersLength = tempMembersArray.length();
                tempMembers = new AddingUser[tempMembersLength];
                for (int j = 0; j < tempMembersLength; j++) {
                    tempObject = tempMembersArray.getJSONObject(j);
                    tempMembers[j] = new AddingUser();
                    tempMembers[j].setData(tempObject.getString("name"), tempObject.getString("id"));
                }
                tempInformer = new ListInformer(groupId, groupName);
                tempGroup = new UserGroup(groupName, groupId, tempMembers);
                tempGroup.setOwner(owner);
                tempInformer.setGroup(tempGroup);
                informers[i] = tempInformer;
            }
        } catch (JSONException e) {                                                                      //TODO

        }
        return informers;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
