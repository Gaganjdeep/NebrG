package gagan.com.communities.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.PostProfileFragment;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.utills.GlobalConstants;
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


        dataModel = (CommunitiesListModel) getIntent().getSerializableExtra("data");
        setData(dataModel);
        hitWebserviceG();

        myPost = new PostProfileFragment();
        Bundle bundlePost = new Bundle();
        bundlePost.putString("userid", "g");
        bundlePost.putString("c_id", dataModel.getCid());
        myPost.setArguments(bundlePost);


    }

    PostProfileFragment myPost;

    FrameLayout frameLayout;


    @Override
    protected void onResume()
    {


       getSupportFragmentManager().beginTransaction().replace(R.id.container, myPost,"my_post").commit();

        super.onResume();
    }

    @Override
    protected void onPause()
    {
        Fragment f = getSupportFragmentManager().findFragmentByTag("my_post");
        if(f!=null)
        {
            getSupportFragmentManager().beginTransaction().remove(f).commit();
        }
        super.onPause();
    }

    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        tvMessage.setText(communitiesListModel.getC_description());
        tvTitle.setText(communitiesListModel.getC_name());

        tvFollow.setVisibility(View.INVISIBLE);
        tvFollow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                follOw();
            }
        });
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


                            if (jobj.getString("is_user_following").equals("1"))
                            {
                                tvFollow.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                tvFollow.setVisibility(View.VISIBLE);
                            }


                            membersList = jobj.getString("userinfo");

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

    public void maPview(View view)
    {
        Intent intnt = new Intent(CommunityDetailsActivity.this, ShowFragmentActivity.class);
        intnt.putExtra("title", "Communities");
        intnt.putExtra("data", dataModel);
        startActivity(intnt);
    }

    public void follOw()
    {
        try
        {

            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("invite_user_id", sharedPrefHelper.getUserId());
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
                            tvFollow.setVisibility(View.INVISIBLE);
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
}
