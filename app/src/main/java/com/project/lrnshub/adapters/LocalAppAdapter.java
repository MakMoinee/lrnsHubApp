package com.project.lrnshub.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.R;
import com.project.lrnshub.interfaces.AdapterListener;
import com.project.lrnshub.ui.home.App;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalAppAdapter extends RecyclerView.Adapter<LocalAppAdapter.ViewHolder> {

    Context context;
    List<App> installedAppList;
    AdapterListener listener;
    Map<Integer, Boolean> isClick = new HashMap<>();

    public LocalAppAdapter(Context context, List<App> installedAppList, AdapterListener l) {
        this.context = context;
        this.installedAppList = installedAppList;
        this.listener = l;
    }

    @NonNull
    @Override
    public LocalAppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalAppAdapter.ViewHolder holder, int position) {
        App item = installedAppList.get(position);
        holder.imageView.setImageDrawable(item.getImage());
        holder.titleTextView.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(holder.getAbsoluteAdapterPosition());
                if (isClick.containsKey(holder.getAbsoluteAdapterPosition())) {
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    isClick.remove(holder.getAbsoluteAdapterPosition());
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#EDEDED"));
                    isClick.put(holder.getAbsoluteAdapterPosition(), true);
                }

                if (isClick.size() > 0) {
                    listener.onEnableButton(true);
                } else {
                    listener.onEnableButton(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return installedAppList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
        }
    }
}
