package com.project.lrnshub.ui.todo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.project.lrnshub.R;

import org.w3c.dom.Text;

public class NotesAdapter extends FirebaseRecyclerAdapter<Notes, NotesAdapter.HouseViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private static NotesAdapter.ClickListener clickListener;

    public NotesAdapter(Context context, FirebaseRecyclerOptions options) {
        super(options);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_house, parent, false);

        return new HouseViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull HouseViewHolder holder, int position, @NonNull Notes model) {
        holder.bind(model);
    }

    public class HouseViewHolder extends RecyclerView.ViewHolder {

        private TextView mAddressTextView;
        private TextView constraintLayout;
        private RecyclerView mRoomRecyclerView;

        public HouseViewHolder(View itemView) {
            super(itemView);
            mAddressTextView = itemView.findViewById(R.id.text_view_address);
            mRoomRecyclerView = itemView.findViewById(R.id.recycler_view_rooms);
            constraintLayout = itemView.findViewById(R.id.on_click);
        }

        public void bind(Notes notes) {
            mAddressTextView.setText(notes.getTitle());
            ItemAdapter roomAdapter = new ItemAdapter(context, notes.getItems());
            mRoomRecyclerView.setAdapter(roomAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setReverseLayout(true);
            mRoomRecyclerView.setLayoutManager(linearLayoutManager);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(notes, v);
                }
            });
        }
    }

    public void setOnItemClickListener(NotesAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Notes item, View v);
    }
}
