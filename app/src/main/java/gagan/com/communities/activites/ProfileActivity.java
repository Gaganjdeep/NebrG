package gagan.com.communities.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.FollowersFragment;
import gagan.com.communities.activites.fragment.HomeFragment;
import gagan.com.communities.activites.fragment.MessageFragment;
import gagan.com.communities.activites.fragment.MyCommunityFragment;
import gagan.com.communities.activites.fragment.NotificationFragment;
import gagan.com.communities.activites.fragment.PostProfileFragment;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.CallBackG;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;

public class ProfileActivity extends BaseActivityG
{

    TabLayout tabLayoutG;
    ViewPager viewPagerG;

    RoundedCornersGaganImg imgvProfilePic;

    TextView tvUserName, tvProfession, tvLocation;


    AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        settingActionBar();

        viewPagerG = (ViewPager) findViewById(R.id.viewpager);
        if (viewPagerG != null)
        {
            setupViewPager(viewPagerG);
        }
        findViewByID();

        tabLayoutG = (TabLayout) findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.button_pink));
        tabLayoutG.setSelectedTabIndicatorHeight(8);

        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);


    }

    @Override
    void findViewByID()
    {
        imgvProfilePic = (RoundedCornersGaganImg) findViewById(R.id.imgvProfilePic);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvProfession = (TextView) findViewById(R.id.tvProfession);

        appbar = (AppBarLayout) findViewById(R.id.appbar);
    }

    @Override
    protected void onResume()
    {
        setData();
        super.onResume();
    }

    private void setData()
    {
        UserDataModel userData = SharedPrefHelper.read(ProfileActivity.this);
        imgvProfilePic.setRadius(270);

        tvUserName.setText(userData.getName());
        tvProfession.setText(userData.getProfession());
        tvLocation.setText(userData.getLocation());


        imgvProfilePic.setImageWithCallBack(ProfileActivity.this, userData.getProfile_pic(), new CallBackG<Bitmap>()
        {
            @Override
            public void OnFinishG(Bitmap output)
            {
                try
                {

//                    Drawable g = imgvProfilePic.getDrawable();
//
//                    appbar.setBackground(g);
//                    imgvProfilePic.setScaleType(ImageView.ScaleType.FIT_XY);
//                    imgvProfilePic.setRadius(200);
//                    imgvProfilePic.setBackground(g);

                    Bitmap bmp = output;
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
                    bitmapDrawable.setGravity(Gravity.CENTER| Gravity.FILL);
                    appbar.setBackground(bitmapDrawable);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    void hitWebserviceG()
    {

    }

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.edit_profile:
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));

                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager)
    {


        FollowersFragment myFollowers = new FollowersFragment();
        Bundle            bundleall   = new Bundle();
        bundleall.putString("follower", "1");
        bundleall.putString("userid", sharedPrefHelper.getUserId());
        myFollowers.setArguments(bundleall);


        FollowersFragment myFollowings = new FollowersFragment();
        Bundle            bundleOther  = new Bundle();
        bundleOther.putString("follower", "2");
        bundleOther.putString("userid", sharedPrefHelper.getUserId());
        myFollowings.setArguments(bundleOther);


        PostProfileFragment myPost     = new PostProfileFragment();
//        Bundle              bundlePost = new Bundle();
//        bundlePost.putString("userid", sharedPrefHelper.getUserId());
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