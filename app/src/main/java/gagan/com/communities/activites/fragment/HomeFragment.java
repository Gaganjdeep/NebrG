package gagan.com.communities.activites.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullToRefreshListView;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.AddPostActivity;
import gagan.com.communities.activites.CurrentLocationPostActivity;
import gagan.com.communities.adapters.HomeAdapter;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.CallBackG;
import gagan.com.communities.utills.CallBackNotifierHome;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragmentG implements CallBackNotifierHome, SearchView.OnQueryTextListener, PullAndLoadListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener, CallBackG
{


    public HomeFragment()
    {
        // Required empty public constructor
    }

    PullAndLoadListView listViewNotiMsg;
    HomeAdapter         homeadapter;
    ProgressBar         progressBar;

    TextView tvNoPost;
    public static HomeFragment homeFragment;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View v = null;
        try
        {

            v = inflater.inflate(R.layout.home_fragment, container, false);


            settingActionBar(v);

            homeFragment = this;

            tvNoPost = (TextView) v.findViewById(R.id.tvNoPost);

            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);
            listViewNotiMsg = (PullAndLoadListView) v.findViewById(R.id.listViewHomeList);


            if (listHome != null)
            {
                progressBar.setVisibility(View.GONE);
                homeadapter = new HomeAdapter(getActivity(), listHome);
                listViewNotiMsg.setAdapter(homeadapter);
            }


            listHome = new ArrayList<>();
            listHome.clear();
            listViewNotiMsg.setOnLoadMoreListener(this);
            listViewNotiMsg.setOnRefreshListener(this);

            limit = 10;
            startId = 0;


            fetchHomeData(startId, limit);


            setHasOptionsMenu(true);
            return v;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return v;
    }


    int limit = 10, startId = 0;

    private void fetchHomeData(int start, final int end)
    {

        try
        {

            if (startId == 0)
            {
                progressBar.setVisibility(View.VISIBLE);
            }

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("limit", limit + "");
            data.put("startId", startId + "");
            data.put("distance", sharedPrefHelper.getDistanceParamHome() + "");


            new SuperWebServiceG(GlobalConstants.URL + "homeFeeduserradius", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {


                    startId = limit;
                    limit = limit + 10;


                    processOutput(output);


                    listViewNotiMsg.onLoadMoreComplete();

                    listViewNotiMsg.onRefreshComplete();


                    if (progressBar.getVisibility() == View.VISIBLE)
                    {
                        progressBar.setVisibility(View.GONE);
                    }

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
    private void processOutput(String response)
    {

        try
        {

            JSONObject jsonMain = new JSONObject(response);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
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
                tvNoPost.setVisibility(View.GONE);
                for (int g = 0; g < jsonarrayData.length(); g++)
                {
                    JSONObject jobj = jsonarrayData.optJSONObject(g);

                    HomeModel homemodel = new HomeModel();
                    homemodel.setId(jobj.optString("id"));

                    homemodel.setLocation(jobj.optString("home_location"));

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
                if (homeadapter == null)
                {
                    homeadapter = new HomeAdapter(getActivity(), listHome);
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
                tvNoPost.setVisibility(View.VISIBLE);
                Utills.showToast("No post available", getActivity(), true);
            }

        }
        catch (Exception e)
        {
            if (listHome == null || listHome.isEmpty())
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
//        View v = listViewNotiMsg.getChildAt(0);
//        top = (v == null) ? 0 : v.getTop();


    }


    private void settingActionBar(View view)
    {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        ImageView txtvAddPost = (ImageView) toolbar.findViewById(R.id.txtvAddPost);

        if (!sharedPrefHelper.getPincodeStatus())
        {
            txtvAddPost.setAlpha(0.4f);
            txtvAddPost.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Utills.show_dialog_msg(getActivity(), "We are yet to launch in your location. Please Change your Home location in Settings to Add post", null);
                }
            });
        }
        else
        {
            txtvAddPost.setAlpha(1f);
            txtvAddPost.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(getActivity(), AddPostActivity
                            .class);
                    i.putExtra("Cid", "");
                    startActivity(i);

                }
            });
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.home_menu, menu);


//        MenuItem gMenu = menu.add("Search");
//        gMenu.setIcon(R.mipmap.ic_search);
//        gMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//
//        SearchView searchView = new SearchView(getActivity());

//        searchView.setOnQueryTextListener(this);

//        gMenu.setActionView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case R.id.navigate_home:

                Intent intnt = new Intent(getActivity(), CurrentLocationPostActivity
                        .class);
                startActivity(intnt);

                break;
            case R.id.set_distance:

                Utills.ShowDialogProgress(getActivity(), this);


                break;


        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroy()
    {
        sharedPrefHelper.setDistanceParamHome(20);
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        return false;
    }

    @Override
    public void onLoadMore()
    {

        saveVisibleItemPosiotion();
        fetchHomeData(startId, limit);
    }

    @Override
    public void onRefresh()
    {
        homeadapter = null;
        if (listHome != null)
        {
            listHome.clear();
        }
        index = 0;
        limit = 10;
        startId = 0;
        fetchHomeData(startId, limit);
    }

    @Override
    public void notifier(int index, String count)
    {

        if (listHome.size() >= index)
        {
            listHome.get(index).setComments_count(count);
            homeadapter.notifyDataSetChanged();
        }

    }

    @Override
    public void OnFinishG(Object output)
    {
        homeadapter = null;
        if (listHome != null)
        {
            listHome.clear();
        }
        index = 0;
        limit = 10;
        startId = 0;
        fetchHomeData(startId, limit);
    }
}
