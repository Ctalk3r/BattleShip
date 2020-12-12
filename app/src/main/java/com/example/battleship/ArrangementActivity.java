package com.example.battleship;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.UUID;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ArrangementActivity extends AppCompatActivity {

    private int[][] used;
    private int fromBoard = -1;
    private int shipCount = 0;
    private String gameId;
    private String userName;
    private String userId;
    private View shouldRelease = null;
    private int releasePos = 0;
    private Random rand = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrangement);

        userName = getIntent().getStringExtra("name");
        userId = getIntent().getStringExtra("userId");

        used = new int[10][10];
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                used[i][j] = -1;
            }
        }

        findViewById(R.id.random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.clear).callOnClick();
                ImageView ship = findViewById(R.id.quad1);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.triple1);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.triple2);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.double1);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.double2);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.double3);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.single1);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.single2);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.single3);
                while(!trySetShip(ship)) { }

                ship = findViewById(R.id.single4);
                while(!trySetShip(ship)) { }
            }
        });

        findViewById(R.id.play).setEnabled(false);
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();
                for (int i = 0; i < 10; ++i) {
                    for (int j = 0; j < 10; ++j) {
                        if (used[i][j] > 0) {
                            int id = res.getIdentifier("frame_image" + Integer.toString(i * 10 + j), "id", getApplicationContext().getPackageName());
                            ((ImageView)findViewById(id)).setImageResource(R.drawable.shape);
                        }
                        used[i][j] = -1;
                    }
                }

                findViewById(R.id.play).setEnabled(false);
                fromBoard = -1;
                shouldRelease = null;
                releasePos = 0;
                shipCount = 0;

                ((ImageView)findViewById(R.id.single1)).setImageResource(R.drawable.shape_single);
                ((ImageView)findViewById(R.id.single1)).setTag("single1");
                ((ImageView)findViewById(R.id.single2)).setImageResource(R.drawable.shape_single);
                ((ImageView)findViewById(R.id.single2)).setTag("single2");
                ((ImageView)findViewById(R.id.single3)).setImageResource(R.drawable.shape_single);
                ((ImageView)findViewById(R.id.single3)).setTag("single3");
                ((ImageView)findViewById(R.id.single4)).setImageResource(R.drawable.shape_single);
                ((ImageView)findViewById(R.id.single4)).setTag("single4");
                ((ImageView)findViewById(R.id.double1)).setImageResource(R.drawable.shape_double);
                ((ImageView)findViewById(R.id.double1)).setTag("double1");
                ((ImageView)findViewById(R.id.double2)).setImageResource(R.drawable.shape_double);
                ((ImageView)findViewById(R.id.double2)).setTag("double2");
                ((ImageView)findViewById(R.id.double3)).setImageResource(R.drawable.shape_double);
                ((ImageView)findViewById(R.id.double3)).setTag("double3");
                ((ImageView)findViewById(R.id.triple1)).setImageResource(R.drawable.shape_triple);
                ((ImageView)findViewById(R.id.triple1)).setTag("triple1");
                ((ImageView)findViewById(R.id.triple2)).setImageResource(R.drawable.shape_triple);
                ((ImageView)findViewById(R.id.triple2)).setTag("triple2");
                ((ImageView)findViewById(R.id.quad1)).setImageResource(R.drawable.shape_quad);
                ((ImageView)findViewById(R.id.quad1)).setTag("quad1");
            }
        });
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(true);
            }
        });

        //region BoardSubscription
        findViewById(R.id.frame_image0).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame0).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image1).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame1).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image2).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame2).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image3).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame3).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image4).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame4).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image5).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame5).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image6).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame6).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image7).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame7).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image8).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame8).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image9).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame9).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image10).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame10).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image11).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame11).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image12).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame12).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image13).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame13).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image14).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame14).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image15).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame15).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image16).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame16).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image17).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame17).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image18).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame18).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image19).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame19).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image20).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame20).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image21).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame21).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image22).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame22).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image23).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame23).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image24).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame24).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image25).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame25).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image26).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame26).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image27).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame27).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image28).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame28).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image29).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame29).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image30).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame30).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image31).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame31).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image32).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame32).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image33).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame33).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image34).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame34).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image35).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame35).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image36).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame36).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image37).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame37).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image38).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame38).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image39).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame39).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image40).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame40).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image41).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame41).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image42).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame42).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image43).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame43).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image44).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame44).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image45).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame45).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image46).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame46).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image47).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame47).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image48).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame48).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image49).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame49).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image50).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame50).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image51).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame51).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image52).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame52).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image53).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame53).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image54).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame54).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image55).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame55).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image56).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame56).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image57).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame57).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image58).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame58).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image59).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame59).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image60).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame60).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image61).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame61).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image62).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame62).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image63).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame63).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image64).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame64).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image65).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame65).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image66).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame66).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image67).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame67).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image68).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame68).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image69).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame69).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image70).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame70).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image71).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame71).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image72).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame72).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image73).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame73).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image74).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame74).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image75).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame75).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image76).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame76).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image77).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame77).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image78).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame78).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image79).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame79).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image80).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame80).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image81).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame81).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image82).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame82).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image83).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame83).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image84).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame84).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image85).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame85).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image86).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame86).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image87).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame87).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image88).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame88).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image89).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame89).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image90).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame90).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image91).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame91).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image92).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame92).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image93).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame93).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image94).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame94).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image95).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame95).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image96).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame96).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image97).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame97).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image98).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame98).setOnDragListener(new MyDragListener());

        findViewById(R.id.frame_image99).setOnTouchListener(new MyCombinedTouchListener());
        findViewById(R.id.frame99).setOnDragListener(new MyDragListener());
        //endregion

        findViewById(R.id.single1).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.single2).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.single3).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.single4).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.double1).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.double2).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.double3).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.triple1).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.triple2).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.quad1).setOnTouchListener(new MyTouchListener());
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getTag() == null || view.getTag().toString().equals("empty"))
                return false;
            ClipData data = ClipData.newPlainText("", "");
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                switchShipImage((ImageView)view, true);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyCombinedTouchListener implements View.OnTouchListener {
        View view;
        final Handler handler = new Handler();
        boolean goneFlag = false;
        public boolean onTouch(View currentView, MotionEvent motionEvent) {
            if (currentView == null || currentView.getTag() == null || currentView.getTag().toString().equals("empty"))
                return false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    goneFlag = true;
                    ClipData data = ClipData.newPlainText("", "");
                    if (view.getTag().toString().contains("_")) {
                        int pos = Integer.parseInt(((String) view.getTag()).substring(11));
                        int a = pos / 10;
                        int b = pos % 10;
                        int val = used[a][b] / 10000;
                        boolean verticalFlag = ((used[a][b] / 1000) % 10 == 1);
                        if (used[a][b] < 0)
                            return;
                        ImageView translateView = null;
                        Resources res = getResources();
                        switch (val) {
                            case 1:
                                fromBoard = used[a][b] % 100;
                                translateView = findViewById(R.id.single1);
                                if (!translateView.getTag().equals("empty")) {
                                    translateView = findViewById(R.id.single2);
                                    if (!translateView.getTag().equals("empty")) {
                                        translateView = findViewById(R.id.single3);
                                        if (!translateView.getTag().equals("empty")) {
                                            translateView = findViewById(R.id.single4);
                                            translateView.setTag("single4");
                                        } else {
                                            translateView.setTag("single3");
                                        }
                                    } else {
                                        translateView.setTag("single2");
                                    }
                                } else {
                                    translateView.setTag("single1");
                                }
                                translateView.setImageResource(R.drawable.shape_single);
                                break;
                            case 2:
                                if (verticalFlag) {
                                    if (pos != used[a][b] % 100) {
                                        int id = res.getIdentifier("frame_image" + Integer.toString(used[a][b] % 100), "id", getApplicationContext().getPackageName());
                                        view = findViewById(id);
                                    }
                                    fromBoard = used[a][b] % 100;
                                    translateView = findViewById(R.id.double1v);
                                    translateView.setTag("double1v");
                                    translateView.setImageResource(R.drawable.shape_double_v);
                                } else {
                                    if (pos != used[a][b] % 100) {
                                        int id = res.getIdentifier("frame_image" + Integer.toString(used[a][b] % 100), "id", getApplicationContext().getPackageName());
                                        view = findViewById(id);
                                    }
                                    fromBoard = used[a][b] % 100;
                                    translateView = findViewById(R.id.double1);
                                    if (!translateView.getTag().equals("empty")) {
                                        translateView = findViewById(R.id.double2);
                                        if (!translateView.getTag().equals("empty")) {
                                            translateView = findViewById(R.id.double3);
                                            translateView.setTag("double3");
                                        } else {
                                            translateView.setTag("double2");
                                        }
                                    } else {
                                        translateView.setTag("double1");
                                    }
                                    translateView.setImageResource(R.drawable.shape_double);
                                }
                                break;
                            case 3:
                                if (verticalFlag) {
                                    if (pos != used[a][b] % 100 + 10) {
                                        int id = res.getIdentifier("frame_image" + Integer.toString(used[a][b] % 100 + 10), "id", getApplicationContext().getPackageName());
                                        view = findViewById(id);
                                    }
                                    fromBoard = used[a][b] % 100 + 10;
                                    translateView = findViewById(R.id.triple1v);
                                    translateView.setTag("triple1v");
                                    translateView.setImageResource(R.drawable.shape_triple_v);
                                }
                                else {
                                    if (pos != used[a][b] % 100 + 1) {
                                        int id = res.getIdentifier("frame_image" + Integer.toString(used[a][b] % 100 + 1), "id", getApplicationContext().getPackageName());
                                        view = findViewById(id);
                                    }
                                    fromBoard = used[a][b] % 100 + 1;
                                    translateView = findViewById(R.id.triple1);
                                    if (!translateView.getTag().equals("empty")) {
                                        translateView = findViewById(R.id.triple2);
                                        translateView.setTag("triple2");
                                    } else {
                                        translateView.setTag("triple1");
                                    }
                                    translateView.setImageResource(R.drawable.shape_triple);
                                }
                                break;
                            case 4:
                                if (verticalFlag) {
                                    if (pos != used[a][b] % 100 + 10) {
                                        int id = res.getIdentifier("frame_image" + Integer.toString(used[a][b] % 100 + 10), "id", getApplicationContext().getPackageName());
                                        view = findViewById(id);
                                    }
                                    fromBoard = used[a][b] % 100 + 10;
                                    translateView = findViewById(R.id.quad1v);
                                    translateView.setTag("quad1v");
                                    translateView.setImageResource(R.drawable.shape_quad_v);
                                } else {
                                    if (pos != used[a][b] % 100 + 1) {
                                        int id = res.getIdentifier("frame_image" + Integer.toString(used[a][b] % 100 + 1), "id", getApplicationContext().getPackageName());
                                        view = findViewById(id);
                                    }
                                    fromBoard = used[a][b] % 100 + 1;
                                    translateView = findViewById(R.id.quad1);
                                    translateView.setTag("quad1");
                                    translateView.setImageResource(R.drawable.shape_quad);
                                }
                                break;
                            default:
                                translateView = findViewById(R.id.triple1);
                                translateView.setImageResource(R.drawable.shape_single);
                                translateView.setTag("single1");
                        }
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(translateView);
                        view.startDrag(data, shadowBuilder, translateView, 0);
                        releasePos = a * 10 + b;
                        shouldRelease = view;
                        switchShipImage((ImageView)translateView, true);

                    } else {
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(data, shadowBuilder, view, 0);
                        switchShipImage((ImageView)view, true);
                    }
                }
            };
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view = currentView;
                    goneFlag = false;
                    handler.postDelayed(runnable, 100);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacksAndMessages(null);
                    if (!goneFlag) {
                        tryRotateShip(view);
                        return true;
                    }
                    break;
            }
            return true;
        }
    }

    private boolean trySetShip(ImageView ship) {
        Resources res = getResources();
        int verticalFlag = rand.nextInt(2);
        String initTag = ship.getTag().toString();
        if (verticalFlag != 0) {
            ship.setTag(ship.getTag() + "v");
        }
        int pos = rand.nextInt(100);
        int id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
        ImageView cell = findViewById(id);
        if (canDrop(ship, cell)) {
            setShip(ship, cell);
            switchShipImage(ship, true);
            ship.setTag("empty");
            return true;
        }
        if (verticalFlag != 0) {
            ship.setTag(initTag);
        }
        return false;
    }

    private void tryRotateShip(View cell) {
        int pos = Integer.parseInt(((String) cell.getTag()).substring(11));
        int a = pos / 10;
        int b = pos % 10;
        int val = used[a][b] / 10000;
        if (val == 1)
            return;
        boolean verticalFlag = ((used[a][b] / 1000) % 10 == 1);
        if (verticalFlag  && b == 9)
            return;
        if (!verticalFlag && a == 9)
            return;
        Resources res = getResources();

        if (used[a][b] < 0)
            return;
        ImageView checkView = findViewById(R.id.quad1);
        int setPos = used[a][b] % 100;
        int add = 0;
        switch (val) {
            case 1:
                checkView.setTag("single1");
                break;
            case 2:
                checkView.setTag("double1");
                break;
            case 3:
                checkView.setTag("triple1");
                if (!verticalFlag) {
                    setPos += 10;
                    add++;
                } else {
                    add += 10;
                    setPos++;
                }
                break;
            case 4:
                checkView.setTag("quad1");
                if (!verticalFlag) {
                    setPos += 10;
                    add++;
                } else {
                    add += 10;
                    setPos++;
                }
                break;
        }
        if (!verticalFlag)
            checkView.setTag(checkView.getTag().toString() + "v");
        int id = res.getIdentifier("frame_image" + Integer.toString(used[a][b] % 100 + add), "id", getApplicationContext().getPackageName());
        cell = findViewById(id);

        if (!verticalFlag) {
            checkView.setTag(checkView.getTag().toString().substring(0, checkView.getTag().toString().length() - 1));
            releaseShip(checkView, used[a][b]);
            checkView.setTag(checkView.getTag().toString() + "v");
        } else {
            checkView.setTag(checkView.getTag().toString() + "v");
            releaseShip(checkView, used[a][b]);
            checkView.setTag(checkView.getTag().toString().substring(0, checkView.getTag().toString().length() - 1));
        }

        id = res.getIdentifier("frame_image" + Integer.toString(setPos), "id", getApplicationContext().getPackageName());
        View setView = findViewById(id);

        if (canDrop(checkView, setView)) {
            setShip(checkView, setView);
        } else {
            if (!verticalFlag)
                checkView.setTag(checkView.getTag().toString().substring(0, checkView.getTag().toString().length() - 1));
            else
                checkView.setTag(checkView.getTag().toString() + "v");
            setShip(checkView, cell);
        }
        checkView.setTag("quad1");
    }

    public void switchShipImage(ImageView ship, boolean first) {
        String type = (String) ship.getTag();
        type = type.substring(0, type.length() - 1);
        if (Character.isDigit(type.charAt(type.length() - 1)))
            type = type.substring(0, type.length() - 1);
        Resources res = getResources();
        int id;
        if (first) {
            id = res.getIdentifier("shape_empty_" + type, "drawable", getPackageName());
        } else {
            id = res.getIdentifier("shape_" + type, "drawable", getPackageName());
        }
        ship.setImageResource(id);
    }

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (shouldRelease != null) {
                        releaseShip(shouldRelease, used[releasePos / 10][releasePos % 10]);
                        shouldRelease = null;
                    }
                    return canDrop((View) event.getLocalState(), v);
                case DragEvent.ACTION_DRAG_ENTERED:
                    setCellBackground((View) event.getLocalState(), v, enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    setCellBackground((View) event.getLocalState(), v, normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    setCellBackground((View) event.getLocalState(), v, normalShape);
                    setShip((View) event.getLocalState(), v);
                    /*ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    FrameLayout container = (FrameLayout) v;
                    container.removeAllViews();
                    container.addView(view);*/

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    view = (View) event.getLocalState();
                    if (view.getTag() != null && !view.getTag().equals("empty")) {
                        if (!event.getResult()) {
                            if (fromBoard == -1)
                                switchShipImage((ImageView)view, false);
                            else {
                                Resources res = getResources();
                                int id = res.getIdentifier("frame" + Integer.toString(fromBoard), "id", getApplicationContext().getPackageName());
                                setShip(view, findViewById(id));
                                if (!view.getTag().toString().contains("_"))
                                    view.setTag("empty");
                            }
                        } else {
                            if (!view.getTag().toString().contains("_"))
                                view.setTag("empty");
                        }
                        fromBoard = -1;
                    }

                default:
                    break;
            }
            return true;
        }
    }

    private boolean canDrop(View ship, View cell) {
        String type = (String) ship.getTag();
        if (type == null || cell.getTag() == null || !cell.getTag().toString().substring(0, 5).equals("frame"))
            return false;
        int pos;
        if (cell.getTag().toString().contains("_"))
            pos = Integer.parseInt(((String) cell.getTag()).substring(11));
        else
            pos = Integer.parseInt(((String) cell.getTag()).substring(5));
        int a = pos / 10;
        int b = pos % 10;
        boolean verticalFlag = type.charAt(type.length() - 1) == 'v';
        switch (type.substring(0, type.length() - (verticalFlag ? 2 : 1))) {
            case "single":
                if (used[a][b] != -1)
                    return false;
                break;
            case "double":
                if (verticalFlag) {
                    if (a + 1 >= 10)
                        return false;
                    for (int i = a; i < a + 2; ++i) {
                        if (used[i][b] != -1)
                            return false;
                    }
                } else {
                    if (b + 1 >= 10)
                        return false;
                    for (int j = b; j < b + 2; ++j) {
                        if (used[a][j] != -1)
                            return false;
                    }
                }
                break;
            case "triple":
                if (verticalFlag) {
                    if (a - 1 < 0 || a + 1 >= 10)
                        return false;
                    for (int i = a - 1; i < a + 2; ++i) {
                        if (used[i][b] != -1)
                            return false;
                    }
                } else {
                    if (b - 1 < 0 || b + 1 >= 10)
                        return false;
                    for (int j = b - 1; j < b + 2; ++j) {
                        if (used[a][j] != -1)
                            return false;
                    }
                }
                break;
            case "quad":
                if (verticalFlag) {
                    if (a - 1 < 0 || a + 2 >= 10)
                        return false;
                    for (int i = a - 1; i < a + 3; ++i) {
                        if (used[i][b] != -1)
                            return false;
                    }
                } else {
                    if (b - 1 < 0 || b + 2 >= 10)
                        return false;
                    for (int j = b - 1; j < b + 3; ++j) {
                        if (used[a][j] != -1)
                            return false;
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void setCellBackground(View ship, View cell, Drawable shape) {
        String type = (String) ship.getTag();
        int pos = Integer.parseInt(((String) cell.getTag()).substring(5));
        int a = pos / 10;
        int b = pos % 10;
        cell.setBackground(shape);

        Resources res = getResources();
        boolean verticalFlag = type.charAt(type.length() - 1) == 'v';
        switch (type.substring(0, type.length() - (verticalFlag ? 2 : 1))) {
            case "single":
                break;
            case "double":
                int id;
                if (verticalFlag) {
                    id = res.getIdentifier("frame" + Integer.toString(pos + 10), "id", getApplicationContext().getPackageName());
                } else {
                    id = res.getIdentifier("frame" + Integer.toString(pos + 1), "id", getApplicationContext().getPackageName());
                }
                findViewById(id).setBackground(shape);
                break;
            case "triple":
                if (verticalFlag) {
                    id = res.getIdentifier("frame" + Integer.toString(pos - 10), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                    id = res.getIdentifier("frame" + Integer.toString(pos + 10), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                } else {
                    id = res.getIdentifier("frame" + Integer.toString(pos - 1), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                    id = res.getIdentifier("frame" + Integer.toString(pos + 1), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                }

                break;
            case "quad":
                if (verticalFlag) {
                    id = res.getIdentifier("frame" + Integer.toString(pos - 10), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                    id = res.getIdentifier("frame" + Integer.toString(pos + 10), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                    id = res.getIdentifier("frame" + Integer.toString(pos + 20), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                } else {
                    id = res.getIdentifier("frame" + Integer.toString(pos - 1), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                    id = res.getIdentifier("frame" + Integer.toString(pos + 1), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                    id = res.getIdentifier("frame" + Integer.toString(pos + 2), "id", getApplicationContext().getPackageName());
                    findViewById(id).setBackground(shape);
                }
                break;
            default:
                break;
        }
    }

    public void setShip(View ship, View cell) {
        String type = (String) ship.getTag();
        int pos;
        if (cell.getTag().toString().contains("_"))
            pos = Integer.parseInt(((String) cell.getTag()).substring(11));
        else
            pos = Integer.parseInt(((String) cell.getTag()).substring(5));
        int a = pos / 10;
        int b = pos % 10;

        shipCount++;
        Resources res = getResources();
        boolean verticalFlag = type.charAt(type.length() - 1) == 'v';
        switch (type.substring(0, type.length() - (verticalFlag ? 2 : 1))) {
            case "single":
                int id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
                ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_single);
                for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                    for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                        --used[i][j];
                    }
                }
                used[a][b] = 11000 + pos;
                break;
            case "double":
                if (verticalFlag) {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_bottom_side_opened);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_top_side_opened);
                    for (int i = max(0, a - 1); i < min(10, a + 3); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                            --used[i][j];
                        }
                    }
                    used[a][b] = used[a + 1][b] = 21000 + pos;
                } else {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_right_side_opened);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_left_side_opened);
                    for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 3, 10); ++j) {
                            --used[i][j];
                        }
                    }
                    used[a][b] = used[a][b + 1] = 20000 + pos;
                }
                break;
            case "triple":
                if (verticalFlag) {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos - 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_bottom_side_opened);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_two_side_vertical);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_top_side_opened);

                    for (int i = max(0, a - 2); i < min(10, a + 3); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                            --used[i][j];
                        }
                    }
                    used[a - 1][b] = used[a][b] = used[a + 1][b] = 31000 + pos - 10;
                } else {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos - 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_right_side_opened);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_two_side_horizontal);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_left_side_opened);

                    for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                        for (int j = max(0, b - 2); j < min(b + 3, 10); ++j) {
                            --used[i][j];
                        }
                    }
                    used[a][b - 1] = used[a][b] = used[a][b + 1] = 30000 + pos - 1;
                }

                break;
            case "quad":
                if (verticalFlag) {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos - 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_bottom_side_opened);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_two_side_vertical);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_two_side_vertical);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 20), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_top_side_opened);

                    for (int i = max(0, a - 2); i < min(10, a + 4); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                            --used[i][j];
                        }
                    }
                    used[a - 1][b] = used[a][b] = used[a + 1][b] = used[a + 2][b] = 41000 + pos - 10;
                } else {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos - 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_right_side_opened);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_two_side_horizontal);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_two_side_horizontal);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos + 2), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageResource(R.drawable.shape_left_side_opened);

                    for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                        for (int j = max(0, b - 2); j < min(b + 4, 10); ++j) {
                            --used[i][j];
                        }
                    }
                    used[a][b - 1] = used[a][b] = used[a][b + 1] = used[a][b + 2] = 40000 + pos - 1;
                }
                break;
            default:
                break;
        }
        if (shipCount == 10) {
            findViewById(R.id.play).setEnabled(true);
        }
    }

    public void releaseShip(View ship, int pos) {
        String type = (String) ship.getTag();
        int a = (pos % 1000) / 10;
        int b = pos % 10;

        Resources res = getResources();
        shipCount--;
        boolean verticalFlag = ((pos / 1000) % 10 == 1);
        switch (pos / 10000) {
            case 1:
                int id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000), "id", getApplicationContext().getPackageName());
                ((ImageView)findViewById(id)).setImageDrawable(null);
                used[a][b] = -2;
                for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                    for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                        ++used[i][j];
                    }
                }
                break;
            case 2:
                if (verticalFlag) {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    used[a][b] = used[a + 1][b] = -2;
                    for (int i = max(0, a - 1); i < min(10, a + 3); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                            ++used[i][j];
                        }
                    }
                } else {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    used[a][b] = used[a][b + 1] = -2;
                    for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 3, 10); ++j) {
                            ++used[i][j];
                        }
                    }
                }

                break;
            case 3:
                if (verticalFlag) {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 20), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);

                    used[a][b] = used[a + 1][b] = used[a + 2][b] = -2;

                    for (int i = max(0, a - 1); i < min(10, a + 4); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                            ++used[i][j];
                        }
                    }
                } else {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 2), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);

                    used[a][b] = used[a][b + 1] = used[a][b + 2] = -2;

                    for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 4, 10); ++j) {
                            ++used[i][j];
                        }
                    }
                }

                break;
            case 4:
                if (verticalFlag) {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 10), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 20), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 30), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);

                    used[a][b] = used[a + 1][b] = used[a + 2][b] = used[a + 3][b] = -2;
                    for (int i = max(0, a - 1); i < min(10, a + 5); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 2, 10); ++j) {
                            ++used[i][j];
                        }
                    }
                } else {
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 1), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 2), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);
                    id = res.getIdentifier("frame_image" + Integer.toString(pos % 1000 + 3), "id", getApplicationContext().getPackageName());
                    ((ImageView)findViewById(id)).setImageDrawable(null);

                    used[a][b] = used[a][b + 1] = used[a][b + 2] = used[a][b + 3] = -2;
                    for (int i = max(0, a - 1); i < min(10, a + 2); ++i) {
                        for (int j = max(0, b - 1); j < min(b + 5, 10); ++j) {
                            ++used[i][j];
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    void showDialog(boolean cancelable) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialog_layout)
                .setTitle("Choose game mode")
                .setPositiveButton("Connect", null)
                .setCancelable(cancelable)
                .create();
        final String hostGameId = UUID.randomUUID().toString().substring(0, 25).replace("-", "");
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                ((RadioGroup)((AlertDialog)dialog).findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.connect:
                                TextInputEditText editText = ((AlertDialog)dialog).findViewById(R.id.game_id);
                                editText.setText("");
                                editText.setHint("Enter game id here");
                                editText.setTag("");
                                editText.requestFocus();
                                break;
                            case R.id.create:
                                editText = ((AlertDialog)dialog).findViewById(R.id.game_id);
                                editText.setText(hostGameId);
                                editText.setTag("host");
                                editText.setHint("Copy and send game id");
                                editText.requestFocus();
                                break;
                            default:
                                break;
                        }
                    }
                });
                ((RadioButton)((AlertDialog)dialog).findViewById(R.id.create)).setChecked(true);
                ((AlertDialog)dialog).findViewById(R.id.copy_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputEditText editText = ((AlertDialog)dialog).findViewById(R.id.game_id);
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("game_id", editText.getText());
                        clipboard.setPrimaryClip(clip);
                    }
                });
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputEditText editText = ((AlertDialog)dialog).findViewById(R.id.game_id);
                        if (editText.getText().length() != hostGameId.length()) {
                            Toast.makeText(ArrangementActivity.this, "Invalid game id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        gameId = editText.getText().toString();
                        final DatabaseReference state = FirebaseDatabase.getInstance().getReference("game").child(gameId);

                        if (editText.getTag().equals("host")) {
                            ((RadioButton)((AlertDialog)dialog).findViewById(R.id.connect)).setEnabled(false);
                            ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            ((ProgressBar)((AlertDialog)dialog).findViewById(R.id.connect_progress)).getLayoutParams().width = 120;
                            ((ProgressBar)((AlertDialog)dialog).findViewById(R.id.connect_progress)).requestLayout();
                            state.child("host").child("name").setValue(userName);
                            state.child("client").child("name").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists())
                                        return;
                                    ((ProgressBar)((AlertDialog)dialog).findViewById(R.id.connect_progress)).setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                    ArrangementActivity.this.finish();
                                    Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                                    intent.putExtra("gameId", gameId);
                                    intent.putExtra("board", getBoard());
                                    intent.putExtra("name", userName);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("isHost", true);
                                    startActivity(intent);
                                    state.child("host").child("name").removeEventListener(this);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            state.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        Toast.makeText(ArrangementActivity.this, "There is no host connection with such id", Toast.LENGTH_SHORT).show();
                                    } else {
                                        FirebaseDatabase.getInstance().getReference("game").child(gameId).child("client").child("name").setValue(userName);
                                        dialog.dismiss();
                                        ArrangementActivity.this.finish();
                                        Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                                        intent.putExtra("gameId", gameId);
                                        intent.putExtra("board", getBoard());
                                        intent.putExtra("name", userName);
                                        intent.putExtra("userId", userId);
                                        intent.putExtra("isHost", false);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private String getBoard() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                if (used[i][j] > 0) {
                    board.append('1');
                } else {
                    board.append('0');
                }
            }
        }
        return board.toString();
    }
}
