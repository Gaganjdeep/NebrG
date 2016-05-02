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

    List<NotificationModel> listNOti;

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


        if (listNOti != null)
        {
            NotificationAdapter msgAdapter = new NotificationAdapter(getActivity(), listNOti);
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

                            if (listNOti == null)
                            {
                                listNOti = new ArrayList<NotificationModel>();
                            }
                            else
                            {
                                listNOti.clear();
                            }

                            for (int g = 0; g < jsonarrayData.length(); g++)
                            {


                                JSONObject jobj = jsonarrayData.optJSONObject(g);

//                                "id": "354",
//                                    "from_userid": "12",
//                                    "to_userid": "48",
//                                    "message": "Your message have been sent",
//                                    "status": "0",
//                                    "created_at": "2016-04-08 22:29:40",
//                                    "username": "Gagan Deep",
//                                    "profile_pic": "http://orasisdata.com/Neiber/http://graph.facebook.com/1152850238072299/picture"
                                String id               = jobj.optString("id");
                                 String message          = jobj.optString("message");
                                String created_at       = jobj.optString("created_at");
                                String username         = jobj.optString("username");
                                String profile_pic      = jobj.optString("profile_pic");

                                listNOti.add(new NotificationModel(username,created_at,message,profile_pic));
                            }

//                            Collections.reverse(listNOti);
                            NotificationAdapter msgAdapter = new NotificationAdapter(getActivity(), listNOti);
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
