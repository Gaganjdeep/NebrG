package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.IOException;

import gagan.com.communities.R;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.CallBackG;
import gagan.com.communities.utills.CurrentLocActivityG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;


public class SignUp extends CurrentLocActivityG
{


    final int REQUEST_PLACE_PICKER = 11;
    private TextView tvLocation;
    private EditText edFullNameS, edEmailS, edPasswordS, edHomeSociety, edprofession, edConfirmPasswordS;
    Button btnMaleS, btnFemaleS;


    String maleFemale = "male";

    Location locationCurrent = null;

    LatLng locationCurrentHome = null;

    public static Context context;

    @Override
    public void getCurrentLocationG(Location currentLocation)
    {
        locationCurrent = currentLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        context=this;

        findViewByID();
    }


    void findViewByID()
    {

        edEmailS = (EditText) findViewById(R.id.edEmailS);
        edPasswordS = (EditText) findViewById(R.id.edPasswordS);
        edConfirmPasswordS = (EditText) findViewById(R.id.edConfirmPasswordS);
        edFullNameS = (EditText) findViewById(R.id.edFullNameS);
        edHomeSociety = (EditText) findViewById(R.id.edHomeSociety);

        edprofession = (EditText) findViewById(R.id.edprofession);
        btnMaleS = (Button) findViewById(R.id.btnMaleS);
        btnFemaleS = (Button) findViewById(R.id.btnFemaleS);

        tvLocation = (TextView) findViewById(R.id.tvLocation);

        setGender(true);
    }


    void hitWebserviceG()
    {
        try
        {
            if (locationCurrent == null)
            {
                displayLocation();
                return;
            }


            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("name", edFullNameS.getText().toString().trim());
            data.put("email", edEmailS.getText().toString().trim());
            data.put("password", edPasswordS.getText().toString().trim());
            data.put("gender", maleFemale);
            data.put("home_society", "" /*edHomeSociety.getText().toString().trim()*/);
            data.put("profession", edprofession.getText().toString().trim());
            data.put("location", tvLocation.getText().toString().trim());
            data.put("device_type", "android");
            data.put("device_token", DeviceID);

//            data.put("home_pincode", "android");

            if (locationCurrentHome != null)
            {
                data.put("home_lat", locationCurrentHome.latitude + "");
                data.put("home_long", locationCurrentHome.longitude + "");
            }
            else
            {
                data.put("home_lat", locationCurrent.getLatitude() + "");
                data.put("home_long", locationCurrent.getLongitude() + "");
            }

            new SuperWebServiceG(GlobalConstants.URL + "signup", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {


                    cancelDialog();

                    processOutput(output);


                }
            }).execute();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void processOutput(String response)
    {

        try
        {

            JSONObject jsonMain = new JSONObject(response);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.optString("code").equals("200"))
            {
                UserDataModel userDataModel = new UserDataModel();
                userDataModel.setEmail(edEmailS.getText().toString().trim());
                userDataModel.setName(edFullNameS.getText().toString().trim());
                userDataModel.setGender(maleFemale);
                userDataModel.setHome_society(tvLocation.getText().toString().trim());
                userDataModel.setLocation(tvLocation.getText().toString().trim());
                userDataModel.setPassword(edPasswordS.getText().toString().trim());
                userDataModel.setProfession(edprofession.getText().toString().trim());
                userDataModel.setProfile_pic("");
                userDataModel.setuId(jsonMainResult.optString("userId"));

                sharedPrefHelper.setUserId(jsonMainResult.optString("userId"));
                sharedPrefHelper.setUserName(edFullNameS.getText().toString().trim());

                sharedPrefHelper.setPincodeStatus(jsonMainResult.optString("pincode_status").equals("1"));
                sharedPrefHelper.setPswd(edPasswordS.getText().toString());
                String location = tvLocation.getText().toString().trim();

                try
                {
                    location = jsonMainResult.optString("pincode_society").equals("null") ? location : jsonMainResult.optString("pincode_society");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                sharedPrefHelper.setHomeLocation(location, locationCurrent.getLatitude() + "", locationCurrent.getLongitude() + "");

                SharedPrefHelper.write(SignUp.this, userDataModel);

                sharedPrefHelper.setDeviceToken(DeviceID);
                sharedPrefHelper.setEmailVerified(false);


                startActivity(new Intent(SignUp.this, CodeVerificationActivity.class));
//                finish();
            }


            Utills.showToast(jsonMainResult.optString("status"), SignUp.this, true);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void setGender(boolean male)
    {
        if (male)
        {
            btnMaleS.setBackgroundColor(getResources().getColor(R.color.button_pink));
            btnFemaleS.setBackgroundColor(getResources().getColor(R.color.app_background));
            maleFemale = "male";
        }
        else
        {
            btnFemaleS.setBackgroundColor(getResources().getColor(R.color.button_pink));
            btnMaleS.setBackgroundColor(getResources().getColor(R.color.app_background));
            maleFemale = "female";
        }

    }


    public void signUp(View view)
    {

        if (validation())
        {
            hitWebserviceG();
        }

    }

    public void gotoLogin(View view)
    {
        startActivity(new Intent(SignUp.this, LoginActivity.class));
        finish();
    }

    public void openMapPicker(View view)
    {
        // Construct an intent for the place picker
        try
        {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(SignUp.this);
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

            locationCurrentHome = place.getLatLng();

//            if (name.toString().contains("("))
//            {
                showProgressDialog();
                Utills.getLocationName(SignUp.this, place.getLatLng(), new CallBackG<String>()
                {
                    @Override
                    public void OnFinishG(String output)
                    {
                        cancelDialog();
                        tvLocation.setText(output);
                    }
                });
//            }
//            else
//            {
//                tvLocation.setText(name.toString());
//            }


        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        getRegisterationID();
    }


    private boolean validation()
    {
        if (edFullNameS.getText().toString().trim().isEmpty())
        {
            edFullNameS.setError("Please enter name");
            return false;
        }

        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmailS.getText().toString().trim()).matches())
        {
            edEmailS.setError("Enter a valid email");
            return false;
        }
        else if (edPasswordS.getText().toString().trim().length() < 4)
        {
            edPasswordS.setError("Password length should more than 4");
            return false;
        }
        else if (!edPasswordS.getText().toString().trim().equals(edConfirmPasswordS.getText().toString().trim()))
        {
            edConfirmPasswordS.setError("Password not matched");
            return false;
        }
        else if (tvLocation.getText().toString().equals("Location"))
        {
            Utills.showToast("Please select a location", SignUp.this, true);
            return false;
        }

        return true;
    }


    GoogleCloudMessaging gcm;

    String DeviceID = "";

    public void getRegisterationID()
    {

        new AsyncTask<Object, Object, Object>()
        {
            @Override
            protected Object doInBackground(Object... params)
            {

                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(SignUp.this);
                    }
                    DeviceID = gcm.register(GlobalConstants.SENDER_ID);


                    // try
                    msg = "Device registered, registration ID=" + DeviceID;

                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            protected void onPostExecute(Object result)
            {

            }

        }.

                execute(null, null, null);


    }

    public void selectMale(View view)
    {
        setGender(true);
    }

    public void selectFemale(View view)
    {
        setGender(false);
    }
}
