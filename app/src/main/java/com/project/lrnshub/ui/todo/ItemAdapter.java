package com.project.lrnshub.ui.todo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.R;
import com.project.lrnshub.ui.newnote.Items;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.RoomViewHolder> {

    private List<Items> mItems;
    private LayoutInflater mInflater;


    public ItemAdapter(Context context, List<Items> item) {
        this.mInflater = LayoutInflater.from(context);
       this.mItems = item;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Items item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mNameTextView;

        public RoomViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.text_view_name);
        }

        public void bind(Items item) {
            mNameTextView.setText(item.getText());
            mNameTextView.setChecked(item.getChecked());
        }
    }
}
