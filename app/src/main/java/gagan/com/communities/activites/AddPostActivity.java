package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

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

public class AddPostActivity extends BaseActivityG
{


    EditText edTitle, edMessage;
    RoundedCornersGaganImg imgImageSend,imgvUserimg;
    TextView               tvlocation, tvType, tvUserName;
    String Base64String = "", anon_user = "0";


    android.support.v7.widget.AppCompatSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        settingActionBar();
        findViewByID();
    }

    @Override
    void findViewByID()
    {
        edTitle = (EditText) findViewById(R.id.edTitle);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(sharedPrefHelper.getUserName());
        imgvUserimg= (RoundedCornersGaganImg) findViewById(R.id.imgvUserimg);

        UserDataModel data= SharedPrefHelper.read(AddPostActivity.this);
        imgvUserimg.setRadius(170);
        imgvUserimg.setImageUrl(AddPostActivity.this,data.getProfile_pic());

        edMessage = (EditText) findViewById(R.id.edMessage);
        imgImageSend = (RoundedCornersGaganImg) findViewById(R.id.imgImageSend);
        tvlocation = (TextView) findViewById(R.id.tvlocation);

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




    }

    @Override
    void hitWebserviceG()
    {


        try
        {


            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("title", edTitle.getText().toString().trim());
            data.put("message", edMessage.getText().toString().trim());
            data.put("image", Base64String);
            data.put("anon_user", anon_user);
            data.put("location", tvlocation.getText().toString().trim());
            data.put("type", spinner.getSelectedItem().toString());
//            data.put("post_pincode", );
            if (tvlocation.getTag() != null)
            {
                data.put("lat", ((LatLng) tvlocation.getTag()).latitude + "");
                data.put("lng", ((LatLng) tvlocation.getTag()).longitude + "");
            }

            new SuperWebServiceG(GlobalConstants.URL + "addPost", data, new CallBackWebService()
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
                        Utills.showToast(jsonMainResult.getString("status"), AddPostActivity.this, true);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
//        startActivity(new Intent(AddBuisness.this, MyBuisness.class));
//                    finish();
                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private boolean validation()
    {
        if (edTitle.getText().toString().trim().isEmpty())
        {
            edTitle.setError("Please enter title");
            return false;
        }
        else if (tvType.getText().toString().trim().equals("Select genre"))
        {
            tvType.requestFocus();
            Utills.showToast("Please select a genre", AddPostActivity.this, true);
            return false;
        }


        return true;
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

    public void pickImage(View view)
    {
        BitmapDecoderG.selectImage(AddPostActivity.this, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {

            if (requestCode == REQUEST_PLACE_PICKER)
            {
                // The user has selected a place. Extract the name and address.
                final Place place = PlacePicker.getPlace(data, this);

                final CharSequence name    = place.getName();
                final CharSequence address = place.getAddress();


                tvlocation.setVisibility(View.VISIBLE);
                tvlocation.setText(name);

                tvlocation.setTag(place.getLatLng());

            }
            else
            {
                if (resultCode == RESULT_OK)
                {

                    try{
                        imgImageSend.setVisibility(View.VISIBLE);
                        Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data);

                        imgImageSend.setImageUrl(AddPostActivity.this,uri.toString());

                        Base64String = BitmapDecoderG.getBytesImage(AddPostActivity.this, uri);

                    }
                    catch (Exception |Error e)
                    {
                        BitmapDecoderG.selectImage(AddPostActivity.this, null);
                        e.printStackTrace();
                    }

                }

            }


        }


    }

    final int REQUEST_PLACE_PICKER = 11;

    public void pickLocation(View view)
    {
        // Construct an intent for the place picker
        try
        {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(AddPostActivity.this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }
    }

    public void postAdd(View view)
    {
        if (validation())
        {
            hitWebserviceG();
        }
    }

    public void openSpinner(View view)
    {
        spinner.performClick();
    }

    String anonymous   = "Anonymous";
//    String homesociety = "Change Subscribed Home Society";

    public void optIons(View view)
    {
        PopupMenu popup = new PopupMenu(AddPostActivity.this, view);

        SpannableString s = new SpannableString(anonymous);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        popup.getMenu().add(s);

//        SpannableString sHome = new SpannableString(homesociety);
//        sHome.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sHome.length(), 0);
//        popup.getMenu().add(sHome);


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

//                if (item.getTitle().toString().equals(homesociety))
//                {
//
//
//                }
//                else
//                {
                    if (anon_user.equals("0"))
                    {
                        anonymous = "Non Anonymous";
                        anon_user = "1";
                    }
                    else
                    {
                        anonymous = "Anonymous";
                        anon_user = "0";
                    }
//                }


                return false;
            }
        });
        popup.show();
    }
}
