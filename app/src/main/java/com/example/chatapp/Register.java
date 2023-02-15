package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://chat-application-d734b-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText name = findViewById(R.id.r_name);
        final  EditText mobile = findViewById(R.id.r_mobile);
        final  EditText email = findViewById(R.id.r_email);
        final AppCompatButton registerbtn = findViewById(R.id.r_registerbtn);

        ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Lodding...");

        if (!MemoryData.getData(this).isEmpty()){
            Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Register.this,MainActivity.class);
            i.putExtra("mobile", MemoryData.getData(this));
            i.putExtra("name",MemoryData.getName(this));
            i.putExtra("email","");
            startActivity(i);
            finish();
        }


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final  String Nametext= name.getText().toString();
                final  String Mobiletext= mobile.getText().toString();
                final  String Emailtext= email.getText().toString();
                if(Nametext.isEmpty() || Mobiletext.isEmpty() || Emailtext.isEmpty()){
                    Toast.makeText(Register.this, "All Fields Required!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressDialog.dismiss();
                            if(snapshot.child("users").hasChild(Mobiletext)){
                                Toast.makeText(Register.this, "Mobile already exits", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                databaseReference.child("users").child(Mobiletext).child("name").setValue(Nametext);
                                databaseReference.child("users").child(Mobiletext).child("email").setValue(Emailtext);
                                databaseReference.child("users").child(Mobiletext).child("profile_pic").setValue("");
                                MemoryData.saveData(Mobiletext,Register.this);

                                MemoryData.saveName(Nametext,Register.this);

                                Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Register.this,MainActivity.class);
                                i.putExtra("mobile",Mobiletext);
                                i.putExtra("name",Nametext);

                                i.putExtra("email",Emailtext);
                                startActivity(i);
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                         progressDialog.dismiss();
                        }
                    });


                }

            }
        });
    }
}