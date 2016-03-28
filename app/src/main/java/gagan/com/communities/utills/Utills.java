package gagan.com.communities.utills;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.InputStream;
import java.io.OutputStream;

import gagan.com.communities.R;

/**
 * Created by gagandeep on 22 Dec 2015.
 */
public class Utills
{


    public static Toast toast;


    public static void showToast(String msg, Context context, boolean center)
    {
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
    static int  progress = 0;

    public static void ShowDialogProgress(Context con, final CallBackG callBackG)
    {

        progress=0;
        final SharedPrefHelper shrdHeler = new SharedPrefHelper(con);
        Dialog dialog = new Dialog(con, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.set_distance_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

// compile 'org.adw.library:discrete-seekbar:1.0.1'
//        final AppCompatSeekBar seek             = (AppCompatSeekBar) dialog.findViewById(R.id.seekbar);
        DiscreteSeekBar        seek = (DiscreteSeekBar)dialog. findViewById(R.id.seekbar);
        seek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value;
            }
        });

//        seek.setMax(255);
//        seek.setKeyProgressIncrement(1);


        final TextView tvKm =(TextView)dialog.findViewById(R.id.tvKm);


        seek.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener()
        {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int i, boolean fromUser)
            {
                tvKm.setText(i + " km");
                progress = i;

                shrdHeler.setDistanceParam(progress);
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

        seek.setProgress(shrdHeler.getDistanceParam());


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
        dialog.show();





        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                callBackG.OnFinishG(null);
            }
        });




       /* final AlertDialog.Builder popDialog = new AlertDialog.Builder(con);


        final SharedPrefHelper shrdHeler = new SharedPrefHelper(con);
        progress = 0;

        LinearLayout layout = new LinearLayout(con);
        layout.setOrientation(LinearLayout.VERTICAL);


        final TextView tvKm = new TextView(con);
        tvKm.setGravity(Gravity.RIGHT);
        tvKm.setPadding(0, 0, 20, 0);

        final SeekBar seek = new SeekBar(con);
        seek.setMax(255);
        seek.setKeyProgressIncrement(1);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {

                tvKm.setText(i + " km");
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        layout.addView(seek);
        layout.addView(tvKm);
        seek.setProgress(shrdHeler.getDistanceParam());

        popDialog.setTitle("Please Select distance");
        popDialog.setView(layout);

        popDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                shrdHeler.setDistanceParam(progress);
                dialogInterface.dismiss();

            }
        });
        popDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });


        popDialog.show();*/
    }
    // seekbar dialog end


}
