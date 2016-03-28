package gagan.com.communities.activites.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import gagan.com.communities.adapters.MessageAdapter;
import gagan.com.communities.adapters.NotificationAdapter;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.models.NotificationModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;


public class NotificationFragment extends BaseFragmentG
{


    public NotificationFragment()
    {
        // Required empty public constructor
    }

    List<NotificationModel> listMsg;

    RecyclerView recyclerList;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler_list_layout, container, false);

        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (listMsg != null)
        {
            NotificationAdapter msgAdapter = new NotificationAdapter(getActivity(), null);
            recyclerList.setAdapter(msgAdapter);

        }


        hitWebserviceG();


        return v;
    }


    void hitWebserviceG()
    {
        try
        {

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());

            new SuperWebServiceG(GlobalConstants.URL + "usernotification", data, new CallBackWebService()
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

                            JSONArray jsonarrayData = jsonMainResult.getJSONArray("message");

                            if (listMsg == null)
                            {
                                listMsg = new ArrayList<NotificationModel>();
                            }
                            else
                            {
                                listMsg.clear();
                            }

                            for (int g = 0; g < jsonarrayData.length(); g++)
                            {


                                JSONObject jobj = jsonarrayData.optJSONObject(g);


//                                String id               = jobj.optString("id");
//                                String sender_userid    = jobj.optString("sender_userid");
//                                String recipient_userid = jobj.optString("recipient_userid");
//                                String message          = jobj.optString("message");
//                                String created_at       = jobj.optString("created_at");
//                                String username         = jobj.optString("username");
//                                String profile_pic      = jobj.optString("profile_pic");

//                                listMsg.add(new MsgDataModel(id, sender_userid, recipient_userid, message, created_at, username, profile_pic));
                            }

                            Collections.reverse(listMsg);
                            NotificationAdapter msgAdapter = new NotificationAdapter(getActivity(), null);
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


}
