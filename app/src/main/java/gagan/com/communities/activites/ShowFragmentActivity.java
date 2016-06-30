package gagan.com.communities.activites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.AddChatFragment;
import gagan.com.communities.activites.fragment.BuisnessCenterMapFragment;
import gagan.com.communities.activites.fragment.Communitiesfragment;
import gagan.com.communities.activites.fragment.FollowersFragment;
import gagan.com.communities.activites.fragment.PersonalAdsMapFragment;
import gagan.com.communities.activites.fragment.PostsFragment;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.models.UserDataModel;

public class ShowFragmentActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);


        settingActionBar(getIntent().getStringExtra("title"));

        HashMap<String, Fragment> fragmentHashMap = new HashMap<>();
        fragmentHashMap.put("Communities", null);
        fragmentHashMap.put("Communities ", new Communitiesfragment());
        fragmentHashMap.put("Posts", null);
        fragmentHashMap.put("Business Center", new BuisnessCenterMapFragment());
        fragmentHashMap.put("Personal Ads", new PersonalAdsMapFragment());
        fragmentHashMap.put("Current Location", new PersonalAdsMapFragment());
        fragmentHashMap.put("Select User", new AddChatFragment());

        fragmentHashMap.put("Members",null);


        if (getIntent().getStringExtra("title").equals("Posts"))
        {
            PostsFragment postsFragment=new PostsFragment();
            HomeModel homeModel=(HomeModel) getIntent().getSerializableExtra("data");

            postsFragment.setPost(homeModel);

            getSupportFragmentManager().beginTransaction().replace(R.id.container,postsFragment).commit();
        }
       else if (getIntent().getStringExtra("title").equals("Communities"))
        {
            Communitiesfragment communitiesfragment=new Communitiesfragment();
            CommunitiesListModel homeModel=(CommunitiesListModel) getIntent().getSerializableExtra("data");

            communitiesfragment.setGoogleMapCommunity(homeModel);

            getSupportFragmentManager().beginTransaction().replace(R.id.container, communitiesfragment).commit();
        }
        else if (getIntent().getStringExtra("title").equals("Members"))
        {
            FollowersFragment myFollowers = new FollowersFragment();
           String     userDataModel   =(String) getIntent().getStringExtra("data");

            myFollowers.setList(userDataModel);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, myFollowers).commit();
        }

        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentHashMap.get(getIntent().getStringExtra("title"))).commit();
        }


    }


    private void settingActionBar(String title)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);


        TextView tvTitle = (TextView) toolbar.findViewById(R.id.txtvTitle);
        tvTitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }
}
