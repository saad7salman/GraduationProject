package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.example.sss.capstone.Activities.ChatMessages;
import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.ChatGroup;

import java.util.ArrayList;

/**
 * Created by SSS on 3/2/18.
 */

public class ChatGroupAdapter extends RecyclerView.Adapter<ChatGroupAdapter.ChatGroupViewHolder>{
    private Context context;
    private ArrayList<ChatGroup> chatGroupsAl;
    private android.support.v4.app.FragmentManager fragmentManager;
    public ChatGroupAdapter(Context context, ArrayList<ChatGroup> chatGroupsAl,FragmentManager fragmentManager) {
        this.context = context;
        this.chatGroupsAl = chatGroupsAl;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ChatGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.message_group_cardview,parent,false);
        ChatGroupViewHolder viewHolder = new ChatGroupViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatGroupViewHolder holder, int position) {
                final ChatGroup chatGroup = chatGroupsAl.get(position);
                holder.customerName.setText(chatGroup.customerFullName);
                holder.chatDate.setText(chatGroup.chatDate);
                holder.cvChatGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChatMessages chatMessages = new ChatMessages();
                        Bundle bundle = new Bundle();
                        bundle.putString("userID",chatGroup.customerID);
                        chatMessages.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.fragmentContentId,
                                chatMessages,chatMessages.getTag()).commit();
                    }
                });
    }
    @Override
    public int getItemCount() {
        if (chatGroupsAl != null){
            return chatGroupsAl.size();
        }
        return 0;
    }

    public static class ChatGroupViewHolder extends RecyclerView.ViewHolder{

        CardView cvChatGroup;
        TextView customerName;
        TextView chatDate;
        public ChatGroupViewHolder(View itemView) {
            super(itemView);
            cvChatGroup = itemView.findViewById(R.id.cvMessageGroups);
            customerName = itemView.findViewById(R.id.customerName);
            chatDate = itemView.findViewById(R.id.chatDate);
        }
    }
}
