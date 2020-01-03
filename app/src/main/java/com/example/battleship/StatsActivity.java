package com.example.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private ArrayList<State> statistics;
    private RecyclerView mRecyclerView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        userId = getIntent().getStringExtra("userId");
        mProgressBar = findViewById(R.id.progressBarStats);
        mRecyclerView = findViewById(R.id.stats_list);
        DatabaseReference statsRef = FirebaseDatabase.getInstance().getReference("stat").child(userId);
        statistics = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        statsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    try {
                        State value = ds.getValue(State.class);
                        statistics.add(value);
                    }
                    catch (Exception e) {

                    }

                }
                mRecyclerView.setAdapter(new StatsListAdapter(statistics));
                mProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}