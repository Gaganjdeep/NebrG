package gagan.com.communities.activites.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.CommunityDetailsActivity;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.utills.CallBackG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Communitiesfragment extends SupportMapFragment implements GoogleMap.OnMyLocationChangeListener
{


    private GoogleMap googleMapCommunity;

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

                if (communitiesListModelG == null)
                {
                    googleMapCommunity.setOnMyLocationChangeListener(Communitiesfragment.this);
                }
                else
                {

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(communitiesListModelG.getLatLng(), 9);
                    googleMapCommunity.animateCamera(cameraUpdate);

                    setUpMap(communitiesListModelG);
                }


            }
        });


    }


    public void setGoogleMapCommunity(CommunitiesListModel data)
    {

        communitiesListModelG = data;

    }

    CommunitiesListModel communitiesListModelG;

    HashMap<String, CommunitiesListModel> MarkerDataC;


    //    ====================marker
    private void setUpMap(CommunitiesListModel data)
    {


        LatLng markerLoc = data.getLatLng();

        Marker markr = googleMapCommunity.addMarker(new MarkerOptions().position(markerLoc).draggable(false).title(data.getC_name()).snippet(data.getC_genre()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));

        markr.showInfoWindow();


        if (MarkerDataC != null)
        {
            MarkerDataC.put(markr.getId(), data);
        }

        googleMapCommunity.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {

            @Override
            public void onInfoWindowClick(Marker arg0)
            {
                if (MarkerDataC != null)
                {
                    Intent intnt = new Intent(getActivity(), CommunityDetailsActivity.class);
                    intnt.putExtra("data", MarkerDataC.get(arg0.getId()));
                    getActivity().startActivity(intnt);

                }

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
        if (!mIsReceiverRegistered)
        {
            if (mReceiver == null)
                mReceiver = new UpdateCommunityReceiver();
            getActivity().registerReceiver(mReceiver, new IntentFilter(GlobalConstants.UPDATE_MAPTAB));
            mIsReceiverRegistered = true;
        }
    }
    @Override
    public void onDestroy()
    {
        if (mIsReceiverRegistered)
        {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
            mIsReceiverRegistered = false;
        }
        super.onDestroy();
    }


    private void fetchDataNearby(Location currentLocation)
    {
        try
        {
            JSONObject data = new JSONObject();
            data.put("lat", currentLocation.getLatitude());
            data.put("long", currentLocation.getLongitude());
            data.put("start", "0");
            data.put("limit", "100");
            data.put("distance", "50");


            new SuperWebServiceG(GlobalConstants.URL + "mynearcommunity", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {


                    processOutput(output);

                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //   ==============process output++++
    private void processOutput(String output)
    {
        try
        {

            List<CommunitiesListModel> list = new ArrayList<>();

            JSONObject jsonMain = new JSONObject(output);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
            {

                JSONArray jsonarrayData = jsonMainResult.getJSONArray("community");

                for (int g = 0; g < jsonarrayData.length(); g++)
                {
                    JSONObject           jobj = jsonarrayData.optJSONObject(g);
                    CommunitiesListModel data = new CommunitiesListModel();

                    data.setCid(jobj.optString("cid"));
                    data.setC_description(jobj.optString("c_description"));
                    data.setC_genre(jobj.optString("c_genre"));
                    data.setC_name(jobj.optString("c_name"));
                    data.setCreated_at(jobj.optString("created_at"));
                    try
                    {
                        LatLng latlng = new LatLng(Double.parseDouble(jobj.optString("c_lat")), Double.parseDouble(jobj.optString("c_long")));
                        data.setLatLng(latlng);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    data.setOwner_id(jobj.optString("owner_id"));
                    data.setUser_id(jobj.optString("user_id"));

                    setUpMap(data);
                    list.add(data);
                }


            }
            else
            {
                googleMapCommunity.setOnMyLocationChangeListener(Communitiesfragment.this);
                Utills.showToast("No communities available", getActivity(), true);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static Location locationToSearch;

    @Override
    public void onMyLocationChange(Location location)
    {
        MarkerDataC = new HashMap<>();

        if (locationToSearch == null)
        {
            locationToSearch = location;
        }


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locationToSearch.getLatitude(), locationToSearch.getLongitude()), 9);
        googleMapCommunity.animateCamera(cameraUpdate);
        fetchDataNearby(locationToSearch);

        googleMapCommunity.setOnMyLocationChangeListener(null);
    }

//   =============================


    UpdateCommunityReceiver mReceiver;
    private boolean mIsReceiverRegistered = false;


    private class UpdateCommunityReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {


                if (MarkerDataC == null)
                {
                    MarkerDataC = new HashMap<>();
                }
                else
                {
                    MarkerDataC.clear();
                }


                if (googleMapCommunity == null)
                {

                    onResume();
//
                }
                else
                {
                    googleMapCommunity.clear();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locationToSearch.getLatitude(), locationToSearch.getLongitude()), 9);
                    googleMapCommunity.animateCamera(cameraUpdate);


                    fetchDataNearby(locationToSearch);

                    googleMapCommunity.setOnMyLocationChangeListener(null);
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }


}