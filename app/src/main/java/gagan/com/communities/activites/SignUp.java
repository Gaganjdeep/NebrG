package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;


public class SignUp extends BaseActivityG {


    final int REQUEST_PLACE_PICKER = 11;
    TextView tvLocation;
    EditText edFullNameS, edEmailS, edPasswordS, edHomeSociety, edprofession;
    Button btnMaleS, btnFemaleS;


    String maleFemale = "male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewByID();
    }


    @Override
    void findViewByID() {

        edEmailS = (EditText) findViewById(R.id.edEmailS);
        edPasswordS = (EditText) findViewById(R.id.edPasswordS);

        edFullNameS = (EditText) findViewById(R.id.edFullNameS);
        edHomeSociety = (EditText) findViewById(R.id.edHomeSociety);

        edprofession = (EditText) findViewById(R.id.edprofession);
        btnMaleS = (Button) findViewById(R.id.btnMaleS);
        btnFemaleS = (Button) findViewById(R.id.btnFemaleS);

        tvLocation = (TextView) findViewById(R.id.tvLocation);

        setGender(true);
    }

    @Override
    void hitWebserviceG() {


        try {


            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("name", edFullNameS.getText().toString().trim());
            data.put("email", edEmailS.getText().toString().trim());
            data.put("password", edPasswordS.getText().toString().trim());
            data.put("gender", maleFemale);
            data.put("home_society", edHomeSociety.getText().toString().trim());
            data.put("profession", edprofession.getText().toString().trim());
            data.put("location", tvLocation.getText().toString().trim());
            data.put("device_type", "android");
            data.put("device_token", DeviceID);


//            data.put("home_pincode", "android");
//            data.put("home_lat", DeviceID);
//            data.put("home_long", DeviceID);

            new SuperWebServiceG(GlobalConstants.URL + "signup", data, new CallBackWebService() {
                @Override
                public void webOnFinish(String output) {


                    cancelDialog();

                    processOutput(output);


                }
            }).execute();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }








    private void processOutput(String response)
    {

        try
        {

            JSONObject jsonMain=new JSONObject(response);

            JSONObject jsonMainResult=jsonMain.getJSONObject("result");

            if(jsonMainResult.optString("code").equals("200"))
            {

                JSONObject data = new JSONObject();
                data.put("name", edFullNameS.getText().toString().trim());
                data.put("email", edEmailS.getText().toString().trim());
                data.put("password", edPasswordS.getText().toString().trim());
                data.put("gender", maleFemale);
                data.put("home_society", edHomeSociety.getText().toString().trim());
                data.put("profession", edprofession.getText().toString().trim());
                data.put("location", tvLocation.getText().toString().trim());
                data.put("device_type", "android");
                data.put("device_token", DeviceID);

                UserDataModel userDataModel=new UserDataModel();
                userDataModel.setEmail(edEmailS.getText().toString().trim());
                userDataModel.setName(edFullNameS.getText().toString().trim());
                userDataModel.setGender(maleFemale);
                userDataModel.setHome_society( edHomeSociety.getText().toString().trim());
                userDataModel.setLocation(tvLocation.getText().toString().trim());
                userDataModel.setPassword( edPasswordS.getText().toString().trim());
                userDataModel.setProfession(edprofession.getText().toString().trim());
                userDataModel.setProfile_pic("");
                userDataModel.setuId(jsonMainResult.optString("userId"));

                sharedPrefHelper.setUserId(jsonMainResult.optString("userId"));
                sharedPrefHelper.setUserName(edFullNameS.getText().toString().trim());

                SharedPrefHelper.write(SignUp.this,userDataModel);

                startActivity(new Intent(SignUp.this, MainTabActivity.class));
                finish();
            }


            Utills.showToast(jsonMainResult.optString("status"), SignUp.this, true);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }








    private void setGender(boolean male) {
        if (male) {
            btnMaleS.setBackgroundColor(getResources().getColor(R.color.button_pink));
            btnFemaleS.setBackgroundColor(getResources().getColor(R.color.app_background));
            maleFemale = "male";
        }
        else {
            btnFemaleS.setBackgroundColor(getResources().getColor(R.color.button_pink));
            btnMaleS.setBackgroundColor(getResources().getColor(R.color.app_background));
            maleFemale = "female";
        }

    }


    public void signUp(View view) {

        if (validation()) {

            hitWebserviceG();
        }

    }

    public void gotoLogin(View view) {
        startActivity(new Intent(SignUp.this, LoginActivity.class));
        finish();
    }

    public void openMapPicker(View view) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(SignUp.this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode, Intent data
    ) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();

            tvLocation.setText(address);

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        getRegisterationID();
    }


    private boolean validation() {
        if (edFullNameS.getText().toString().trim().isEmpty()) {
            edFullNameS.setError("Please enter name");
            return false;
        }

        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmailS.getText().toString().trim()).matches()) {
            edEmailS.setError("Enter a valid email");
            return false;
        }
        else if (edPasswordS.getText().toString().trim().length() < 5) {
            edPasswordS.setError("Password length should more than 5");
            return false;
        }
        else if (edHomeSociety.getText().toString().trim().isEmpty()) {
            edHomeSociety.setError("Please select a home society");
            return false;
        }
        else if (tvLocation.getText().toString().trim().isEmpty()) {
            Utills.showToast("Please select a location", SignUp.this, true);
            return false;
        }

        return true;
    }


    GoogleCloudMessaging gcm;

    String DeviceID = "";

    public void getRegisterationID() {

        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {

                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(SignUp.this);
                    }
                    DeviceID = gcm.register(GlobalConstants.SENDER_ID);


                    // try
                    msg = "Device registered, registration ID=" + DeviceID;

                }
                catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            protected void onPostExecute(Object result) {

            }

        }.

                execute(null, null, null);


    }

    public void selectMale(View view) {
        setGender(true);
    }

    public void selectFemale(View view) {
        setGender(false);
    }
}
