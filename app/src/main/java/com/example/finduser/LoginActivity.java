package com.example.finduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    Button btnGetOtp,btnEnterOtp;
    EditText etOtp,etNumber;
    String codeSent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();
        btnEnterOtp = findViewById(R.id.btnEnterOtp);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        etOtp = findViewById(R.id.etOtp);
        etNumber = findViewById(R.id.etNumber);

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etNumber.getText().toString();
                System.out.println(phoneNumber);
                sendverificationcode(phoneNumber);
            }
        });

        btnEnterOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
    }

    private void verifySignInCode() {
        String code = etOtp.getText().toString();
        if(code.isEmpty())
        {
            etOtp.setError("This section cant be empty");
            etOtp.requestFocus();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Status", "signInWithCredential:success");
                            Intent i = new Intent(LoginActivity.this,NameActivity.class);
                            startActivity(i);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Status", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "Incorrect OTP ENTERED", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendverificationcode(String phoneNumber) {
        if(phoneNumber.isEmpty())
        {
            etNumber.setError("Phone Number is required");
            etNumber.requestFocus();
            return;
        }
        if(phoneNumber.length()!=10)
        {
            etNumber.setError("Phone Number should be of 10 digits");
            etNumber.requestFocus();
            return;
        }
        String phone  ="+91" + phoneNumber;
        System.out.println(phone);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }


    };
}
