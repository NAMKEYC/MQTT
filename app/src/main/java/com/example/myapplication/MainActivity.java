package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mail, pass;
    private TextView reset_Pass, signup;
    private Button btnlogin;
    private ImageView btngoogle, btnphone;
    private FirebaseAuth mAuth;
    private long backPressTime;


    public void onBackPressed(){
        if (backPressTime+2000>System.currentTimeMillis()){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);

        } else {
            Toast.makeText(MainActivity.this,"Press back again to exit the application", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.Email);
        pass = findViewById(R.id.password);
        btnlogin = findViewById(R.id.btnlogin);
        btngoogle = findViewById(R.id.btngoogle);
        btnphone = findViewById(R.id.btnphone);
        signup = findViewById(R.id.SignUp);
        reset_Pass = findViewById(R.id.reset_password);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInPhone();
            }
        });

        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingoogle();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
            }
        });
        reset_Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void SignInPhone() {
        Intent i = new Intent(MainActivity.this, PhoneSignInActivity.class);
        startActivity(i);
    }

    private void signingoogle() {
        Intent i = new Intent(MainActivity.this, GoogleSignInActivity.class);
        startActivity(i);
    }

    private void Signup() {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void resetPassword() {
        Intent i = new Intent(MainActivity.this, resetpasswordActivity.class);
        startActivity(i);
    }

    private void login() {
        String var_mail, var_pass;
        var_mail = mail.getText().toString();
        var_pass = pass.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        ProgressDialog progressDialog;

        if (!var_mail.matches(emailPattern)) {
            mail.setError("Enter Connext mail");
            mail.requestFocus();
        }
        if (TextUtils.isEmpty(var_mail)) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(var_pass)) {
            Toast.makeText(this, "Vui lòng nhập password!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(var_mail, var_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                        Toast.makeText(getApplicationContext(), "Sign In Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginMQTT.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Verify to login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "login fail!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}