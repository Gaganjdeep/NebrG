package gagan.com.communities.activites.fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.FollowerFollowingAdapter;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends BaseFragmentG
{


    public FollowersFragment()
    {
        // Required empty public constructor
    }

    RecyclerView recyclerList;
    ProgressBar  progressBar;


    private boolean isAlreadySet = false;


    List<UserDataModel> listG;

    public void setList(String json)
    {
        try
        {
            listG = new ArrayList<>();
            isAlreadySet = true;

            JSONArray jsonarrayData = new JSONArray(json);


            for (int g = 0; g < jsonarrayData.length(); g++)
            {
                JSONObject jobj = jsonarrayData.optJSONArray(g).getJSONObject(0);

                UserDataModel homemodel = new UserDataModel();

                homemodel.setProfile_pic(jobj.optString("profile_pic"));
                homemodel.setuId(jobj.optString("uId"));
                homemodel.setName(jobj.optString("name"));
                homemodel.setProfession(jobj.optString("profession"));


                listG.add(homemodel);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View v = inflater.inflate(R.layout.recycler_list_layout, container, false);


        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);
        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!isAlreadySet)
        {
            hitWebserviceG();
        }
        else
        {
            FollowerFollowingAdapter msgAdapter = new FollowerFollowingAdapter(getActivity(), listG);
            recyclerList.setAdapter(msgAdapter);
        }

//        FollowerFollowingAdapter msgAdapter = new FollowerFollowingAdapter(getActivity(), null);
//        recyclerList.setAdapter(msgAdapter);

        return v;
    }


    void hitWebserviceG()
    {
        try
        {

            progressBar.setVisibility(View.VISIBLE);

            JSONObject data = new JSONObject();
            data.put("user_id", getArguments().getString("userid"));

//            data.put("user_id", "1");

            data.put("f_status", getArguments().getString("follower"));  // 1 for followers 2 for following.


            new SuperWebServiceG(GlobalConstants.URL + "getfollowerList", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    progressBar.setVisibility(View.GONE);
                    processOutput(output);
                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void processOutput(String output)
    {
        try
        {

            JSONObject jsonMain = new JSONObject(output);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("200"))
            {


                JSONArray jsonarrayData;

                if (getArguments().getString("follower").equals("1"))
                {
                    jsonarrayData = jsonMainResult.getJSONArray("followers");
                }
                else
                {
                    jsonarrayData = jsonMainResult.getJSONArray("following");
                }


                if (jsonarrayData == null)
                {
                    return;
                }

//                {"uId":"5","name":"Gagan","email":"gagan2@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","gender":"male","home_society":"bdbdbdbd","session_key":"","create_date":"2016-02-25 18:02:51","role_id":"2","profession":"sharp shooter","location":"Jagadhari Road, Sarsehri, Haryana 133004, India","delete_status":"0","device_type":"0","device_token":"0","update_date":"2016-02-25 11:02:51","profile_pic":"","path":"","is_fb":"0","facebook_id":"","is_gp":"0","gplus_id":"","login_status":"0"},

                List<UserDataModel> list = new ArrayList<>();

                for (int g = 0; g < jsonarrayData.length(); g++)
                {
                    JSONObject jobj = jsonarrayData.optJSONObject(g);

                    UserDataModel homemodel = new UserDataModel();

                    homemodel.setProfile_pic(jobj.optString("profile_pic"));
                    homemodel.setuId(jobj.optString("uId"));
                    homemodel.setName(jobj.optString("name"));
                    homemodel.setProfession(jobj.optString("profession"));


                    list.add(homemodel);
                }

                FollowerFollowingAdapter msgAdapter = new FollowerFollowingAdapter(getActivity(), list);
                msgAdapter.setChatEnable(chatEnable);
                msgAdapter.setShowUnfollow(showUnFollow);
                recyclerList.setAdapter(msgAdapter);

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


    private boolean chatEnable = false;

    public void setChatEnable(boolean chat)
    {
        chatEnable = chat;
    }


    private boolean showUnFollow = false;

    public void setShowUnFollow(boolean unFollow)
    {
        showUnFollow = unFollow;
    }


}
