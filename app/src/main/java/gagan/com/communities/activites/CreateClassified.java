package gagan.com.communities.activites;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.utills.BitmapDecoderG;
import gagan.com.communities.utills.CurrentLocActivityG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class CreateClassified extends CurrentLocActivityG
{

    @Override
    public void getCurrentLocationG(Location currentLocation)
    {
        if (googleMap != null)
        {
            latLngG = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            addMarker(latLngG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_classified);


        settingActionBar();

        findViewByID();
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


    private MapFragment mapFragment;

    GoogleMap googleMap;

    EditText edTitle, edDescription;
    AppCompatSpinner       spinner;
    TextView               tvType;
    RoundedCornersGaganImg imgClassified;

    void findViewByID()
    {
        imgClassified = (RoundedCornersGaganImg) findViewById(R.id.imgClassified);
        edTitle = (EditText) findViewById(R.id.edTitle);
        edDescription = (EditText) findViewById(R.id.edDescription);
        tvType = (TextView) findViewById(R.id.tvType);

        spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                tvType.setText(((TextView) view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        initializeMapFragment();
    }


    private LatLng latLngG = null;


    private void initializeMapFragment()
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

                        addMarker(latLng);
                        latLngG = latLng;
                    }
                });

            }
        });


    }


    private void addMarker(LatLng latLng)
    {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 6);
        googleMap.animateCamera(cameraUpdate);
    }


    void hitWebserviceG()
    {
        if (validation())
        {
            try
            {

                showProgressDialog();

                JSONObject data = new JSONObject();
                data.put("userid", sharedPrefHelper.getUserId());
                data.put("title", edTitle.getText().toString().trim());
                data.put("description", edDescription.getText().toString().trim());
                data.put("lat", latLngG.latitude);
                data.put("lng", latLngG.longitude);
                data.put("category", tvType.getText().toString().trim());
                data.put("invite_user_id", "");
                data.put("image", Base64String);

                new SuperWebServiceG(GlobalConstants.URL + "addclassified", data, new CallBackWebService()
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
                            Utills.showToast(jsonMainResult.getString("status"), CreateClassified.this, true);
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


    private boolean validation()
    {
        if (edTitle.getText().toString().trim().isEmpty())
        {
            edTitle.setError("Please enter title");
            return false;
        }
        else if (edDescription.getText().toString().trim().length() < 0)
        {
            edDescription.setError("Please enter some description");
            return false;
        }
        return true;
    }


    public void subMitt(View view)
    {
        hitWebserviceG();
    }

    public void openSpinner(View view)
    {
        spinner.performClick();
    }

    public void imgClick(View view)
    {
        BitmapDecoderG.selectImage(CreateClassified.this, null);
    }

    String Base64String = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {


            Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data);


            imgClassified.setImageUrl(CreateClassified.this, uri.toString());

            imgClassified.setScaleType(ImageView.ScaleType.CENTER_CROP);

            try
            {
                Base64String = BitmapDecoderG.getBytesImage(CreateClassified.this, uri);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
