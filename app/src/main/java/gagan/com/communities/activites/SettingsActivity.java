package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.utills.SharedPrefHelper;

public class SettingsActivity extends AppCompatActivity
{


    LinearLayout layoutChangepswd;
    TextView     tvCurrentLocation;

    SharedPrefHelper        sharedPrefHelper;
    HashMap<String, String> currentLocData;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPrefHelper = new SharedPrefHelper(SettingsActivity.this);
//        currentLocData=new HashMap<>();
        currentLocData = sharedPrefHelper.getHomeLocation();

        settingActionBar();


        layoutChangepswd = (LinearLayout) findViewById(R.id.layoutChangepswd);
        tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);


        if (sharedPrefHelper.getlogInFrom().equals(SharedPrefHelper.loginWith.manual.toString()))
        {
            layoutChangepswd.setVisibility(View.VISIBLE);
        }
        else
        {
            layoutChangepswd.setVisibility(View.GONE);
        }


        tvCurrentLocation.setText(currentLocData.get(SharedPrefHelper.HOMELOC_NAME));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    final int REQUEST_PLACE_PICKER = 11;

    public void changeLocation(View view)
    {

        // Construct an intent for the place picker
        try
        {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(SettingsActivity.this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode, Intent data
    )
    {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK)
        {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name    = place.getName();
            final CharSequence address = place.getAddress();


            sharedPrefHelper.setHomeLocation(name.toString(), place.getLatLng().latitude + "", place.getLatLng().longitude + "");

            tvCurrentLocation.setText(name);

        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void changePswd(View view)
    {
        startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
    }

    public void changeSociety(View view)
    {
        startActivity(new Intent(SettingsActivity.this, ChangeSocietyActivity.class));
    }
}
