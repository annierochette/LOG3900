package com.example.polydraw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatChannelAdapter extends RecyclerView.Adapter<ChatChannelAdapter.MyViewHolder> {
    private List<ChatChannel> ChatChannelList;
    private OnChannelListener mOnChannelListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView channelName;
        public ImageView channelDot;

        OnChannelListener onChannelListener;

        public MyViewHolder(View view, OnChannelListener onChannelListener) {
            super(view);

            channelName = (TextView) view.findViewById(R.id.channelName);
            channelDot = (ImageView) view.findViewById(R.id.greendot);
            this.onChannelListener = onChannelListener;

            view.setOnClickListener(this);
            
        }

        @Override
        public void onClick(View v) {
            onChannelListener.onChannelClick(getAdapterPosition());
        }
    }

    public ChatChannelAdapter(List<ChatChannel> ChatChannelList, OnChannelListener onChannelListener) {
        this.ChatChannelList = ChatChannelList;
        this.mOnChannelListener = onChannelListener;
    }

    @Override public int getItemCount() {
        return ChatChannelList.size();
    }

    public interface OnChannelListener{
        void onChannelClick(int position);
    }

    @Override
    public ChatChannelAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);

        return new ChatChannelAdapter.MyViewHolder(itemView, mOnChannelListener);
    }

    @Override public void onBindViewHolder(final ChatChannelAdapter.MyViewHolder holder, final int position){
        final ChatChannel channel = ChatChannelList.get(position);
        holder.channelName.setText(channel.getChannelName());

        if(channel.isCurrent()){
            holder.channelDot.setVisibility(View.VISIBLE);
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(channel.getChannelName());
//            }
//        });

    }
}
