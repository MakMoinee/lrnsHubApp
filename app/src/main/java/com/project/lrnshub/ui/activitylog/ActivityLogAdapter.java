package com.project.lrnshub.ui.activitylog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.R;

import java.util.List;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ItemViewHolder> {

    private List<ItemActivity> itemList;
    private LayoutInflater mInflater;
//    private static ClickListener clickListener;

    public ActivityLogAdapter(Context context, List<ItemActivity> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activitylog_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemActivity item = itemList.get(position);
        holder.imageView.setImageDrawable(item.getImage());
        holder.titleTextView.setText(item.getTitle());
        holder.timeTextView.setText(item.getTime());
//        int val = item.getTime() / 86400 * 100;
        holder.progressBar.setProgress((int) item.getSeconds());
        holder.percentage.setVisibility(View.INVISIBLE);
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    clickListener.onItemClick(item, v);
//                                                }
//                                            }
//        );
    }

    public void updateList (List<ItemActivity> items) {
        if (items != null && items.size() > 0) {
            itemList.clear();
            itemList.addAll(items);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView titleTextView, timeTextView, percentage;
        public ProgressBar progressBar;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            titleTextView = itemView.findViewById(R.id.name);
            timeTextView = itemView.findViewById(R.id.time);
            progressBar = itemView.findViewById(R.id.progress1);
            percentage = itemView.findViewById(R.id.progress_percentage);
        }

    }

//    public void setOnItemClickListener(ClickListener clickListener) {
//        ActivityLogAdapter.clickListener = clickListener;
//    }
//
//    public interface ClickListener {
//        void onItemClick(Item item, View v);
//    }
}
