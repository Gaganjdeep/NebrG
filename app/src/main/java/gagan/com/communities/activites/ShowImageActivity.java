package gagan.com.communities.activites;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import gagan.com.communities.R;
import gagan.com.communities.utills.DLmanager;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.Utills;

public class ShowImageActivity extends Activity
{


    RoundedCornersGaganImg imgPic;

    String imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);


//        settingActionBar();

        imageURL = getIntent().getStringExtra("image");


        imgPic = (RoundedCornersGaganImg) findViewById(R.id.imgPic);

        imgPic.setImageUrl(ShowImageActivity.this, imageURL);

    }


   /* private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }*/


    public void sAve(View view)
    {
        Utills.showToast("Saving image",ShowImageActivity.this,true);
        DLmanager.useDownloadManager(imageURL, System.currentTimeMillis() + "g", ShowImageActivity.this);
        finish();
    }

    public void canCel(View view)
    {
        finish();
    }
}
