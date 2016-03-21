package gagan.com.communities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;


/**
 * Created by Gagan on 21/3/16.
 */
public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.MyViewHolderG>

{

    private LayoutInflater inflater;

    Context con;

    private List<MsgDataModel> dataList;


    SharedPrefHelper sharedPrefHelper;


    public ChatMsgAdapter(Context context, List<MsgDataModel> dList) {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);

        sharedPrefHelper = new SharedPrefHelper(con);

    }


    final int MY_MSG = 1;
    final int OTHER_MSG = 2;


    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position).getSender_userid().equals(sharedPrefHelper.getUserId())) {
            return MY_MSG;
        }
        else {
            return OTHER_MSG;
        }
    }

    @Override
    public ChatMsgAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.custom_other_chat, parent, false);

        if (viewType == MY_MSG) {
            return new MyViewHolderG(inflater.inflate(R.layout.custom_my_chat, parent, false));
        }
        else {
            return new MyViewHolderG(inflater.inflate(R.layout.custom_other_chat, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position) {
        MsgDataModel currentData = dataList.get(position);


        holder.tvMSG.setText(currentData.getMessage());
        holder.tvTime.setText(currentData.getCreated_at());

        holder.DP.setRadius(180);
        holder.DP.setImageUrl(con, currentData.getProfile_pic());


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder {
        TextView tvMSG, tvTime;
        //        LinearLayout           linearLayout;
        View view;
        RoundedCornersGaganImg DP;

        public MyViewHolderG(View itemView) {
            super(itemView);

            tvMSG = (TextView) itemView.findViewById(R.id.tvChatmsg);

            DP = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgChat);
//            linearLayout = (LinearLayout) itemView.findViewById(R.id.FrameLayoutotherChat);

            tvTime = (TextView) itemView.findViewById(R.id.tvChatDate);

            view = itemView;
        }
    }

}
