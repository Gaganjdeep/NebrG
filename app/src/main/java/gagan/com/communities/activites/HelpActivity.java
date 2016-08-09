package gagan.com.communities.activites;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;

import gagan.com.communities.R;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;

public class HelpActivity extends AppCompatActivity
{


    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        settingActionBar();


        FacebookSdk.sdkInitialize(HelpActivity.this);
        callbackManager = CallbackManager.Factory.create();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    public void whatsApp(View view)
    {
        try
        {
            PackageManager pm       = getPackageManager();
            Intent         waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, GlobalConstants.SHARE_MSG);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        }
        catch (PackageManager.NameNotFoundException e)
        {
            Utills.showToast("WhatsApp not Installed", HelpActivity.this, true);
        }
    }

    public void faceBook(View view)
    {
        share_to_facebook();
    }

    public void gPlus(View view)
    {
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(GlobalConstants.SHARE_MSG)
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=gagan.com.communities&hl=en"))
                .getIntent();

        startActivityForResult(shareIntent, 0);
    }

    public void sMs(View view)
    {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", GlobalConstants.SHARE_MSG);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }


    public void share_to_facebook()
    {
        callbackManager = CallbackManager.Factory.create();
        final ShareDialog dailog_share = new ShareDialog(this);
        dailog_share.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>()
        {
            @Override
            public void onSuccess(Sharer.Result result)
            {
                Utills.showToast("App Link shared successfully on facebook profile.", HelpActivity.this, true);
            }

            @Override
            public void onCancel()
            {
            }

            @Override
            public void onError(FacebookException error)
            {
            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class))
        {
            ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle("Neibr Connect").setContentDescription(GlobalConstants.SHARE_MSG).setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=gagan.com.communities&hl=en")).build();
            dailog_share.show(linkContent);
        }
    }


    public void fAq(View view)
    {
        Intent intent=new Intent(HelpActivity.this,ShowTextActivity.class);
        intent.putExtra("title","FAQ");
        intent.putExtra("text",GlobalConstants.faqURL);
        startActivity(intent);
    }

    public void aboutUs(View view)
    {
        Intent intent=new Intent(HelpActivity.this,ShowTextActivity.class);
        intent.putExtra("title","About Us");
        intent.putExtra("text",GlobalConstants.aboutusURL);
        startActivity(intent);
    }

    public void termAndConditions(View view)
    {
        Intent intent=new Intent(HelpActivity.this,ShowTextActivity.class);
        intent.putExtra("title","Term and Conditions");
        intent.putExtra("text",GlobalConstants.termsURL);
        startActivity(intent);
    }
}
