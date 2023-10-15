package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class VerificationActivity extends Activity {
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Button btn_ver_phone;
    private EditText codeOTP;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mAuth = FirebaseAuth.getInstance();
        codeOTP = findViewById(R.id.OTP_Ver);
        btn_ver_phone = findViewById(R.id.btn_ver_Phone);




        String phoneNo = "+"+getIntent().getStringExtra("phoneNo");

        startPhoneNumberVerification(phoneNo);

        btn_ver_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeOTP.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    codeOTP.setError("WRONG OTP");
                    codeOTP.requestFocus();
                    return;
                }
                vericode(code);
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNo) {
        // [START start_phone_auth]
        System.out.println(phoneNo + "sdt");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            String code = credential.getSmsCode();
            System.out.println(code + "otp");
            System.out.println(code + "code");
            if (code != null){
                //progressBar.setVisibility(View.VISIBLE);
                vericode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            //Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

        }
    };
//    private void
    private void vericode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,codeByUser);
        signInWithPhoneAuthCredential(credential);
}
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        String phoneNo = "+"+getIntent().getStringExtra("phoneNo");
        mAuth.signInWithCredential(credential )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String UID = user.getUid();
                            HashMap<String, String> profile = new HashMap<>();

                            // Add keys and values (Country, City)
                            profile.put("Email","");
                            profile.put("UID",UID);
                            profile.put("Name", "");
                            profile.put("Phone",phoneNo);
                            profile.put("IDD","");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("users");
                            reference.child(user.getUid()).setValue(profile);
                            // Update
                            updateUI(user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(VerificationActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user){
        Intent i = new Intent(VerificationActivity.this,LoginMQTT.class );
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}

