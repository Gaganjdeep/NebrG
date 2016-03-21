package com.support.android.designlibdemo.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.support.android.designlibdemo.R;

import java.util.List;


/**
 * Created by Gagan on 21/3/16.
 */
public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.MyViewHolderG>

{

    private LayoutInflater inflater;

    Context con;

    private List<String> dataList;


    public ChatMsgAdapter(Context context, List<String> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
    }


    final int MY_MSG    = 1;
    final int OTHER_MSG = 2;


    @Override
    public int getItemViewType(int position)
    {
        if (dataList.get(position).equals("myid"))
        {
            return MY_MSG;
        }
        else
        {
            return OTHER_MSG;
        }
    }

    @Override
    public ChatMsgAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
//        View view = inflater.inflate(R.layout.custom_other_chat, parent, false);

        if (viewType == MY_MSG)
        {
            return new MyViewHolderG(inflater.inflate(R.layout.custom_my_chat, parent, false));
        }
        else
        {
            return new MyViewHolderG(inflater.inflate(R.layout.custom_other_chat, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {




    }


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView tvMSG, tvTime;
        LinearLayout           linearLayout;
        View                   view;
        RoundedCornersGaganImg DP;

        public MyViewHolderG(View itemView)
        {
            super(itemView);

            tvMSG = (TextView) itemView.findViewById(R.id.txtV_otherChatmsg);

            RoundedCornersGaganImg DP = (ImageView) itemView.findViewById(R.id.fb_otherChat);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.FrameLayoutotherChat);

            tvTime = (TextView) itemView.findViewById(R.id.txtV_otherChatDate);

            view = itemView;
        }
    }

}
