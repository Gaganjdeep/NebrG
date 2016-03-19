package gagan.com.communities.activites.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import gagan.com.communities.R;
import gagan.com.communities.activites.MainTabActivity;
import gagan.com.communities.activites.OtherProfileActivity;
import gagan.com.communities.adapters.MessageAdapter;
import gagan.com.communities.adapters.NotificationAdapter;


public class NotificationFragment extends Fragment {


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification_message, container, false);

        ListView listViewNotiMsg = (ListView) v.findViewById(R.id.listViewNotiMsg);


        NotificationAdapter msgAdapter = new NotificationAdapter(getActivity(), null);
        listViewNotiMsg.setAdapter(msgAdapter);


        return v;
    }

}
