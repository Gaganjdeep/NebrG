package gagan.com.communities.activites.fragment;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.CurrentLocFragment;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Communitiesfragment extends SupportMapFragment implements GoogleMap.OnMyLocationChangeListener
{


    private GoogleMap   googleMapCommunity;

    public Communitiesfragment()
    {
        // Required empty public constructor
    }


    private void initializeMapFragment()
    {

        getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                googleMapCommunity = googleMap;

                googleMapCommunity.setMyLocationEnabled(true);
                googleMapCommunity.getUiSettings().setMapToolbarEnabled(false);

                googleMapCommunity.setOnMyLocationChangeListener(Communitiesfragment.this);

            }
        });



    }


    //    ====================marker
    private void setUpMap(CommunitiesListModel data)
    {


        LatLng markerLoc = data.getLatLng();

        Marker markr = googleMapCommunity.addMarker(new MarkerOptions().position(markerLoc).draggable(false).title(data.getC_name()).snippet(data.getC_genre()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));

        markr.showInfoWindow();
//    MarkerData.put(markr.getId(), data);

        googleMapCommunity.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {

            @Override
            public void onInfoWindowClick(Marker arg0)
            {


            }
        });

    }


    @Override
    public void onResume()
    {
        super.onResume();


        if (googleMapCommunity == null)
        {
            initializeMapFragment();
        }


    }



    private void fetchDataNearby(Location currentLocation) {
        try {
            JSONObject data = new JSONObject();
            data.put("lat", currentLocation.getLatitude());
            data.put("long", currentLocation.getLongitude());
            data.put("start", "0");
            data.put("limit", "100");
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

    //   ==============process output++++
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

                    setUpMap(data);
                    list.add(data);
                }


            }
            else {
                googleMapCommunity.setOnMyLocationChangeListener(Communitiesfragment.this);
                Utills.showToast("No communities available", getActivity(), true);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMyLocationChange(Location location)
    {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 6);
        googleMapCommunity.animateCamera(cameraUpdate);
        fetchDataNearby(location);

        googleMapCommunity.setOnMyLocationChangeListener(null);
    }
//   =============================


}