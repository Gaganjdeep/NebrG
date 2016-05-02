package gagan.com.communities.activites;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import gagan.com.communities.R;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class ShowPostActivity extends AppCompatActivity
{


    HomeModel dataHome;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);


        settingActionBar();


        try
        {

            dataHome = (HomeModel) getIntent().getSerializableExtra("data");


            RoundedCornersGaganImg imgUserPic = (RoundedCornersGaganImg) findViewById(R.id.imgUserPic);
            RoundedCornersGaganImg imgMessage = (RoundedCornersGaganImg) findViewById(R.id.imgMessage);
            imgMessage.setImageResource(R.drawable.grey_bg);

            TextView  tvTitle         = (TextView) findViewById(R.id.tvTitle);
            TextView  tvLocationGenre = (TextView) findViewById(R.id.tvLocationGenre);
            TextView  tvGenre         = (TextView) findViewById(R.id.tvGenre);
            ImageView imgOptions      = (ImageView) findViewById(R.id.imgOptions);

            TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
            TextView tvTime     = (TextView) findViewById(R.id.tvTime);
            TextView tvMessage  = (TextView) findViewById(R.id.tvMessage);
            TextView tvLikes    = (TextView) findViewById(R.id.tvLikes);
            TextView tvDislikes = (TextView) findViewById(R.id.tvDislikes);
            TextView tvComments = (TextView) findViewById(R.id.tvComments);

            ImageView tvShowOnMap = (ImageView) findViewById(R.id.tvShowOnMap);
          /*  if (dataHome.getLatLng().longitude * dataHome.getLatLng().latitude != 0)
            {
                tvShowOnMap.setVisibility(View.VISIBLE);
                tvShowOnMap.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        HomeModel homeModel = (HomeModel) v.getTag();

                        Intent intnt = new Intent(ShowPostActivity.this, ShowFragmentActivity.class);
                        intnt.putExtra("title", "Posts");
                        intnt.putExtra("data", homeModel);
                        startActivity(intnt);

                    }
                });
            }
            else
            {
                tvShowOnMap.setVisibility(View.GONE);
            }*/

            tvShowOnMap.setVisibility(View.GONE);



            imgUserPic.setRadius(120);
            if (!dataHome.isAnon_user())
            {
                imgUserPic.setImageUrl(ShowPostActivity.this, dataHome.getProfile_pic());
            }


            tvTitle.setText(dataHome.getTitle());


            try
            {

                SimpleDateFormat sdf       = new SimpleDateFormat(GlobalConstants.SEVER_FORMAT);
                SimpleDateFormat sdfDesire = new SimpleDateFormat("dd.MMM hh:mm a");
                Date             date      = sdf.parse(dataHome.getCreate_date());
                tvTime.setText(sdfDesire.format(date));


                StringBuilder sb = new StringBuilder(dataHome.getLocation());
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                String loc = sb.toString();

                tvLocationGenre.setText(loc);
                tvLocationGenre.setSelected(true);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            tvGenre.setText(dataHome.getType());
            tvUsername.setText(dataHome.getUsername());
            tvMessage.setText(dataHome.getMessage());
            tvComments.setText(dataHome.getComments_count() + " comments");

            tvLikes.setText(dataHome.getLike_count() + " useful");
            tvDislikes.setText(dataHome.getDislike_count() + " not useful");


            tvComments.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intnt = new Intent(ShowPostActivity.this, CommentsListActivity
                            .class);
                    intnt.putExtra("postID", dataHome.getId());
                    intnt.putExtra("index", 0);
                    startActivity(intnt);
                }
            });


            if (dataHome.is_liked())
            {
                tvLikes.setTextColor(getResources().getColor(R.color.blue));
                tvLikes.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_like_sel), null, null, null);
            }
            else
            {
                tvLikes.setTextColor(getResources().getColor(R.color.greydefault));
                tvLikes.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_like), null, null, null);
            }


            if (dataHome.is_disliked())
            {
                tvDislikes.setTextColor(getResources().getColor(R.color.blue));
                tvDislikes.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_dislike_sel), null, null, null);

            }
            else
            {
                tvDislikes.setTextColor(getResources().getColor(R.color.greydefault));
                tvDislikes.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_dislike), null, null, null);

            }


            if (!dataHome.getImage().equals(""))
            {
                imgMessage.setVisibility(View.VISIBLE);
                imgMessage.setImageUrl(ShowPostActivity.this, dataHome.getImage());
            }
            else
            {
                imgMessage.setVisibility(View.GONE);
            }


            tvLikes.setTag(dataHome);
            tvLikes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (((HomeModel) v.getTag()).is_liked())
                    {
//                    Utills.showToast("Already marked as useful", ShowPostActivity.this, true);
                        like_dislike("0", (HomeModel) v.getTag());
                    }
                    else
                    {
                        like_dislike("1", (HomeModel) v.getTag());
                    }


                }
            });
            tvDislikes.setTag(dataHome);
            tvDislikes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (((HomeModel) v.getTag()).is_disliked())
                    {
//                    Utills.showToast("Already marked as not useful", ShowPostActivity.this, true);
                        like_dislike("0", (HomeModel) v.getTag());
                    }
                    else
                    {
                        like_dislike("2", (HomeModel) v.getTag());
                    }
                }
            });

            imgOptions.setTag(dataHome);
            imgOptions.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    final HomeModel homeModel = (HomeModel) v.getTag();


                    final String title = (new SharedPrefHelper(ShowPostActivity.this).getUserId()).equals(homeModel.getUserid()) ? "Delete post" : "Report Abuse";


                    PopupMenu popup = new PopupMenu(ShowPostActivity.this, v);

                    SpannableString s = new SpannableString(title);
                    s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                    popup.getMenu().add(s);


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {

                            if (title.equals("Report Abuse"))
                            {
                                reportAbuse(homeModel.getId(), homeModel, false);
                            }
                            else
                            {
                                reportAbuse(homeModel.getId(), homeModel, true);
                            }


                            return false;
                        }
                    });
                    popup.show();
                }
            });


        }

        catch (Exception e)

        {
            e.printStackTrace();
        }

    }


    private void reportAbuse(String postID, final HomeModel homeModel, boolean delete)
    {
        try
        {

            String serviceName;


            JSONObject data = new JSONObject();
            if (delete)
            {
                serviceName = "deletePost";
                data.put("postid", postID);
            }
            else
            {
                serviceName = "reportAbuse";

                data.put("user_id", new SharedPrefHelper(ShowPostActivity.this).getUserId());
                data.put("post_id", postID);
            }

            new SuperWebServiceG(GlobalConstants.URL + serviceName, data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    try
                    {
                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {
                            finish();
                        }
                        Utills.showToast(jsonMainResult.getString("status"), ShowPostActivity.this, true);

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


    private void like_dislike(final String likeDislike, final HomeModel homeModel)
    {
        try
        {
            final Dialog dialog = new Dialog(ShowPostActivity.this, R.style.Theme_Dialog);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();


            final JSONObject data = new JSONObject();
            data.put("user_id", new SharedPrefHelper(ShowPostActivity.this).getUserId());
            data.put("like_status", likeDislike);
            data.put("post_id", homeModel.getId());

            new SuperWebServiceG(GlobalConstants.URL + "likePost", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    try
                    {

                        if (dialog != null)
                        {
                            dialog.cancel();
                        }


                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("200"))
                        {

                            try
                            {

                                int likecount    = Integer.parseInt(dataHome.getLike_count());
                                int dislikecount = Integer.parseInt(dataHome.getDislike_count());


                                if (likeDislike.equals("1"))
                                {

                                    dataHome.setLike_count((likecount + 1) + "");
                                    dataHome.setIs_liked(true);

                                    if (homeModel.is_disliked())
                                    {
                                        dataHome.setDislike_count(((dislikecount - 1) < 0 ? 0 : (dislikecount - 1)) + "");
                                    }
                                    dataHome.setIs_disliked(false);


                                    Utills.showToast("Marked as useful", ShowPostActivity.this, true);
                                }
                                else if (likeDislike.equals("2"))
                                {

                                    dataHome.setDislike_count((dislikecount + 1) + "");

                                    if (homeModel.is_liked())
                                    {
                                        dataHome.setLike_count(((likecount - 1) < 0 ? 0 : (likecount - 1)) + "");
                                    }

                                    dataHome.setIs_disliked(true);
                                    dataHome.setIs_liked(false);

                                    Utills.showToast("Marked as not useful", ShowPostActivity.this, true);

                                }
                                else
                                {
                                    if (homeModel.is_liked())
                                    {
                                        dataHome.setLike_count(((likecount - 1) < 0 ? 0 : (likecount - 1)) + "");
                                    }
                                    else if (homeModel.is_disliked())
                                    {
                                        dataHome.setDislike_count(((dislikecount - 1) < 0 ? 0 : (dislikecount - 1)) + "");
                                    }


                                    dataHome.setIs_disliked(false);
                                    dataHome.setIs_liked(false);

                                }

                                recreate();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Utills.showToast(jsonMainResult.getString("status"), ShowPostActivity.this, true);
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


}
