package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.chatapp.messages.MessagesAdapter;
import com.example.chatapp.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

private  final List<MessagesList> messagesLists = new ArrayList<>();

     private String mobile;
     private String name;
     private String email;

     private int unseenmessages = 0 ;
    private String lastMessage= "";
    private MessagesAdapter messagesAdapter;
    private  String chatKey= "";
    private boolean dataSet= false;
    private RecyclerView messagesRecycleView;
     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chat-application-d734b-default-rtdb.firebaseio.com/" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);

        messagesRecycleView = findViewById(R.id.messagesRecycleView);

        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        messagesRecycleView.setHasFixedSize(true);
        messagesRecycleView.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new MessagesAdapter(messagesLists,MainActivity.this);


        messagesRecycleView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Lodding...");
        progressDialog.show();

      databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              final String profilePicUrl= snapshot.child("users").child(mobile).child("profile_pic").getValue(String.class);
              if (!profilePicUrl.isEmpty()){
                  Picasso.get().load(profilePicUrl).into(userProfilePic);
              }

              progressDialog.dismiss();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
           progressDialog.dismiss();
          }
      });

      databaseReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              messagesLists.clear();
              unseenmessages = 0;
              lastMessage = "";
              chatKey = "";
              for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){
                  final String getMobile = dataSnapshot.getKey();
                  dataSet = false;
                  if(!getMobile.equals(mobile)){
                      final String getName = dataSnapshot.child("name").getValue(String.class);
                      final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);

                      databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              int getChatCounts = (int) snapshot.getChildrenCount();

                              if (getChatCounts > 0){
                                  for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                      final String getKey = dataSnapshot1.getKey();

                                      chatKey = getKey;
                                   if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                      final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                      final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                      if ((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile) && getUserTwo.equals(getMobile))){
                                          for ( DataSnapshot chatDataSnapshot :  dataSnapshot1.child("messages").getChildren()) {
                                              final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                              final long getLastSeenMessage = Long.parseLong(MemoryData.getLastmsg(MainActivity.this,getKey));
                                              lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                              if (getMessageKey > getLastSeenMessage) {
                                                  unseenmessages++;
                                              }
                                          }
                                          }
                                      }
                                  }
                              }
                              if (!dataSet){
                                  dataSet = true;
                                  MessagesList messagesList = new MessagesList(getName, getMobile, lastMessage,getProfilePic, unseenmessages,chatKey);
                                  messagesLists.add(messagesList);
                                  messagesAdapter.updateData(messagesLists);
                              }

                          }
                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });


                  }
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

    }
}