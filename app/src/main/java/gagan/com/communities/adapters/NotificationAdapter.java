package gagan.com.communities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.OtherProfileActivity;
import gagan.com.communities.activites.ProfileActivity;
import gagan.com.communities.models.NotificationModel;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;

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

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {

        final NotificationModel currentData = dataList.get(position);

//        holder.tvName.setText(currentData.getName());
        holder.tvmsg.setText(currentData.getMsg());

        holder.img_profilepic.setRadius(170);
        holder.img_profilepic.setImageUrl(con,currentData.getImage());


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


    }


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }



    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView tvName, tvmsg,tvTime;
        View                   view;
        RoundedCornersGaganImg img_profilepic;

        public MyViewHolderG(View itemView)
        {
            super(itemView);

            tvTime= (TextView) itemView.findViewById(R.id.tvTime);
            tvmsg = (TextView) itemView.findViewById(R.id.tvmsg);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            img_profilepic= (RoundedCornersGaganImg) itemView.findViewById(R.id.img_profilepic);
            view = itemView;
        }
    }

}