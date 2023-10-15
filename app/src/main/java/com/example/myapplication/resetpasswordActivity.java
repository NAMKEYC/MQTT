package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpasswordActivity extends AppCompatActivity {
    private EditText email_reset;
    private Button btnreset;
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        mAuth=FirebaseAuth.getInstance();

        email_reset = findViewById(R.id.Email_reset);
        btnreset=findViewById(R.id.btnreset);

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_reset();
            }
        });
    }
    private void btn_reset(){
        String var_email_reset;
        var_email_reset=email_reset.getText().toString();
        if(TextUtils.isEmpty(var_email_reset)){
            Toast.makeText(this,"Vui lòng nhập email!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.sendPasswordResetEmail(var_email_reset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra email!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(resetpasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"Tài khoản chưa tạo!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
