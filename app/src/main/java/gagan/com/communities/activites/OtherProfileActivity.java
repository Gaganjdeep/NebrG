package gagan.com.communities.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.FollowersFragment;
import gagan.com.communities.activites.fragment.MessageFragment;
import gagan.com.communities.activites.fragment.PostProfileFragment;
import gagan.com.communities.adapters.FollowerFollowingAdapter;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.CallBackG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class OtherProfileActivity extends BaseActivityG
{

    TabLayout tabLayoutG;
    ViewPager viewPagerG;

    RoundedCornersGaganImg imgvProfilePic;

    TextView tvUserName, tvProfession, tvLocation;

    AppBarLayout appbar;
    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        settingActionBar();


        user_id = getIntent().getStringExtra("user_id");

        viewPagerG = (ViewPager) findViewById(R.id.viewpager);
        if (viewPagerG != null)
        {
            setupViewPager(viewPagerG);
        }


        tabLayoutG = (TabLayout) findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.button_pink));
        tabLayoutG.setSelectedTabIndicatorHeight(8);

        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);

        findViewByID();
    }

    @Override
    void findViewByID()
    {
        imgvProfilePic = (RoundedCornersGaganImg) findViewById(R.id.imgvProfilePic);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvProfession = (TextView) findViewById(R.id.tvProfession);
        appbar = (AppBarLayout) findViewById(R.id.appbar);

        hitWebserviceG();

        ifFollowedByMe();
    }


    private void ifFollowedByMe()
    {
        try
        {

            JSONObject data = new JSONObject();
            data.put("user_id", sharedPrefHelper.getUserId());
            data.put("f_status", "2");  // 1 for followers 2 for following.


            new SuperWebServiceG(GlobalConstants.URL + "getfollowerList", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    try
                    {
                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {


                            JSONArray jsonarrayData;
//
//                            if (getArguments().getString("follower").equals("1"))
//                            {
//                                jsonarrayData = jsonMainResult.getJSONArray("followers");
//                            }
//                            else
//                            {
                            jsonarrayData = jsonMainResult.getJSONArray("following");
//                            }


                            if (jsonarrayData == null)
                            {
                                return;
                            }

                            for (int g = 0; g < jsonarrayData.length(); g++)
                            {
                                JSONObject jobj = jsonarrayData.optJSONObject(g);
//                                UserDataModel homemodel = new UserDataModel();
//                                homemodel.setProfile_pic(jobj.optString("profile_pic"));
//                                homemodel.setuId(jobj.optString("uId"));
//                                homemodel.setName(jobj.optString("name"));
//                                homemodel.setProfession(jobj.optString("profession"));

                                if(user_id.equals(jobj.optString("uId")))
                                {
                                    FOLLOW="Un Follow";
                                    FOLLOW_UNFOLLOW="1";
                                    supportInvalidateOptionsMenu();
                                   break;
                                }
                            }

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
    void hitWebserviceG()
    {
        try
        {


            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("userid", user_id);
//            data.put("f_user_id", user_id);

            new SuperWebServiceG(GlobalConstants.URL + "userProfile", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    cancelDialog();

                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
                        {

                            JSONObject jsonarrayData = jsonMainResult.getJSONObject("userinfo");

                            UserDataModel userDataModel = new UserDataModel();
                            userDataModel.setEmail(jsonarrayData.optString("email"));
                            userDataModel.setName(jsonarrayData.optString("name"));
                            userDataModel.setGender(jsonarrayData.optString("gender"));
                            userDataModel.setHome_society(jsonarrayData.optString("home_society"));
                            userDataModel.setLocation(jsonarrayData.optString("location"));
                            userDataModel.setPassword(jsonarrayData.optString("password"));
                            userDataModel.setProfession(jsonarrayData.optString("profession"));
                            userDataModel.setProfile_pic(jsonarrayData.optString("profile_pic"));
                            userDataModel.setuId(jsonarrayData.optString("uId"));

                            imgvProfilePic.setRadius(200);

                            tvUserName.setText(userDataModel.getName());
                            tvProfession.setText(userDataModel.getProfession());
                            tvLocation.setText(userDataModel.getLocation());

                            imgvProfilePic.setImageWithCallBack(OtherProfileActivity.this, userDataModel.getProfile_pic(), new CallBackG<Bitmap>()
                            {
                                @Override
                                public void OnFinishG(Bitmap output)
                                {
                                    try
                                    {

                                        Bitmap         bmp            = output;
                                        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
                                        bitmapDrawable.setGravity(Gravity.CENTER | Gravity.FILL);
                                        appbar.setBackground(bitmapDrawable);
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            });

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

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.other_profile_menu, menu);

        menu.findItem(R.id.follow).setTitle(Html.fromHtml("<font color='#0000'>" + FOLLOW + "</font>"));
        return super.onCreateOptionsMenu(menu);
    }


    private String FOLLOW = "Follow";


    private String FOLLOW_UNFOLLOW = "0";


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.follow:


                try
                {
                    showProgressDialog();

                    JSONObject data = new JSONObject();
                    data.put("user_id", sharedPrefHelper.getUserId());
                    data.put("f_user_id", user_id);
                    data.put("remove", FOLLOW_UNFOLLOW);


                    new SuperWebServiceG(GlobalConstants.URL + "managefollower", data, new CallBackWebService()
                    {
                        @Override
                        public void webOnFinish(String output)
                        {
                            try
                            {
                                cancelDialog();

                                JSONObject jsonMain = new JSONObject(output);

                                JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                                if (jsonMainResult.getString("code").equals("200"))
                                {
                                    if (FOLLOW_UNFOLLOW.equals("0"))
                                    {
                                        Utills.showToast("Followed", OtherProfileActivity.this, true);
                                        FOLLOW = "Un Follow";
                                        FOLLOW_UNFOLLOW = "1";
                                    }
                                    else
                                    {
                                        Utills.showToast("Un Followed", OtherProfileActivity.this, true);
                                        FOLLOW = "Follow";
                                        FOLLOW_UNFOLLOW = "0";
                                    }
                                    supportInvalidateOptionsMenu();
                                }
                                else
                                {
                                    Utills.showToast(jsonMainResult.optString("status"), OtherProfileActivity.this, true);
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

                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        FollowersFragment myFollowers = new FollowersFragment();
        Bundle            bundleall   = new Bundle();
        bundleall.putString("follower", "1");
        bundleall.putString("userid", user_id);
        myFollowers.setArguments(bundleall);


        FollowersFragment myFollowings = new FollowersFragment();
        Bundle            bundleOther  = new Bundle();
        bundleOther.putString("follower", "2");
        bundleOther.putString("userid", user_id);
        myFollowings.setArguments(bundleOther);


        PostProfileFragment myPost     = new PostProfileFragment();
        Bundle              bundlePost = new Bundle();
        bundlePost.putString("userid", user_id);
        myPost.setArguments(bundleOther);


        MainTabActivity.Adapter adapter = new MainTabActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(myPost, "post");
        adapter.addFragment(myFollowers, "Followers");
        adapter.addFragment(myFollowings, "Following");


        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(2);
    }

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager)
    {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setupWithViewPager(mViewpager);

    }
}