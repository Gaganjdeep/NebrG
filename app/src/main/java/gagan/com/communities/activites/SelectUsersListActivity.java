package gagan.com.communities.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.FollowerFollowingAdapter;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class SelectUsersListActivity extends BaseActivityG
{


    private RecyclerView listview;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users_list);


        settingActionBar();
        findViewByID();

        hitWebserviceG();
    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.select_user_community, menu);

        menu.findItem(R.id.done).setTitle(Html.fromHtml("<font color='#0000'>Done</font>"));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (list != null)
        {
            Intent resultIntent = new Intent();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++)
            {
                SelectUsersListActivity.SelectedUser userData = list.get(i);

                if (userData.isSelected())
                {
                    if (i == 0)
                    {
                        sb.append(userData.getUserid());
                    }
                    else
                    {
                        sb.append(",").append(userData.getUserid());
                    }
                }
            }


            resultIntent.putExtra("data", sb.toString());


            setResult(Activity.RESULT_OK, resultIntent);

        }
        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    void findViewByID()
    {
        listview = (RecyclerView) findViewById(R.id.recyclerList);
        listview.setLayoutManager(new LinearLayoutManager(SelectUsersListActivity.this));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    void hitWebserviceG()
    {

        hitWebAgain("1");
    }


    private void hitWebAgain(final String status)
    {
        try
        {
//            JSONObject data = new JSONObject();
//            data.put("userid", sharedPrefHelper.getUserId());
//
//
//            new SuperWebServiceG(GlobalConstants.URL + "getuserlist", data, new CallBackWebService()
//            {
//                @Override
//                public void webOnFinish(String output)
//                {
//                    progressBar.setVisibility(View.GONE);
//                    processOutput(output);
//                }
//            }).execute();

            progressBar.setVisibility(View.VISIBLE);
            JSONObject data = new JSONObject();
            data.put("user_id", sharedPrefHelper.getUserId());
            data.put("f_status", status);  // 1 for followers 2 for following.


            new SuperWebServiceG(GlobalConstants.URL + "getfollowerList", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    progressBar.setVisibility(View.GONE);
                    processOutput(output, status);
                }
            }).execute();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    List<SelectedUser> list;

    private void processOutput(String output, String status)
    {
        try
        {

            JSONObject jsonMain = new JSONObject(output);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("200"))
            {

                JSONArray jsonarrayData;

                if (status.equals("1"))
                {
                    jsonarrayData = jsonMainResult.getJSONArray("followers");
                }
                else
                {
                    jsonarrayData = jsonMainResult.getJSONArray("following");
                }


                if (jsonarrayData == null)
                {
                    if (status.equals("1"))
                    {
                        hitWebAgain("2");
                    }
                    return;
                }

                if (list == null)
                {
                    list = new ArrayList<>();
                }


                for (int g = 0; g < jsonarrayData.length(); g++)
                {

                    JSONObject jobj = jsonarrayData.optJSONObject(g);

                    SelectedUser homemodel = new SelectedUser();

                    homemodel.setImage(jobj.optString("profile_pic"));
                    homemodel.setUserid(jobj.optString("uId"));
                    homemodel.setName(jobj.optString("name"));


                    if (CommunityDetailsActivity.ListOfMembers != null)
                    {
                        if (CommunityDetailsActivity.ListOfMembers.contains(jobj.optString("uId")))
                        {

                        }
                        else
                        {
                            list.add(homemodel);
                        }
                    }
                    else
                    {
                        list.add(homemodel);
                    }


                }

                if (msgAdapter == null)
                {
                    msgAdapter = new ContactsAdapter(SelectUsersListActivity.this, list);

                    listview.setAdapter(msgAdapter);

                }
                else
                {
                    msgAdapter.notifyDataSetChanged();
                }

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (status.equals("1"))
        {
            hitWebAgain("2");
        }
    }

    ContactsAdapter msgAdapter;
   /* private void processOutput(String output)
    {
        try
        {

            JSONObject jsonMain = new JSONObject(output);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").contains("200"))
            {


                JSONArray jsonarrayData;

                jsonarrayData = jsonMainResult.getJSONArray("userlist");


//                {"uId":"5","name":"Gagan","email":"gagan2@gmail.com","password":"e10adc3949ba59abbe56e057f20f883e","gender":"male","home_society":"bdbdbdbd","session_key":"","create_date":"2016-02-25 18:02:51","role_id":"2","profession":"sharp shooter","location":"Jagadhari Road, Sarsehri, Haryana 133004, India","delete_status":"0","device_type":"0","device_token":"0","update_date":"2016-02-25 11:02:51","profile_pic":"","path":"","is_fb":"0","facebook_id":"","is_gp":"0","gplus_id":"","login_status":"0"},

                list = new ArrayList<>();

                for (int g = 0; g < jsonarrayData.length(); g++)
                {
                    JSONObject jobj = jsonarrayData.optJSONObject(g);

                    SelectedUser homemodel = new SelectedUser();

                    homemodel.setImage(jobj.optString("profile_pic"));
                    homemodel.setUserid(jobj.optString("uId"));
                    homemodel.setName(jobj.optString("name"));


                    list.add(homemodel);
                }

                ContactsAdapter msgAdapter = new ContactsAdapter(SelectUsersListActivity.this, list);

                listview.setAdapter(msgAdapter);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/


    public class SelectedUser implements Serializable
    {
        private String name, image, userid;
        private boolean isSelected;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getImage()
        {
            return image;
        }

        public void setImage(String image)
        {
            this.image = image;
        }

        public String getUserid()
        {
            return userid;
        }

        public void setUserid(String userid)
        {
            this.userid = userid;
        }

        public boolean isSelected()
        {
            return isSelected;
        }

        public void setSelected(boolean selected)
        {
            isSelected = selected;
        }
    }


    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolderG>

    {
        private LayoutInflater inflater;

        Context con;

        private List<SelectedUser> dataList;


        public ContactsAdapter(Context context, List<SelectedUser> dList)
        {

            this.dataList = dList;
            con = context;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(R.layout.select_contact_inflator, parent, false);
            return new MyViewHolderG(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolderG holder, int position)
        {


            holder.title.setText(dataList.get(position).getName());

            holder.icon.setRadius(170);
            holder.icon.setImageUrl(con, dataList.get(position).getImage());

            holder.chkBox.setOnCheckedChangeListener(null);

            holder.chkBox.setChecked(dataList.get(position).isSelected());

            holder.chkBox.setTag(position);

            holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    CheckBox chkBoxx = (CheckBox) compoundButton;

                    int position = (int) chkBoxx.getTag();

                    dataList.get(position).setSelected(b);
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


        class MyViewHolderG extends RecyclerView.ViewHolder
        {
            TextView               title;
            View                   view;
            RoundedCornersGaganImg icon;
            AppCompatCheckBox      chkBox;

            public MyViewHolderG(View itemView)
            {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.name);
                icon = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgUserPic);
                chkBox = (AppCompatCheckBox) itemView.findViewById(R.id.chkBox);
                view = itemView;
            }
        }

    }


}
