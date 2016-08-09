package gagan.com.communities.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.PostProfileFragment;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class CommunityDetailsActivity extends BaseActivityG
{

    CommunitiesListModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_details);
        settingActionBar();
        findViewByID();


        try
        {
            dataModel = (CommunitiesListModel) getIntent().getSerializableExtra("data");

            if (dataModel == null)
            {
                finish();
            }


            setData(dataModel);
            hitWebserviceG();

            myPost = new PostProfileFragment();
            Bundle bundlePost = new Bundle();
            bundlePost.putString("userid", "g");
            bundlePost.putString("c_id", dataModel.getCid());
            myPost.setArguments(bundlePost);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    PostProfileFragment myPost;

    FrameLayout frameLayout;


    @Override
    protected void onResume()
    {

        Fragment f = getSupportFragmentManager().findFragmentByTag("my_post");
        if (f == null)
        {
            try
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myPost, "my_post").commitAllowingStateLoss();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause()
    {
        Fragment f = getSupportFragmentManager().findFragmentByTag("my_post");
        if (f != null)
        {
            getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
        super.onPause();
    }

    ImageView imgvisible;

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgvisible = (ImageView) toolbar.findViewById(R.id.imgvisible);

        ImageView txtvAddPost = (ImageView) toolbar.findViewById(R.id.txtvAddPost);

        txtvAddPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CommunityDetailsActivity.this, AddPostActivity
                        .class);
                i.putExtra("Cid", dataModel.getCid());
                startActivity(i);

            }
        });

    }


    private void setData(CommunitiesListModel communitiesListModel)
    {
        try
        {

            tvMessage.setText(communitiesListModel.getC_description());
            tvTitle.setText(communitiesListModel.getC_name());

            tvFollow.setVisibility(View.GONE);
            tvFollow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    follOw(sharedPrefHelper.getUserId());
                }
            });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    TextView tvTitle, tvFollow, tvMessage;

    @Override
    void findViewByID()
    {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvFollow = (TextView) findViewById(R.id.tvFollow);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        frameLayout = (FrameLayout) findViewById(R.id.container);
    }


    String membersList;


    boolean showDelete = false;


    @Override
    void hitWebserviceG()
    {
        try
        {

            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("group_id", dataModel.getCid());

            new SuperWebServiceG(GlobalConstants.URL + "getcommunitydetail", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    try
                    {

                        cancelDialog();
                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            JSONArray jsonarrayData = jsonMainResult.getJSONArray("groupData");

                            JSONObject jobj = jsonarrayData.getJSONObject(0);




                            membersList = jobj.getString("userinfo");


                            showDelete = jobj.getString("user_id").equals(sharedPrefHelper.getUserId());





                            supportInvalidateOptionsMenu();

                            if (jobj.optString("is_user_following").equals("1"))
                            {
                                tvFollow.setVisibility(View.GONE);
                            }
                            else
                            {
                                tvFollow.setVisibility(View.VISIBLE);
                            }

                        }
//                        Utills.showToast(jsonMainResult.getString("status"), CommunityDetailsActivity.this, true);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static List<String> ListOfMembers;

    public void membersList(String json)
    {
        ListOfMembers = new ArrayList<>();
        try
        {
            JSONArray jsonarrayData = new JSONArray(json);
            for (int g = 0; g < jsonarrayData.length(); g++)
            {
                JSONObject jobj = jsonarrayData.optJSONArray(g).getJSONObject(0);

                ListOfMembers.add(jobj.optString("uId"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void maPview(View view)
    {
        Intent intnt = new Intent(CommunityDetailsActivity.this, ShowFragmentActivity.class);
        intnt.putExtra("title", "Communities");
        intnt.putExtra("data", dataModel);
        startActivity(intnt);
    }

    public void follOw(String userslist)
    {
        try
        {

            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("invite_user_id", userslist);
            data.put("c_id", dataModel.getCid());

            new SuperWebServiceG(GlobalConstants.URL + "addtocommunity", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    try
                    {

                        cancelDialog();
                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            tvFollow.setVisibility(View.GONE);

                            hitWebserviceG();
                        }
                        Utills.showToast(jsonMainResult.getString("status"), CommunityDetailsActivity.this, true);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void viewMembers(View view)
    {
        Intent intnt = new Intent(CommunityDetailsActivity.this, ShowFragmentActivity
                .class);
        intnt.putExtra("title", "Members");
        intnt.putExtra("data", membersList);
        startActivity(intnt);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (showDelete)
        {
            imgvisible.setVisibility(View.GONE);
            getMenuInflater().inflate(R.menu.del_menu, menu);
        }
        else
        {
            imgvisible.setVisibility(View.INVISIBLE);
        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case R.id.delete:

                try
                {


                    View.OnClickListener onclick = new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            try
                            {
                                showProgressDialog();

                                JSONObject data = new JSONObject();
                                data.put("userid", sharedPrefHelper.getUserId());
                                data.put("c_id", dataModel.getCid());

                                new SuperWebServiceG(GlobalConstants.URL + "deletecommunity", data, new CallBackWebService()
                                {
                                    @Override
                                    public void webOnFinish(String output)
                                    {
                                        try
                                        {

                                            cancelDialog();
                                            JSONObject jsonMain = new JSONObject(output);

                                            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                                            if (jsonMainResult.getString("code").contains("200"))
                                            {
                                                Utills.showToast("Community deleted", CommunityDetailsActivity.this, true);
                                                finish();
                                            }
                                            else
                                            {
                                                Utills.showToast("Please try later", CommunityDetailsActivity.this, true);
                                            }

                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }
                                }).execute();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };


                    Utills.show_dialog_msg(CommunityDetailsActivity.this, "Are you sure,you want to delete this community ?", onclick);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                break;


            case R.id.add:


                membersList(membersList);

                startActivityForResult(new Intent(CommunityDetailsActivity.this, SelectUsersListActivity.class), 11);


                break;


        }

        return super.onOptionsItemSelected(item);
//        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {

            if (resultCode == RESULT_OK)
            {

                String inviteUsers = data.getStringExtra("data");


                if (!inviteUsers.equals(""))
                {
                    follOw(inviteUsers);
                }

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
