package gagan.com.communities.activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.HomeFragment;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class CommentsListActivity extends AppCompatActivity
{


    EditText     edComment;
    RecyclerView recyclerList;

    CommentsAdapter commentsAdapter;
    private List<CommentsModel> listData;

    SharedPrefHelper sharedPrefHelper;
    ProgressBar      progressBar;

    int index = 0;


    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);

        postID = getIntent().getStringExtra("postID");
        index = getIntent().getIntExtra("index", 0);

        sharedPrefHelper = new SharedPrefHelper(CommentsListActivity.this);

        img = SharedPrefHelper.read(CommentsListActivity.this).getProfile_pic();


        settingActionBar();

        findViewByID();
        hitWebserviceG();


    }


    private void settingActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        finish();


        return super.onOptionsItemSelected(item);
    }


    void findViewByID()
    {

        listData = new ArrayList<>();

        edComment = (EditText) findViewById(R.id.edComment);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentsListActivity.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerList.setLayoutManager(linearLayoutManager);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.button_pink), PorterDuff.Mode.MULTIPLY);

        commentsAdapter = new CommentsAdapter(CommentsListActivity.this, listData);


        recyclerList.setAdapter(commentsAdapter);


    }

    Dialog dialog;

    public void showProgressDialog()
    {
        dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();

    }

    public void cancelDialog()
    {
        if (dialog != null)
        {
            dialog.cancel();
        }
    }

    void hitWebserviceG()
    {
        try
        {

            progressBar.setVisibility(View.VISIBLE);


            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("postid", postID);

            new SuperWebServiceG(GlobalConstants.URL + "getPostcomment", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    progressBar.setVisibility(View.GONE);
                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").equals("200"))
                        {


                            JSONArray jsonarrayData = jsonMainResult.getJSONArray("comments");

                            for (int g = 0; g < jsonarrayData.length(); g++)
                            {
                                JSONObject jobj = jsonarrayData.optJSONObject(g);


                                String name        = jobj.optString("user_name");
                                String cmntmsg     = jobj.optString("comments");
                                String comntId     = jobj.optString("id");
                                String userid      = jobj.optString("userid");
                                String create_date = jobj.optString("create_date");

                                boolean ismyComment = jobj.optString("is_user_comment").equals("1");

                                CommentsModel data = new CommentsModel(name, cmntmsg, comntId, jobj.optString("profile_pic"), userid, create_date, ismyComment);

                                listData.add(data);
                            }


//                            Collections.reverse(listData);

                            commentsAdapter.notifyDataSetChanged();

                        }
                        else
                        {
//                Utills.showToast("No users available", getActivity(), true);
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

    String           postID = "";
    SimpleDateFormat sdf    = new SimpleDateFormat(GlobalConstants.SEVER_FORMAT);

    public void postComment(View view)
    {
        try
        {


            if (edComment.getText().toString().trim().isEmpty())
            {
                return;
            }


            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("comments", edComment.getText().toString());
            data.put("post_id", postID);

            new SuperWebServiceG(GlobalConstants.URL + "addPostcomment", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    cancelDialog();

                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            String name    = sharedPrefHelper.getUserName();
                            String cmntmsg = edComment.getText().toString();
                            String comntId = jsonMainResult.optString("postId");


                            CommentsModel data = new CommentsModel(name, cmntmsg, comntId, img, sharedPrefHelper.getUserId(), sdf.format(new Date(System.currentTimeMillis())), true);

                            listData.add(0, data);


                            commentsAdapter.notifyDataSetChanged();


                            edComment.setText("");

                            if (HomeFragment.homeFragment != null)
                            {
                                (HomeFragment.homeFragment).notifier(index, listData.size() + "");
                            }

                        }
                        else
                        {
                            Utills.showToast("Please try again..!", CommentsListActivity.this, true);
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


    public class CommentsModel
    {
        private String name, commentmsg, commentid, imgUrl, userid, create_date;

        private boolean isMyComment;

        public CommentsModel(String name, String commentmsg, String commentid, String imgUrl, String userid, String create_date, boolean isMyComment)
        {
            this.name = name;
            this.commentmsg = commentmsg;
            this.commentid = commentid;
            this.imgUrl = imgUrl;
            this.userid = userid;
            this.create_date = create_date;
            this.isMyComment = isMyComment;
        }

        public String getImgUrl()
        {
            return imgUrl;
        }

        public String getUserid()
        {
            return userid;
        }

        public String getCreate_date()
        {
            return create_date;
        }

        public String getName()
        {
            return name;
        }

        public String getCommentmsg()
        {
            return commentmsg;
        }

        public String getCommentid()
        {
            return commentid;
        }

        public boolean isMyComment()
        {
            return isMyComment;
        }
    }


    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolderG>

    {
        private LayoutInflater inflater;

        Context con;

        private List<CommentsModel> dataList;


        public CommentsAdapter(Context context, List<CommentsModel> dList)
        {

            this.dataList = dList;
            con = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public CommentsAdapter.MyViewHolderG onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(R.layout.followers_inflator, parent, false);
            return new MyViewHolderG(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolderG holder, int position)
        {

            final CommentsModel currentData = dataList.get(position);

            holder.tvName.setText(currentData.getName());
            holder.tvText.setText(currentData.getCommentmsg());


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
            params.gravity = Gravity.TOP;
            holder.imgUserPic.setLayoutParams(params);

            holder.imgUserPic.setRadius(170);
            holder.imgUserPic.setImageUrl(con, currentData.getImgUrl());


            try
            {
                holder.tvtime.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf       = new SimpleDateFormat(GlobalConstants.SEVER_FORMAT);
                SimpleDateFormat sdfDesire = new SimpleDateFormat("dd MMM hh:mm a");
                Date             date      = sdf.parse(currentData.getCreate_date());
                holder.tvtime.setText(sdfDesire.format(date));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            holder.imgUserPic.setTag(currentData.getUserid());
            holder.tvName.setTag(currentData.getUserid());
            holder.imgUserPic.setOnClickListener(onClickListenerShowProfile);
            holder.tvName.setOnClickListener(onClickListenerShowProfile);


            holder.view.setTag(currentData);
            holder.view.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    final CommentsModel commentsModel = (CommentsModel) v.getTag();


                    if (commentsModel.isMyComment())
                    {
                        PopupMenu popup = new PopupMenu(con, v);

                        SpannableString s = new SpannableString("Delete comment");
                        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                        popup.getMenu().add(s);

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                        {
                            @Override
                            public boolean onMenuItemClick(MenuItem item)
                            {

                                delComment(commentsModel);
                                return false;
                            }
                        });
                        popup.show();
                    }

                    return false;
                }
            });


        }


        View.OnClickListener onClickListenerShowProfile = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!sharedPrefHelper.getUserId().equals(v.getTag().toString()))
                {
                    Intent intnt = new Intent(con, OtherProfileActivity.class);
                    intnt.putExtra("user_id", v.getTag().toString());
                    con.startActivity(intnt);
                }
            }
        };


        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


        class MyViewHolderG extends RecyclerView.ViewHolder
        {
            TextView tvName, tvText, tvtime;
            View                   view;
            RoundedCornersGaganImg imgUserPic;

            public MyViewHolderG(View itemView)
            {
                super(itemView);
                tvtime = (TextView) itemView.findViewById(R.id.tvtime);
                tvText = (TextView) itemView.findViewById(R.id.tvText);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                imgUserPic = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgUserPic);
                view = itemView;
            }
        }

    }


    private void delComment(final CommentsModel cmntId)
    {
        try
        {
            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("comment_id", cmntId.getCommentid());

            new SuperWebServiceG(GlobalConstants.URL + "deletecomment", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    cancelDialog();

                    try
                    {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
                        {
                            listData.remove(cmntId);
                            commentsAdapter.notifyDataSetChanged();
                            (HomeFragment.homeFragment).notifier(index, listData.size() + "");
                        }
                        else
                        {
                            Utills.showToast("unable to delete comment", CommentsListActivity.this, true);
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


}

