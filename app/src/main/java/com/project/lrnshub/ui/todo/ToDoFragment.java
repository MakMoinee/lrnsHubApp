package com.project.lrnshub.ui.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.project.lrnshub.databinding.FragmentSettingsBinding;
import com.project.lrnshub.ui.newnote.NewNote;

public class ToDoFragment extends Fragment {
    private FragmentSettingsBinding binding;
    NotesAdapter noteAdapter;
    FirebaseAuth fAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TodoViewModel todoViewModel =
                new ViewModelProvider(this).get(TodoViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fAuth = FirebaseAuth.getInstance();
        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("notes").child(fAuth.getCurrentUser().getUid()), Notes.class)
                        .build();


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        binding.recyclerViewInstalled.setLayoutManager(staggeredGridLayoutManager);
         noteAdapter = new NotesAdapter(getContext(), options);
        binding.recyclerViewInstalled.setAdapter(noteAdapter);


        binding.fab.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), NewNote.class));
        });

        noteAdapter.setOnItemClickListener((item, v) -> {
            Intent intent = new Intent(requireContext(), NewNote.class);
            intent.putExtra("list", item);
            intent.putExtra("id", item.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            requireContext().startActivity(intent);
        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FirebaseRecyclerOptions<Notes> options =
                        new FirebaseRecyclerOptions.Builder<Notes>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("notes").child(fAuth.getCurrentUser().getUid()).orderByChild("title").startAt(s.toString()).endAt(s + "\uf8ff"), Notes.class)
                                .build();
                noteAdapter.updateOptions(options);
                noteAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }
}