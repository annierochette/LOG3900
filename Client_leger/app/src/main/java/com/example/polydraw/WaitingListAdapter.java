package com.example.polydraw;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.polydraw.Socket.SocketIO;

import java.util.ArrayList;
import java.util.List;

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.MyViewHolder> {
    private List<String> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.playerName);
        }

    }

    public WaitingListAdapter(List<String> userList) {
        this.userList = userList;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public WaitingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_list, parent, false);

        return new WaitingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WaitingListAdapter.MyViewHolder holder, final int position) {
        final String s = userList.get(position);
        holder.name.setText(s);
        System.out.println();

    }
}
