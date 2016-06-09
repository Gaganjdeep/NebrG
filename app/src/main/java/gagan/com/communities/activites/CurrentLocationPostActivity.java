package gagan.com.communities.activites;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.HomeAdapter;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.CallBackG;
import gagan.com.communities.utills.CurrentLocActivityG;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class CurrentLocationPostActivity extends CurrentLocActivityG implements PullAndLoadListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener, CallBackG
{


//    SharedPreferences pref;

    @Override
    public void getCurrentLocationG(Location currentLocation)
    {
        fetchHomeData(currentLocation);
    }

    PullAndLoadListView listViewNotiMsg;
    HomeAdapter         homeadapter;

    TextView tvNoPost;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_post);
        settingActionBar();


        tvNoPost = (TextView) findViewById(R.id.tvNoPost);
        listViewNotiMsg = (PullAndLoadListView) findViewById(R.id.listViewHomeList);

        if (listHome != null)
        {
            homeadapter = new HomeAdapter(CurrentLocationPostActivity.this, listHome);
            listViewNotiMsg.setAdapter(homeadapter);
            tvNoPost.setVisibility(View.INVISIBLE);
        }


        listHome = new ArrayList<>();
        listHome.clear();
        listViewNotiMsg.setOnLoadMoreListener(this);
        listViewNotiMsg.setOnRefreshListener(this);

        limit = 10;
        startId = 0;

    }


//
//    @Override
//    protected void onPause()
//    {
//        pref.unregisterOnSharedPreferenceChangeListener(listener);
//        super.onPause();
//    }

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }


    int limit = 10, startId = 0;

    private void fetchHomeData(Location location)
    {

        try
        {
            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("limit", limit + "");
            data.put("startId", startId + "");
            data.put("lat", location.getLatitude() + "");
            data.put("long", location.getLongitude() + "");
            data.put("distance", sharedPrefHelper.getDistanceParam());

            new SuperWebServiceG(GlobalConstants.URL + "nerbyPost", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    startId = limit;
                    limit = limit + 10;


                    processOutput(output);


                    listViewNotiMsg.onLoadMoreComplete();

                    listViewNotiMsg.onRefreshComplete();
                }
            }).execute();
        }
        catch (Exception e)
        {
            tvNoPost.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }

    }


    List<HomeModel> listHome;

    //   ==============process output++++
    private void processOutput(String response)
    {

        try
        {

            JSONObject jsonMain = new JSONObject(response);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("200"))
            {

                JSONArray jsonarrayData = jsonMainResult.getJSONArray("feedData");


                if (jsonarrayData.length() > 9)
                {
                    listViewNotiMsg.setOnLoadMoreListener(this);
                }
                else
                {
                    listViewNotiMsg.setOnLoadMoreListener(null);
                }


                for (int g = 0; g < jsonarrayData.length(); g++)
                {
                    JSONObject jobj = jsonarrayData.optJSONObject(g);

                    HomeModel homemodel = new HomeModel();
                    homemodel.setId(jobj.optString("id"));

                    homemodel.setLocation(jobj.optString("post_location"));
//                    homemodel.setLocation(jobj.optString("home_location"));

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


                    listHome.add(homemodel);
//                "id": "1", "like_count": "1",
//                    "dislike_count": "0"
//                    "userid": "1",
//                    "title": "post1",
//                    "message": "message",
//                    "path": "",
//                    "image": "",
//                    "location": "chandigarh",
//                    "type": "",
//                    "create_date": "2016-02-19 00:00:00",
//                    "status": "0",
//                    "comments_count": "1"
                }

                if (listHome.isEmpty()
                        )
                {
                    tvNoPost.setVisibility(View.VISIBLE);
                    Utills.showToast("No post available", CurrentLocationPostActivity.this, true);
                    return;
                }
                tvNoPost.setVisibility(View.INVISIBLE);
                if (homeadapter == null)
                {
                    homeadapter = new HomeAdapter(CurrentLocationPostActivity.this, listHome);
                    listViewNotiMsg.setAdapter(homeadapter);
                }
                else
                {
//                    listViewNotiMsg.invalidateViews();

                    if (jsonarrayData.length() > 0)
                    {
                        homeadapter.notifyDataSetChanged();

                        scrollToLastKnownTop();
                    }

                }

            }
            else
            {
                if (listHome.isEmpty())
                {
                    tvNoPost.setVisibility(View.VISIBLE);
                }
                Utills.showToast("No post available", CurrentLocationPostActivity.this, true);
            }

        }
        catch (Exception e)
        {
            if (listHome.isEmpty())
            {
                tvNoPost.setVisibility(View.VISIBLE);
            }
            e.printStackTrace();
//            fetchHomeData(startId, limit);
        }

    }
//   =============================

    private void scrollToLastKnownTop()
    {

        new Handler().post(new Runnable()
        {
            @Override
            public void run()
            {

                listViewNotiMsg.setSelection(index);

            }
        });
    }

    int index = 0, top = 0;

    private void saveVisibleItemPosiotion()
    {
        // save index and top position
        index = listViewNotiMsg.getFirstVisiblePosition();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.set_distance, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case R.id.set_distance:

                Utills.ShowDialogProgress(CurrentLocationPostActivity.this, this);


                break;
            case android.R.id.home:
                finish();
                break;


        }

        return super.onOptionsItemSelected(item);
//        return true;
    }


    @Override
    public void onLoadMore()
    {
        saveVisibleItemPosiotion();
        displayLocation();
    }

    @Override
    public void onRefresh()
    {
        if (listHome != null)
        {
            listHome.clear();
        }
        index = 0;
        limit = 10;
        startId = 0;
        displayLocation();
    }

    @Override
    public void OnFinishG(Object output)
    {
        if (!listHome.isEmpty())
        {
            listHome.clear();
        }
        limit = 10;
        startId = 0;

        displayLocation();
    }

    @Override
    protected void onDestroy()
    {
        sharedPrefHelper.setDistanceParam(20);
        super.onDestroy();
    }



    /* SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
        {
            if (key.equals("distanceParam"))
            {
                if (!listHome.isEmpty())
                {
                    listHome.clear();
                }


                displayLocation();

            }
        }
    };*/


}