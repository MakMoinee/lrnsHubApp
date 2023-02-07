package com.project.lrnshub.ui.newnote;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lrnshub.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewNoteAdapter extends RecyclerView.Adapter<NewNoteAdapter.ViewHolder> {

    private List<Items> list;
    private ItemTouchHelper itemTouchHelper;
    private LayoutInflater mInflater;
    private static NewNoteAdapter.ClickListener clickListener;
    private RecyclerView recyclerView;
    private Boolean wait = false;
    private Boolean pressedEnter = false;
    Context context;
    private Boolean textChange = false;
    NewNoteAdapter.ViewHolder holder;
    TextWatcher textWatcher;

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // Swap the positions of the items in the list
            Collections.swap(list, viewHolder.getPosition(), target.getPosition());
            // Notify the adapter that the item has moved
            holder.textView.clearFocus();
            notifyItemMoved(viewHolder.getPosition(), target.getPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // Not implemented
        }
    };


    public NewNoteAdapter(Context context, List<Items> list, RecyclerView recyclerView) {
        this.mInflater = LayoutInflater.from(context);
        this.recyclerView = recyclerView;
        this.list = list;
        this.context = context;

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public NewNoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.newnote_layout, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final NewNoteAdapter.ViewHolder holder, int position) {
        this.holder = holder;
        Items item = list.get(position);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.getChecked());
        holder.textView.setText(item.getText());

        holder.move.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                pressedEnter = true;
                wait = true;
                itemTouchHelper.startDrag(holder);
            }
            return false;
        });

//        holder.move.setOnFocusChangeListener((v, hasFocus) -> {
//            if(hasFocus){
//                holder.textView.clearFocus();
//            }
//        });

        holder.textView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (list.size() != 1) {
                    if(position == 0){
                        holder.cross.setVisibility(View.INVISIBLE);
                    } else {
                        holder.cross.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if(pressedEnter){
                    pressedEnter = false;
                } else {
                    changeText(item, holder.textView.getText().toString(), position);
                }
                holder.cross.setVisibility(View.INVISIBLE);
            }
        });


//         textWatcher = new TextWatcher() {
//
//            CountDownTimer timer = null;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (timer != null) {
//                    timer.cancel();
//                }
//                timer = new CountDownTimer(1000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                    }
//
//                    public void onFinish() {
//                        textChange = true;
//                    }
//
//                }.start();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        };
//
//        holder.textView.addTextChangedListener(textWatcher);


        holder.textView.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                holder.textView.clearFocus();
                addItem(position);
                return true;
            }
            return false;
        });

        holder.cross.setOnClickListener(v -> {
            removeItem(position);
            pressedEnter = true;
            clickListener.onDeleteClick(list.get(position), position);
        });
//
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.textView.clearFocus();
            changeItem(item, isChecked, position);
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void updateList() {
        if (list != null && list.size() > 0) {
            list.sort(Comparator.comparing(Items::getText));
            Collections.reverse(list);
           notifyDataSetChanged();
        }
    }

    public void addItem(int position) {
//        item.setText(text);
//        item.setChecked(checked);
//        list.set(position, item);
//        notifyItemChanged(position);
        if(position == 0){
            list.add(0, new Items("0", false, ""));
            notifyItemInserted(0);
            notifyItemRangeChanged(0, list.size());
        }
    }

    public void changeItem(Items item, Boolean bool, int position) {
        item.setChecked(bool);
        list.set(position, item);
        notifyItemChanged(position);
    }

    public void changeText(Items item, String text, int position) {
        item.setText(text);
        list.set(position, item);
        notifyItemChanged(position);
    }

    public List<Items> getList() {
        return list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView textView, move;
        public ImageView cross;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            checkBox = itemView.findViewById(R.id.check);
            textView = itemView.findViewById(R.id.name);
            cross = itemView.findViewById(R.id.cross);
            move = itemView.findViewById(R.id.move);
        }
    }

    private void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onDeleteClick(Items item, int position);

        void onAddNew(Items item, String text, int position);

        void onTextChanged(Items item, String text, int position);

        void onItemCheck(Items item, Boolean bool, int position);
    }
}

//                        debounceTextChange(holder, position, item);

//    private static final long DEBOUNCE_TIME_MS = 500; // Debounce time of 500ms
//    private final Handler mHandler = new Handler(Looper.getMainLooper());
//    private void debounceTextChange(ViewHolder holder, int position, Items item) {
//        Runnable mUpdateDataRunnable = () -> {
//
//        };
//
//        mHandler.removeCallbacks(mUpdateDataRunnable);
//        mHandler.postDelayed(mUpdateDataRunnable, DEBOUNCE_TIME_MS);
//    }