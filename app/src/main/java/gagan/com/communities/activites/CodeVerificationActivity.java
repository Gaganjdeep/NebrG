package gagan.com.communities.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.entercode.CodeInput;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class CodeVerificationActivity extends BaseActivityG
{

    CodeInput codeView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);


        codeView = (CodeInput) findViewById(R.id.codeView);


    }

    @Override
    void findViewByID()
    {

    }

    @Override
    void hitWebserviceG()
    {

    }

    @Override
    public void onBackPressed()
    {

    }

    public void dOne(View view)
    {
        try
        {

            StringBuilder code = new StringBuilder();


            for (int i = 0; i < codeView.getCode().length; i++)
            {
                code.append(codeView.getCode()[i]);
            }


            showProgressDialog();
            JSONObject data = new JSONObject();
            data.put("userid", sharedPrefHelper.getUserId());
            data.put("otp", code.toString());
            data.put("device_type", "android");
            data.put("device_token", sharedPrefHelper.getDeviceToken());

            new SuperWebServiceG(GlobalConstants.URL + "confirmotp", data, new CallBackWebService()
            {
                @Override
                public void webOnFinish(String output)
                {
                    try
                    {
                        cancelDialog();
                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");

                        if (jsonMainResult.optString("code").equals("200"))
                        {

                            sharedPrefHelper.setEmailVerified(true);

                            startActivity(new Intent(CodeVerificationActivity.this, MainTabActivity.class));
                            finish();
                        }
                        else
                        {
                            Utills.showToast("Wrong OTP entered.", CodeVerificationActivity.this, true);
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
