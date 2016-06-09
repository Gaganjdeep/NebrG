package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class AddPostActivity extends BaseActivityG
{


    EditText edTitle, edMessage;
    RoundedCornersGaganImg imgImageSend, imgvUserimg;
    TextView tvlocation, tvType, tvUserName, tvpickPhoto, tvpicklocation;
    String Base64String = "", anon_user = "0", COMMUNITY_ID = "";


    android.support.v7.widget.AppCompatSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        COMMUNITY_ID = getIntent().getStringExtra("Cid");


        settingActionBar();
        findViewByID();
    }

    UserDataModel dataUser;

    @Override
    void findViewByID()
    {
        edTitle = (EditText) findViewById(R.id.edTitle);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(sharedPrefHelper.getUserName());
        imgvUserimg = (RoundedCornersGaganImg) findViewById(R.id.imgvUserimg);

        dataUser = SharedPrefHelper.read(AddPostActivity.this);
        imgvUserimg.setRadius(170);
        imgvUserimg.setImageUrl(AddPostActivity.this, dataUser.getProfile_pic());

        edMessage = (EditText) findViewById(R.id.edMessage);
        imgImageSend = (RoundedCornersGaganImg) findViewById(R.id.imgImageSend);
        tvlocation = (TextView) findViewById(R.id.tvlocation);

        tvpicklocation = (TextView) findViewById(R.id.tvpicklocation);
        tvpickPhoto = (TextView) findViewById(R.id.tvpickPhoto);
        tvpickPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pickImage();
            }
        });
        tvpicklocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pickLocation();
            }
        });


        tvlocation.setTag(null);

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


            data.put("type", spinner.getSelectedItem().toString());
//            data.put("post_pincode", );
            if (tvlocation.getTag() != null)
            {
                data.put("tag_lat", ((LatLng) tvlocation.getTag()).latitude + "");
                data.put("tag_long", ((LatLng) tvlocation.getTag()).longitude + "");
                data.put("location", tvlocation.getText().toString().trim());
            }
            else
            {
//                data.put("lat", "0");
//                data.put("lng", "0");
                data.put("location", dataUser.getHome_society() + "");
            }


            if (!COMMUNITY_ID.equals(""))
            {
                data.put("c_id", COMMUNITY_ID);
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
                            Utills.showToast("Post added successfully.", AddPostActivity.this, true);
                            finish();
                        }
                        else
                        {
                            Utills.showToast(jsonMainResult.getString("status"), AddPostActivity.this, true);
                        }

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

    public void pickImage()
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

                    try
                    {
                        imgImageSend.setVisibility(View.VISIBLE);
                        Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data);


                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 70);
                        params.setMargins(5, 5, 5, 5);
                        imgImageSend.setLayoutParams(params);


//                        imgImageSend.setImageUrl(AddPostActivity.this, uri.toString());

//                        Base64String = BitmapDecoderG.getBytesImage(AddPostActivity.this, uri);


                        Picasso.with(AddPostActivity.this).load(uri.toString()).centerInside().resize(640, 300).into(imgImageSend, new Callback()
                        {
                            @Override
                            public void onSuccess()
                            {
                                Base64String = BitmapDecoderG.getBytesImageBItmap(AddPostActivity.this, ((BitmapDrawable) imgImageSend.getDrawable()).getBitmap());
                            }

                            @Override
                            public void onError()
                            {

                            }
                        });


                    }
                    catch (Exception | Error e)
                    {
                        BitmapDecoderG.selectImage(AddPostActivity.this, null);
                        e.printStackTrace();
                    }

                }

            }


        }


    }

    final int REQUEST_PLACE_PICKER = 11;

    public void pickLocation()
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
        try
        {
            Utills.hideKeyboard(AddPostActivity.this, getCurrentFocus());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        spinner.performClick();
    }

    String anonymous = "Anonymous";
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
