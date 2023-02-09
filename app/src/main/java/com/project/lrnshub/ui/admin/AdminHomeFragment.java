package com.project.lrnshub.ui.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.project.lrnshub.R;
import com.project.lrnshub.adapters.UserAdapter;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.service.LocalFirestore;

import java.util.List;

public class AdminHomeFragment extends Fragment {

    Context mContext;
    ListView lv;
    UserAdapter adapter;
    LocalFirestore fs;
    ProgressDialog pd;

    public AdminHomeFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_admin, container, false);
        initViews(mView);
        return mView;
    }

    private void initViews(View mView) {
        lv = mView.findViewById(R.id.lv);
        fs = new LocalFirestore(mContext);
        pd = new ProgressDialog(mContext);
        pd.setMessage("Getting Data ...");
        pd.setCancelable(false);

        pd.show();
        fs.getAllUsers(new SimpleListener() {
            @Override
            public void onSuccessUserList(List<Users> usersList) {
                pd.dismiss();
                lv.setAdapter(null);
                if (usersList != null) {
                    adapter = new UserAdapter(mContext, usersList);
                    lv.setAdapter(adapter);
                }

            }

            @Override
            public void onError(Exception e) {
                pd.dismiss();
                Toast.makeText(mContext, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
