package gagan.com.communities.activites;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.MyCommunityFragment;

public class CommunityTabActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_fragment);

        settingActionBar();

        viewPagerG = (ViewPager) findViewById(R.id.viewpager);
        if (viewPagerG != null)
        {
            setupViewPager(viewPagerG);
        }


        tabLayoutG = (TabLayout) findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.button_pink_dark));
        tabLayoutG.setSelectedTabIndicatorHeight(8);

        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);

    }


    TabLayout tabLayoutG;
    ViewPager viewPagerG;
    TextView  tvaddCommunity;

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvaddCommunity = (TextView) toolbar.findViewById(R.id.tvaddCommunity);
        tvaddCommunity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addCummunity();
            }
        });
    }


    private void setupViewPager(ViewPager viewPager)
    {


        MyCommunityFragment myCommunityFragment = new MyCommunityFragment();
        Bundle              bundleall           = new Bundle();
        bundleall.putBoolean("myData", true);
        myCommunityFragment.setArguments(bundleall);


        MyCommunityFragment myCommunityFragmentOther = new MyCommunityFragment();
        Bundle              bundleOther              = new Bundle();
        bundleOther.putBoolean("myData", false);
        myCommunityFragmentOther.setArguments(bundleOther);

        MainTabActivity.Adapter adapter = new MainTabActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(myCommunityFragment, "My Communities");
        adapter.addFragment(myCommunityFragmentOther, "Communities Around Me");
        viewPager.setAdapter(adapter);


    }

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager)
    {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setupWithViewPager(mViewpager);

    }

    public void addCummunity()
    {
        startActivity(new Intent(CommunityTabActivity.this, CreateCommunity
                .class));
    }

    public void showMap(View view)
    {

        Intent intnt = new Intent(CommunityTabActivity.this, ShowFragmentActivity
                .class);
        intnt.putExtra("title", "Communities ");
        startActivity(intnt);
    }
}