package com.example.finduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NameActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText etName;
    DatePicker datePicker;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference cid;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        btnSubmit = findViewById(R.id.btnSubmit);
        datePicker = findViewById(R.id.datePicker1);
        etName = findViewById(R.id.etName);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();

        cid = FirebaseDatabase.getInstance().getReference().child("users");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cid.child("Name").setValue(etName.getText().toString());
                String date = datePicker.getDayOfMonth()+"/"+ (datePicker.getMonth() + 1)+"/"+datePicker.getYear();
                cid.child("Date").setValue(date);
                cid.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            Intent i = new Intent(NameActivity.this,UserActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            System.out.println("There was some problem");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
