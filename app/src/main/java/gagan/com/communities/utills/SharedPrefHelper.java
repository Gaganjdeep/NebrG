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
        ed.putString("homeLocation", address);
        ed.putString("homelat", lat);
        ed.putString("homelng", lng);
        ed.apply();

    }


    public HashMap<String, String> getHomeLocation()
    {
        HashMap<String, String> data = new HashMap<>();


        data.put("homeLocation", sharedPreferences.getString("homeLocation", ""));
        data.put("homelat", sharedPreferences.getString("homelat", ""));
        data.put("homelat", sharedPreferences.getString("homelat", ""));

        return data;
    }


//    ======================================


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


    public static enum loginWith
    {
        manual,
        google,
        facebook
    }

//    ==============================================


}
