package gagan.com.communities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.OtherProfileActivity;
import gagan.com.communities.activites.ProfileActivity;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.RoundedCornersGaganImg;

/**
 * Created by sony on 12-03-2016.
 */
public class FollowerFollowingAdapter extends RecyclerView.Adapter<FollowerFollowingAdapter.MyViewHolderG>

{
    private LayoutInflater inflater;

    Context con;

    private List<UserDataModel> dataList;


    public FollowerFollowingAdapter(Context context, List<UserDataModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
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


        if (con instanceof ProfileActivity)
        {
            holder.view.setTag(currentData.getuId());
            holder.view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intnt= new Intent(con, OtherProfileActivity.class);
                    intnt.putExtra("user_id",v.getTag().toString());
                    con.startActivity(intnt);
                }
            });
        }


    }


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView tvName, tvText;
        View                   view;
        RoundedCornersGaganImg imgUserPic;

        public MyViewHolderG(View itemView)
        {
            super(itemView);
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