package com.example.finduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userId;
    MultiAutoCompleteTextView acHobbies;
    RadioGroup rgGender,rgMyGender,rgDrink,rgSmoke;
    RadioButton rbGender,rbMyGender,rbDrink,rbSmoke;
    DatabaseReference cid,search;
    Button btnMatch;
    EditText etLocality,etLower;
    int i,max=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        acHobbies = findViewById(R.id.acHobbies);
        String[] Hobbies = getResources().getStringArray(R.array.Hobbies);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Hobbies);
        acHobbies.setAdapter(adapter);
        acHobbies.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        rgGender = findViewById(R.id.rgGender);
        rgMyGender = findViewById(R.id.rgMyGender);
        btnMatch =findViewById(R.id.btnMatch);
        rgDrink = findViewById(R.id.rgDrink);
        //rgSmoke = findViewById(R.id.rgSmoke);
        etLocality = findViewById(R.id.etLocality);
        //etLower = findViewById(R.id.etLower);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        cid= FirebaseDatabase.getInstance().getReference();
        search = FirebaseDatabase.getInstance().getReference("users");

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = acHobbies.getText().toString().trim();
                String[] singleInputs = input.split("\\s*,\\s*");

                List<String> valSetOne = new ArrayList<>();
                HashMap<String ,Object> hobbies = new HashMap<>();
                for(i=0;i<singleInputs.length;i++)
                {
                    valSetOne.add(singleInputs[i]);
                }

                Object[] objArray = valSetOne.toArray();
                List<Object> obj = new ArrayList<>();
                for(i=0;i<singleInputs.length;i++)
                {
                    obj.add(objArray[i]);
                }
                hobbies.put("hobbies",obj);
                cid.child("users").child(userId).updateChildren(hobbies);

                String Locality = etLocality.getText().toString();
                cid.child("users").child(userId).child("Locality").setValue(Locality);
                cid.child("users").child(userId).child("Age").setValue(etLower.getText().toString());


                Query query2 =  FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("sex").equalTo(rbGender.getText().toString());

                        /*.orderByChild("6").startAt(Integer.parseInt(etLower.getText().toString()))
                        .endAt(Integer.parseInt(etLower.getText().toString())+3)
                        .orderByChild("4").equalTo(rbDrink.getText().toString())
                        .orderByChild("5").equalTo(rbSmoke.getText().toString()).limitToFirst(1);*/
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            Query query = FirebaseDatabase.getInstance().getReference("users")
                                    .orderByChild("1").equalTo(rbMyGender.getText().toString()).limitToFirst(1);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Toast.makeText(UserActivity.this, "A Match Found!!!!"+dataSnapshot.getChildren(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else {
                            Toast.makeText(UserActivity.this, "No match found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                /*for(i=1;i<=6;i++)
                {
                    String str = Integer.toString(i);
                    cid.child(userId).child(str);
                }*/
            }
        });
    }

    public void checkButton(View view) {
        int radioid = rgGender.getCheckedRadioButtonId();
        rbGender = findViewById(radioid);
        cid.child("users").child(userId).child("1").setValue(rbGender.getText());
    }
    public void checkButton2(View view) {
        int radioid = rgDrink.getCheckedRadioButtonId();
        rbDrink = findViewById(radioid);
        cid.child("users").child(userId).child("4").setValue(rbDrink.getText());

    }
    public  void  checkButton3(View view)
    {
        int radioid = rgSmoke.getCheckedRadioButtonId();
        rbSmoke = findViewById(radioid);
        cid.child("users").child(userId).child("5").setValue(rbSmoke.getText());

    }
    public void checkButton4(View view) {
        int radioid = rgMyGender.getCheckedRadioButtonId();
        rbMyGender = findViewById(radioid);
        cid.child("users").child(userId).child("sex").setValue(rbMyGender.getText());
    }
}
