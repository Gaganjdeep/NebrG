package gagan.com.communities.activites.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gagan.com.communities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalAdsMapFragment extends Fragment {


    public PersonalAdsMapFragment() {
        // Required empty public constructor
    }
    View rootView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_personal_ads_map, container, false);
        }
        catch (Exception | Error e) {
            e.printStackTrace();
        }

//        initializeMapFragment();


        return rootView;
    }

}
