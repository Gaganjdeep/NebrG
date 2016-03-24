package gagan.com.communities.activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.BitmapDecoderG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class EditProfileActivity extends BaseActivityG
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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

    RoundedCornersGaganImg imgvProfilePic;
    EditText               edEmail, edLocation, edGender, edPhoneNumber, edHomeSociety, edprofession, edName;
    String Base64Image = "";


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
        edEmail = (EditText) findViewById(R.id.edEmail);
        edLocation = (EditText) findViewById(R.id.edLocation);
        edHomeSociety = (EditText) findViewById(R.id.edHomeSociety);
        edprofession = (EditText) findViewById(R.id.edprofession);
        edName = (EditText) findViewById(R.id.edName);
        edGender = (EditText) findViewById(R.id.edGender);

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
        edLocation.setText(userData.getLocation());

//        edPhoneNumber.setText(userData.ge);
        edEmail.setText(userData.getEmail());
        edHomeSociety.setText(userData.getHome_society());
        edGender.setText(userData.getGender());


        Base64Image = userData.getProfile_pic();
    }




    private boolean validation()
    {
        if (edName.getText().toString().trim().isEmpty())
        {
            edName.setError("Please enter name");
            return false;
        }

        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString().trim()).matches())
        {
            edEmail.setError("Enter a valid email");
            return false;
        }
        else if (edLocation.getText().toString().trim().isEmpty())
        {
            edLocation.setError("Please enter location");
            return false;
        }
        else if (edHomeSociety.getText().toString().trim().isEmpty())
        {
            edHomeSociety.setError("Please enter home society");
            return false;
        }


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
            data.put("home_society", edHomeSociety.getText().toString().trim());
            data.put("device_type", "android");

            if(!Base64Image.contains("."))
            {
                data.put("image", Base64Image);
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
                userDataModel.setProfile_pic(jsonarrayData.getJSONObject(0).optString("profile_pic"));
                userDataModel.setuId(jsonarrayData.getJSONObject(0).optString("uId"));

                sharedPrefHelper.setUserId(jsonMainResult.optString("userId"));
                sharedPrefHelper.setUserName(jsonarrayData.getJSONObject(0).optString("name"));
                sharedPrefHelper.setUserPhone(edPhoneNumber.getText().toString());


                SharedPrefHelper.write(EditProfileActivity.this, userDataModel);
                Utills.showToast("Profile updated", EditProfileActivity.this, true);
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
            imgvProfilePic.setRadius(120);

            Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data);

            imgvProfilePic.setImageUrl(EditProfileActivity.this, uri.toString());
            Base64Image = BitmapDecoderG.getBytesImage(EditProfileActivity.this, uri);
        }
        catch (Exception |Error e)
        {
            e.printStackTrace();
        }

    }
}
