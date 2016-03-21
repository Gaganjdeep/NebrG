package gagan.com.communities.activites.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.MyAdvertisements;
import gagan.com.communities.adapters.BuisnessAdsAdapter;
import gagan.com.communities.models.GridModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalAdsGridFragment extends Fragment implements View.OnClickListener {


    public PersonalAdsGridFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(),0);
//
//    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_personalad_gridfragment, container, false);


//        EditText edSearch=(EditText)v.findViewById(R.id.edSearch);
//        edSearch.setFocusable(false);


        final GridView gridView=(GridView)v.findViewById(R.id.grid);
         Button btnClassifiedsAll=(Button)v.findViewById(R.id.btnClassifiedsAll);
        btnClassifiedsAll.setOnClickListener(this);


        final List<GridModel> DataList=new ArrayList<>();

        DataList.add(new GridModel(R.mipmap.service_icon,R.drawable.service_icon_img,"Services","900ads"));
        DataList.add(new GridModel(R.mipmap.jobs_icon,R.drawable.jobs_icon_img,"Jobs","10ads"));
        DataList.add(new GridModel(R.mipmap.real_estate_icon,R.drawable.real_estate_img,"Real Estate","10ads"));
        DataList.add(new GridModel(R.mipmap.furnitures_icon,R.drawable.furnitures_img,"Furniture","10ads"));
        DataList.add(new GridModel(R.mipmap.pets_icon,R.drawable.pets_iimg,"Pets","10ads"));
        DataList.add(new GridModel(R.mipmap.other_icon,R.drawable.other_img,"Others","10ads"));



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                BuisnessAdsAdapter msgAdapter=new BuisnessAdsAdapter(getActivity(),DataList);
                gridView.setAdapter(msgAdapter);
//            }
//        },1500);


//        gridView.requestFocus();
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        return v;
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), MyAdvertisements.class));
    }
}