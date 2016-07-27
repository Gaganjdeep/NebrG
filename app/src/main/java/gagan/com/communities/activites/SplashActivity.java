package gagan.com.communities.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import gagan.com.communities.R;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;

public class SplashActivity extends AppCompatActivity
{


    String permissionLiat[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_NETWORK_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED)
        {


            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ))
            {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Utills.show_dialog_msg(SplashActivity.this, "Allow Neibr to access your information .", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ActivityCompat.requestPermissions(
                                SplashActivity.this,
                                permissionLiat,
                                11
                        );

                    }
                });
            }
            else
            {
                ActivityCompat.requestPermissions(
                        SplashActivity.this,
                        permissionLiat,
                        11
                );
            }

            return;
        }


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


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[], int[] grantResults
    )
    {
        switch (requestCode)
        {
            case 11:
            {
                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                recreate();
//                }
//                else
//                {
//                    startUp();
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
