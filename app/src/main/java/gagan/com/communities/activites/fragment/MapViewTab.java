package gagan.com.communities.activites.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

import gagan.com.communities.R;
import gagan.com.communities.activites.AddBuisness;
import gagan.com.communities.activites.AddPostActivity;
import gagan.com.communities.activites.CreateClassified;
import gagan.com.communities.activites.CreateCommunity;
import gagan.com.communities.activites.MainTabActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewTab extends Fragment
{


    public MapViewTab()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_view_tab, container, false);

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


        settingActionBar(v);


        setHasOptionsMenu(false);

        return v;
    }


    @Override
    public void onResume()
    {
        setHasOptionsMenu(false);
        super.onResume();
    }

    private void settingActionBar(View view)
    {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        final Class[] classes = {CreateCommunity.class, AddPostActivity.class, AddBuisness.class, CreateClassified.class};


        ImageView btnAddPost = (ImageView) toolbar.findViewById(R.id.btnAdd);

        btnAddPost.requestFocus();
        btnAddPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(getActivity(), classes[viewPagerG.getCurrentItem()]);
                i.putExtra("Cid", "");
                startActivity(i);


            }
        });

    }

    TabLayout tabLayoutG;
    ViewPager viewPagerG;

    private void setupViewPager(ViewPager viewPager)
    {
        MainTabActivity.Adapter adapter = new MainTabActivity.Adapter(getChildFragmentManager());
        adapter.addFragment(new Communitiesfragment(), "Communities");
        adapter.addFragment(new PostsFragment(), "Posts");
//        adapter.addFragment(new BuisnessCenterMapFragment(), "Business Center");
//        adapter.addFragment(new PersonalAdsMapFragment(), "Personal Ads");
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(0);
    }

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager)
    {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewpager);

    }
}
