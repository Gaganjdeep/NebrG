package gagan.com.communities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import gagan.com.communities.activites.ChatActivity;
import gagan.com.communities.activites.CommunityDetailsActivity;
import gagan.com.communities.activites.MainTabActivity;
import gagan.com.communities.activites.OtherProfileActivity;
import gagan.com.communities.activites.ShowPostActivity;
import gagan.com.communities.activites.SplashActivity;
import gagan.com.communities.activites.fragment.HomeFragment;
import gagan.com.communities.activites.fragment.NotificationTabFragment;
import gagan.com.communities.adapters.NotificationAdapter;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Notification;
import gagan.com.communities.utills.SharedPrefHelper;

public class GCMIntentService extends GCMBaseIntentService
{

    static int flag = 0;

    Bundle bun;

    public GCMIntentService()
    {
        super(GlobalConstants.SENDER_ID);
    }


    private void showNotification(Context context, String message, Intent intent)
    {


        Intent notificationIntent;

        if (isLoggedIn(context))
        {
            notificationIntent = intent;
        }
        else
        {
            notificationIntent = new Intent(context, SplashActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);


//        if (!taskData.getImage().isEmpty())
//        {
//
//            try
//            {
//                Uri imageUri = Uri.parse(taskData.getImage());
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
////        }
//                builder.setLargeIcon(bitmap);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        else
//        {
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.grey_bg));
//        }

        builder.setContentTitle("Neibr");
        builder.setContentText(message);
//        builder.setSubText("Tap to open EasyBeezee and set reminders..!");


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (defaultSound == null)
        {
            defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (defaultSound == null)
            {
                defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }

        builder.setSound(defaultSound);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onRegistered(Context context, String registrationId)
    {
//		Log.i(TAG, "Device registered: regId = " + registrationId);

    }

    @Override
    protected void onUnregistered(Context context, String registrationId)
    {
//		Log.i(TAG, "Device unregistered");

    }


    @Override
    protected void onMessage(Context context, Intent intent)
    {
        try
        {
            bun = intent.getExtras();
            final String message = intent.getStringExtra("message");
            Log.e("++++GCM message++++", message);

//            Bundle[{content=28, message=Gagan Deep sent you a message, title=message recieved, collapse_key=do_not_collapse, status=9, sound=1, senderID=13, message_id=524, vibrate=1, from=679103909775, datetime=2016-05-25 22:45:16}]
//            {"message":"You have successfully liked the post title: \"for liki dislike test post\"",
//                    "title":"Post Liked","status":2,"vibrate":1,"sound":1,"post_id":"45"}

//            JSONObject jobj = new JSONObject(message);
            try
            {
                if (intent.getStringExtra("status").equals(Notification.MessageRecieved.getValue() + ""))
                {

//                    MainTabActivity.tabToOpen = 2;

                    Intent intnt = new Intent(context, ChatActivity
                            .class);
                    intnt.putExtra("id", intent.getStringExtra("senderID"));
                    intnt.putExtra("pic", intent.getStringExtra("image"));
                    intnt.putExtra("name", intent.getStringExtra("name"));

                    showNotification(context, message, intnt);


                    Intent intentBroadcasst = new Intent(GlobalConstants.UPDATE_CHAT);
                    intentBroadcasst.putExtra("id", intent.getStringExtra("senderID"));
                    intentBroadcasst.putExtra("msg", intent.getStringExtra("content"));
                    intentBroadcasst.putExtra("message_id", intent.getStringExtra("message_id"));
                    sendBroadcast(intentBroadcasst);

                    sendBroadcast(new Intent(GlobalConstants.UPDATE_MSG_FRAGMENT));


                }
                else if (intent.getStringExtra("status").equals(Notification.PostAdded.getValue() + ""))
                {
                    if (HomeFragment.homeFragment != null)
                    {
                        (HomeFragment.homeFragment).notifier(0, "0");
                    }
                }

                else if (intent.getStringExtra("status").equals(Notification.PostLiked.getValue() + ""))
                {
                 

                    HomeModel homemodel = parsePOStjson(intent.getStringExtra("post"));


                    Intent intnt = new Intent(context, ShowPostActivity.class);
                    intnt.putExtra("data", homemodel);

                    showNotification(context, message, intnt);


                    sendBroadcast(new Intent(GlobalConstants.UPDATE_NOTI_FRAGMENT));

                }
                else if (intent.getStringExtra("status").equals(Notification.CommentAdded.getValue() + ""))
                {
                 

//                    "{"image":"","lng":"80.2195","post_location":"saidapet","tag_lat":"0","title":"Saidapet","message":"Testing","type":"Ask a Question","userid":"12","tag_pincode":"0","path":"","tag_long":"0","post_pincode":"600015","anon_user":"0","c_id":"0","location":"saidapet","id":"57","tag_status":"0","create_date":"2016-06-29 23:19:26","lat":"13.0224","status":"0"}"
//                    JSONObject jobj      = new JSONObject(intent.getStringExtra("post"));
                    HomeModel homemodel = parsePOStjson(intent.getStringExtra("post"));


                    Intent intnt = new Intent(context, ShowPostActivity.class);
                    intnt.putExtra("data", homemodel);

                    showNotification(context, message, intnt);
                    sendBroadcast(new Intent(GlobalConstants.UPDATE_NOTI_FRAGMENT));

                }
                else if (intent.getStringExtra("status").equals(Notification.Userfollow.getValue() + ""))
                {
                 


                    Intent intnt = new Intent(context, OtherProfileActivity.class);
                    intnt.putExtra("user_id", intent.getStringExtra("user_id"));


                    showNotification(context, message, intnt);
                    sendBroadcast(new Intent(GlobalConstants.UPDATE_NOTI_FRAGMENT));

                }
                else if (intent.getStringExtra("status").equals(Notification.GroupInvitation.getValue() + ""))
                {
//                 


                    JSONObject           jobj = new JSONArray(intent.getStringExtra("comunity")).getJSONObject(0);
                    CommunitiesListModel data = new CommunitiesListModel();

                    data.setCid(jobj.optString("cid"));
                    data.setC_description(jobj.optString("c_description"));
                    data.setC_genre(jobj.optString("c_genre"));
                    data.setC_name(jobj.optString("c_name"));
                    data.setCreated_at(jobj.optString("created_at"));
                    try
                    {
                        LatLng latlng = new LatLng(Double.parseDouble(jobj.optString("c_lat")), Double.parseDouble(jobj.optString("c_long")));
                        data.setLatLng(latlng);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    data.setOwner_id(jobj.optString("owner_id"));
                    data.setUser_id(jobj.optString("user_id"));
                    data.setMyCommunity(true);

                    Intent intnt = new Intent(context, CommunityDetailsActivity.class);
                    intnt.putExtra("data", data);

                    showNotification(context, message, intnt);
                    sendBroadcast(new Intent(GlobalConstants.UPDATE_NOTI_FRAGMENT));


                }

                if (!intent.getStringExtra("status").equals("11"))
                {
                    SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);

                    sharedPrefHelper.SetbadgeCount(sharedPrefHelper.GetbadgeCount() + 1);

                    sendBroadcast(new Intent(GlobalConstants.UPDATE_BADGE));
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
        catch (Exception | Error e)
        {
            showNotification(context, "new notification", new Intent(context, SplashActivity.class));
            e.printStackTrace();
        }


    }


    private HomeModel parsePOStjson(String postJson)
    {
        try
        {


            JSONObject jobj      = new JSONObject(postJson);
            HomeModel  homemodel = new HomeModel();
            homemodel.setId(jobj.optString("id"));
            homemodel.setLocation(jobj.optString("post_location"));
            homemodel.setComments_count(jobj.optString("comments_count"));
            homemodel.setCreate_date(jobj.optString("create_date"));
            homemodel.setImage(jobj.optString("image"));
            homemodel.setMessage(jobj.optString("message"));
            homemodel.setUserid(jobj.optString("userid"));
            homemodel.setTitle(jobj.optString("title"));
            homemodel.setType(jobj.optString("type"));
            homemodel.setLike_count(jobj.optString("like_count"));
            homemodel.setDislike_count(jobj.optString("dislike_count"));
            homemodel.setUsername(jobj.optString("username"));
            homemodel.setProfile_pic(jobj.optString("profile_pic"));

            homemodel.setIs_liked(jobj.optString("is_liked").equals("1"));
            homemodel.setIs_disliked(jobj.optString("is_disliked").equals("1"));
            homemodel.setAnon_user(jobj.optString("anon_user").equals("1"));


            double lat = Double.parseDouble(jobj.optString("tag_lat"));
            double lng = Double.parseDouble(jobj.optString("tag_long"));
            homemodel.setLatLng(new LatLng(lat, lng));

            return homemodel;


        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;

        }
    }


    private boolean isLoggedIn(Context context)
    {
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);

        return sharedPrefHelper.isLoggedIn() && sharedPrefHelper.getEmailVerified();
    }

    @Override
    protected void onDeletedMessages(Context context, int total)
    {

//		String message = "";
        // generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId)
    {

    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId)
    {
        // log message

        return super.onRecoverableError(context, errorId);
    }
}
