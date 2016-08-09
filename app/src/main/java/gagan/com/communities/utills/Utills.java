package gagan.com.communities.utills;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import gagan.com.communities.R;
import gagan.com.communities.activites.CurrentLocationPostActivity;
import gagan.com.communities.webserviceG.WebServiceHelper;

/**
 * Created by gagandeep on 22 Dec 2015.
 */
public class Utills
{


    public static Toast toast;

    public static String capitalize(String s)
    {
        if (s == null) return null;
        if (s.length() == 1)
        {
            return s.toUpperCase();
        }
        if (s.length() > 1)
        {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return "";
    }

    public static void showToast(String msg, Context context, boolean center)
    {

        msg = capitalize(msg);

        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (center)
        {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }

        toast.show();
    }


    public static void animate(View view)
    {
        view.setPivotX(view.getX());
        view.setAlpha(0.5f);
        view.setScaleX(0.1f);
        view.setScaleY(0.1f);
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }


    public static void hideKeyboard(Context context, View view)
    {
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void transitionToActivity(Activity activity, Class target, View logo, String transitionName)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Intent i = new Intent(activity, target);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    android.util.Pair.create(logo, transitionName)
            );
            activity.startActivity(i, options.toBundle());

        }
        else
        {
            activity.startActivity(new Intent(activity, target));
        }
    }

    public static void copyStream(InputStream input, OutputStream output)
    {

        try
        {
            byte[] buffer = new byte[1024];
            int    bytesRead;
            while ((bytesRead = input.read(buffer)) != -1)
            {
                output.write(buffer, 0, bytesRead);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //dialog onw button
    public static Dialog global_dialog;

    public static void show_dialog_msg(final Context con, String text, View.OnClickListener onClickListener)
    {
        global_dialog = new Dialog(con, R.style.Theme_Dialog);
        global_dialog.setContentView(R.layout.dialog_global);
        global_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tex    = (TextView) global_dialog.findViewById(R.id.text);
        Button   ok     = (Button) global_dialog.findViewById(R.id.ok);
        Button   cancel = (Button) global_dialog.findViewById(R.id.cancel);


        tex.setText(text);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(global_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        global_dialog.show();
        global_dialog.getWindow().setAttributes(lp);


        if (onClickListener != null)
        {
            cancel.setVisibility(View.VISIBLE);
            // ok.setOnClickListener(oc);
            cancel.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    global_dialog.dismiss();

                }
            });


            ok.setOnClickListener(onClickListener);

        }
        else
        {
            ok.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    global_dialog.dismiss();

                }
            });
        }


    }


    public static Dialog global_dialog_rateus;

    public static void show_dialog_rateus(final Context con)
    {
        global_dialog_rateus = new Dialog(con, R.style.Theme_Dialog);
        global_dialog_rateus.setContentView(R.layout.rateus_dialog);
        global_dialog_rateus.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button now   = (Button) global_dialog_rateus.findViewById(R.id.now);
        Button later = (Button) global_dialog_rateus.findViewById(R.id.later);
        Button no    = (Button) global_dialog_rateus.findViewById(R.id.no);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(global_dialog_rateus.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        global_dialog_rateus.show();
        global_dialog_rateus.getWindow().setAttributes(lp);

        View.OnClickListener onclick = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                global_dialog_rateus.dismiss();

            }
        };


        now.setOnClickListener(onclick);
        later.setOnClickListener(onclick);
        no.setOnClickListener(onclick);

    }


    // seekbar dialog for home
    static int progress = 0;

    public static void ShowDialogProgress(final Context con, final CallBackG callBackG)
    {

        progress = 0;
        final SharedPrefHelper shrdHeler = new SharedPrefHelper(con);
        final Dialog           dialog    = new Dialog(con, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.set_distance_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);


        DiscreteSeekBar seek = (DiscreteSeekBar) dialog.findViewById(R.id.seekbar);
        seek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer()
        {
            @Override
            public int transform(int value)
            {
                return value;
            }
        });

        final TextView tvKm = (TextView) dialog.findViewById(R.id.tvKm);


        seek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int i, boolean fromUser)
            {
                tvKm.setText(i + " km");
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar)
            {

            }
        });

        if (con instanceof CurrentLocationPostActivity)
        {
            seek.setProgress(shrdHeler.getDistanceParam());
        }
        else
        {
            seek.setProgress(shrdHeler.getDistanceParamHome());
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
        dialog.show();


        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (con instanceof CurrentLocationPostActivity)
                {
                    shrdHeler.setDistanceParam(progress);
                }
                else
                {
                    shrdHeler.setDistanceParamHome(progress);
                }
                callBackG.OnFinishG(null);
                dialog.dismiss();
            }
        });


    }
    // seekbar dialog end


    public static void startGoogleMaps(Context con, LatLng ltlng)
    {
        String url    = String.format(Locale.ENGLISH, "geo:%f%f", ltlng.latitude, ltlng.longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        con.startActivity(intent);
    }


    public static void shareIntent(Context con, String msg)
    {
        Intent intnt = new Intent(Intent.ACTION_SEND);
        intnt.setType("text/plain");
        intnt.putExtra(Intent.EXTRA_SUBJECT, "Neibr");
        intnt.putExtra(Intent.EXTRA_TEXT, msg);
        con.startActivity(Intent.createChooser(intnt, "Share via"));

    }


    public static void collapse_expand(final View v, int duration, int targetHeight)
    {
        int           prevHeight    = v.getWidth();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                //you can change width or height according to your need.
                v.getLayoutParams().width = (int) animation.getAnimatedValue();
                v.requestLayout();
            }


        });
        valueAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                v.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                v.requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }


    public static void getLocationName(final Context context, final LatLng latLng, final CallBackG<String> callBackG)
    {

        new AsyncTask<Void, Void, String>()
        {

            @Override
            protected String doInBackground(Void... params)
            {
                try
                {
                    return getLocationFromString(latLng);
                  /*  Geocoder      geo       = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.isEmpty())
                    {

                        return "Unknown Location";
                    }
                    else
                    {
                        if (addresses.get(0).getSubLocality() != null)
                        {
                            return addresses.get(0).getSubLocality();
                        }
                        else
                        {
                            if (addresses.get(0).getSubAdminArea() != null)
                            {
                                return addresses.get(0).getSubAdminArea();
                            }
                            else
                            {
                                if (addresses.get(0).getFeatureName() != null)
                                {
                                    return addresses.get(0).getFeatureName();
                                }
                               else if (addresses.get(0).getLocality() != null)
                                {
                                    return addresses.get(0).getLocality();
                                }
                                else
                                {
                                    return addresses.get(0).getAddressLine(0);
                                }
                            }


                        }


                    }*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return getLocationFromString(latLng);
                }

            }

            @Override
            protected void onPostExecute(String s)
            {

                callBackG.OnFinishG(s);
                super.onPostExecute(s);
            }
        }.execute();


    }

    public static String getLocationFromString(LatLng addressssss)
    {
        String addrsssssName = "Unknown Location";
        try
        {

            String URL = "http://maps.google.com/maps/api/geocode/json?latlng=" + URLEncoder.encode(addressssss.latitude + "," + addressssss.longitude, "UTF-8") + "&en&sensor=false";

            JSONObject jsonObject = new JSONObject((new WebServiceHelper()).performGetCall(URL));

            JSONArray results = jsonObject.getJSONArray("results");


            if (results.length() > 0)
            {

//                l = new Latlng_data();
//
//                double lng = results.getJSONObject(j).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
//
//                double lat = results.getJSONObject(j).getJSONObject("geometry").getJSONObject("location").getDouble("lat");


                JSONObject zero               = results.getJSONObject(0);
                JSONArray  address_components = zero.getJSONArray("address_components");


                for (int i = 0; i < address_components.length(); i++)
                {
                    JSONObject zero2     = address_components.getJSONObject(i);
                    String     long_name = zero2.getString("long_name");
                    JSONArray  mtypes    = zero2.getJSONArray("types");
                    String     Type      = mtypes.toString();

                    if (Type.contains("sublocality"))
                    {
                        if (Type.contains("sublocality_level_1"))
                        {
                            return long_name;
                        }

                    }
                    else if (Type.contains("political"))
                    {
                        if (addrsssssName.equals("Unknown Location"))
                        {
                            addrsssssName = long_name;
                        }
                    }
                 /*   else
                    {
                        if (Type.contains("administrative_area_level"))
                        {
                            return long_name;
                        }
                        else if (Type.contains("locality"))
                        {
                            return long_name;

                        }

                    }*/
                }

//                addrsssssName = results.getJSONObject(0).getString("formatted_address");
//                return addrsssssName != null ? addrsssssName : "";
//                l.setAddress(addrsssssName != null ? addrsssssName : "");
//
//                l.setLat(lng);
//                l.setLng(lat);
//
//                Ldata.add(l);
            }

        }
        catch (Exception e)
        {
            return addrsssssName;
        }
        catch (Error e)
        {
            return addrsssssName;
        }

        return addrsssssName;
    }


}
