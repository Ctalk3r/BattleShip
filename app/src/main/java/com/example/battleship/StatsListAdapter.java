package com.example.battleship;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleship.models.State;

import java.util.ArrayList;

public class StatsListAdapter  extends RecyclerView.Adapter<StatsListAdapter.StatViewHolder>{

    private final ArrayList<State> gameStatistics;

    static class StatViewHolder extends RecyclerView.ViewHolder {
        final TextView hostNameView;
        final TextView otherNameView;
        final TextView hostScoreView;
        final TextView otherScoreview;

        final View statView;

        StatViewHolder(View v)
        {
            super(v);
            statView = v;
            hostNameView = v.findViewById(R.id.player1_name);
            otherNameView = v.findViewById(R.id.player2_name);
            hostScoreView = v.findViewById(R.id.score_player1);
            otherScoreview = v.findViewById(R.id.score_player2);
        }
    }

    public StatsListAdapter(ArrayList<State> list)
    {
        gameStatistics = list;
    }

    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stats_element, parent, false);
        return new StatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StatViewHolder holder, int position) {
        final State statItem = gameStatistics.get(position);
        holder.hostNameView.setText(statItem.getHostName());
        holder.otherNameView.setText(statItem.getName());
        holder.hostScoreView.setText(String.valueOf(statItem.getHostDestroyed()));
        holder.otherScoreview.setText(String.valueOf(statItem.getDestroyed()));
    }


    @Override
    public int getItemCount() {
        return gameStatistics.size();
    }
}