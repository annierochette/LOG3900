package com.example.polydraw;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class matchListAdapter extends RecyclerView.Adapter<matchListAdapter.MyViewHolder> {
    private ArrayList<String> matchList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button button;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.gameName);
            button = (Button) view.findViewById(R.id.joinButton);
        }

    }

    public matchListAdapter(ArrayList<String> matchList) {
        this.matchList = matchList;
    }

    @Override public int getItemCount() {
        return matchList.size();
    }

    @Override public matchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);

        return new matchListAdapter.MyViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final matchListAdapter.MyViewHolder holder, final int position){
        final String s = matchList.get(position);
        holder.name.setText(s);

    }
}
