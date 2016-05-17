package gagan.com.communities.activites;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.utills.CurrentLocActivityG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class CreateCommunity extends BaseActivityG
{


    Button btnpublic, btnprivate;

    AppCompatSpinner spinner;
    EditText         edDescription, edCommunityName;
    TextView tvGenre, tvLocation;
    private MapFragment mapFragment;

//    GoogleMap googleMap;


    LinearLayout layoutAddLoc;

    String inviteUsers = "";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);


        settingActionBar();
        findViewByID();
    }

    void findViewByID()
    {

        layoutAddLoc = (LinearLayout) findViewById(R.id.layoutAddLoc);
        btnpublic = (Button) findViewById(R.id.btnpublic);
        btnprivate = (Button) findViewById(R.id.btnprivate);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvGenre = (TextView) findViewById(R.id.tvGenre);
        edDescription = (EditText) findViewById(R.id.edDescription);
        edCommunityName = (EditText) findViewById(R.id.edCommunityName);

        spinner = (AppCompatSpinner) findViewById(R.id.spinner);

//        spinner.setScrollbarFadingEnabled(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                tvGenre.setText(((TextView) view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        layoutAddLoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(CreateCommunity.this);
                    startActivityForResult(intent, 77);

                }
                catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
            }
        });


//        initializeMapFragment();


    }


    private LatLng latLngG = null;


   /* private void initializeMapFragment()
    {

        try
        {
//            mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        }
        catch (Exception | Error e)
        {
            e.printStackTrace();
            if (mapFragment == null)
            {
                mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            }
        }

        if (mapFragment == null)
        {
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        }


        mapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(final GoogleMap gMap)
            {

                googleMap = gMap;

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(LatLng latLng)
                    {

                        try
                        {
                            PlacePicker.IntentBuilder intentBuilder =
                                    new PlacePicker.IntentBuilder();
                            Intent intent = intentBuilder.build(CreateCommunity.this);
                            startActivityForResult(intent, 77);

                        }
                        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
                        {
                            e.printStackTrace();
                        }
//                        addMarker(latLng);
//                        latLngG = latLng;
                    }
                });

            }
        });


    }*/


/*    private void addMarker(LatLng latLng)
    {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 6);
        googleMap.animateCamera(cameraUpdate);
    }*/


    void hitWebserviceG()
    {
        if (validation())
        {
            try
            {

                showProgressDialog();

                JSONObject data = new JSONObject();
                data.put("user_id", sharedPrefHelper.getUserId());
                data.put("c_name", edCommunityName.getText().toString().trim());
                data.put("c_genre", tvGenre.getText().toString().trim());
                data.put("c_lat", latLngG.latitude);
                data.put("c_long", latLngG.longitude);
                data.put("c_description", edDescription.getText().toString().trim());
                data.put("owner_id", sharedPrefHelper.getUserId());
                data.put("invite_user_id", inviteUsers);
                data.put("c_type", isPublic);

                new SuperWebServiceG(GlobalConstants.URL + "addcommunity", data, new CallBackWebService()
                {
                    @Override
                    public void webOnFinish(String output)
                    {

                        cancelDialog();

                        try
                        {

                            JSONObject jsonMain = new JSONObject(output);

                            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                            if (jsonMainResult.getString("code").contains("200"))
                            {
                                finish();
                            }
                            Utills.showToast(jsonMainResult.getString("status"), CreateCommunity.this, true);
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


    String isPublic = "0";

    public void publicPrivate(View view)
    {
        if (view.getId() == R.id.btnpublic)
        {
            btnpublic.setTextColor(getResources().getColor(R.color.white));
            btnpublic.setBackgroundColor(getResources().getColor(R.color.button_pink));

            btnprivate.setTextColor(getResources().getColor(R.color.black));
            btnprivate.setBackgroundColor(getResources().getColor(R.color.lt_grey));

            isPublic = "0";
        }
        else if (view.getId() == R.id.btnprivate)
        {

            btnprivate.setTextColor(getResources().getColor(R.color.white));
            btnprivate.setBackgroundColor(getResources().getColor(R.color.button_pink));

            btnpublic.setTextColor(getResources().getColor(R.color.black));
            btnpublic.setBackgroundColor(getResources().getColor(R.color.lt_grey));
            isPublic = "1";
        }
    }


    private boolean validation()
    {
        if (edCommunityName.getText().toString().trim().isEmpty())
        {
            edCommunityName.setError("Please enter community name");
            return false;
        }
        else if (edDescription.getText().toString().trim().length() < 0)
        {
            edDescription.setError("Please enter some description");
            return false;
        }
        else if (tvGenre.getText().toString().trim().equals("Select genre"))
        {
            Utills.showToast("Please select genre", CreateCommunity.this, true);
            tvGenre.requestFocus();
            return false;
        }
        else if (latLngG == null)
        {
            Utills.showToast("Please select a location", CreateCommunity.this, true);
            tvLocation.requestFocus();
            return false;
        }
        return true;
    }


    public void openSpinner(View view)
    {
        spinner.performClick();
    }

    public void subMitt(View view)
    {
        hitWebserviceG();
    }

    public void selectUsers(View view)
    {
//        Utills.showToast("No user in your circle..!", CreateCommunity.this, true);

        startActivityForResult(new Intent(CreateCommunity.this, SelectUsersListActivity.class), 11);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {

            if (resultCode == RESULT_OK)
            {


                if (requestCode == 77)
                {
                    final Place place = PlacePicker.getPlace(data, this);

                    latLngG = place.getLatLng();
//                    addMarker(latLngG);

                    tvLocation.setText(place.getName());

                    return;
                }


                inviteUsers = data.getStringExtra("data");


            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
