package gagan.com.communities.activites.fragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gagan.com.communities.R;
import gagan.com.communities.activites.AddBuisness;
import gagan.com.communities.activites.LoginActivity;
import gagan.com.communities.activites.MainTabActivity;
import gagan.com.communities.activites.MyBuisness;

public class SignupBuisness extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_company);
    }


    public void addBuisness(View view) {
        startActivity(new Intent(SignupBuisness.this, MyBuisness.class));
        finish();
    }
}