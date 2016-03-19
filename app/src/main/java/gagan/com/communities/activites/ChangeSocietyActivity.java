package gagan.com.communities.activites;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import gagan.com.communities.R;

public class ChangeSocietyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_society);

        settingActionBar();


        final AppCompatRadioButton radiobtn=(AppCompatRadioButton)findViewById(R.id.radiobtn);

        ColorStateList colorStateList=new ColorStateList(new int[][]{new int[]{android.R.attr.checked},new int[]{android.R.attr.checked}},new int[]{Color.parseColor("#FD5A54"),Color.parseColor("#FD5A54")});
        radiobtn.setSupportButtonTintList(colorStateList);
//        final AppCompatRadioButton radiobtng=(AppCompatRadioButton)findViewById(R.id.radiobtng);
//        radiobtng.setSupportButtonTintList(colorStateList);




    }


    private void settingActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        finish();


        return super.onOptionsItemSelected(item);
    }
    final int REQUEST_PLACE_PICKER = 11;
    public void openPlacePicker(View view)
    {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(ChangeSocietyActivity.this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
