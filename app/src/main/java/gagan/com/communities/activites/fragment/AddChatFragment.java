package gagan.com.communities.activites.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gagan.com.communities.R;
import gagan.com.communities.activites.MainTabActivity;

public class AddChatFragment extends BaseFragmentG
{


    public AddChatFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

//        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);
//        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
//        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));

//        hitWebserviceG();

//        FollowerFollowingAdapter msgAdapter = new FollowerFollowingAdapter(getActivity(), null);
//        recyclerList.setAdapter(msgAdapter);


        viewPagerG = (ViewPager) v.findViewById(R.id.viewpager);
        if (viewPagerG != null)
        {
            setupViewPager(viewPagerG);
        }


        tabLayoutG = (TabLayout) v.findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.button_pink_dark));
        tabLayoutG.setSelectedTabIndicatorHeight(8);

        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);


        return v;
    }


    TabLayout tabLayoutG;
    ViewPager viewPagerG;

    private void setupViewPager(ViewPager viewPager)
    {

        FollowersFragment myFollowers = new FollowersFragment();
        myFollowers.setChatEnable(true);
        Bundle bundleall = new Bundle();
        bundleall.putString("follower", "1");
        bundleall.putString("userid", sharedPrefHelper.getUserId());
        myFollowers.setArguments(bundleall);


        FollowersFragment myFollowings = new FollowersFragment();
        myFollowings.setChatEnable(true);
        Bundle bundleOther = new Bundle();
        bundleOther.putString("follower", "2");
        bundleOther.putString("userid", sharedPrefHelper.getUserId());
        myFollowings.setArguments(bundleOther);


        MainTabActivity.Adapter adapter = new MainTabActivity.Adapter(getChildFragmentManager());
        adapter.addFragment(myFollowers, "Followers");
        adapter.addFragment(myFollowings, "Following");
        viewPager.setAdapter(adapter);


    }

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager)
    {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewpager);

    }




  /*   void hitWebserviceG()
    {
        try
        {

            progressBar.setVisibility(View.VISIBLE);

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());


            new SuperWebServiceG(GlobalConstants.URL + "getuserlist", data, new CallBackWebService()
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

                    jsonarrayData= jsonMainResult.getJSONArray("userlist");



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
                msgAdapter.setOnclickOnView(true);
                recyclerList.setAdapter(msgAdapter);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/


}