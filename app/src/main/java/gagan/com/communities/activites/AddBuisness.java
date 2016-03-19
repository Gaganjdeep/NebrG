package gagan.com.communities.activites;

import android.content.Intent;
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

import org.json.JSONObject;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.utills.BitmapDecoderG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class AddBuisness extends BaseActivityG implements View.OnClickListener {


    EditText edCompanyName, edEmailC, edLocation, edDescription;
    android.support.v7.widget.AppCompatSpinner spinner;
    TextView tvcategory;

    RoundedCornersGaganImg imgBuisnessimg;
    String Base64String = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buisness);

        findViewByID();
        settingActionBar();
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







    @Override
    void findViewByID() {
        edCompanyName = (EditText) findViewById(R.id.edCompanyName);
        edEmailC = (EditText) findViewById(R.id.edEmailC);
        edLocation = (EditText) findViewById(R.id.edLocation);
        edDescription = (EditText) findViewById(R.id.edDescription);
        tvcategory = (TextView) findViewById(R.id.tvcategory);
        imgBuisnessimg = (RoundedCornersGaganImg) findViewById(R.id.imgBuisnessimg);


        imgBuisnessimg.setOnClickListener(this);


        spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                tvcategory.setText(((TextView) view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    void hitWebserviceG() {

        try {

            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("company_name", edCompanyName.getText().toString().trim());
            data.put("email", edEmailC.getText().toString().trim());
            data.put("description", edDescription.getText().toString().trim());
            data.put("location", edLocation.getText().toString().trim());
            data.put("image", Base64String);
            data.put("category", tvcategory.getText().toString().trim());
//        {"userid":"1","company_name":"comany name","email":"email","image":"base64 codeed mage","image_name":"testimage.jpg",
//                "location":"chandigarh","description":"description","category":"category"}

            new SuperWebServiceG(GlobalConstants.URL + "addBuisness", data, new CallBackWebService() {
                @Override
                public void webOnFinish(String output) {

                    cancelDialog();

                    try {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            finish();
                        }
                        Utills.showToast(jsonMainResult.getString("status"), AddBuisness.this, true);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


    private boolean validation() {
        if (edCompanyName.getText().toString().trim().isEmpty()) {
            edCompanyName.setError("Please enter company name");
            return false;
        }

        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmailC.getText().toString().trim()).matches()) {
            edEmailC.setError("Enter a valid email");
            return false;
        }
        else if (edDescription.getText().toString().trim().length() < 0) {
            edDescription.setError("Password length should more than 5");
            return false;
        }
        else if (edLocation.getText().toString().trim().isEmpty()) {
            edLocation.setError("Please select a location");
            return false;
        }


        return true;
    }


    public void subMitt(View view) {
        if (validation()) {

            hitWebserviceG();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgBuisnessimg:

                BitmapDecoderG.selectImage(AddBuisness.this, null);

                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data);


        imgBuisnessimg.setImageUrl(AddBuisness.this,uri.toString());

        imgBuisnessimg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        try
        {
            Base64String = BitmapDecoderG.getBytesImage(AddBuisness.this, uri);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void openSpinner(View view) {
        spinner.performClick();
    }
}
