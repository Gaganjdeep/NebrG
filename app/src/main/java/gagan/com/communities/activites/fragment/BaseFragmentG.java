package gagan.com.communities.activites.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gagan.com.communities.R;
import gagan.com.communities.utills.SharedPrefHelper;

/**
 * Created by sony on 27-02-2016.
 */
public class BaseFragmentG extends Fragment {


    public   Dialog dialog;
  public   SharedPrefHelper sharedPrefHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPrefHelper = new SharedPrefHelper(getActivity());
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog() {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();

    }


    public void cancelDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

}
