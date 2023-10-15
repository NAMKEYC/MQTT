package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;


public class PhoneSignInActivity extends Activity {
    private FirebaseAuth mAuth;
    private String Phone;
    private Button btnphone;
    private EditText numPhone;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonesignin);
        mAuth = FirebaseAuth.getInstance();
        numPhone = findViewById(R.id.phone_number);
        btnphone= findViewById(R.id.btnloginPhone);

        btnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog;
                Phone = numPhone.getText().toString().trim();
                String emailPattern = "(84[3|5|7|8|9])+([0-9]{8})\\b";
                if(Phone.matches(emailPattern)){
                    Intent intent = new Intent(PhoneSignInActivity.this, VerificationActivity.class);
                    intent.putExtra("phoneNo", Phone);
                    startActivity(intent);
                }else{
                    numPhone.setError("Input Your number Phone again");
                    numPhone.requestFocus();
                }
            }
        });
    }
}

