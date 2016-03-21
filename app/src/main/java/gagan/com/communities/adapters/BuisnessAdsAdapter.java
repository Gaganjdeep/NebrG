package gagan.com.communities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.models.GridModel;

/**
 * Created by sony on 23-01-2016.
 */
public class BuisnessAdsAdapter extends BaseAdapter {
    Context con;

    List<GridModel> DataList;

    int windoWidth;


    public BuisnessAdsAdapter(Context con, List<GridModel> dataList) {
        this.con = con;
        DataList = dataList;

//        windoWidth=((Activity)con).getWindowManager().getDefaultDisplay().getWidth();
    }

    @Override
    public int getCount() {
//        return DataList.size();
        return DataList.size();
    }

    @Override
    public GridModel getItem(int i) {

        return DataList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    
    @Override
    public View getView(int i, View viewOther, ViewGroup viewGroup) {

        final GridModel data = getItem(i);
//
        if (viewOther == null) {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewOther = inflater.inflate(R.layout.grid_inflator, viewGroup, false);
        }
//
//
//        viewOther.post(new Runnable() {
//            @Override
//            public void run() {

                ImageView icon = (ImageView) viewOther.findViewById(R.id.icon);

                icon.setBackgroundResource(data.getIcon());




//                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(viewOther.getWidth(), viewOther.getWidth());
                ((ImageView) viewOther.findViewById(R.id.backimg)).setBackgroundResource(data.getBack_img());
//        viewOther.setLayoutParams(param);

                TextView txtv_title = (TextView) viewOther.findViewById(R.id.tvTitle);
                TextView txtv_date_time = (TextView) viewOther.findViewById(R.id.tvSubTitle);

                txtv_title.setText(data.getTitle());
                txtv_date_time.setText(data.getSubTitle());

//
//            }
//        });

        return viewOther;
    }
}