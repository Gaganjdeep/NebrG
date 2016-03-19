package gagan.com.communities.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.activites.AddPostActivity;
import gagan.com.communities.activites.CommentsListActivity;
import gagan.com.communities.activites.CurrentLocationPostActivity;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.models.NotificationModel;
import gagan.com.communities.utills.CallBackNotifierHome;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * Created by sony on 17-01-2016.
 */
public class HomeAdapter extends BaseAdapter
{
    Context con;

    List<HomeModel> DataList;



    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }


    public void updateList(List<HomeModel> dataList)
    {
        DataList.clear();
        DataList.addAll(dataList);
        notifyDataSetChanged();
    }


    public HomeAdapter(Context con, List<HomeModel> dataList)
    {
        this.con = con;
        DataList = dataList;
    }

    @Override
    public int getCount()
    {
//        return DataList.size();
        return DataList == null ? 7 : DataList.size();
    }

    @Override
    public HomeModel getItem(int i)
    {


        if (DataList == null)
        {
            return null;
        }
        return DataList.get(i);
//        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }


    @Override
    public View getView(final int i, View viewOther, ViewGroup viewGroup)
    {

        try
        {


            final HomeModel data = getItem(i);
//
            if (viewOther == null)
            {
                LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                viewOther = inflater.inflate(R.layout.home_inflator, viewGroup, false);
            }

            RoundedCornersGaganImg imgUserPic = (RoundedCornersGaganImg) viewOther.findViewById(R.id.imgUserPic);
            RoundedCornersGaganImg imgMessage = (RoundedCornersGaganImg) viewOther.findViewById(R.id.imgMessage);
            imgMessage.setImageResource(R.drawable.grey_bg);

            TextView  tvTitle         = (TextView) viewOther.findViewById(R.id.tvTitle);
            TextView  tvLocationGenre = (TextView) viewOther.findViewById(R.id.tvLocationGenre);
            TextView  tvGenre         = (TextView) viewOther.findViewById(R.id.tvGenre);
            ImageView imgOptions      = (ImageView) viewOther.findViewById(R.id.imgOptions);

            TextView tvUsername = (TextView) viewOther.findViewById(R.id.tvUsername);
            TextView tvTime     = (TextView) viewOther.findViewById(R.id.tvTime);
            TextView tvMessage  = (TextView) viewOther.findViewById(R.id.tvMessage);
            TextView tvLikes    = (TextView) viewOther.findViewById(R.id.tvLikes);
            TextView tvDislikes = (TextView) viewOther.findViewById(R.id.tvDislikes);
            TextView tvComments = (TextView) viewOther.findViewById(R.id.tvComments);

            imgUserPic.setRadius(120);
            if (!data.isAnon_user())
            {
                imgUserPic.setImageUrl(con, data.getProfile_pic());
            }


            tvTitle.setText(data.getTitle());
            tvTime.setText(data.getCreate_date().substring(data.getCreate_date().indexOf(" ")));
            tvLocationGenre.setText(data.getLocation());
            tvLocationGenre.setSelected(true);
            tvGenre.setText(data.getType());
            tvUsername.setText(data.getUsername());
            tvMessage.setText(data.getMessage());
            tvComments.setText(data.getComments_count() + " comments");

            tvLikes.setText(data.getLike_count() + " useful");
            tvDislikes.setText(data.getDislike_count() + " not useful");








            tvComments.setTag(i);
            tvComments.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intnt = new Intent(con, CommentsListActivity
                            .class);
                    intnt.putExtra("postID",DataList.get((Integer) v.getTag()).getId());
                    intnt.putExtra("index",(Integer) v.getTag());
                    con.startActivity(intnt);
                }
            });







            if (data.is_liked())
            {
                tvLikes.setTextColor(con.getResources().getColor(R.color.blue));
                tvLikes.setCompoundDrawablesWithIntrinsicBounds(con.getResources().getDrawable(R.mipmap.ic_like_sel), null, null, null);
            }
            else
            {
                tvLikes.setTextColor(con.getResources().getColor(R.color.greydefault));
                tvLikes.setCompoundDrawablesWithIntrinsicBounds(con.getResources().getDrawable(R.mipmap.ic_like), null, null, null);
            }


            if (data.is_disliked())
            {
                tvDislikes.setTextColor(con.getResources().getColor(R.color.blue));
                tvDislikes.setCompoundDrawablesWithIntrinsicBounds(con.getResources().getDrawable(R.mipmap.ic_dislike_sel), null, null, null);

            }
            else
            {
                tvDislikes.setTextColor(con.getResources().getColor(R.color.greydefault));
                tvDislikes.setCompoundDrawablesWithIntrinsicBounds(con.getResources().getDrawable(R.mipmap.ic_dislike), null, null, null);

            }


            if (!data.getImage().equals(""))
            {
                imgMessage.setVisibility(View.VISIBLE);
                imgMessage.setImageUrl(con, data.getImage());
            }
            else
            {
                imgMessage.setVisibility(View.GONE);
            }


            tvLikes.setTag(data);
            tvLikes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (((HomeModel) v.getTag()).is_liked())
                    {
                        Utills.showToast("Already marked as useful", con, true);
                    }
                    else
                    {
                        like_dislike("1", (HomeModel) v.getTag());
                    }


                }
            });
            tvDislikes.setTag(data);
            tvDislikes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (((HomeModel) v.getTag()).is_disliked())
                    {
                        Utills.showToast("Already marked as not useful", con, true);
                    }
                    else
                    {
                        like_dislike("2", (HomeModel) v.getTag());
                    }
                }
            });

            imgOptions.setTag(data);
            imgOptions.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    final HomeModel homeModel = (HomeModel) v.getTag();


                    final String title =(new SharedPrefHelper(con).getUserId()).equals(homeModel.getUserid()) ? "Delete post" : "Report Abuse";




                    PopupMenu popup = new PopupMenu(con, v);

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
                                reportAbuse(homeModel.getId(), homeModel,false);
                            }
                            else
                            {
                                reportAbuse(homeModel.getId(), homeModel,true);
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


        return viewOther;
    }


    private void reportAbuse(String postID, final HomeModel homeModel,boolean delete)
    {
        try
        {

            String serviceName;


            JSONObject data = new JSONObject();
            if (delete)
            {
                serviceName="deletePost";
                data.put("postid", postID);
            }
            else
            {
                serviceName="reportAbuse";

                data.put("user_id", new SharedPrefHelper(con).getUserId());
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
                            DataList.remove(homeModel);

                            notifyDataSetChanged();
                        }
                        Utills.showToast(jsonMainResult.getString("status"), con, true);

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
            final Dialog dialog = new Dialog(con, R.style.Theme_Dialog);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();


            JSONObject data = new JSONObject();
            data.put("user_id", new SharedPrefHelper(con).getUserId());
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

                                int likecount    = Integer.parseInt(DataList.get(DataList.indexOf(homeModel)).getLike_count());
                                int dislikecount = Integer.parseInt(DataList.get(DataList.indexOf(homeModel)).getDislike_count());


                                if (likeDislike.equals("1"))
                                {

                                    DataList.get(DataList.indexOf(homeModel)).setLike_count((likecount + 1) + "");
                                    DataList.get(DataList.indexOf(homeModel)).setIs_liked(true);

                                    if (homeModel.is_disliked())
                                    {
                                        DataList.get(DataList.indexOf(homeModel)).setDislike_count(((dislikecount - 1) < 0 ? 0 : (dislikecount - 1)) + "");
                                    }
                                    DataList.get(DataList.indexOf(homeModel)).setIs_disliked(false);


                                    Utills.showToast("Marked as useful", con, true);
                                }
                                else
                                {

                                    DataList.get(DataList.indexOf(homeModel)).setDislike_count((dislikecount + 1) + "");

                                    if (homeModel.is_liked())
                                    {
                                        DataList.get(DataList.indexOf(homeModel)).setLike_count(((likecount - 1) < 0 ? 0 : (likecount - 1)) + "");
                                    }

                                    DataList.get(DataList.indexOf(homeModel)).setIs_disliked(true);
                                    DataList.get(DataList.indexOf(homeModel)).setIs_liked(false);

                                    Utills.showToast("Marked as not useful", con, true);

                                }

                                notifyDataSetChanged();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Utills.showToast(jsonMainResult.getString("status"), con, true);
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