package gagan.com.communities.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import gagan.com.communities.R;
import gagan.com.communities.adapters.MyAdAdapter;
import gagan.com.communities.adapters.NotificationAdapter;

public class MyAdvertisements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_advertisements);


        ListView listviewMyAds = (ListView) findViewById(R.id.listviewMyAds);


        MyAdAdapter msgAdapter = new MyAdAdapter(MyAdvertisements.this, null);
        listviewMyAds.setAdapter(msgAdapter);


    }
}
