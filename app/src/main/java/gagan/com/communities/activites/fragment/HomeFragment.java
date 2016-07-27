package gagan.com.communities.activites.fragment;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.costum.android.widget.LoadMoreListView;
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
public class HomeFragment extends BaseFragmentG implements CallBackNotifierHome, SearchView.OnQueryTextListener, CallBackG, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener
{


    public HomeFragment()
    {
        // Required empty public constructor
    }

    LoadMoreListView listViewNotiMsg;
    HomeAdapter      homeadapter;
    ProgressBar      progressBar;

    TextView tvNoPost, tvNewPost;
    public static HomeFragment homeFragment;

    SwipeRefreshLayout swipeRefresh;

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
            swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);

            tvNoPost = (TextView) v.findViewById(R.id.tvNoPost);

            tvNewPost = (TextView) v.findViewById(R.id.tvNewPost);

            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);
            listViewNotiMsg = (LoadMoreListView) v.findViewById(R.id.listViewHomeList);


            if (listHome != null)
            {
                progressBar.setVisibility(View.GONE);
                homeadapter = new HomeAdapter(getActivity(), listHome);
                listViewNotiMsg.setAdapter(homeadapter);
            }


            listHome = new ArrayList<>();


            limit = 10;
            startId = 0;

            index = 0;
            top = 0;

            fetchHomeData(startId, limit);


            listViewNotiMsg.setOnLoadMoreListener(this);
//            listViewNotiMsg.setOnRefreshListener(this);

            swipeRefresh.setOnRefreshListener(this);

            setHasOptionsMenu(true);
            return v;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return v;
    }


    @Override
    public void onResume()
    {


        super.onResume();
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


                    processOutput(output);


                    listViewNotiMsg.onLoadMoreComplete();

                    swipeRefresh.setRefreshing(false);


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


                startId = limit;
                limit = limit + 10;


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

//                    homemodel.setLocation(jobj.optString("home_location"));
                    homemodel.setLocation(jobj.optString("post_location"));

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


//                    double lat = Double.parseDouble(jobj.optString("lat"));
//                    double lng = Double.parseDouble(jobj.optString("lng"));
//
//                    homemodel.setLatLng(new LatLng(lat, lng));
//


                    if (jobj.optString("tag_status").equals("1"))
                    {
                        double lat = Double.parseDouble(jobj.optString("tag_lat"));
                        double lng = Double.parseDouble(jobj.optString("tag_long"));

                        homemodel.setLatLng(new LatLng(lat, lng));
                    }
                    else
                    {
                        homemodel.setLatLng(new LatLng(0, 0));
                    }


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
                    Utills.show_dialog_msg(getActivity(), "We are yet to launch in your location. Please change your home location in Settings to add post", null);
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
//    @Override
//    public void onRefresh()
//    {
//        homeadapter = null;
//        if (listHome != null)
//        {
//            listHome.clear();
//        }
//        index = 0;
//        limit = 10;
//        startId = 0;
//        fetchHomeData(startId, limit);
//    }

    @Override
    public void notifier(final int indexg, final String count)
    {
        try
        {
            if (count.equals("0"))
            {
                final Handler UIHandler = new Handler(Looper.getMainLooper());
                UIHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        if (tvNewPost != null)
                        {
                            tvNewPost.setVisibility(View.VISIBLE);
                            tvNewPost.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    tvNewPost.setVisibility(View.GONE);
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
                            });
                        }


                    }
                });
            }
            else
            {
                if (listHome.size() >= indexg)
                {
                    listHome.get(indexg).setComments_count(count);
                    homeadapter.notifyDataSetChanged();
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
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
