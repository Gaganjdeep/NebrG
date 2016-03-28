package gagan.com.communities.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import gagan.com.communities.R;
import gagan.com.communities.utills.RoundedCornersGaganImg;

public class ShowImageActivity extends AppCompatActivity
{


    RoundedCornersGaganImg imgPic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);


        settingActionBar();

        imgPic=(RoundedCornersGaganImg)findViewById(R.id.imgPic);

        imgPic.setImageUrl(ShowImageActivity.this,getIntent().getStringExtra("image"));

    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }


}
