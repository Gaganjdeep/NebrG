package gagan.com.communities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.OtherProfileActivity;
import gagan.com.communities.activites.ProfileActivity;
import gagan.com.communities.models.NotificationModel;

/**
 * Created by sony on 17-01-2016.
 */
public class NotificationAdapter extends BaseAdapter
{
    Context con;

    List<NotificationModel> DataList;


    public NotificationAdapter(Context con, List<NotificationModel> dataList)
    {
        this.con = con;
        DataList = dataList;
    }

    @Override
    public int getCount()
    {
        //        return DataList.size();
        return 10;
    }

    @Override
    public NotificationModel getItem(int i)
    {

//        return DataList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View viewOther, ViewGroup viewGroup)
    {

//        final NotificationModel data = getItem(i);

        if (viewOther == null)
        {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewOther = inflater.inflate(R.layout.notification_inflator, viewGroup, false);
        }


//        TextView txtv_title = (TextView) viewOther.findViewById(R.id.txtv_title);
//        TextView txtv_date_time = (TextView) viewOther.findViewById(R.id.txtv_date_time);
//        TextView txtv_speed = (TextView) viewOther.findViewById(R.id.txtv_speed);
//
//
//        txtv_title.setText(data.getEventName());
//
//
//        txtv_speed.setText(data.getAddress());


//        View v = new View(con);
//        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
//        v.setBackgroundColor(getResources().getColor(R.color.lt_grey));

        if (con instanceof ProfileActivity) {
            viewOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    con.startActivity(new Intent(con, OtherProfileActivity.class));
                }
            });
        }

        return viewOther;
    }
}