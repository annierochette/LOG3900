package com.example.polydraw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatChannelAdapter extends RecyclerView.Adapter<ChatChannelAdapter.MyViewHolder> {
    private List<ChatChannel> ChatChannelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView channelName;


        public MyViewHolder(View view) {
            super(view);

            channelName = (TextView) view.findViewById(R.id.username);
            
        }
    }

    public ChatChannelAdapter(List<ChatChannel> ChatChannelList) {
        this.ChatChannelList = ChatChannelList;
    }

    @Override public int getItemCount() {
        return ChatChannelList.size();
    }

    @NonNull
    @Override
    public ChatChannelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override public void onBindViewHolder(final ChatChannelAdapter.MyViewHolder holder, final int position){
        final ChatChannel channel = ChatChannelList.get(position);
        holder.channelName.setText(channel.getChannelName());
    }
}
