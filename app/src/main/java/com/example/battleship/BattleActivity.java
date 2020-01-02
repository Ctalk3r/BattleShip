package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BattleActivity extends AppCompatActivity {


    public String gameId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        gameId = getIntent().getStringExtra("gameId");
        Toast.makeText(this, getIntent().getStringExtra("name"), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("game").child(gameId);
        ref.removeValue();
    }
}
