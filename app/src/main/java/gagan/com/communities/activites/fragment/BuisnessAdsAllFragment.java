package gagan.com.communities.activites.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.BuisnessAdsAdapter;
import gagan.com.communities.models.GridModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuisnessAdsAllFragment extends Fragment {


    public BuisnessAdsAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buisness_ads_all, container, false);


        GridView gridView=(GridView)v.findViewById(R.id.grid);


        List<GridModel> DataList=new ArrayList<>();

        DataList.add(new GridModel(R.mipmap.restaurants_icon,R.drawable.restaurants_img,"Resturants","900ads"));
        DataList.add(new GridModel(R.mipmap.apparel_icon,R.drawable.apparel_img,"Apparel","10ads"));
        DataList.add(new GridModel(R.mipmap.grocery_icon,R.drawable.grocery_img,"Grocery","10ads"));
        DataList.add(new GridModel(R.mipmap.saloon_icon,R.drawable.saloon_img,"Saloon/SPA","10ads"));
        DataList.add(new GridModel(R.mipmap.doctors_icon,R.drawable.doctors_img,"Doctors","10ads"));
        DataList.add(new GridModel(R.mipmap.service_icon,R.drawable.service_icon_img,"Service","10ads"));
        DataList.add(new GridModel(R.mipmap.electronics_icon,R.drawable.electronics_img,"Electronics","10ads"));
        DataList.add(new GridModel(R.mipmap.other_icon,R.drawable.other_img_buisness,"Others","10ads"));

        BuisnessAdsAdapter msgAdapter=new BuisnessAdsAdapter(getActivity(),DataList);
        gridView.setAdapter(msgAdapter);

        return v;
    }

}
