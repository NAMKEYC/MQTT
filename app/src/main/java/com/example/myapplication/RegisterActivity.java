package com.example.myapplication;
import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText mail,pass,conf_pass,inf_name,inf_phone;
    private Button btnlregister;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        inf_name=findViewById(R.id.inf_name);
        inf_phone=findViewById(R.id.inf_phone);
        mail=findViewById(R.id.inf_Email);
        pass=findViewById(R.id.password);
        conf_pass=findViewById(R.id.confirm);
        btnlregister = findViewById(R.id.btnregister);

        btnlregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String var_mail, var_confirm, var_pass;
        var_mail = mail.getText().toString();
        var_pass = pass.getText().toString();
        var_confirm = conf_pass.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(var_mail) ) {
            Toast.makeText(this, "Vui lòng nhập Account!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(var_pass)) {
            Toast.makeText(this, "Vui lòng nhập password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(var_confirm)) {
            Toast.makeText(this, "Vui lòng nhập Confirm password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (var_confirm.equals(var_pass) & (!TextUtils.isEmpty(var_mail)) & (var_mail.matches(emailPattern)) ) {
            mAuth.createUserWithEmailAndPassword(var_mail, var_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        ProgressDialog progressDialog;
                        FirebaseUser user =mAuth.getCurrentUser();
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    String user_email = user.getEmail();
                                    String user_UID = user.getUid();
                                    String user_phone= inf_phone.getText().toString();
                                    String user_name = inf_name.getText().toString();
                                    HashMap<String, String> profile = new HashMap<>();
                                    // Add keys and values (Country, City)
                                    profile.put("Email", user_email);
                                    profile.put("UID", user_UID);
                                    profile.put("Name",user_name);
                                    profile.put("Phone",user_phone);
                                    profile.put("IDD","");
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("users");
                                    reference.child(user_UID).setValue(profile);
                                    Toast.makeText(getApplicationContext(), "Success. Please check your email", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "send mail Verification FAIL, Please Sign Up again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Account Fail, Sign Up again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Confirm password fail", Toast.LENGTH_SHORT).show();
        }
    }
}