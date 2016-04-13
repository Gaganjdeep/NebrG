package gagan.com.communities.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.HomeFragment;

/**
 * Created by sony on 16-03-2016.
 */
public class FrameActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);


        settingActionBar("Home feed");


        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }


    private void settingActionBar(String title)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setVisibility(View.GONE);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
//
//
//        TextView tvTitle = (TextView) toolbar.findViewById(R.id.txtvTitle);
//        tvTitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
