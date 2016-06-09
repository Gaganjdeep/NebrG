package gagan.com.communities.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class ChangePasswordActivity extends BaseActivityG
{

    EditText edNewPswd, edConfirmPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        findViewByID();
    }

    @Override
    void findViewByID()
    {
        edConfirmPswd = (EditText) findViewById(R.id.edConfirmPswd);
        edNewPswd = (EditText) findViewById(R.id.edNewPswd);

    }

    @Override
    void hitWebserviceG()
    {

        try
        {

            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("password", edNewPswd.getText().toString());

            new SuperWebServiceG(GlobalConstants.URL + "editProfile", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    try
                    {

                        cancelDialog();

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.getString("code").contains("20") && !jsonMainResult.getString("code").equals("201"))
                        {

                            Utills.showToast("Password changed successfully", ChangePasswordActivity.this, true);
                            finish();
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


    private boolean validation()
    {
        if (edNewPswd.getText().toString().trim().length() < 4)
        {
            edNewPswd.setError("Password length should more than 4");
            return false;
        }

        else if (!edNewPswd.getText().toString().trim().equals(edConfirmPswd.getText().toString().trim()))
        {
            edConfirmPswd.setError("Password not matched");
            return false;
        }


        return true;
    }


    public void dOne(View view)
    {
        try
        {


            if (validation())
            {
                hitWebserviceG();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
