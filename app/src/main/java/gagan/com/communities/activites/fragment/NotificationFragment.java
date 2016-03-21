package gagan.com.communities.activites.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gagan.com.communities.R;
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
        View v = inflater.inflate(R.layout.recycler_list_layout, container, false);

        RecyclerView recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));

        NotificationAdapter msgAdapter = new NotificationAdapter(getActivity(), null);
        recyclerList.setAdapter(msgAdapter);


        return v;
    }

}
