package gagan.com.communities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.models.NotificationModel;

/**
 * Created by sony on 23-01-2016.
 */
public class MyAdAdapter extends BaseAdapter {
    Context con;

    List<HomeModel> DataList;


    public MyAdAdapter(Context con, List<HomeModel> dataList) {
        this.con = con;
        DataList = dataList;
    }

    @Override
    public int getCount() {
//        return DataList.size();
        return 7;
    }

    @Override
    public NotificationModel getItem(int i) {

//        return DataList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View viewOther, ViewGroup viewGroup) {

//        final NotificationModel data = getItem(i);
//
        if (viewOther == null) {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewOther = inflater.inflate(R.layout.my_advertisement_inflator, viewGroup, false);
        }


//        TextView txtv_title = (TextView) viewOther.findViewById(R.id.txtv_title);
//        TextView txtv_date_time = (TextView) viewOther.findViewById(R.id.txtv_date_time);
//        TextView txtv_speed = (TextView) viewOther.findViewById(R.id.txtv_speed);
//
//
//        txtv_title.setText(data.getEventName());
//


        return viewOther;
    }
}