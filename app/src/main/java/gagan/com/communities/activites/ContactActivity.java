package gagan.com.communities.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class ContactActivity extends BaseActivityG {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        settingActionBar();
        findViewByID();
    }

    @Override
    void findViewByID() {
       edSubject=(EditText) findViewById(R.id.edSubject) ;
       edMsg=(EditText) findViewById(R.id.edMsg) ;
    }

    @Override
    void hitWebserviceG() {

    }

          EditText edSubject,edMsg;
    private void settingActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        finish();


        return super.onOptionsItemSelected(item);
    }

    public void subMitt(View view)
    {

        try
        {
            if(edSubject.getText().toString().trim().equals(""))
            {
                 Utills.showToast("Please enter subject", ContactActivity.this, true);
                       return;
            }
            else if(edMsg.getText().toString().trim().equals(""))
            {
                          Utills.showToast("Please enter some message", ContactActivity.this, true);

                     return;
            }


            showProgressDialog();

            UserDataModel userDateModel=SharedPrefHelper.read(ContactActivity.this);


            JSONObject dataJ = new JSONObject();
            dataJ.put("user_email", userDateModel.getEmail());
            dataJ.put("name", userDateModel.getName());
            dataJ.put("subject", edSubject.getText().toString().trim());
            dataJ.put("message", edMsg.getText().toString().trim());

            new SuperWebServiceG(GlobalConstants.URL + "contactus", dataJ, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String response)
                {
                    try
                    {
                        cancelDialog();

                        JSONObject jsonMain = new JSONObject(response);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").equals("200"))
                        {
                            finish();
                            Utills.showToast("Your message has been sent", ContactActivity.this, true);
                        }
                        else
                        {
                            Utills.showToast(jsonMainResult.optString("status"), ContactActivity.this, true);
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
