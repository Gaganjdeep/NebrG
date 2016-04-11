package gagan.com.communities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.HomeFragment;
import gagan.com.communities.models.MsgDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;


/**
 * Created by Gagan on 21/3/16.
 */
public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.MyViewHolderG>

{

    private LayoutInflater inflater;

    Context con;

    private List<MsgDataModel> dataList;


    SharedPrefHelper sharedPrefHelper;


    public ChatMsgAdapter(Context context, List<MsgDataModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);

        sharedPrefHelper = new SharedPrefHelper(con);

    }


    final int MY_MSG    = 1;
    final int OTHER_MSG = 2;


    @Override
    public int getItemViewType(int position)
    {
        if (dataList.get(position).getSender_userid().equals(sharedPrefHelper.getUserId()))
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
        MsgDataModel currentData = dataList.get(position);


        holder.tvMSG.setText(currentData.getMessage());


        try
        {

            SimpleDateFormat sdf       = new SimpleDateFormat(GlobalConstants.SEVER_FORMAT);
            SimpleDateFormat sdfDesire = new SimpleDateFormat("hh:mm a");
            Date             date      = sdf.parse(currentData.getCreated_at());


            holder.tvTime.setText(sdfDesire.format(date));


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.DP.setRadius(180);
        holder.DP.setImageUrl(con, currentData.getProfile_pic());


        holder.view.setTag(currentData);


        holder.view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(final View v)
            {
                PopupMenu popup = new PopupMenu(con, v);

                SpannableString s = new SpannableString("Delete message");
                s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                popup.getMenu().add(s);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {

                        delComment((MsgDataModel) v.getTag());
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });

        if (currentData.isLastMsg())
        {
            holder.chatbubble.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            holder.chatbubble.requestLayout();
            Utills.collapse_expand(holder.chatbubble, 1500, holder.view.getWidth() / 2);

            dataList.get(position).setLastMsg(false);
        }
        else
        {
            holder.chatbubble.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }


    }


    private void delComment(final MsgDataModel cmntId)
    {
        try
        {

            JSONObject data = new JSONObject();
            data.put("message_id", cmntId.getId());
            data.put("sender_userid", cmntId.getSender_userid());

            new SuperWebServiceG(GlobalConstants.URL + "deletemessage", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            dataList.remove(cmntId);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            Utills.showToast("unable to delete comment", con, true);
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute();
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
        TextView tvMSG, tvTime;
        LinearLayout           chatbubble;
        View                   view;
        RoundedCornersGaganImg DP;

        public MyViewHolderG(View itemView)
        {
            super(itemView);

            tvMSG = (TextView) itemView.findViewById(R.id.tvChatmsg);

            DP = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgChat);
            chatbubble = (LinearLayout) itemView.findViewById(R.id.chatbubble);

            tvTime = (TextView) itemView.findViewById(R.id.tvChatDate);

            view = itemView;
        }
    }

}
