package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Intent;
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

import org.json.JSONObject;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class SettingsActivity extends BaseActivityG
{


    LinearLayout layoutChangepswd;
    TextView     tvCurrentLocation;


    HashMap<String, String> currentLocData;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


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
    void findViewByID()
    {

    }

    @Override
    void hitWebserviceG()
    {

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


            try
            {


                showProgressDialog();

                JSONObject dataJ = new JSONObject();
                dataJ.put("userid", sharedPrefHelper.getUserId());
                dataJ.put("home_lat", place.getLatLng().latitude + "");
                dataJ.put("home_long", place.getLatLng().longitude + "");

                new SuperWebServiceG(GlobalConstants.URL + "editProfile", dataJ, new CallBackWebService()
                {
                    @Override
                    public void webOnFinish(String response)
                    {
                        try
                        {
                            cancelDialog();

                            JSONObject jsonMain = new JSONObject(response);

                            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                            if (jsonMainResult.getString("code").equals("200"))
                            {

                                if (!jsonMainResult.optString("pincode_status").equals("1"))
                                {
                                    Utills.show_dialog_msg(SettingsActivity.this, "We are yet to enable our service in your area, however you can the view posts", null);
                                }

                                sharedPrefHelper.setPincodeStatus(jsonMainResult.optString("pincode_status").equals("1"));
                                sharedPrefHelper.setHomeLocation(name.toString(), place.getLatLng().latitude + "", place.getLatLng().longitude + "");

                                tvCurrentLocation.setText(name);
                            }
                            else
                            {
                                Utills.showToast(jsonMainResult.optString("status"), SettingsActivity.this, true);
                            }


                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }).execute();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


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

    public void deActivate(View view)
    {

        Utills.show_dialog_msg(SettingsActivity.this, "Are you sure you want to Deactivate your account ?", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    showProgressDialog();

                    JSONObject dataJ = new JSONObject();
                    dataJ.put("userid", sharedPrefHelper.getUserId());
                    dataJ.put("status", "0");

                    new SuperWebServiceG(GlobalConstants.URL + "editProfile", dataJ, new CallBackWebService()
                    {
                        @Override
                        public void webOnFinish(String response)
                        {
                            try
                            {
                                cancelDialog();

                                JSONObject jsonMain = new JSONObject(response);

                                JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                                if (jsonMainResult.getString("code").equals("200"))
                                {
                                    cancelDialog();

                                    sharedPrefHelper.logOut();
                                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                                    finish();

                                }
                                else
                                {
                                    Utills.showToast(jsonMainResult.optString("status"), SettingsActivity.this, true);
                                }


                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }).execute();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


    }
}
