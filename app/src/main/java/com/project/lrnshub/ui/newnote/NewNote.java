package com.project.lrnshub.ui.newnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.lrnshub.databinding.ActivityNewnoteBinding;
import com.project.lrnshub.ui.alarm.AlarmFragment;
import com.project.lrnshub.ui.todo.Notes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewNote extends AppCompatActivity {

    ActivityNewnoteBinding binding;
    List<Items> list;
    Bundle extras;
    Notes notes;
    NewNoteAdapter newNoteAdapter;
    DatabaseReference databaseReference;
    Handler handler;
    Runnable r;
    Boolean fromAddNewList = false;
    FirebaseAuth fAuth;
    Boolean isSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewnoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fAuth = FirebaseAuth.getInstance();

        extras = getIntent().getExtras();
        databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(fAuth.getCurrentUser().getUid());

        if (extras != null) {
            notes = (Notes) extras.getSerializable("list");
            binding.titleName.setText(notes.getTitle());
            binding.specialCode.setText(notes.getCode().toString());
        }

        binding.alarmButton.setOnClickListener(v -> {
//            Intent intent = new Intent(NewNote.this, Alarm.class);
//            intent.putExtra("list", notes);
//            startActivity(intent);
            AlarmFragment alarmFragment = new AlarmFragment();
            Bundle args = new Bundle();
            args.putSerializable("list", notes);
            alarmFragment.setArguments(args);
            alarmFragment.show(getSupportFragmentManager(), "alarm");
        });


        RecyclerView recyclerView = binding.recyclerViewInstalled;
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setReverseLayout(true);
        linearLayout.getStackFromEnd();
        binding.recyclerViewInstalled.setLayoutManager(linearLayout);
        if (extras != null) {
            newNoteAdapter = new NewNoteAdapter(this, notes.getItems(), recyclerView);
        } else {
            newNoteAdapter = new NewNoteAdapter(this, getHouses(), recyclerView);
        }

        binding.recyclerViewInstalled.setAdapter(newNoteAdapter);


        newNoteAdapter.setOnItemClickListener(new NewNoteAdapter.ClickListener() {
            @Override
            public void onDeleteClick(Items item, int position) {
                if (extras != null) {
                    String string = extras.getString("id", "0");
                    databaseReference.child(string).child("items").child(String.valueOf(position)).removeValue();
                }
            }

            @Override
            public void onAddNew(Items item, String text, int position) {

            }


            @Override
            public void onTextChanged(Items item, String text, int position) {
                newNoteAdapter.changeText(item, text, position);
            }

            @Override
            public void onItemCheck(Items item, Boolean bool, int position) {
                newNoteAdapter.changeItem(item, bool, position);
            }
        });

        binding.backButton.setOnClickListener(v -> {
            finish();
        });

        binding.saveButton.setOnClickListener(v -> {
            if(binding.titleName.getText().toString().equals("") && newNoteAdapter.getList().size() == 1 && newNoteAdapter.getList().get(0).getText().equals("") ){
                Toast.makeText(this, "Please input new task", Toast.LENGTH_SHORT).show();
            } else {
                save(recyclerView);
            }
        });


        if (extras != null) {
            binding.deleteButton.setVisibility(View.VISIBLE);
        }

        binding.deleteButton.setOnClickListener(v -> {
          delete();
        });


        handler = new Handler();
        r = () -> {
//            binding.addNewItem.setVisibility(View.VISIBLE);
            binding.addNewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyEvent enterEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER);
                    fromAddNewList = true;
                    binding.addNewItem.setVisibility(View.INVISIBLE);
                    if (recyclerView.getFocusedChild() != null) {
                        recyclerView.getFocusedChild().dispatchKeyEvent(enterEvent);
                    }
                }
            });
        };

        startHandler();

        binding.specialCode.setOnClickListener(v -> {
                    newNoteAdapter.updateList();
                }
        );
    }

    private void save(RecyclerView recyclerView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to save?");
//            builder.setMessage("Message to be displayed in the alert dialog");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (recyclerView.getFocusedChild() != null) {
                    KeyEvent enterEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER);
                    recyclerView.getFocusedChild().dispatchKeyEvent(enterEvent);
                }
                if (extras != null) {
                    String string = extras.getString("id", "0");
                    Notes notesData = new Notes(string, Integer.parseInt(binding.specialCode.getText().toString()), binding.titleName.getText().toString(), newNoteAdapter.getList());
                    databaseReference.child(string).setValue(notesData).addOnCompleteListener(task1 -> {
                        Toast.makeText(NewNote.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Random randomNum = new Random();
                    int code = randomNum.nextInt(2147483647);
                    String key = databaseReference.push().getKey();
                    Notes notesData = new Notes(key, code, binding.titleName.getText().toString(), newNoteAdapter.getList());
                    databaseReference.child(key).setValue(notesData).addOnCompleteListener(task1 -> {
                        finish();
                    });
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete?");
//            builder.setMessage("Message to be displayed in the alert dialog");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (extras != null) {
                    String string = extras.getString("id", "0");
                    databaseReference.child(string).equalTo(string).getRef().removeValue().addOnCompleteListener(task -> finish());
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private List<Items> getHouses() {
        list = new ArrayList<>();
        list.add(new Items("0", false, ""));
        return list;
    }

    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(r);
    }

    public void startHandler() {
        handler.postDelayed(r, 4000);
    }


}