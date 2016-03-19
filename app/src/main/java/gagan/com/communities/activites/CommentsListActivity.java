package gagan.com.communities.activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.fragment.HomeFragment;
import gagan.com.communities.models.CommunitiesListModel;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.CallBackNotifierHome;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class CommentsListActivity extends Activity
{


    EditText     edComment;
    RecyclerView recyclerList;

    CommentsAdapter commentsAdapter;
    private List<CommentsModel> listData;

    SharedPrefHelper sharedPrefHelper;
    ProgressBar      progressBar;

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);

        postID = getIntent().getStringExtra("postID");
        index = getIntent().getIntExtra("index", 0);

        sharedPrefHelper = new SharedPrefHelper(CommentsListActivity.this);

        findViewByID();
        hitWebserviceG();


    }


    void findViewByID()
    {

        listData = new ArrayList<>();

        edComment = (EditText) findViewById(R.id.edComment);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(CommentsListActivity.this));

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


                                String name    = jobj.optString("user_name");
                                String cmntmsg = jobj.optString("comments");
                                String comntId = jobj.optString("id");

                                boolean ismyComment = jobj.optString("is_user_comment").equals("1");

                                CommentsModel data = new CommentsModel(name, cmntmsg, comntId, ismyComment);

                                listData.add(data);
                            }


                            Collections.reverse(listData);

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

    String postID = "";

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


                            CommentsModel data = new CommentsModel(name, cmntmsg, comntId, true);

                            listData.add(data);


                            commentsAdapter.notifyDataSetChanged();


                            edComment.setText("");

                            (HomeFragment.homeFragment).notifier(index, listData.size() + "");
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


    public class CommentsModel
    {
        private String name, commentmsg, commentid;

        private boolean isMyComment;

        public CommentsModel(String name, String commentmsg, String commentid, boolean isMyComment)
        {
            this.name = name;
            this.commentmsg = commentmsg;
            this.commentid = commentid;
            this.isMyComment = isMyComment;
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

            holder.imgUserPic.setVisibility(View.GONE);

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


        @Override
        public int getItemCount()
        {
            return dataList.size();
        }


        class MyViewHolderG extends RecyclerView.ViewHolder
        {
            TextView tvName, tvText;
            View                   view;
            RoundedCornersGaganImg imgUserPic;

            public MyViewHolderG(View itemView)
            {
                super(itemView);
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

