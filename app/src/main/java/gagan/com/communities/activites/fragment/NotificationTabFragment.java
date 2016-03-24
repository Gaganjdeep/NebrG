package gagan.com.communities.activites.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gagan.com.communities.R;
import gagan.com.communities.activites.AddPostActivity;
import gagan.com.communities.activites.MainTabActivity;
import gagan.com.communities.activites.ShowFragmentActivity;


public class NotificationTabFragment extends android.support.v4.app.Fragment {


    public NotificationTabFragment() {
        // Required empty public constructor
    }

    TextView tvTitle;


    private void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);


        settingActionBar(v);





        viewPagerG = (ViewPager) v.findViewById(R.id.viewpager);
        if (viewPagerG != null) {
            setupViewPager(viewPagerG);
        }


        tabLayoutG = (TabLayout) v.findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.button_pink_dark));
        tabLayoutG.setSelectedTabIndicatorHeight(8);

        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);

//        setHasOptionsMenu(true);

        return v;
    }



    private void settingActionBar(View view)
    {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

    }





    TabLayout tabLayoutG;
    ViewPager viewPagerG;

    private void setupViewPager(ViewPager viewPager) {
        MainTabActivity.Adapter adapter = new MainTabActivity.Adapter(getChildFragmentManager());
        adapter.addFragment(new NotificationFragment(), "Notifications");
        adapter.addFragment(new MessageFragment(), "Messages");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        setTitle(position == 0 ? "Notifications" : "Messages");
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );

    }

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setupWithViewPager(mViewpager);

    }









   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.add_chat, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

           case R.id.addChat:

               Intent intnt = new Intent(getActivity(), ShowFragmentActivity
                       .class);
               intnt.putExtra("title", "Select User");
               startActivity(intnt);

                break;


        }

        return super.onOptionsItemSelected(item);
//        return true;
    }*/








}
