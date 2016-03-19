package gagan.com.communities.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import gagan.com.communities.R;

public class MyBuisness extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buisness);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void createNewAd(View view)
    {
        startActivity(new Intent(MyBuisness.this, CreateClassified.class));
    }

    public void viewMyAd(View view) {
        startActivity(new Intent(MyBuisness.this, MyAdvertisements.class));
    }
}
