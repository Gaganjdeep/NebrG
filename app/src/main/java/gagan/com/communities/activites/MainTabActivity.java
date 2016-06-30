package gagan.com.communities.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.HomeFragment;
import gagan.com.communities.activites.fragment.MapViewTab;
import gagan.com.communities.activites.fragment.MoreFragment;
import gagan.com.communities.activites.fragment.NotificationTabFragment;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;


public class MainTabActivity extends AppCompatActivity
{


    public static MainTabActivity mainTabActivity;

    public static int tabToOpen=0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        mainTabActivity=this;

        sharedPrefHelper = new SharedPrefHelper(MainTabActivity.this);


        viewPagerG = (ViewPager) findViewById(R.id.viewpager);
        if (viewPagerG != null)
        {
            setupViewPager(viewPagerG);
        }


        tabLayoutG = (TabLayout) findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.button_pink));
        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);


        viewPagerG.setCurrentItem(tabToOpen);

        tabToOpen=0;
    }


    TabLayout tabLayoutG;
    ViewPager viewPagerG;

    private void setupViewPager(final ViewPager viewPager)
    {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new MapViewTab(), "Map View");
        adapter.addFragment(new NotificationTabFragment(), "Notification");
        adapter.addFragment(new MoreFragment(), "More");
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (position == 2)
                {
                    if (sharedPrefHelper.GetbadgeCount() > 0)
                    {
                        sharedPrefHelper.SetbadgeCount(0);
                        setBAdge();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                supportInvalidateOptionsMenu();
            }
        });
    }


    private void setBAdge()
    {
        if (tvBadgeCount != null)
        {

            if (sharedPrefHelper.GetbadgeCount() == 0)
            {
                tvBadgeCount.setVisibility(View.GONE);
            }
            else
            {
                tvBadgeCount.setVisibility(View.VISIBLE);
                tvBadgeCount.setText(sharedPrefHelper.GetbadgeCount() + "");
            }
        }
    }


    private TextView tvBadgeCount;

    SharedPrefHelper sharedPrefHelper;

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager)
    {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setupWithViewPager(mViewpager);


        for (int i = 0; i < tabLayout.getTabCount(); i++)
        {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

            ImageView icon  = (ImageView) tab.findViewById(R.id.icon);
            TextView  title = (TextView) tab.findViewById(R.id.title);


            if (i == 2)
            {
                tvBadgeCount = (TextView) tab.findViewById(R.id.tvBadgeCount);
                setBAdge();

            }


            icon.setBackgroundResource(selectedIcons[i]);
            title.setText(selectedTitle[i]);
            tabLayout.getTabAt(i).setCustomView(tab);

//            tabLayout.getTabAt(i).setIcon(selectedIcons[i]);


            tab.setSelected(i == 0);
        }


        //..
    }


    final private int selectedIcons[] = {R.drawable.tab_home_selector, R.drawable.tab_mapview_selector, R.drawable.tab_notification_selector, R.drawable.tab_more_selector};

    final private String selectedTitle[] = {"Home", "Map View", "Notification", "More"};

    public static class Adapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragments      = new ArrayList<>();
        private final List<String>   mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm)
        {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragments.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitles.get(position);
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (!mIsReceiverRegistered)
        {
            if (mReceiver == null)
                mReceiver = new UpdateBadgeReceiver();

            registerReceiver(mReceiver, new IntentFilter(GlobalConstants.UPDATE_BADGE));
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

    // BROADCAST RECEIVER TO UPDATE MSG LIST.
    UpdateBadgeReceiver mReceiver;
    private boolean mIsReceiverRegistered = false;


    private class UpdateBadgeReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (viewPagerG.getCurrentItem() != 2)
            {
                setBAdge();
            }
        }
    }


}
