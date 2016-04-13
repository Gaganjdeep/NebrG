package gagan.com.communities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.ChatActivity;
import gagan.com.communities.activites.OtherProfileActivity;
import gagan.com.communities.activites.ProfileActivity;
import gagan.com.communities.activites.ShowFragmentActivity;
import gagan.com.communities.activites.fragment.AddChatFragment;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * Created by sony on 12-03-2016.
 */
public class FollowerFollowingAdapter extends RecyclerView.Adapter<FollowerFollowingAdapter.MyViewHolderG>

{

    private boolean chatEnable   = false;
    private boolean showUnfollow = false;


    private LayoutInflater inflater;

    Context con;

    private List<UserDataModel> dataList;


    public FollowerFollowingAdapter(Context context, List<UserDataModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
    }


    public void setChatEnable(boolean chat)
    {
        chatEnable = chat;
    }

    public void setShowUnfollow(boolean unfollow)
    {
        showUnfollow = unfollow;
    }


    @Override
    public FollowerFollowingAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.followers_inflator, parent, false);
        return new MyViewHolderG(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {

        final UserDataModel currentData = dataList.get(position);

        holder.tvName.setText(currentData.getName());
        holder.tvText.setText(currentData.getProfession());

        holder.imgUserPic.setRadius(170);
        if (!currentData.getProfile_pic().equals(""))
        {

            holder.imgUserPic.setImageUrl(con, currentData.getProfile_pic());
        }


        if (chatEnable)
        {
//            if (setOnClickListener)
//            {
            holder.view.setTag(currentData);
            holder.view.setOnClickListener(
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            UserDataModel data = (UserDataModel) v.getTag();

                            Intent intnt = new Intent(con, ChatActivity
                                    .class);
                            intnt.putExtra("id", data.getuId());
                            intnt.putExtra("pic", data.getProfile_pic());
                            con.startActivity(intnt);
//                                ((Activity)con).finish();

                        }
                    }
            );
//            }
        }
        else if (con instanceof ProfileActivity)
        {
            holder.view.setTag(currentData.getuId());
            holder.view.setOnClickListener(
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intnt = new Intent(con, OtherProfileActivity.class);
                            intnt.putExtra("user_id", v.getTag().toString());
                            con.startActivity(intnt);
                        }
                    }
            );

            if (showUnfollow)
            {
                holder.tvUnfolow.setVisibility(View.VISIBLE);
                holder.tvUnfolow.setTag(currentData);
                holder.tvUnfolow.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        final UserDataModel gData = ((UserDataModel) v.getTag());

                        try
                        {
                            JSONObject data = new JSONObject();
                            data.put("user_id", new SharedPrefHelper(con).getUserId());
                            data.put("f_user_id", gData.getuId());
                            data.put("remove", "1");


                            new SuperWebServiceG(GlobalConstants.URL + "managefollower", data, new CallBackWebService()
                            {
                                @Override
                                public void webOnFinish(String output)
                                {
                                    dataList.remove(gData);

                                    notifyDataSetChanged();
                                }
                            }).execute();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                    }
                });

            }


        }


    }

//    boolean setOnClickListener=false;
//
//    public void setOnclickOnView(boolean onclickOnView) {
//        setOnClickListener = onclickOnView;
//    }


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView tvName, tvText, tvUnfolow;
        View                   view;
        RoundedCornersGaganImg imgUserPic;

        public MyViewHolderG(View itemView)
        {
            super(itemView);

            tvUnfolow = (TextView) itemView.findViewById(R.id.tvUnfolow);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            imgUserPic = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgUserPic);
            view = itemView;
        }
    }

}


//if (con instanceof ProfileActivity) {
//        viewOther.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//
//        con.startActivity(new Intent(con, OtherProfileActivity.class));
//        }
//        });
//        }