package gagan.com.communities.activites.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.ChatActivity;
import gagan.com.communities.activites.ShowFragmentActivity;
import gagan.com.communities.adapters.MessageAdapter;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;


public class MessageFragment extends BaseFragmentG implements View.OnClickListener
{


    FloatingActionButton btnAddChat;

    public MessageFragment()
    {
        // Required empty public constructor
    }

    RecyclerView recyclerList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View v = inflater.inflate(R.layout.recycler_list_layout, container, false);

        btnAddChat = (FloatingActionButton) v.findViewById(R.id.btnAddChat);
        btnAddChat.setVisibility(View.VISIBLE);
        btnAddChat.setOnClickListener(this);


        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (listMsg != null)
        {
            MessageAdapter msgAdapter = new MessageAdapter(getActivity(), listMsg);
            recyclerList.setAdapter(msgAdapter);
        }


        hitWebserviceG();

        return v;
    }


    List<MsgDataModel> listMsg;


    void hitWebserviceG()
    {
        try
        {

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());

            new SuperWebServiceG(GlobalConstants.URL + "allusermessage", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").equals("200"))
                        {

                            JSONArray jsonarrayData = jsonMainResult.getJSONArray("alluserdata");

                            if (listMsg == null)
                            {
                                listMsg = new ArrayList<MsgDataModel>();
                            }
                            else
                            {
                                listMsg.clear();
                            }

                            for (int g = 0; g < jsonarrayData.length(); g++)
                            {


//    "id": "12",
//            "sender_userid": "24",
//            "recipient_userid": "1",
//            "message": "hi",
//            "created_at": "2016-03-20 22:33:21",
//            "updated_date": "2016-03-20 10:03:21",
//            "status": "0",
//            "username": "vimajl",
//            "profile_pic": "http://orasisdata.com/Neiber/images/1454321304431.jpg"


                                JSONObject jobj = jsonarrayData.optJSONObject(g);


                                String id               = jobj.optString("id");
                                String sender_userid    = jobj.optString("sender_userid");
                                String recipient_userid = jobj.optString("recipient_userid");
                                String message          = jobj.optString("message");
                                String created_at       = jobj.optString("created_at");
                                String username         = jobj.optString("username");
                                String profile_pic      = jobj.optString("profile_pic");

                                listMsg.add(new MsgDataModel(id, sender_userid, recipient_userid, message, created_at, username, profile_pic));
                            }

                            Collections.reverse(listMsg);
                            MessageAdapter msgAdapter = new MessageAdapter(getActivity(), listMsg);
                            recyclerList.setAdapter(msgAdapter);

                        }


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


    @Override
    public void onResume()
    {
        super.onResume();
//		mapFragment.onResume();
        if (!mIsReceiverRegistered)
        {
            if (mReceiver == null)
                mReceiver = new UpdateMessageListReceiver();
            getActivity().registerReceiver(mReceiver, new IntentFilter(GlobalConstants.UPDATE_MSG_FRAGMENT));
            mIsReceiverRegistered = true;
        }
    }


//        if (mIsReceiverRegistered)
//        {
//            getActivity().unregisterReceiver(mReceiver);
//            mReceiver = null;
//            mIsReceiverRegistered = false;
//        }


    @Override
    public void onDestroy()
    {
        if (mIsReceiverRegistered)
        {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
            mIsReceiverRegistered = false;
        }
        super.onDestroy();
    }

    // BROADCAST RECEIVER TO UPDATE MSG LIST.
    UpdateMessageListReceiver mReceiver;
    private boolean mIsReceiverRegistered = false;

    @Override
    public void onClick(View v)
    {

        Intent intnt = new Intent(getActivity(), ChatActivity
                .class);
        intnt.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intnt.putExtra("id", "0");
//        intnt.putExtra("pic", data.getProfile_pic());
        getActivity().startActivity(intnt);



    }

    private class UpdateMessageListReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            hitWebserviceG();
        }
    }


    // BROADCAST RECEIVER TO UPDATE MSG LIST.END


}
