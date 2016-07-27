package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.BitmapDecoderG;
import gagan.com.communities.utills.CallBackG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class EditProfileActivity extends BaseActivityG
{


    boolean startMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        settingActionBar();


        startMain = getIntent().getBooleanExtra("startmain", false);


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
    public void onBackPressed()
    {

        if (!startMain)
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        if (!startMain)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    RoundedCornersGaganImg imgvProfilePic;
    private EditText  edLocation, edPhoneNumber, /*edHomeSociety,*/
            edprofession, edName;
    String Base64Image = "";

    private TextView edGender,edEmail;

    LatLng selectedLocationLatlng;

    @Override
    protected void onResume()
    {

        super.onResume();
    }

    @Override
    void findViewByID()
    {

        imgvProfilePic = (RoundedCornersGaganImg) findViewById(R.id.imgvProfilePic);
        edPhoneNumber = (EditText) findViewById(R.id.edPhoneNumber);

        edEmail = (TextView) findViewById(R.id.edEmail);

        edLocation = (EditText) findViewById(R.id.edLocation);
//        edHomeSociety = (EditText) findViewById(R.id.edHomeSociety);
        edprofession = (EditText) findViewById(R.id.edprofession);
        edName = (EditText) findViewById(R.id.edName);
        edGender = (TextView) findViewById(R.id.edGender);

        setData();
    }

    private void setData()
    {
        edPhoneNumber.setText(sharedPrefHelper.getUserPhone());

        UserDataModel userData = SharedPrefHelper.read(EditProfileActivity.this);

        imgvProfilePic.setRadius(120);

        imgvProfilePic.setImageUrl(EditProfileActivity.this, userData.getProfile_pic());


        edName.setText(userData.getName());
        edprofession.setText(userData.getProfession());

        edLocation.setText(sharedPrefHelper.getHomeLocation().get(SharedPrefHelper.HOMELOC_NAME));

//        edPhoneNumber.setText(userData.ge);
        edEmail.setText(userData.getEmail());
//        edHomeSociety.setText(userData.getHome_society());
        edGender.setText(userData.getGender());


        Base64Image = userData.getProfile_pic();


        edLocation.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    openMapPicker();
                }
                return false;
            }
        });


        edGender.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    PopupMenu popup = new PopupMenu(EditProfileActivity.this, edGender);

                    SpannableString s = new SpannableString("Male");
                    s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                    popup.getMenu().add(s);

                    SpannableString s2 = new SpannableString("Female");
                    s2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s2.length(), 0);
                    popup.getMenu().add(s2);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            edGender.setText(item.getTitle());

                            return false;
                        }
                    });
                    popup.show();
                }
                return false;
            }
        });


    }


    public void openMapPicker()
    {
        // Construct an intent for the place picker
        try
        {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(EditProfileActivity.this);
            startActivityForResult(intent, 11);
        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }
    }


    private boolean validation()
    {
        if (edName.getText().toString().trim().isEmpty())
        {
            edName.setError("Please enter name");
            return false;
        }

      /*  else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString().trim()).matches())
        {
            edEmail.setError("Enter a valid email");
            return false;
        }*/
        else if (edLocation.getText().toString().trim().isEmpty())
        {
            edLocation.setError("Please enter location");
            return false;
        }
       /* else if (edHomeSociety.getText().toString().trim().isEmpty())
        {
            edHomeSociety.setError("Please enter home society");
            return false;
        }*/


        return true;
    }


    @Override
    void hitWebserviceG()
    {
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(EditProfileActivity.this);

        try
        {
            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("email", edEmail.getText().toString().trim());
            data.put("name", edName.getText().toString().trim());
            data.put("location", edLocation.getText().toString().trim());
            data.put("profession", edprofession.getText().toString().trim());
//            data.put("home_society", edLocation.getText().toString().trim());
            data.put("device_type", "android");

            if (!Base64Image.contains("."))
            {
                data.put("image", Base64Image);


                if (!sharedPrefHelper.getlogInFrom().equals(SharedPrefHelper.loginWith.manual.toString()))
                {
                    data.put("is_social_uploaded", "1");
                }
            }
            if (selectedLocationLatlng == null)
            {
                HashMap<String, String> loc = sharedPrefHelper.getHomeLocation();

                data.put("home_lat", loc.get(SharedPrefHelper.HOME_LAT) + "");
                data.put("home_long", loc.get(SharedPrefHelper.HOME_LNG) + "");
            }
            else
            {
                data.put("home_lat", selectedLocationLatlng.latitude + "");
                data.put("home_long", selectedLocationLatlng.longitude + "");
            }


            data.put("gender", edGender.getText().toString().trim());
//        {"userid":"1","company_name":"comany name","email":"email","image":"base64 codeed mage","image_name":"testimage.jpg",
//                "location":"chandigarh","description":"description","category":"category"}

            new SuperWebServiceG(GlobalConstants.URL + "editProfile", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
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
            cancelDialog();

            JSONObject jsonMain = new JSONObject(response);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
            {

                JSONArray jsonarrayData = jsonMainResult.getJSONArray("userdata");

                UserDataModel userDataModel = new UserDataModel();
                userDataModel.setEmail(jsonarrayData.getJSONObject(0).optString("email"));
                userDataModel.setName(jsonarrayData.getJSONObject(0).optString("name"));
                userDataModel.setGender(jsonarrayData.getJSONObject(0).optString("gender"));
                userDataModel.setHome_society(jsonarrayData.getJSONObject(0).optString("home_society"));
                userDataModel.setLocation(jsonarrayData.getJSONObject(0).optString("location"));
                userDataModel.setPassword(jsonarrayData.getJSONObject(0).optString("password"));
                userDataModel.setProfession(jsonarrayData.getJSONObject(0).optString("profession"));



                if (Base64Image.contains("."))
                {
                    userDataModel.setProfile_pic(Base64Image);
                }
                else
                {
                    userDataModel.setProfile_pic(jsonarrayData.getJSONObject(0).optString("profile_pic"));

                }





                userDataModel.setuId(jsonarrayData.getJSONObject(0).optString("uId"));

                sharedPrefHelper.setUserId(jsonMainResult.optString("userId"));
                sharedPrefHelper.setUserName(jsonarrayData.getJSONObject(0).optString("name"));
                sharedPrefHelper.setUserPhone(edPhoneNumber.getText().toString());


                sharedPrefHelper.setPincodeStatus(jsonMainResult.optString("pincode_status").equals("1"));

                if (selectedLocationLatlng != null)
                {

//                    "pincode_exist": "no",
//                        "pincode_status": 0,
//                        "userId": "12",
//                        "pincode_city": null,
//                        "pincode_society": null,
                    String location;


                    location = jsonMainResult.optString("pincode_society").equals("null") ? jsonarrayData.getJSONObject(0).optString("location") : jsonMainResult.optString("pincode_society");

                    sharedPrefHelper.setHomeLocation(location, selectedLocationLatlng.latitude + "", selectedLocationLatlng.longitude + "");
                }


                SharedPrefHelper.write(EditProfileActivity.this, userDataModel);
                Utills.showToast("Profile updated", EditProfileActivity.this, true);


                if (startMain)
                {
                    startActivity(new Intent(EditProfileActivity.this, MainTabActivity.class));

                }

                finish();

            }
            else
            {
                Utills.showToast(jsonMainResult.optString("status"), EditProfileActivity.this, true);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void sAve(View view)
    {

        if (validation())
        {

            hitWebserviceG();
        }
    }

    public void pickImage(View view)
    {
        BitmapDecoderG.selectImage(EditProfileActivity.this, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            if (requestCode == 11 && resultCode == Activity.RESULT_OK)
            {

                // The user has selected a place. Extract the name and address.
                final Place place = PlacePicker.getPlace(data, this);

                final CharSequence name    = place.getName();
                final CharSequence address = place.getAddress();

                selectedLocationLatlng = place.getLatLng();


                if (name.toString().contains("("))
                {
                    showProgressDialog();
                    Utills.getLocationName(EditProfileActivity.this, place.getLatLng(), new CallBackG<String>()
                    {
                        @Override
                        public void OnFinishG(String output)
                        {
                            cancelDialog();
                            edLocation.setText(output);
                        }
                    });
                }
                else
                {
                    edLocation.setText(name.toString());
                }


            }
            else
            {
                imgvProfilePic.setRadius(120);

                Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data);

//                imgvProfilePic.setImageUrl(EditProfileActivity.this, uri.toString());

                Picasso.with(EditProfileActivity.this).load(uri.toString()).resize(200, 200).centerCrop().into(imgvProfilePic, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        Base64Image = BitmapDecoderG.getBytesImageBItmap(EditProfileActivity.this, ((BitmapDrawable) imgvProfilePic.getDrawable()).getBitmap());
                    }

                    @Override
                    public void onError()
                    {

                    }
                });
//                Base64Image = BitmapDecoderG.getBytesImage(EditProfileActivity.this, uri);
            }

        }
        catch (Exception | Error e)
        {
            e.printStackTrace();
        }

    }
}
