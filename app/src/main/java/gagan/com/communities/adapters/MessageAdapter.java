package gagan.com.communities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.ChatActivity;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;

/**
 * Created by sony on 17-01-2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolderG>

{
    private LayoutInflater inflater;

    Context con;

    private List<MsgDataModel> dataList;


    public MessageAdapter(Context context, List<MsgDataModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MessageAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.message_inflator, parent, false);
        return new MyViewHolderG(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {

        final MsgDataModel currentData = dataList.get(position);

        holder.tvUserName.setText(currentData.getUsername());
        holder.tvMessage.setText(currentData.getMessage());


        try
        {

            SimpleDateFormat sdf       = new SimpleDateFormat(GlobalConstants.SEVER_FORMAT);
            SimpleDateFormat sdfDesire = new SimpleDateFormat("dd MMM hh:mm a");
            Date             date      = sdf.parse(currentData.getCreated_at());
            holder.tvTime.setText(sdfDesire.format(date));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.imgUserPic.setRadius(170);
        holder.imgUserPic.setImageUrl(con, currentData.getProfile_pic());


        holder.view.setTag(currentData);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MsgDataModel data = (MsgDataModel) v.getTag();

                Intent intnt = new Intent(con, ChatActivity
                        .class);
                intnt.putExtra("id", data.getRecipient_userid());
                intnt.putExtra("pic", data.getProfile_pic());
                intnt.putExtra("name", data.getUsername());
                con.startActivity(intnt);
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
        TextView tvUserName, tvMessage, tvTime;
        View                   view;
        RoundedCornersGaganImg imgUserPic;

        public MyViewHolderG(View itemView)
        {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);

            imgUserPic = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgUserPic);
            view = itemView;
        }
    }

}