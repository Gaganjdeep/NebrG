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
import gagan.com.communities.activites.CommunityDetailsActivity;
import gagan.com.communities.models.CommunitiesListModel;

/**
 * Created by sony on 08-03-2016.
 */
public class CommunitiesAdapter extends RecyclerView.Adapter<CommunitiesAdapter.MyViewHolderG>

{
    private LayoutInflater inflater;

    Context con;

    private List<CommunitiesListModel> dataList;


    public CommunitiesAdapter(Context context, List<CommunitiesListModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CommunitiesAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.communities_inflator, parent, false);
        return new MyViewHolderG(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {

        final CommunitiesListModel currentData = dataList.get(position);

        holder.tvGenre.setText(currentData.getC_genre());
        holder.name.setText(currentData.getC_name());
        holder.description.setText(currentData.getC_description());


        holder.view.setTag(currentData);
        holder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                Intent intnt = new Intent(con, CommunityDetailsActivity.class);
                intnt.putExtra("data", (CommunitiesListModel) v.getTag());
                con.startActivity(intnt);
//                ((Activity) con).finish();


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
        TextView name, tvGenre, description;
        View view;
//        RoundedCornersGaganImg icon;

        public MyViewHolderG(View itemView)
        {
            super(itemView);
            tvGenre = (TextView) itemView.findViewById(R.id.tvGenre);
            name = (TextView) itemView.findViewById(R.id.tvName);
            description = (TextView) itemView.findViewById(R.id.tvdescription);
//            icon = (RoundedCornersGaganImg) itemView.findViewById(R.id.image);
            view = itemView;
        }
    }

}