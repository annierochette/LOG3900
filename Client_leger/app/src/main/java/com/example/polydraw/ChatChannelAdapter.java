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

            channelName = (TextView) view.findViewById(R.id.channelName);
            
        }
    }


    public ChatChannelAdapter(List<ChatChannel> ChatChannelList) {
        this.ChatChannelList = ChatChannelList;
    }

    @Override public int getItemCount() {
        return ChatChannelList.size();
    }

    @Override
    public ChatChannelAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);

        return new ChatChannelAdapter.MyViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final ChatChannelAdapter.MyViewHolder holder, final int position){
        final ChatChannel channel = ChatChannelList.get(position);
        holder.channelName.setText(channel.getChannelName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(channel.getChannelName());
            }
        });


    }
}
