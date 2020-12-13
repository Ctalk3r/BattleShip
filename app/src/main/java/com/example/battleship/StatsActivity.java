package com.example.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.battleship.models.ErrorInfo;
import com.example.battleship.models.StackTraceElementInfo;
import com.example.battleship.models.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

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
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("error").child("stats_load");
                        StackTraceElement lastStackTraceElement = e.getStackTrace()[e.getStackTrace().length - 1];
                        ref.setValue(new ErrorInfo(e.getMessage(), e.getLocalizedMessage(), new StackTraceElementInfo(lastStackTraceElement.getClassName(), lastStackTraceElement.getFileName(), lastStackTraceElement.getLineNumber(), lastStackTraceElement.getMethodName())));
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    statistics.sort(Comparator.comparing(State::getDate));
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