package gagan.com.communities.activites.fragment;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.adapters.HomeAdapter;
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
public class PostsFragment extends SupportMapFragment implements GoogleMap.OnMyLocationChangeListener

{



    private  GoogleMap   googleMapPost;


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




                googleMapPost.setOnMyLocationChangeListener(PostsFragment.this);

            }
        });
    }





    //    ====================marker
    private void setUpMap(String name, String genre, LatLng data)
    {


        LatLng markerLoc = data;

        Marker markr = googleMapPost.addMarker(new MarkerOptions().position(markerLoc).draggable(false).title(name).snippet(genre).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));

        markr.showInfoWindow();
//    MarkerData.put(markr.getId(), data);

        googleMapPost.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
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


        if (googleMapPost == null)
        {
            initializeMapFragment();
        }


    }


    private void fetchHomeData(Location location)
    {

        try
        {
            JSONObject data = new JSONObject();
            data.put("userid", new SharedPrefHelper(getActivity()).getUserId());
            data.put("limit", "100");
            data.put("start", "0");
            data.put("lat", location.getLatitude() + "");
            data.put("long", location.getLongitude() + "");

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


                    double lat = Double.parseDouble(jobj.optString("lat"));
                    double lng = Double.parseDouble(jobj.optString("lng"));

                    setUpMap(jobj.optString("title"), jobj.optString("type"), new LatLng(lat, lng));

                }
//                "post": [
//                {
//                    "message": "bshbwbbss",
//                        "id": "43",
//                        "username": "Vz",
//                        "distance": "0.16712854019496423",
//                        "anon_user": "0",
//                        "title": "bsbsbdbbdhdhh",
//                        "create_date": "2016-03-10 18:46:36",
//                        "location": "dav public school river side",
//                        "userid": "24",
//                        "image": "http://orasisdata.com/Neiber/images/1457635596.png",
//                        "lng": "76.8745",
//                        "profile_pic": "",
//                        "lat": "30.336"
//                }
//                ],


            }
            else
            {
                Utills.showToast("No post available", getActivity(), true);

                googleMapPost.setOnMyLocationChangeListener(PostsFragment.this);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
//            fetchHomeData(startId, limit);
        }

    }

    @Override
    public void onMyLocationChange(Location location)
    {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 6);
        googleMapPost.animateCamera(cameraUpdate);
        fetchHomeData(location);

        googleMapPost.setOnMyLocationChangeListener(null);
    }


//   =============================


}