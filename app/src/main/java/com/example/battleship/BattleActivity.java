package com.example.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.battleship.models.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class BattleActivity extends AppCompatActivity {


    private String gameId;
    private String userId;
    private String yourName;
    private String enemyName;
    private StringBuilder board;
    private StringBuilder eboard;
    private DatabaseReference enemyState;
    private GridLayout enemyBoard;
    private GridLayout yourBoard;
    private DatabaseReference yourState;
    private ArrayList<Integer> leftShips;
    private ArrayList<Integer> leftEnemyShips;

    private Drawable missShape;
    private Drawable destroyedShape;
    private Drawable hideShip;
    private TextView Score;
    private boolean yourTurn;

    public enum CellType {
        EMPTY,
        SHIP,
        MISS,
        DESTROYED,
        NONE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        gameId = getIntent().getStringExtra("gameId");
        userId = getIntent().getStringExtra("userId");
        yourName = getIntent().getStringExtra("name");
        boolean isHost = getIntent().getBooleanExtra("isHost", false);
        board = new StringBuilder();
        eboard = new StringBuilder();
        board.append(getIntent().getStringExtra("board"));
        Score = findViewById(R.id.score);

        if (isHost) {
            yourTurn = true;
            setChoice(yourTurn);
            enemyState = FirebaseDatabase.getInstance().getReference("game").child(gameId).child("client");
            yourState = FirebaseDatabase.getInstance().getReference("game").child(gameId).child("host");
        } else {
            yourTurn = false;
            setChoice(yourTurn);
            enemyState = FirebaseDatabase.getInstance().getReference("game").child(gameId).child("host");
            yourState = FirebaseDatabase.getInstance().getReference("game").child(gameId).child("client");
        }

        enemyState.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                enemyName = dataSnapshot.getValue(String.class);
                Score.setText(yourName + "  0 : 0  " + enemyName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        leftShips = new ArrayList<>();
        leftEnemyShips = new ArrayList<>();
        leftShips.add(0);
        leftShips.add(4);
        leftShips.add(3);
        leftShips.add(2);
        leftShips.add(1);

        leftEnemyShips.add(0);
        leftEnemyShips.add(4);
        leftEnemyShips.add(3);
        leftEnemyShips.add(2);
        leftEnemyShips.add(1);

        yourState.child("leftShips").setValue(leftShips);
        yourState.child("board").setValue(board.toString());

        yourState.child("leftShips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                GenericTypeIndicator<ArrayList<Integer>> t = new GenericTypeIndicator<ArrayList<Integer>>() {};
                setScore(dataSnapshot.getValue(t));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        enemyBoard = findViewById(R.id.enemy_board);
        yourBoard = findViewById(R.id.your_board);

        missShape = getDrawable(R.drawable.shape_miss);
        destroyedShape = getDrawable(R.drawable.shape_destroyed);
        hideShip = getDrawable(R.drawable.shape_hide_ship);

        //region EnemyBoard Subscription
        enemyBoard.findViewById(R.id.frame0).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame1).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame2).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame3).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame4).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame5).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame6).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame7).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame8).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame9).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame10).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame11).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame12).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame13).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame14).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame15).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame16).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame17).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame18).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame19).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame20).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame21).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame22).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame23).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame24).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame25).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame26).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame27).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame28).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame29).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame30).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame31).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame32).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame33).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame34).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame35).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame36).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame37).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame38).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame39).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame40).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame41).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame42).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame43).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame44).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame45).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame46).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame47).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame48).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame49).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame50).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame51).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame52).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame53).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame54).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame55).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame56).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame57).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame58).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame59).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame60).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame61).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame62).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame63).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame64).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame65).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame66).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame67).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame68).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame69).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame70).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame71).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame72).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame73).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame74).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame75).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame76).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame77).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame78).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame79).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame80).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame81).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame82).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame83).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame84).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame85).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame86).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame87).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame88).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame89).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame90).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame91).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame92).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame93).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame94).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame95).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame96).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame97).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame98).setOnClickListener(new MyClickListener());
        enemyBoard.findViewById(R.id.frame99).setOnClickListener(new MyClickListener());        //endregion

        Resources res = getResources();
        for (int i = 0; i < board.length(); ++i) {
            if (board.charAt(i) == '1') {
                int id = res.getIdentifier("frame" + i, "id", getApplicationContext().getPackageName());
                yourBoard.findViewById(id).setBackground(hideShip);
            }
        }
        yourState.child("board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                String newBoard = dataSnapshot.getValue(String.class);
                Resources res = getResources();
                int cnt = 0;
                for (int i = 0; i < newBoard.length(); ++i)
                    if (newBoard.charAt(i) != board.charAt(i)) {
                        int id = res.getIdentifier("frame" + i, "id", getApplicationContext().getPackageName());
                        switch (getCellType(i / 10, i % 10, newBoard)) {
                            case MISS:
                                yourBoard.findViewById(id).setBackground(missShape);
                                ++cnt;
                                break;
                            case DESTROYED:
                                cnt = 2;
                                yourBoard.findViewById(id).setBackground(destroyedShape);
                                break;
                            default:
                                break;
                        }
                }
                if (cnt == 1) {
                    yourTurn = true;
                    setChoice(yourTurn);
                }
                board.setLength(0);
                board.append(newBoard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        enemyState.child("board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                eboard.append(dataSnapshot.getValue(String.class));
                enemyState.child("board").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private CellType getCellType(int a, int b, CharSequence board) {
        if (a < 0 || a >= 10 || b < 0 || b >= 10)
            return CellType.NONE;
        int pos = a * 10 + b;
        return CellType.values()[Character.getNumericValue(board.charAt(pos))];
    }

    private final class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!yourTurn)
                return;
            int pos = Integer.parseInt(((String) v.getTag()).substring(5));
            int a = pos / 10;
            int b = pos % 10;
            switch (getCellType(a, b, eboard)) {
                case EMPTY:
                    v.setBackground(missShape);
                    eboard.setCharAt(pos, Character.forDigit(CellType.MISS.ordinal(), 10));
                    enemyState.child("board").setValue(eboard.toString());
                    yourTurn = false;
                    setChoice(yourTurn);
                    break;
                case SHIP:
                    v.setBackground(destroyedShape);
                    eboard.setCharAt(pos, Character.forDigit(CellType.DESTROYED.ordinal(), 10));
                    CellType top = getCellType(a - 1, b, eboard);
                    CellType bottom = getCellType(a + 1, b, eboard);
                    CellType left = getCellType(a, b - 1, eboard);
                    CellType right = getCellType(a, b + 1, eboard);
                    Resources res = getResources();
                    if (top != CellType.SHIP && top != CellType.DESTROYED &&
                        bottom != CellType.SHIP && bottom != CellType.DESTROYED &&
                        left != CellType.SHIP && left != CellType.DESTROYED &&
                        right != CellType.SHIP && right != CellType.DESTROYED) {
                        for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                            for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                                if (i == a && j == b)
                                    continue;
                                int id = res.getIdentifier("frame" + (i * 10 + j), "id", getApplicationContext().getPackageName());
                                eboard.setCharAt(i * 10 + j, Character.forDigit(CellType.MISS.ordinal(), 10));
                                enemyBoard.findViewById(id).setBackground(missShape);
                            }
                        }
                        leftEnemyShips.set(1, leftEnemyShips.get(1) - 1);
                        enemyState.child("leftShips").setValue(leftEnemyShips);
                    } else {
                        if ((top == CellType.SHIP || top == CellType.DESTROYED) ||
                            (bottom == CellType.SHIP || bottom == CellType.DESTROYED)) {
                            int idx = a - 1, cnt = 0;
                            CellType cell = getCellType(idx, b, eboard);
                            while (cell == CellType.SHIP || cell == CellType.DESTROYED) {
                                --idx;
                                cell = getCellType(idx, b, eboard);
                            }
                            int start = idx;
                            cell = getCellType(++idx, b, eboard);
                            while (cell == CellType.SHIP || cell == CellType.DESTROYED) {
                                ++idx;
                                if (cell == CellType.DESTROYED)
                                    ++cnt;
                                cell = getCellType(idx, b, eboard);
                            }
                            if (idx - start - 1 == cnt) {
                                for (int i = max(0, start); i < min(idx + 1, 10); ++i) {
                                    for (int j = max(0, b - 1); j < min(10, b + 2); ++j) {
                                        if (j == b && (i > start && i < idx))
                                            continue;
                                        int id = res.getIdentifier("frame" + (i * 10 + j), "id", getApplicationContext().getPackageName());
                                        eboard.setCharAt(i * 10 + j, Character.forDigit(CellType.MISS.ordinal(), 10));
                                        enemyBoard.findViewById(id).setBackground(missShape);
                                   }
                                }
                                leftEnemyShips.set(cnt, leftEnemyShips.get(cnt) - 1);
                                enemyState.child("leftShips").setValue(leftEnemyShips);
                            }
                        } else {
                            int jdx = b - 1, cnt = 0;
                            CellType cell = getCellType(a, jdx, eboard);
                            while (cell == CellType.SHIP || cell == CellType.DESTROYED) {
                                --jdx;
                                cell = getCellType(a, jdx, eboard);
                            }
                            int start = jdx;
                            cell = getCellType(a, ++jdx, eboard);
                            while (cell == CellType.SHIP || cell == CellType.DESTROYED) {
                                ++jdx;
                                if (cell == CellType.DESTROYED)
                                    ++cnt;
                                cell = getCellType(a, jdx, eboard);
                            }
                            if (jdx - start - 1 == cnt) {
                                for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                                    for (int j = max(0, start); j < min(jdx + 1, 10); ++j) {
                                        if (i == a && (j > start && j < jdx))
                                            continue;
                                        int id = res.getIdentifier("frame" + (i * 10 + j), "id", getApplicationContext().getPackageName());
                                        eboard.setCharAt(i * 10 + j, Character.forDigit(CellType.MISS.ordinal(), 10));
                                        enemyBoard.findViewById(id).setBackground(missShape);
                                    }
                                }
                                leftEnemyShips.set(cnt, leftEnemyShips.get(cnt) - 1);
                                enemyState.child("leftShips").setValue(leftEnemyShips);
                            }
                        }
                    }
                    enemyState.child("board").setValue(eboard.toString());
                    setScore(leftShips);
                    break;
                case MISS:
                case DESTROYED:
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        FirebaseDatabase.getInstance().getReference("game").child(gameId).removeValue();
        super.onDestroy();
    }

    private void finishGame(int score1, int score2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (score1 > score2) {
            builder.setTitle("Victory!")
                    .setMessage("You've won in this game")
                    .setIcon(R.drawable.ic_ok);
            setStatistics(score1, score2);
        } else {
            builder.setTitle("Fail...")
                    .setMessage("You've lost")
                    .setIcon(R.drawable.ic_bad);
            setStatistics(score1, score2);
        }
        builder.setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                BattleActivity.this.finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("SetTextI18n")
    private void setScore(ArrayList<Integer> data) {
        leftShips = new ArrayList<Integer>(data);
        if (!((TextView)findViewById(R.id.score1)).getText().equals(leftShips.get(1).toString())) {
            ((TextView)findViewById(R.id.score1)).setText(leftShips.get(1).toString());
        }
        if (!((TextView)findViewById(R.id.score2)).getText().equals(leftShips.get(2).toString())) {
            ((TextView)findViewById(R.id.score2)).setText(leftShips.get(2).toString());
        }
        if (!((TextView)findViewById(R.id.score3)).getText().equals(leftShips.get(3).toString())) {
            ((TextView)findViewById(R.id.score3)).setText(leftShips.get(3).toString());
        }
        if (!((TextView)findViewById(R.id.score4)).getText().equals(leftShips.get(4).toString())) {
            ((TextView)findViewById(R.id.score4)).setText(leftShips.get(4).toString());
        }
        int score1 = (20 - (leftEnemyShips.get(1) + leftEnemyShips.get(2) * 2 + leftEnemyShips.get(3) * 3 + leftEnemyShips.get(4) * 4));
        int score2 = (20 - (leftShips.get(1) + leftShips.get(2) * 2 + leftShips.get(3) * 3 + leftShips.get(4) * 4));
        Score.setText(yourName + "  " +
                score1
                + " : " +
                score2
                + "  " + enemyName);
        if (score1 == 20 || score2 == 20) {
            finishGame(score1, score2);
        }
    }

    private void setStatistics(int score1, int score2) {
        State stat = new State(board.toString(), eboard.toString(), score1, score2);
        stat.setHostName(yourName);
        stat.setName(enemyName);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("stat").child(userId).child(gameId);
        ref.setValue(stat);
    }

    private void setChoice(boolean yourChoice) {
        if (yourChoice) {
            ((TextView)findViewById(R.id.turn)).setText("Your turn");
            ((TextView)findViewById(R.id.turn)).setTextColor(getResources().getColor(R.color.colorPrimary));
            findViewById(R.id.turn_progress).setVisibility(View.INVISIBLE);
        } else {
            ((TextView)findViewById(R.id.turn)).setText("Enemy's turn");
            ((TextView)findViewById(R.id.turn)).setTextColor(getResources().getColor(R.color.colorAccent));
            findViewById(R.id.turn_progress).setVisibility(View.VISIBLE);
        }
    }
}
