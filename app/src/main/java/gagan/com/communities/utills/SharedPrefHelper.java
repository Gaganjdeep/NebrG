package gagan.com.communities.utills;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import gagan.com.communities.models.UserDataModel;

/**
 * Created by sony on 10-02-2016.
 */
public class SharedPrefHelper
{

    Context           context;
    SharedPreferences sharedPreferences;

    final String sharedPrefname = "Neibr";
    final String USERID         = "userid";
    final String NAME           = "user_name";
    final String PHONE          = "phone_number";

    public SharedPrefHelper(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(sharedPrefname, Context.MODE_PRIVATE);
    }


    public String getUserId()
    {
        return sharedPreferences.getString(USERID, "0");
    }


    public void setUserId(String userId)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(USERID, userId);
        ed.apply();
    }

    //    ================================
    public String getUserName()
    {
        return sharedPreferences.getString(NAME, "g");
    }


    public void setUserName(String name)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(NAME, name);
        ed.apply();
    }
//    ================================


    //    ================================
    public String getUserPhone()
    {
        return sharedPreferences.getString(PHONE, "");
    }


    public void setUserPhone(String name)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(PHONE, name);
        ed.apply();
    }
//    ================================


    public boolean isLoggedIn()
    {
        return sharedPreferences.contains(USERID);
    }

    public void logOut()
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.apply();
    }


    public static UserDataModel read(Context context)
    {

        ObjectInputStream input       = null;
        UserDataModel     ReturnClass = null;
        String            filename    = "AcceptedRequestNeibr.srl";
        File              directory   = new File(context.getFilesDir().getAbsolutePath() + File.separator + "serlization");
        try
        {

            input = new ObjectInputStream(new FileInputStream(directory + File.separator + filename));
            ReturnClass = (UserDataModel) input.readObject();
            input.close();

        }
        catch (Exception e)
        {

            e.printStackTrace();
            return null;
        }
        return ReturnClass;
    }

    public static void write(Context context, Object UserDataModel)
    {
        File directory = new File(context.getFilesDir().getAbsolutePath() + File.separator + "serlization");
        if (!directory.exists())
        {
            directory.mkdirs();
        }

        String       filename = "AcceptedRequestNeibr.srl";
        ObjectOutput out      = null;

        try
        {
            out = new ObjectOutputStream(new FileOutputStream(directory + File.separator + filename));
            out.writeObject(UserDataModel);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


//    =======================================

    public void setHomeLocation(String address, String lat, String lng)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(HOMELOC_NAME, address);
        ed.putString(HOME_LAT, lat);
        ed.putString(HOME_LNG, lng);
        ed.apply();

    }


    public static final String HOMELOC_NAME = "homeLocation", HOME_LAT = "homelat", HOME_LNG = "homelng";


    public HashMap<String, String> getHomeLocation()
    {
        HashMap<String, String> data = new HashMap<>();


        data.put(HOMELOC_NAME, sharedPreferences.getString(HOMELOC_NAME, ""));
        data.put(HOME_LAT, sharedPreferences.getString(HOME_LAT, ""));
        data.put(HOME_LNG, sharedPreferences.getString(HOME_LNG, ""));

        return data;
    }


//    ======================================


// set distance

    public void setDistanceParam(int km)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("distanceParam", km);
        ed.apply();
    }


    public int getDistanceParam()
    {
        return sharedPreferences.getInt("distanceParam", 20);
    }

// distance end




// set distance

    public void setDistanceParamHome(int km)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("distanceParamHome", km);
        ed.apply();
    }


    public int getDistanceParamHome()
    {
        return sharedPreferences.getInt("distanceParamHome", 5);
    }

// distance end



    // set distance

    public void setPincodeStatus(boolean status)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean("PincodeStatus", status);
        ed.apply();
    }


    public boolean getPincodeStatus()
    {
        return sharedPreferences.getBoolean("PincodeStatus", true);
    }

// distance end


    //    ============================
    public void logInWith(String login)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString("loginWith", login);
        ed.apply();
    }

    public String getlogInFrom()
    {
        return sharedPreferences.getString("loginWith", loginWith.facebook.toString());
    }



    public void setPswd(String login)
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString("pswd", login);
        ed.apply();
    }

    public String getPswd()
    {
        return sharedPreferences.getString("pswd", "");
    }


    public static enum loginWith
    {
        manual,
        google,
        facebook
    }

//    ==============================================


}
