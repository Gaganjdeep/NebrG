package gagan.com.communities.activites.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import gagan.com.communities.R;
import gagan.com.communities.activites.AddBuisness;
import gagan.com.communities.activites.AddPostActivity;
import gagan.com.communities.activites.CreateClassified;
import gagan.com.communities.activites.CreateCommunity;
import gagan.com.communities.activites.MainTabActivity;
import gagan.com.communities.utills.GlobalConstants;

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


        searchView = (AutoCompleteTextView) toolbar.findViewById(R.id.searchView);
        searchView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    try
                    {
                        PlacePicker.IntentBuilder intentBuilder =
                                new PlacePicker.IntentBuilder();
                        Intent intent = intentBuilder.build(getActivity());
                        startActivityForResult(intent, 77);

                    }
                    catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
                    {
                        e.printStackTrace();
                    }
                }


                return false;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {

            if (requestCode == 77)
            {
                // The user has selected a place. Extract the name and address.
                final Place place = PlacePicker.getPlace(data, getActivity());

                final CharSequence name    = place.getName();
                final CharSequence address = place.getAddress();

                searchView.setText(name);

//
//                communitiesfragment.OnFinishG(place.getLatLng());
//                postsFragment.OnFinishG(place.getLatLng());
                Location l = new Location("");
                l.setLatitude(place.getLatLng().latitude);
                l.setLongitude(place.getLatLng().longitude);
                Communitiesfragment.locationToSearch = l;
                PostsFragment.locationToSearchP = l;


                Intent intent = new Intent(GlobalConstants.UPDATE_MAPTAB);
                getActivity().sendBroadcast(intent);

            }
        }
    }

    AutoCompleteTextView searchView;

    TabLayout tabLayoutG;
    ViewPager viewPagerG;


    Communitiesfragment communitiesfragment;
    PostsFragment       postsFragment;

    private void setupViewPager(ViewPager viewPager)
    {

        communitiesfragment = new Communitiesfragment();
        postsFragment = new PostsFragment();
        MainTabActivity.Adapter adapter = new MainTabActivity.Adapter(getChildFragmentManager());
        adapter.addFragment(communitiesfragment, "Communities");
        adapter.addFragment(postsFragment, "Posts");
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
