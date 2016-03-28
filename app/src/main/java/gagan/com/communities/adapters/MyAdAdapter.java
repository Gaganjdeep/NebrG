package gagan.com.communities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.models.MyClassifiedModel;
import gagan.com.communities.utills.RoundedCornersGaganImg;

/**
 * Created by sony on 23-01-2016.
 */
public class MyAdAdapter extends RecyclerView.Adapter<MyAdAdapter.MyViewHolderG>

{
    private LayoutInflater inflater;

    Context con;

    private List<MyClassifiedModel> dataList;


    public MyAdAdapter(Context context, List<MyClassifiedModel> dList)
    {

        this.dataList = dList;
        con = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyAdAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.my_advertisement_inflator, parent, false);
        return new MyViewHolderG(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderG holder, int position)
    {

        final MyClassifiedModel currentData = dataList.get(position);


        holder.tvTitle.setText(currentData.getTitle());
        holder.tvTime.setText(currentData.getCreate_date());
        holder.tvdescription.setText(currentData.getDescription());
        holder.tvCategory.setText(currentData.getCategory());


        holder.imgImage.setImageUrl(con,currentData.getImage());

    }


    @Override
    public int getItemCount()
    {
        return dataList.size();
    }


    class MyViewHolderG extends RecyclerView.ViewHolder
    {
        TextView tvCategory, tvTime, tvTitle, tvdescription;
        View                   view;
        RoundedCornersGaganImg imgUserPic, imgImage;

        public MyViewHolderG(View itemView)
        {
            super(itemView);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvdescription = (TextView) itemView.findViewById(R.id.tvdescription);

            imgUserPic = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgUserPic);
            imgImage = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgImage);

            view = itemView;
        }
    }

}