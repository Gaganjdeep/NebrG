package gagan.com.communities.activites.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.HomeAdapter;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostProfileFragment extends BaseFragmentG
{


    public PostProfileFragment()
    {
        // Required empty public constructor
    }


    HomeAdapter homeadapter;
    ListView    listViewPost;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View v = inflater.inflate(R.layout.fragment_post_profile, container, false);

        listViewPost = (ListView) v.findViewById(R.id.listViewPost);


        if (getArguments().getString("userid").equals("g"))
        {
            fetchCommunityPost();
        }
        else
        {
            fetchHomeData();
        }


        setHasOptionsMenu(true);
        return v;
    }


    private void fetchHomeData()
    {

        try
        {


            listHome = new ArrayList<>();
            listHome.clear();


            JSONObject data = new JSONObject();
            data.put("userid", getArguments().getString("userid"));
            data.put("limit", "100");
            data.put("startId", "0");

            new SuperWebServiceG(GlobalConstants.URL + "getUserPost", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    processOutput(output,false);

                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void fetchCommunityPost()
    {

        try
        {
            listHome = new ArrayList<>();
            listHome.clear();


            JSONObject data = new JSONObject();
            data.put("c_id", getArguments().getString("c_id"));
            data.put("userid", sharedPrefHelper.getUserId());

            new SuperWebServiceG(GlobalConstants.URL + "getcommunitypost", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    processOutput(output,true);

                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    List<HomeModel> listHome;

    //   ==============process output++++
    private void processOutput(String response,boolean Iscommunitypost)
    {

        try
        {

            JSONObject jsonMain = new JSONObject(response);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
            {

                JSONArray jsonarrayData ;


                if (Iscommunitypost)
                {
                    jsonarrayData  = jsonMainResult.getJSONArray("groupData");
                }
                else
                {
                    jsonarrayData  = jsonMainResult.getJSONArray("feedData");
                }

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



                    listHome.add(homemodel);
                }
                homeadapter = new HomeAdapter(getActivity(), listHome);
                listViewPost.setAdapter(homeadapter);


            }
            else
            {
                Utills.showToast("No post available", getActivity(), true);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}
