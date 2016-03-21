package gagan.com.communities.activites;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.ChatMsgAdapter;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class ChatActivity extends BaseActivityG {


    EditText edComment;
    RecyclerView recyclerList;

    ChatMsgAdapter chatMsgAdapter;
    private List<MsgDataModel> listData;


    ProgressBar progressBar;

    String otherUserID = "", profilePicOther = "";

    UserDataModel userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent intnt = getIntent();

        otherUserID = intnt.getStringExtra("id");
        profilePicOther = intnt.getStringExtra("pic");


        userData = sharedPrefHelper.read(ChatActivity.this);

        findViewByID();
        hitWebserviceG();

    }

    @Override
    void findViewByID() {

        listData = new ArrayList<>();

        edComment = (EditText) findViewById(R.id.edComment);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);

        chatMsgAdapter = new ChatMsgAdapter(ChatActivity.this, listData);


        recyclerList.setAdapter(chatMsgAdapter);


    }

    @Override
    void hitWebserviceG() {
        try {

            progressBar.setVisibility(View.VISIBLE);


            JSONObject data = new JSONObject();
            data.put("sender_userid", sharedPrefHelper.getUserId());
            data.put("recipient_userid", otherUserID);

            new SuperWebServiceG(
                    GlobalConstants.URL + "usermessage", data, new CallBackWebService() {
                @Override
                public void webOnFinish(String output) {

                    progressBar.setVisibility(View.GONE);
                    try {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").equals("200")) {


                            JSONArray jsonarrayData = jsonMainResult.getJSONArray("message");

                            for (int g = 0; g < jsonarrayData.length(); g++) {
                                JSONObject jobj = jsonarrayData.optJSONObject(g);

                                String id = jobj.optString("id");
                                String sender_userid = jobj.optString("sender_userid");
                                String recipient_userid = jobj.optString("recipient_userid");
                                String message = jobj.optString("message");
                                String created_at = jobj.optString("created_at");
                                String username = jobj.optString("username");


                                String profile_pic = sender_userid.equals(userData.getuId()) ? userData.getProfile_pic() : otherUserID;

                                listData.add(new MsgDataModel(id, sender_userid, recipient_userid, message, created_at, username, profile_pic));

                            }


                            Collections.reverse(listData);

                            chatMsgAdapter.notifyDataSetChanged();

                        }
                        else {
//                Utills.showToast("No users available", getActivity(), true);
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ).execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void sendMsg(View view)
    {
    }
}
