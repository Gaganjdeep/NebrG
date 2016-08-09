package gagan.com.communities.activites;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.CurrentLocActivityG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivityG implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

{


    EditText edEmail, edPswd;
    CallbackManager callbackManager;
    String          FirstName;
    LoginButton     loginButton;


    private String ProfilePic = "";

    //    =====google +
    private GoogleApiClient mGoogleApiClient;
    private int             mSignInProgress;
    private PendingIntent   mSignInIntent;
    private int             mSignInError;
    private              boolean mServerHasToken   = true;
    private static final int     STATE_DEFAULT     = 0;
    private static final int     STATE_SIGN_IN     = 1;
    private static final int     STATE_IN_PROGRESS = 2;
    private static final int     RC_SIGN_IN        = 0;
    private static final String  SAVED_PROGRESS    = "sign_in_progress";


    //======================
//    Location locationCurrent = null;

//    @Override
//    public void getCurrentLocationG(Location currentLocation)
//    {
//        locationCurrent = currentLocation;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);


        findViewByID();


        //******************************************Facebook*******************************************


        try
        {
            LoginManager.getInstance().logOut();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }






        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        // Application code
                        try
                        {

                            if (DeviceID.isEmpty())
                            {
                                getRegisterationID();
                                return;
                            }

                            String fb_profile_image = "http://graph.facebook.com/" + object.optString("id") + "/picture";
//                                editor.commit();
                            HashMap<String, String> loginData = new HashMap<String, String>();
                            loginData.put("full_name", object.getString("name"));
                            try
                            {
                                loginData.put("email", object.getString("email"));
                            }
                            catch (Exception e)
                            {
                                loginData.put("email", object.getString("id"));

                                Log.e("CANNOT GET EMAIL", "----------------------------- REGISTERING WITH FB ID -------------------------------");
                            }

                            loginData.put("fb_id", object.getString("id"));


//                            if (locationCurrent == null)
//                            {
//                                displayLocation();
//                                return;
//                            }


                            ProfilePic = fb_profile_image;

                            JSONObject data = new JSONObject();
                            data.put("email", loginData.get("email"));
                            data.put("device_type", "android");
                            data.put("device_token", DeviceID);
                            data.put("is_fb", "1");
                            data.put("facebook_id", object.getString("id"));
                            data.put("fb_profile_pic", fb_profile_image);
                            data.put("name", loginData.get("full_name"));

//                            if (locationCurrent != null)
//                            {
//                                data.put("home_lat", locationCurrent.getLatitude() + "");
//                                data.put("home_long", locationCurrent.getLongitude() + "");
//                            }


                            sharedPrefHelper.logInWith(SharedPrefHelper.loginWith.facebook.toString());

                            showProgressDialog();
                            new SuperWebServiceG(GlobalConstants.URL + "fblogin", data, new CallBackWebService()
                            {
                                @Override
                                public void webOnFinish(String output)
                                {
                                    cancelDialog();

                                    processOutput(output);


                                }
                            }).execute();


                        }
                        catch (Exception e)
                        {
                            //                            stop_fb();
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel()
            {
//                loginButton.performClick();
            }

            @Override
            public void onError(FacebookException error)
            {
                loginButton.performClick();
            }


        });


        if (savedInstanceState != null)
        {
            mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }
        mGoogleApiClient = buildGoogleApiClientG();
        if (mGoogleApiClient.isConnected())
        {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
        }


    }


    @Override
    protected void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK)
                {
                    mSignInProgress = STATE_SIGN_IN;
                }
                else
                {
                    mSignInProgress = STATE_DEFAULT;
                }
                if (!mGoogleApiClient.isConnecting())
                {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    private void resolveSignInError()
    {
        if (mSignInIntent != null)
        {
            try
            {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
            }
            catch (IntentSender.SendIntentException e)
            {
                Log.e("====gPlus signIn Ex===", "Sign in intent could" +
                        " not be " +
                        "sent: " +
                        e.getLocalizedMessage());
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        }

    }

    private GoogleApiClient buildGoogleApiClientG()
    {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN);
        return builder.build();
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        getRegisterationID();
    }


    void findViewByID()
    {

        edEmail = (EditText) findViewById(R.id.edEmail);
        edPswd = (EditText) findViewById(R.id.edPswd);
        loginButton = (LoginButton) findViewById(R.id.login_button); // facebook login button
//        loginButton.setBackgroundResource(R.mipmap.fb);
//        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        loginButton.setReadPermissions("email,user_friends");


        ((ImageButton) findViewById(R.id.ingbtn_googlelogin)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                googleLogIn();
            }
        });
    }


    void hitWebserviceG()
    {
        try
        {


            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("email", edEmail.getText().toString().trim());
            data.put("password", edPswd.getText().toString().trim());
            data.put("device_type", "android");
            data.put("device_token", DeviceID);


            sharedPrefHelper.logInWith(SharedPrefHelper.loginWith.manual.toString());


            new SuperWebServiceG(GlobalConstants.URL + "login", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    cancelDialog();

                    sharedPrefHelper.setPswd(edPswd.getText().toString());

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


                if (sharedPrefHelper.getlogInFrom().equals(SharedPrefHelper.loginWith.manual.toString()))
                {
                    userDataModel.setProfile_pic(jsonarrayData.getJSONObject(0).optString("profile_pic"));

                }
                else
                {
                    if (jsonarrayData.getJSONObject(0).optString("is_social_uploaded").equals("1"))
                    {
                        userDataModel.setProfile_pic(jsonarrayData.getJSONObject(0).optString("profile_pic"));
                    }
                    else
                    {
                        userDataModel.setProfile_pic(ProfilePic);
                    }
                }

                userDataModel.setuId(jsonarrayData.getJSONObject(0).optString("uId"));


                try
                {
//                TODO: badge count added here.
                    sharedPrefHelper.SetbadgeCount(Integer.parseInt(jsonarrayData.getJSONObject(0).optString("badge")));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                sharedPrefHelper.setUserId(jsonMainResult.optString("userId"));
                sharedPrefHelper.setUserName(jsonarrayData.getJSONObject(0).optString("name"));

                boolean status = jsonMainResult.optString("pincode_status").equals("1");

                sharedPrefHelper.setPincodeStatus(status);


                SharedPrefHelper.write(LoginActivity.this, userDataModel);


                String location = jsonMainResult.optString("pincode_society").equals("null") ? jsonarrayData.getJSONObject(0).optString("location") : jsonMainResult.optString("pincode_society");

                sharedPrefHelper.setEmailVerified(true);
                sharedPrefHelper.setHomeLocation(location, jsonarrayData.getJSONObject(0).optString("home_lat") + "", jsonarrayData.getJSONObject(0).optString("home_long") + "");


                if (sharedPrefHelper.getlogInFrom().equals(SharedPrefHelper.loginWith.manual.toString()))
                {


//                    if (locationCurrent != null)
//                    {


//
//                    }
//                    else
//                    {
//                        sharedPrefHelper.setHomeLocation(jsonarrayData.getJSONObject(0).optString("home_society"), "", "");
//                    }

                    startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                }
                else
                {
                    if (jsonarrayData.getJSONObject(0).optString("is_social_uploaded").equals("1") || sharedPrefHelper.getPincodeStatus())
                    {
                        startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                    }
                    else
                    {
                        Intent intent = new Intent(LoginActivity.this, EditProfileActivity.class);
                        intent.putExtra("startmain", true);
                        startActivity(intent);
                    }

                }


                finish();
            }
            else
            {
                Utills.showToast("Login failed..", LoginActivity.this, true);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private boolean validation()
    {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString().trim()).matches())
        {
            edEmail.setError("Enter a valid email");
            return false;
        }
        else if (edPswd.getText().toString().trim().length() < 5)
        {
            edPswd.setError("Password length should more than 5");
            return false;
        }


        return true;
    }


    public void logIn(View view)
    {
        if (validation())
        {
            hitWebserviceG();
        }
    }

    public void gotoSignup(View view)
    {
        startActivity(new Intent(LoginActivity.this, SignUp.class));
        finish();
    }


    GoogleCloudMessaging gcm;

    String DeviceID = "";

    public void getRegisterationID()
    {

        new AsyncTask<Object, Object, Object>()
        {
            @Override
            protected Object doInBackground(Object... params)
            {

                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    DeviceID = gcm.register(GlobalConstants.SENDER_ID);

                    // try
                    msg = "Device registered, registration ID=" + DeviceID;

                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            protected void onPostExecute(Object result)
            {


//                Utills.showToast(result.toString(), LoginActivity.this, true);
            }

        }.

                execute(null, null, null);


    }


    public void forgetPswd(View view)
    {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
//        finish();
    }

    public void fbLogIn(View view)
    {
        loginButton.performClick();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        googleAfterConnect();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.e("==gPlus conn ===", "connection " + "suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.e("====gPlus FAILED===", "onConnectionFailed: " +
                "ConnectionResult" +
                ".getErrorCode() = " +
                "" + result.getErrorCode());
        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE)
        {
            Log.w("google", "API Unavailable.");
        }
        else if (mSignInProgress != STATE_IN_PROGRESS)
        {
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();
            if (mSignInProgress == STATE_SIGN_IN)
            {
                resolveSignInError();
            }
        }
    }


    private void googleAfterConnect()
    {
        try
        {
            if (DeviceID.isEmpty())
            {
                getRegisterationID();
                return;
            }


            if (!mGoogleApiClient.isConnected())
            {
//                if (!mGoogleApiClient.isConnecting())
//                {
//                    mSignInProgress = STATE_SIGN_IN;
//                    mGoogleApiClient.connect();
//                }
                return;
            }


            Log.e("google integration====", "onConnected");
            final Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            currentUser.getImage().getUrl();


//            Log.e("==Success name==", currentUser.getDisplayName());
//            Log.e("google plus ===", "ETT connected" +
//                    " to " + Plus.AccountApi.getAccountName(mGoogleApiClient));
//            Log.e("==Success name==", Plus.AccountApi.getAccountName(mGoogleApiClient));
//        if (sp.contains("GCM_Reg_id"))
//        {
//            HashMap<String, String> loginData = new HashMap<>();
//            loginData.put("full_name", currentUser.getDisplayName());
//            loginData.put("email", Plus.AccountApi.getAccountName(mGoogleApiClient));
//            loginData.put("type", "G");
//            loginData.put("device_id", sp.getString("GCM_Reg_id", ""));
//            login(loginData);
//        }
//        else
//        {
//            registerInBackground();
//            GlobalUtils.show_ToastCenter("Please " + "check network", con);
//        }

            JSONObject data = new JSONObject();
            data.put("email", Plus.AccountApi.getAccountName(mGoogleApiClient));
            data.put("device_type", "android");
            data.put("device_token", DeviceID);
            data.put("is_gp", "1");
            data.put("gplus_id", currentUser.getId());
            data.put("profile_pic", currentUser.getImage().getUrl());
            data.put("name", currentUser.getDisplayName());


//            data.put("home_lat", locationCurrent.getLatitude() + "");
//            data.put("home_long", locationCurrent.getLongitude() + "");

            ProfilePic = currentUser.getImage().getUrl();
            sharedPrefHelper.logInWith(SharedPrefHelper.loginWith.google.toString());


            showProgressDialog();
            new SuperWebServiceG(GlobalConstants.URL + "gpluslogin", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    cancelDialog();

                    processOutput(output);


                }
            }).execute();
        }
        catch (Exception e)
        {
            if (!mGoogleApiClient.isConnecting())
            {
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
            e.printStackTrace();
        }
        mSignInProgress = STATE_DEFAULT;
    }


    public void googleLogIn()
    {
//        resolveSignInError();

        if (!mGoogleApiClient.isConnecting())
        {
            mSignInProgress = STATE_SIGN_IN;
            mGoogleApiClient.connect();
        }
        else
        {
            if (mGoogleApiClient.isConnected())
            {
                googleAfterConnect();
            }
        }

    }


}