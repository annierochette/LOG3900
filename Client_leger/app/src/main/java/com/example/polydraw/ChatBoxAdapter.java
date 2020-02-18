package com.example.polydraw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private List<Message> MessageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView message;
        public TextView timestamp;


        public MyViewHolder(View view) {
            super(view);

            username = (TextView) view.findViewById(R.id.password);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
        }

    }

    public ChatBoxAdapter(List<Message> MessagesList) {
        this.MessageList = MessagesList;
    }

    @Override public int getItemCount() {
        return MessageList.size();
    }

    @Override public ChatBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final ChatBoxAdapter.MyViewHolder holder, final int position){
        final Message m = MessageList.get(position);
        holder.username.setText(m.getUsername());
        holder.timestamp.setText(m.getTimestamp());
        holder.message.setText(m.getMessage());
    }
}


