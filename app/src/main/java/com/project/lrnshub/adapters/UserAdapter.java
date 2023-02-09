package com.project.lrnshub.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.project.lrnshub.EditUserAppsActivity;
import com.project.lrnshub.R;
import com.project.lrnshub.models.Users;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    Context context;
    List<Users> usersList;

    TextView lblNum, txtName, txtUserID;
    ImageButton btnEdit, btnDelete;
    AlertDialog editDialog;
    TextInputEditText dialogEditName, dialogEditID;


    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int i) {
        return usersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = LayoutInflater.from(context).inflate(R.layout.item_users, viewGroup, false);
        initViews(mView);
        Users users = usersList.get(i);
        if (users != null) {
            lblNum.setText(Integer.toString(i + 1));
            String name = users.getName();
            String docID = users.getDocID();
            if (name.length() >= 6) {
                txtName.setText(name.substring(0, 6) + " ...");
            } else {
                txtName.setText(name);
            }
            if (docID.length() >= 6) {
                txtUserID.setText(docID.substring(0, 6) + " ...");
            } else {
                txtUserID.setText(docID);
            }

        }
        initListeners(i);
        return mView;
    }

    private void initListeners(int position) {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users users = usersList.get(position);
                Intent intent = new Intent(context, EditUserAppsActivity.class);
                intent.putExtra("userID", users.getDocID());
                context.startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                DialogInterface.OnClickListener dListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                };
                mBuilder.setMessage("Are You Sure You Want To Delete User?")
                        .setPositiveButton("No", dListener)
                        .setNegativeButton("Yes", dListener)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void initDialogViews(View mView, Users users) {
        dialogEditName = mView.findViewById(R.id.editName);
        dialogEditID = mView.findViewById(R.id.editUserID);
        dialogEditName.setText(users.getName());
        dialogEditID.setText(users.getDocID());
    }

    private void initViews(View mView) {
        lblNum = mView.findViewById(R.id.lblNum);
        txtName = mView.findViewById(R.id.txtName);
        txtUserID = mView.findViewById(R.id.txtUserID);
        btnEdit = mView.findViewById(R.id.btnEdit);
        btnDelete = mView.findViewById(R.id.btnDelete);

    }
}
