package gagan.com.communities.activites.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import gagan.com.communities.R;
import gagan.com.communities.adapters.MessageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_notification_message, container, false);


        ListView listViewNotiMsg=(ListView)v.findViewById(R.id.listViewNotiMsg);





        MessageAdapter msgAdapter=new MessageAdapter(getActivity(),null);
        listViewNotiMsg.setAdapter(msgAdapter);

        return v;
    }

}
