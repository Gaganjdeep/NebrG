package gagan.com.communities.activites;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gagan.com.communities.R;
import gagan.com.communities.adapters.HomeAdapter;
import gagan.com.communities.adapters.MyAdAdapter;
import gagan.com.communities.models.HomeModel;
import gagan.com.communities.models.MyClassifiedModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class MyAdvertisements extends BaseActivityG {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_advertisements);


        findViewByID();


        hitWebserviceG();
    }
    RecyclerView listviewMyAds;

    @Override
    void findViewByID()
    {
         listviewMyAds = (RecyclerView) findViewById(R.id.listviewMyAds);
        listviewMyAds.setLayoutManager(new LinearLayoutManager(MyAdvertisements.this));
    }

    @Override
    void hitWebserviceG()
    {
        try
        {

            showProgressDialog();


            JSONObject data = new JSONObject();
            data.put("user_id", sharedPrefHelper.getUserId());

            new SuperWebServiceG(GlobalConstants.URL + "myclassified", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {

                    cancelDialog();


                    processOutput(output);




                }
            }).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }











    List<MyClassifiedModel> listHome;

    //   ==============process output++++
    private void processOutput(String response)
    {

        try
        {
            listHome=new ArrayList<>();
            JSONObject jsonMain = new JSONObject(response);

            JSONObject jsonMainResult = jsonMain.getJSONObject("result");

            if (jsonMainResult.getString("code").equals("200") )
            {

                JSONArray jsonarrayData = jsonMainResult.getJSONArray("classified");


                for (int g = 0; g < jsonarrayData.length(); g++)
                {
                    JSONObject jobj = jsonarrayData.optJSONObject(g);

                    MyClassifiedModel myClassifiedModel = new MyClassifiedModel();
                    myClassifiedModel.setId(jobj.optString("id"));
                    myClassifiedModel.setTitle(jobj.optString("title"));

                    myClassifiedModel.setImage(jobj.optString("image"));
                    myClassifiedModel.setDescription(jobj.optString("description"));
                    myClassifiedModel.setCategory(jobj.optString("category"));
                    myClassifiedModel.setCreate_date(jobj.optString("create_date"));

                    try
                    {
                        double lat = Double.parseDouble(jobj.optString("lat"));
                        double lng = Double.parseDouble(jobj.optString("lng"));
                        myClassifiedModel.setLatLng(new LatLng(lat,lng));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    listHome.add(myClassifiedModel);
                }

                    MyAdAdapter myAdAdapter = new MyAdAdapter(MyAdvertisements.this, listHome);
                    listviewMyAds.setAdapter(myAdAdapter);



            }
            else
            {
                Utills.showToast("No post available", MyAdvertisements.this, true);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
//            fetchHomeData(startId, limit);
        }

    }
//   =============================


















}
