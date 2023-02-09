package com.project.lrnshub.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.ui.home.App;

import java.util.List;

public class InstalledAppAdapter extends RecyclerView.Adapter<InstalledAppAdapter.ViewHolder> {

    Context context;
    List<App> installedAppList;

    public InstalledAppAdapter(Context context, List<App> installedAppList) {
        this.context = context;
        this.installedAppList = installedAppList;
    }

    @NonNull
    @Override
    public InstalledAppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InstalledAppAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return installedAppList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
