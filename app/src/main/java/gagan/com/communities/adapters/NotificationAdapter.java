package gagan.com.communities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.ChatActivity;
import gagan.com.communities.activites.CommunityDetailsActivity;
import gagan.com.communities.activites.LoginActivity;
import gagan.com.communities.activites.OtherProfileActivity;
import gagan.com.communities.activites.ProfileActivity;
import gagan.com.communities.activites.ShowPostActivity;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.models.NotificationModel;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * Created by sony on 17-01-2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolderG>

{
    private LayoutInflater inflater;

    Context con;

    private List<NotificationModel> dataList;


    public NotificationAdapter(Context context, List<NotificationModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NotificationAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.notification_inflator, parent, false);
        return new MyViewHolderG(view);
    }

    Intent intnt;

    @Override
    public void onBindViewHolder(MyViewHolderG holder, final int position)
    {

        final NotificationModel currentData = dataList.get(position);

//        holder.tvName.setText(currentData.getName());
        holder.tvmsg.setText(currentData.getMsg());

        holder.img_profilepic.setRadius(170);
        holder.img_profilepic.setImageUrl(con, currentData.getImage());


        try
        {

            SimpleDateFormat sdf       = new SimpleDateFormat(GlobalConstants.SEVER_FORMAT);
            SimpleDateFormat sdfDesire = new SimpleDateFormat("dd MMM hh:mm a");
            Date             date      = sdf.parse(currentData.getTime());
            holder.tvTime.setText(sdfDesire.format(date));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        if (currentData.isRead())
        {
            holder.parentLayout.setBackgroundColor(con.getResources().getColor(R.color.white));
        }
        else
        {
            holder.parentLayout.setBackgroundColor(con.getResources().getColor(R.color.lt_grey));
        }


        switch (currentData.getNotificationType())
        {
            case MessageRecieved:

                HashMap<String, String> hashMap = (HashMap<String, String>) currentData.getObject();

                intnt = new Intent(con, ChatActivity
                        .class);
                intnt.putExtra("id", hashMap.get("id"));
                intnt.putExtra("pic", hashMap.get("pic"));
                intnt.putExtra("name", hashMap.get("name"));


                break;
            case PostAdded:
                intnt = new Intent(con, ShowPostActivity.class);
                intnt.putExtra("data", (HomeModel) currentData.getObject());

                break;
            case PostLiked:
                intnt = new Intent(con, ShowPostActivity.class);
                intnt.putExtra("data", (HomeModel) currentData.getObject());

                break;
            case CommentAdded:
                intnt = new Intent(con, ShowPostActivity.class);
                intnt.putExtra("data", (HomeModel) currentData.getObject());

                break;
            case GroupInvitation:


                intnt = new Intent(con, CommunityDetailsActivity.class);
                intnt.putExtra("data", (CommunitiesListModel) currentData.getObject());


                break;
            case Userfollow:

                intnt = new Intent(con, OtherProfileActivity.class);
                intnt.putExtra("user_id", (String) currentData.getObject());



                break;
        }

        intnt.putExtra("id", currentData.getId());
        intnt.putExtra("position", position);

        holder.view.setTag(intnt);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent=(Intent) v.getTag();


                    JSONObject data = new JSONObject();

                    data.put("notificationid", intent.getStringExtra("id"));

                    new SuperWebServiceG(GlobalConstants.URL + "readnotification", data, new CallBackWebService()
                    {
                        @Override
                        public void webOnFinish(String output)
                        {
                            con.sendBroadcast(new Intent(GlobalConstants.UPDATE_NOTI_FRAGMENT));

                        }
                    }).execute();



                    con.startActivity(intent);

//                    dataList.get(intent.getIntExtra("position",position)).setRead(false);

//                con.startActivity(intnt);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView tvName, tvmsg, tvTime;
        View                   view;
        RoundedCornersGaganImg img_profilepic;
        LinearLayout           parentLayout;

        public MyViewHolderG(View itemView)
        {
            super(itemView);

            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvmsg = (TextView) itemView.findViewById(R.id.tvmsg);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            img_profilepic = (RoundedCornersGaganImg) itemView.findViewById(R.id.img_profilepic);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);


            view = itemView;
        }
    }

}