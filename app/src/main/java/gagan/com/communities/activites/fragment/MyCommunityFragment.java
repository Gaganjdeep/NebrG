package gagan.com.communities.activites.fragment;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.CommunitiesAdapter;
import gagan.com.communities.adapters.HomeAdapter;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.CurrentLocFragment;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCommunityFragment extends CurrentLocFragment {

    RecyclerView listview;

    public MyCommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_community, container, false);


        listview = (RecyclerView) v.findViewById(R.id.listview);

        if(getArguments().getBoolean("myData"))
        {
            fetchData();
        }




        return v;

    }

    private void fetchData() {
        try {
            JSONObject data = new JSONObject();
            data.put("user_id", sharedPrefHelper.getUserId());



            new SuperWebServiceG(GlobalConstants.URL + "mycommunity", data, new CallBackWebService() {
                @Override
                public void webOnFinish(String output) {


                    processOutput(output);

                }
            }).execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void fetchDataNearby(Location currentLocation) {
        try {
            JSONObject data = new JSONObject();
            data.put("lat",currentLocation.getLatitude() );
            data.put("long", currentLocation.getLongitude());
            data.put("start","0");
            data.put("limit","100");
            data.put("distance", "50");


            new SuperWebServiceG(GlobalConstants.URL + "mynearcommunity", data, new CallBackWebService() {
                @Override
                public void webOnFinish(String output) {


                    processOutput(output);

                }
            }).execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processOutput(String output) {
        try {

            List<CommunitiesListModel> list = new ArrayList<>();

            JSONObject jsonMain = new JSONObject(output);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201")) {

                JSONArray jsonarrayData = jsonMainResult.getJSONArray("community");

                for (int g = 0; g < jsonarrayData.length(); g++) {
                    JSONObject jobj = jsonarrayData.optJSONObject(g);
                    CommunitiesListModel data = new CommunitiesListModel();

                    data.setCid(jobj.optString("cid"));
                    data.setC_description(jobj.optString("c_description"));
                    data.setC_genre(jobj.optString("c_genre"));
                    data.setC_name(jobj.optString("c_name"));

                    data.setCreated_at(jobj.optString("created_at"));
                    try {
                        LatLng latlng = new LatLng(Double.parseDouble(jobj.optString("c_lat")), Double.parseDouble(jobj.optString("c_long")));
                        data.setLatLng(latlng);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                    data.setOwner_id(jobj.optString("owner_id"));
                    data.setUser_id(jobj.optString("user_id"));
                    list.add(data);
                }

                listview.setLayoutManager(new LinearLayoutManager(getActivity()));
                listview.setAdapter(new CommunitiesAdapter(getActivity(), list));

            }
            else {
                Utills.showToast("No communities available", getActivity(), true);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getCurrentLocationG(Location currentLocation)
    {
        if(!getArguments().getBoolean("myData"))
        {
            fetchDataNearby(currentLocation);
        }


    }
}
