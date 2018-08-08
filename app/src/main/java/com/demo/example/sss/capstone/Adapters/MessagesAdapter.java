package com.demo.example.sss.capstone.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ocpsoft.prettytime.PrettyTime;

import com.demo.example.sss.capstone.R;
import com.demo.example.sss.capstone.model.Message;
import com.github.library.bubbleview.BubbleTextView;

import org.w3c.dom.Text;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by SSS on 2/18/18.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>{
    private Context context;
    private ArrayList<Message> messagesAl;
    private String userType;
    public MessagesAdapter(Context context, ArrayList<Message> messagesAl,String userType) {
        this.context = context;
        this.messagesAl = messagesAl;
        this.userType = userType;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.message_text_layout,parent,false);
        MessageViewHolder viewHolder = new MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
            Message message = messagesAl.get(position);

        PrettyTime prettyTime = new PrettyTime();

        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        java.util.Date date = null;
        try {
             date= format.parse(message.messageTime);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String ago = prettyTime.format(date);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.time.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.time.setLayoutParams(params);
        holder.time.setText(ago);
            if (message.messageUser.equals(userType)){
                holder.sendMessage.setText(message.messageText);
                holder.recMessage.setVisibility(View.INVISIBLE);
            }else{
                holder.recMessage.setText(message.messageText);
                holder.sendMessage.setVisibility(View.INVISIBLE);
            }
    }

    @Override
    public int getItemCount() {
        if (messagesAl != null){
            return messagesAl.size();
        }
        return 0;
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder{
    CardView cvMessages;
    TextView message;
    TextView userType;
    TextView time;
    RelativeLayout relativeLayout;
    BubbleTextView sendMessage;
    BubbleTextView recMessage;
        public MessageViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeMessage);
            cvMessages = itemView.findViewById(R.id.cvMessages);
            //message = itemView.findViewById(R.id.message_text);
            userType = itemView.findViewById(R.id.message_user);
            time = itemView.findViewById(R.id.message_time);
            sendMessage = itemView.findViewById(R.id.bubbleChatSend);
            recMessage = itemView.findViewById(R.id.bubbleChatRec);
        }
    }
}
