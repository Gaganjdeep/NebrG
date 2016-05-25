package gagan.com.communities.activites.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.activites.ShowPostActivity;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends SupportMapFragment implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraChangeListener

{


    private GoogleMap googleMapPost;

    public PostsFragment()
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
                googleMapPost = googleMap;

                googleMapPost.setMyLocationEnabled(true);

                googleMapPost.getUiSettings().setMapToolbarEnabled(false);


                if (modelToSet == null)
                {
//                    googleMapPost.setOnMyLocationChangeListener(PostsFragment.this);
                    googleMapPost.setOnCameraChangeListener(PostsFragment.this);


                    if (locationToSearchP!=null)
                    {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locationToSearchP.getLatitude(), locationToSearchP.getLongitude()), 12);
                        googleMapPost.animateCamera(cameraUpdate);
                    }
                }
                else
                {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(modelToSet.getLatLng(), 12);
                    googleMapPost.animateCamera(cameraUpdate);

                    setUpMap(modelToSet.getTitle(), modelToSet.getType(), modelToSet.getLatLng(), modelToSet);
                }


            }
        });
    }


    HomeModel modelToSet;

    public void setPost(HomeModel homemodel)
    {
        modelToSet = homemodel;
    }


    HashMap<String, HomeModel> MarkerData;

    //    ====================marker
    private void setUpMap(String name, String genre, LatLng data, HomeModel homeModel)
    {


        LatLng markerLoc = data;

        Marker markr = googleMapPost.addMarker(new MarkerOptions().position(markerLoc).draggable(false).title(name).snippet(genre).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));

        markr.showInfoWindow();
        if (MarkerData != null)
        {
            MarkerData.put(markr.getId(), homeModel);
        }
        googleMapPost.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {

            @Override
            public void onInfoWindowClick(Marker arg0)
            {

                if (MarkerData != null)
                {
                    Intent intnt = new Intent(getActivity(), ShowPostActivity.class);
                    intnt.putExtra("data", MarkerData.get(arg0.getId()));
                    startActivity(intnt);
                }

            }
        });

    }


    @Override
    public void onResume()
    {
        super.onResume();

        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getActivity());

        if (googleMapPost == null)
        {
            initializeMapFragment();
        }
        if (!mIsReceiverRegistered)
        {
            if (mReceiver == null)
                mReceiver = new UpdatePostReceiver();
            getActivity().registerReceiver(mReceiver, new IntentFilter(GlobalConstants.UPDATE_MAPTAB));
            mIsReceiverRegistered = true;
        }




        if (locationToSearchP == null)
        {
            try
            {
                locationToSearchP = new Location("");
                locationToSearchP.setLatitude(Double.parseDouble(sharedPrefHelper.getHomeLocation().get(SharedPrefHelper.HOME_LAT)));
                locationToSearchP.setLongitude(Double.parseDouble(sharedPrefHelper.getHomeLocation().get(SharedPrefHelper.HOME_LNG)));

            }
            catch (Exception e)
            {
                googleMapPost.setOnMyLocationChangeListener(PostsFragment.this);
                locationToSearchP = null;
                e.printStackTrace();
            }
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

    private void fetchHomeData(Location location)
    {

        try
        {
            JSONObject data = new JSONObject();
            data.put("userid", new SharedPrefHelper(getActivity()).getUserId());
            data.put("limit", "15");
            data.put("start", "0");
            data.put("lat", location.getLatitude() + "");
            data.put("long", location.getLongitude() + "");
            data.put("distance", "100");

            new SuperWebServiceG(GlobalConstants.URL + "mynearpost", data, new CallBackWebService()
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
    private void processOutput(String response)
    {

        try
        {

            JSONObject jsonMain = new JSONObject(response);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
            {


                if (googleMapPost == null)
                {
                    return;
                }

                googleMapPost.clear();


                JSONArray jsonarrayData = jsonMainResult.getJSONArray("post");

                MarkerData = new HashMap<>();

                for (int g = 0; g < jsonarrayData.length(); g++)
                {
                    JSONObject jobj = jsonarrayData.optJSONObject(g);

                    HomeModel homemodel = new HomeModel();
                    homemodel.setId(jobj.optString("id"));
                    homemodel.setLocation(jobj.optString("location"));

                    homemodel.setComments_count(jobj.optString("comments_count"));
                    homemodel.setCreate_date(jobj.optString("create_date"));
                    homemodel.setImage(jobj.optString("image"));
                    homemodel.setMessage(jobj.optString("message"));
                    homemodel.setUserid(jobj.optString("userid"));
                    homemodel.setTitle(jobj.optString("title"));
                    homemodel.setType(jobj.optString("type"));
                    homemodel.setLike_count(jobj.optString("like_count"));
                    homemodel.setDislike_count(jobj.optString("dislike_count"));
                    homemodel.setUsername(jobj.optString("username"));
                    homemodel.setProfile_pic(jobj.optString("profile_pic"));

                    homemodel.setIs_liked(jobj.optString("is_liked").equals("1"));
                    homemodel.setIs_disliked(jobj.optString("is_disliked").equals("1"));
                    homemodel.setAnon_user(jobj.optString("anon_user").equals("1"));


                    double lat = Double.parseDouble(jobj.optString("tag_lat"));
                    double lng = Double.parseDouble(jobj.optString("tag_long"));
                    homemodel.setLatLng(new LatLng(lat, lng));

                    setUpMap(jobj.optString("title"), jobj.optString("type"), new LatLng(lat, lng), homemodel);

                }

            }
            else
            {
//                Utills.showToast("No post available", getActivity(), true);

//                googleMapPost.setOnMyLocationChangeListener(PostsFragment.this);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
//            fetchHomeData(startId, limit);
        }

    }


    public static Location locationToSearchP;

    @Override
    public void onMyLocationChange(Location location)
    {

        if (locationToSearchP == null)
        {
            locationToSearchP = location;
        }


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locationToSearchP.getLatitude(), locationToSearchP.getLongitude()), 12);
        googleMapPost.animateCamera(cameraUpdate);
//        fetchHomeData(location);

        googleMapPost.setOnMyLocationChangeListener(null);
    }


    //   =============================
    UpdatePostReceiver mReceiver;
    private boolean mIsReceiverRegistered = false;

    @Override
    public void onCameraChange(final CameraPosition cameraPosition)
    {

        if (locationToSearchP == null)
        {
            return;
        }


        if (ctimerP != null)
        {
            ctimerP.cancel();
        }

        ctimerP = new CountDownTimer(1500, 1500)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {

            }

            @Override
            public void onFinish()
            {
                if (MarkerData == null)
                {
                    MarkerData = new HashMap<>();
                }
                else
                {
                    MarkerData.clear();
                }
                locationToSearchP.setLatitude(cameraPosition.target.latitude);
                locationToSearchP.setLongitude(cameraPosition.target.longitude);


                fetchHomeData(locationToSearchP);
            }
        }.start();


    }

    CountDownTimer ctimerP;

    private class UpdatePostReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {


                if (MarkerData == null)
                {
                    MarkerData = new HashMap<>();
                }
                else
                {
                    MarkerData.clear();
                }

                if (googleMapPost == null)
                {
                    onResume();
                }
                else
                {

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locationToSearchP.getLatitude(), locationToSearchP.getLongitude()), 12);
                    googleMapPost.animateCamera(cameraUpdate);


                    googleMapPost.setOnMyLocationChangeListener(null);
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
