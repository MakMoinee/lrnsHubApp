package com.project.lrnshub.ui.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.project.lrnshub.R;
import com.project.lrnshub.adapters.UserAdapter;
import com.project.lrnshub.interfaces.SimpleListener;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.service.LocalFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment {

    Context mContext;
    ListView lv;
    UserAdapter adapter;
    LocalFirestore fs;

    TextInputEditText editSearch;
    Button btnSearch;
    ProgressDialog pd;

    public AdminHomeFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_admin, container, false);
        initViews(mView);
        initListeners();
        return mView;
    }

    private void initListeners() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editSearch.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please Don't Leave Search Field Empty", Toast.LENGTH_SHORT).show();
                    loadData();
                } else {
                    pd.show();
                    fs.getAllUsers(new SimpleListener() {
                        @Override
                        public void onSuccessUserList(List<Users> usersList) {
                            pd.dismiss();
                            lv.setAdapter(null);

                            List<Users> newUsers = new ArrayList<>();
                            for (Users users : usersList) {
                                if (users.getName().contains(editSearch.getText().toString())) {
                                    newUsers.add(users);
                                }
                            }

                            if (newUsers != null) {
                                adapter = new UserAdapter(mContext, newUsers);
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
        });
    }

    private void initViews(View mView) {
        lv = mView.findViewById(R.id.lv);
        editSearch = mView.findViewById(R.id.editSearch);
        btnSearch = mView.findViewById(R.id.btnSearch);
        fs = new LocalFirestore(mContext);
        pd = new ProgressDialog(mContext);
        pd.setMessage("Getting Data ...");
        pd.setCancelable(false);

        pd.show();
        loadData();
    }

    private void loadData() {
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
