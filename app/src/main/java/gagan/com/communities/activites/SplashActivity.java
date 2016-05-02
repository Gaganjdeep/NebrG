package gagan.com.communities.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import gagan.com.communities.R;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        final SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(SplashActivity.this);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                if (sharedPrefHelper.isLoggedIn() && sharedPrefHelper.getEmailVerified())
                {
                    startActivity(new Intent(SplashActivity.this, MainTabActivity.class));

                }
                else
                {
                    Utills.transitionToActivity(SplashActivity.this, LoginActivity.class, (ImageView) findViewById(R.id.imgLogo), "logo");
                }


            }
        }, 2000);
    }
}
