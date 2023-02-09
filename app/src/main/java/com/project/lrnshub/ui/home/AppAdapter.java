package com.project.lrnshub.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.R;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ItemViewHolder> {

    private List<App> itemList;
    private LayoutInflater mInflater;
    private static ClickListener clickListener;

    public AppAdapter(Context context, List<App> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        App item = itemList.get(position);
        holder.imageView.setImageDrawable(item.getImage());
        holder.titleTextView.setText(item.getTitle());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    clickListener.onItemClick(item, v);
                                                }
                                            }
        );
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView titleTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AppAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(App item, View v);
    }

    public void updateList(List<App> list) {
        this.itemList = list;
        notifyDataSetChanged();
    }
}
