package com.example.chatapp.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.MemoryData;
import com.example.chatapp.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    List<ChatList> chatLists;
     final Context context;
     final String userMobile;



    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userMobile = MemoryData.getData(context);

    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adpter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
    ChatList list2 = chatLists.get(position);
    if(list2.getMobile().equals(userMobile)){
      holder.oppoLayout.setVisibility(View.GONE);
      holder.myLayout.setVisibility(View.VISIBLE);
      holder.myMessage.setText(list2.getMessages());
      holder.myTime.setText(String.format("%s %s", list2.getDate(), list2.getTime()));

    }
    else {

        holder.oppoLayout.setVisibility(View.VISIBLE);
        holder.myLayout.setVisibility(View.GONE);
        holder.oppoMessage.setText(list2.getMessages());
        holder.oppoTime.setText(String.format("%s %s", list2.getDate(), list2.getTime()));
    }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists){
        this.chatLists =chatLists;

    }
    static class  MyViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout oppoLayout,myLayout;
        final TextView oppoMessage,myMessage;
        final TextView oppoTime, myTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oppoLayout = itemView.findViewById(R.id.oppolayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            oppoTime= itemView.findViewById(R.id.oppoMsgTime);
            myLayout = itemView.findViewById(R.id.mylayout);
            myMessage = itemView.findViewById(R.id.myMessage);
            myTime = itemView.findViewById(R.id.myMsgTime);



        }
    }

}
