package gagan.com.communities.activites.fragment;


import android.content.Intent;
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

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.activites.ShowPostActivity;
import gagan.com.communities.models.HomeModel;
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
                    googleMapPost.setOnMyLocationChangeListener(PostsFragment.this);
                }
                else
                {

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(modelToSet.getLatLng(), 9);
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


                    double lat = Double.parseDouble(jobj.optString("lat"));
                    double lng = Double.parseDouble(jobj.optString("lng"));
                    homemodel.setLatLng(new LatLng(lat, lng));

                    setUpMap(jobj.optString("title"), jobj.optString("type"), new LatLng(lat, lng), homemodel);

                }

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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 9);
        googleMapPost.animateCamera(cameraUpdate);
        fetchHomeData(location);

        googleMapPost.setOnMyLocationChangeListener(null);
    }


//   =============================


}
