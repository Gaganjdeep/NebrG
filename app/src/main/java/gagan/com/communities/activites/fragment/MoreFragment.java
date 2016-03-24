package gagan.com.communities.activites.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import gagan.com.communities.R;
import gagan.com.communities.activites.AddBuisness;
import gagan.com.communities.activites.AddPostActivity;
import gagan.com.communities.activites.BuisnessAdsActivity;
import gagan.com.communities.activites.CommunityTabActivity;
import gagan.com.communities.activites.FeedbackActivity;
import gagan.com.communities.activites.FrameActivity;
import gagan.com.communities.activites.HelpActivity;
import gagan.com.communities.activites.LoginActivity;
import gagan.com.communities.activites.MyBuisness;
import gagan.com.communities.activites.PersonalAds;
import gagan.com.communities.activites.ProfileActivity;
import gagan.com.communities.activites.SettingsActivity;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.RoundedCornersGaganImg;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends BaseFragmentG implements View.OnClickListener {


    LinearLayout layoutProfile, layoutAddPost, layoutCommunities, layoutBuisnessAds, layoutPersonalAds, layoutMyBuisness, layoutSettings, layoutHelp, layoutFeedback, layoutLogout;

    RoundedCornersGaganImg img_profilepic;


    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_more, container, false);

        findViewbyIds(v);
        return v;
    }

    TextView tvUserName;
    private void findViewbyIds(View v) {

        img_profilepic = (RoundedCornersGaganImg) v.findViewById(R.id.img_profilepic);
         tvUserName = (TextView) v.findViewById(R.id.tvUserName);


        layoutProfile = (LinearLayout) v.findViewById(R.id.layoutProfile);
        layoutMyBuisness = (LinearLayout) v.findViewById(R.id.layoutMyBuisness);
        layoutPersonalAds = (LinearLayout) v.findViewById(R.id.layoutPersonalAds);
        layoutCommunities = (LinearLayout) v.findViewById(R.id.layoutCommunities);
        layoutAddPost = (LinearLayout) v.findViewById(R.id.layoutAddPost);
        layoutBuisnessAds = (LinearLayout) v.findViewById(R.id.layoutBuisnessAds);
        layoutHelp = (LinearLayout) v.findViewById(R.id.layoutHelp);
        layoutFeedback = (LinearLayout) v.findViewById(R.id.layoutFeedback);
        layoutSettings = (LinearLayout) v.findViewById(R.id.layoutSettings);

        layoutLogout = (LinearLayout) v.findViewById(R.id.layoutLogout);


        img_profilepic.setRadius(200);



        layoutLogout.setOnClickListener(this);


        layoutFeedback.setOnClickListener(this);
        layoutSettings.setOnClickListener(this);
        layoutHelp.setOnClickListener(this);
        layoutAddPost.setOnClickListener(this);
        layoutBuisnessAds.setOnClickListener(this);
        layoutCommunities.setOnClickListener(this);
        layoutPersonalAds.setOnClickListener(this);
        layoutProfile.setOnClickListener(this);
        layoutMyBuisness.setOnClickListener(this);
    }


    @Override
    public void onResume()
    {
        tvUserName.setText(sharedPrefHelper.getUserName());
        img_profilepic.setImageUrl(getActivity(), SharedPrefHelper.read(getActivity()).getProfile_pic());
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layoutProfile:

                Utills.transitionToActivity(getActivity(), ProfileActivity.class, img_profilepic, "profilepic");

                break;


            case R.id.layoutMyBuisness:

                Intent intent = new Intent(getActivity(), AddBuisness.class);
                startActivity(intent);

                break;

            case R.id.layoutPersonalAds:


                startActivity(new Intent(getActivity(), PersonalAds.class));

                break;

            case R.id.layoutCommunities:


                startActivity(new Intent(getActivity(), CommunityTabActivity.class));

                break;

            case R.id.layoutBuisnessAds:


                startActivity(new Intent(getActivity(), BuisnessAdsActivity.class));

                break;
            case R.id.layoutAddPost:


                startActivity(new Intent(getActivity(), FrameActivity.class));

                break;

            case R.id.layoutHelp:


                startActivity(new Intent(getActivity(), HelpActivity.class));

                break;


            case R.id.layoutSettings:


                startActivity(new Intent(getActivity(), SettingsActivity.class));

                break;


            case R.id.layoutFeedback:


                startActivity(new Intent(getActivity(), FeedbackActivity.class));

                break;


            case R.id.layoutLogout:


                Utills.show_dialog_msg(getActivity(), "Are you sure you want to logout ?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        {"":"3"}

                        try {

                            showProgressDialog();

                            JSONObject data = new JSONObject();
                            data.put("userid", sharedPrefHelper.getUserId());

                            new SuperWebServiceG(GlobalConstants.URL + "signout", data, new CallBackWebService() {
                                @Override
                                public void webOnFinish(String output) {


                                    cancelDialog();

                                    sharedPrefHelper.logOut();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().finish();
                                }
                            }).execute();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;


        }
    }
}
