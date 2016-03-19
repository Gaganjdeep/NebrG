package gagan.com.communities.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import gagan.com.communities.R;
import gagan.com.communities.models.UserDataModel;
import gagan.com.communities.utills.GlobalConstants;
import gagan.com.communities.utills.SharedPrefHelper;
import gagan.com.communities.utills.Utills;
import gagan.com.communities.webserviceG.CallBackWebService;
import gagan.com.communities.webserviceG.SuperWebServiceG;

public class ForgetPasswordActivity extends BaseActivityG {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        findViewByID();
    }

    EditText edEmail;

    @Override
    void findViewByID() {
        edEmail = (EditText) findViewById(R.id.edEmail);
    }

    @Override
    void hitWebserviceG() {
        try {


            showProgressDialog();

            JSONObject data = new JSONObject();
            data.put("email", edEmail.getText().toString().trim());

            new SuperWebServiceG(GlobalConstants.URL + "forgotpassword", data, new CallBackWebService() {
                @Override
                public void webOnFinish(String output) {
                    cancelDialog();

                    try {

                        JSONObject jsonMain = new JSONObject(output);

                        JSONObject jsonMainResult = jsonMain.getJSONObject("result");


                        Utills.showToast(jsonMainResult.optString("status"), ForgetPasswordActivity.this, true);

                        finish();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).execute();


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dOne(View view) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString().trim()).matches()) {
            edEmail.setError("Enter a valid email");
            return;
        }


        hitWebserviceG();
    }
}
