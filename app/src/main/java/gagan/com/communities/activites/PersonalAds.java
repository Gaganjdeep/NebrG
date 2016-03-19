package gagan.com.communities.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.BuisnessAdsAllFragment;
import gagan.com.communities.activites.fragment.PersonalAdsGridFragment;

public class PersonalAds extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_ads);





        getSupportFragmentManager().beginTransaction().replace(R.id.layoutContainer, new PersonalAdsGridFragment()).commit();
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        finish();


        return super.onOptionsItemSelected(item);
    }

    public void createNewAd(View view) {
        startActivity(new Intent(PersonalAds.this, CreateClassified
                .class));

    }

    public void showMap(View view) {
        Intent intnt = new Intent(PersonalAds.this, ShowMapActivity
                .class);
        intnt.putExtra("title", "Personal Ads");
        startActivity(intnt);
    }
}
