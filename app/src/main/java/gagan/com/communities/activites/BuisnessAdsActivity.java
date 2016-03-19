package gagan.com.communities.activites;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.BuisnessAdsAllFragment;
import gagan.com.communities.activites.fragment.MessageFragment;
import gagan.com.communities.activites.fragment.NotificationFragment;

public class BuisnessAdsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buisness_ads);
        settingActionBar();

        viewPagerG = (ViewPager) findViewById(R.id.viewpager);
        if (viewPagerG != null) {
            setupViewPager(viewPagerG);
        }


        tabLayoutG = (TabLayout) findViewById(R.id.tabs);
        tabLayoutG.setSelectedTabIndicatorColor(getResources().getColor(R.color.button_pink_dark));
        tabLayoutG.setSelectedTabIndicatorHeight(8);

        tabLayoutG.setupWithViewPager(viewPagerG);


        setupTabLayout(tabLayoutG, viewPagerG);

    }

    private void settingActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

    }

    TabLayout tabLayoutG;
    ViewPager viewPagerG;

    private void setupViewPager(ViewPager viewPager) {
        MainTabActivity.Adapter adapter = new MainTabActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new BuisnessAdsAllFragment(), "All");
        adapter.addFragment(new BuisnessAdsAllFragment(), "Subscribed");
        viewPager.setAdapter(adapter);


    }

    public void setupTabLayout(TabLayout tabLayout, final ViewPager mViewpager) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewpager);

    }

    public void addBuisness(View view) {
        startActivity(new Intent(BuisnessAdsActivity.this, CreateClassified
                .class));

    }

    public void showMap(View view) {
        Intent intnt = new Intent(BuisnessAdsActivity.this, ShowMapActivity
                .class);
        intnt.putExtra("title", "Business Center");
        startActivity(intnt);
    }
}
