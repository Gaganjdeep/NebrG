package gagan.com.communities.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.HomeFragment;
import gagan.com.communities.adapters.ChatMsgAdapter;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.EndlessRecyclerOnScrollListener;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class ChatActivity extends BaseActivityG
{


    EditText     edComment;
    RecyclerView recyclerList;

    ChatMsgAdapter chatMsgAdapter;
    private List<MsgDataModel> listData;


    ProgressBar progressBar;

    String otherUserID = "", profilePicOther = "", otherUserName = "";

    UserDataModel userData;
    Intent        intnt;


    LinearLayout layoutAddRecipients;
    SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstants.SEVER_FORMAT);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        settingActionBar();

        findViewByID();

        intnt = getIntent();

        otherUserID = intnt.getStringExtra("id");

        if (otherUserID.equals("0"))
        {
            needUserSelection();
        }
        else
        {
            startChat();
        }


    }


    private void needUserSelection()
    {
        edComment.setEnabled(false);
        layoutAddRecipients.setVisibility(View.VISIBLE);

    }


    private void startChat()
    {
        edComment.setEnabled(true);
        layoutAddRecipients.setVisibility(View.GONE);

        profilePicOther = intnt.getStringExtra("pic");
        otherUserName = intnt.getStringExtra("name");
        userData = SharedPrefHelper.read(ChatActivity.this);
        hitWebserviceG();
    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }


    @Override
    void findViewByID()
    {

        listData = new ArrayList<>();


        layoutAddRecipients = (LinearLayout) findViewById(R.id.layoutAddRecipients);

        edComment = (EditText) findViewById(R.id.edComment);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerList.setLayoutManager(linearLayoutManager);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);

        chatMsgAdapter = new ChatMsgAdapter(ChatActivity.this, listData);


        recyclerList.setAdapter(chatMsgAdapter);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                if (listData.size() > 14)
                {
                    hitWebserviceG();
                }
            }
        };
        recyclerList.setOnScrollListener(endlessRecyclerOnScrollListener);
    }

    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    int start   = 0;
    int limit   = 15;
    int maxSize = 15;

    @Override
    void hitWebserviceG()
    {
        try
        {

            progressBar.setVisibility(View.VISIBLE);


            JSONObject data = new JSONObject();
            data.put("sender_userid", sharedPrefHelper.getUserId());
            data.put("recipient_userid", otherUserID);
            data.put("start", start);
            data.put("limit", limit);

            new SuperWebServiceG(
                    GlobalConstants.URL + "usermessage", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    progressBar.setVisibility(View.GONE);
                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").equals("200"))
                        {


                            if (start == 0)
                            {
                                listData.clear();
                            }


                            JSONArray jsonarrayData = jsonMainResult.getJSONArray("message");

                            if (jsonarrayData.length() > 0)
                            {
                                start = limit;
                                limit = maxSize + limit;
                            }

                            for (int g = 0; g < jsonarrayData.length(); g++)
                            {
                                JSONObject jobj = jsonarrayData.optJSONObject(g);

                                String id               = jobj.optString("id");
                                String sender_userid    = jobj.optString("sender_userid");
                                String recipient_userid = jobj.optString("recipient_userid");
                                String message          = jobj.optString("message");
                                String created_at       = jobj.optString("created_at");
                                String username         = jobj.optString("username");


                                String       profile_pic  = sender_userid.equals(userData.getuId()) ? userData.getProfile_pic() : profilePicOther;
                                MsgDataModel msgDataModel = new MsgDataModel(false, id, sender_userid, recipient_userid, message, created_at, username, profile_pic);
                                if (!listData.contains(msgDataModel))
                                {
                                    listData.add(msgDataModel);
                                }

                            }


//                            Collections.reverse(listData);


                            chatMsgAdapter.notifyDataSetChanged();

                        }
                        else
                        {
//                Utills.showToast("No users available", getActivity(), true);
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            ).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void sendMsg(final View view)
    {
        try
        {


            if (edComment.getText().toString().trim().isEmpty())
            {
                return;
            }


//            showProgressDialog();
            view.setEnabled(false);

            JSONObject data = new JSONObject();
            data.put("message", edComment.getText().toString());
            data.put("sender_userid", sharedPrefHelper.getUserId());
            data.put("recipient_userid", otherUserID);


            new SuperWebServiceG(GlobalConstants.URL + "sendMessage", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

//                    cancelDialog();

                    try
                    {
                        view.setEnabled(true);

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            String id               = jsonMainResult.optString("message_id");
                            String sender_userid    = sharedPrefHelper.getUserId();
                            String recipient_userid = otherUserID;
                            String message          = edComment.getText().toString();

                            String created_at = sdf.format(new Date(System.currentTimeMillis()));


                            String username = userData.getName();


                            String profile_pic = userData.getProfile_pic();

                            listData.add(0, new MsgDataModel(true, id, sender_userid, recipient_userid, message, created_at, username, profile_pic));


                            edComment.setText("");

                            endlessRecyclerOnScrollListener.setPreviousTotal();
                            chatMsgAdapter.notifyDataSetChanged();


                        }
                        else
                        {
//                Utills.showToast("No users available", getActivity(), true);
                        }


                        Intent intent = new Intent(GlobalConstants.UPDATE_MSG_FRAGMENT);
                        sendBroadcast(intent);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void addRecipients(View view)
    {
        Intent intnt = new Intent(ChatActivity.this, ShowFragmentActivity
                .class);
        intnt.putExtra("title", "Select User");
        startActivity(intnt);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        if (!mIsReceiverRegistered)
        {
            if (mReceiver == null)
                mReceiver = new UpdateMessageListReceiver();
            registerReceiver(mReceiver, new IntentFilter(GlobalConstants.UPDATE_CHAT));
            mIsReceiverRegistered = true;
        }
    }


    @Override
    public void onDestroy()
    {
        if (mIsReceiverRegistered)
        {
            unregisterReceiver(mReceiver);
            mReceiver = null;
            mIsReceiverRegistered = false;
        }
        super.onDestroy();
    }


    UpdateMessageListReceiver mReceiver;
    private boolean mIsReceiverRegistered = false;


    private class UpdateMessageListReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {


//            if (intent.getStringExtra("message_id").equals(otherUserID))
//            {
//                String id               = intent.getString("message_id");
//                String sender_userid    = otherUserID;
//                String recipient_userid = sharedPrefHelper.getUserId();
//                String message          = intent.getString("message_id");
//
//                String created_at = sdf.format(new Date(System.currentTimeMillis()));
//
//
//                String username = otherUserName;
//
//
//                String profile_pic = profilePicOther;
//
//                listData.add(0, new MsgDataModel(true, id, sender_userid, recipient_userid, message, created_at, username, profile_pic));
//            }

            //         hitWebserviceG();
        }
    }


}
