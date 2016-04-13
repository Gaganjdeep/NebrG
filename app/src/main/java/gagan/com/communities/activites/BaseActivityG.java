package gagan.com.communities.activites;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import gagan.com.communities.R;
import gagan.com.communities.utills.SharedPrefHelper;

/**
 * Created by gagaN on 10-02-2016.
 */
abstract class BaseActivityG extends AppCompatActivity {


    SharedPrefHelper sharedPrefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefHelper = new SharedPrefHelper(this);
    }

    Dialog dialog;

    public void showProgressDialog() {
        dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();

    }

    public void cancelDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }


    abstract void findViewByID();

    abstract void hitWebserviceG();

}
