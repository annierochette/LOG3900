package com.example.polydraw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatViewModel extends ViewModel {
    private List<Message> messageList;
    RecyclerView myRecyclerView;

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

    public ChatViewModel(List<Message> MessagesList) {
        this.messageList = MessagesList;
    }
    public int getItemCount() {
        return messageList.size();
    }
    public ChatViewModel.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ChatViewModel.MyViewHolder(itemView);
    }
    public void onBindViewHolder(final ChatViewModel.MyViewHolder holder, final int position){
        final Message m = messageList.get(position);
        holder.username.setText(m.getUsername());
        holder.timestamp.setText(m.getTimestamp());
        holder.message.setText(m.getMessage());
    }
}
