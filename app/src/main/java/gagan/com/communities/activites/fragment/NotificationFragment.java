package gagan.com.communities.activites.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.MessageAdapter;
import gagan.com.communities.adapters.NotificationAdapter;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.models.NotificationModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Notification;
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
                                String id          = jobj.optString("id");
                                String message     = jobj.optString("message");
                                String created_at  = jobj.optString("created_at");
                                String username    = jobj.optString("username");
                                String profile_pic = jobj.optString("profile_pic");
                                boolean status = jobj.optString("status").equals("1");


                                Notification      notification      = null;
                                NotificationModel notificationModel = null;


                                if (jobj.optString("messageType").equals("1"))
                                {
                                    notificationModel = new NotificationModel<CommunitiesListModel>();

                                    notification = Notification.GroupInvitation;

                                    notificationModel.setObject(parseCommunityData(jobj.optString("comunityinfo")));

                                }
                                else if (jobj.optString("messageType").equals("2"))
                                {
                                    notificationModel = new NotificationModel<HomeModel>();

                                    notification = Notification.PostAdded;
                                    notificationModel.setObject(parsePOStjson((jobj.getJSONArray("postinfo")).get(0).toString()));
                                }
                                else if (jobj.optString("messageType").equals("3"))
                                {
                                    notificationModel = new NotificationModel<CommunitiesListModel>();

                                    notification = Notification.GroupInvitation;
                                    notificationModel.setObject(parseCommunityData(jobj.optString("comunityinfo")));

                                }
                                else if (jobj.optString("messageType").equals("4"))
                                {
                                    notificationModel = new NotificationModel<HomeModel>();

                                    notification = Notification.PostLiked;
                                    notificationModel.setObject(parsePOStjson((jobj.getJSONArray("postinfo")).get(0).toString()));

                                }
                                else if (jobj.optString("messageType").equals("5"))
                                {
                                    notificationModel = new NotificationModel<String>();

                                    notification = Notification.Userfollow;
                                    notificationModel.setObject(jobj.optString("from_userid"));
                                }

                                else if (jobj.optString("messageType").equals("6"))
                                {

                                    notificationModel = new NotificationModel<HashMap<String, String>>();

                                    notification = Notification.MessageRecieved;

                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    hashMap.put("id", jobj.optString("from_userid"));
                                    hashMap.put("pic", jobj.optString("profile_pic"));
                                    hashMap.put("name", jobj.optString("username"));
                                    notificationModel.setObject(hashMap);
                                }
                                else if (jobj.optString("messageType").equals("7"))
                                {

                                    notificationModel = new NotificationModel<HomeModel>();
                                    notification = Notification.CommentAdded;
                                    notificationModel.setObject(parsePOStjson((jobj.getJSONArray("postinfo")).get(0).toString()));

                                }


                                notificationModel.setImage(profile_pic);
                                notificationModel.setName(username);
                                notificationModel.setMsg(message);
                                notificationModel.setTime(created_at);
                                notificationModel.setNotificationType(notification);
                                notificationModel.setId(id);
                                notificationModel.setRead(status);
//                                new NotificationModel<HomeModel>(username, created_at, message, profile_pic, new HomeModel(), notification)
                                listNOti.add(notificationModel);
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


    //==========================
    private CommunitiesListModel parseCommunityData(String json)
    {
        CommunitiesListModel data = null;
        try
        {
            JSONObject jobj = new JSONArray(json).getJSONObject(0);
            data = new CommunitiesListModel();

            data.setCid(jobj.optString("cid"));
            data.setC_description(jobj.optString("c_description"));
            data.setC_genre(jobj.optString("c_genre"));
            data.setC_name(jobj.optString("c_name"));
            data.setCreated_at(jobj.optString("created_at"));
            try
            {
                LatLng latlng = new LatLng(Double.parseDouble(jobj.optString("c_lat")), Double.parseDouble(jobj.optString("c_long")));
                data.setLatLng(latlng);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            data.setOwner_id(jobj.optString("owner_id"));
            data.setUser_id(jobj.optString("user_id"));
            data.setMyCommunity(jobj.optString("owner_id").equals(sharedPrefHelper.getUserId()));

            return data;
        }
        catch (Exception e)
        {

            e.printStackTrace();
            return data;
        }
    }


//    -------------------------------

    private HomeModel parsePOStjson(String postJson)
    {
        try
        {


            JSONObject jobj      = new JSONObject(postJson);
            HomeModel  homemodel = new HomeModel();
            homemodel.setId(jobj.optString("id"));
            homemodel.setLocation(jobj.optString("post_location"));
            homemodel.setComments_count(jobj.optString("comments_count"));
            homemodel.setCreate_date(jobj.optString("create_date"));
            homemodel.setImage(jobj.optString("image"));
            homemodel.setMessage(jobj.optString("message"));
            homemodel.setUserid(jobj.optString("userid"));
            homemodel.setTitle(jobj.optString("title"));
            homemodel.setType(jobj.optString("type"));
            homemodel.setLike_count(jobj.optString("like_count"));
            homemodel.setDislike_count(jobj.optString("dislike_count"));
            homemodel.setUsername(jobj.optString("username"));
            homemodel.setProfile_pic(jobj.optString("profile_pic"));

            homemodel.setIs_liked(jobj.optString("is_liked").equals("1"));
            homemodel.setIs_disliked(jobj.optString("is_disliked").equals("1"));
            homemodel.setAnon_user(jobj.optString("anon_user").equals("1"));


            double lat = Double.parseDouble(jobj.optString("tag_lat"));
            double lng = Double.parseDouble(jobj.optString("tag_long"));
            homemodel.setLatLng(new LatLng(lat, lng));

            return homemodel;


        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;

        }
    }
//    -------------------------------


    // BROADCAST RECEIVER TO UPDATE UpdateNotificationListReceiver  LIST.
    UpdateNotificationListReceiver mReceiver;
    private boolean mIsReceiverRegistered = false;

    @Override
    public void onResume()
    {
        super.onResume();
//		mapFragment.onResume();
        if (!mIsReceiverRegistered)
        {
            if (mReceiver == null)
                mReceiver = new UpdateNotificationListReceiver();
            getActivity().registerReceiver(mReceiver, new IntentFilter(GlobalConstants.UPDATE_NOTI_FRAGMENT));
            mIsReceiverRegistered = true;
        }
    }


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

    private class UpdateNotificationListReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            hitWebserviceG();
        }
    }


    // BROADCAST RECEIVER TO UPDATE MSG LIST.END


}
