package com.example.fithub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText emailL, passL;
    Button loginL;
    TextView forgot;
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseUser user;
  //  FirebaseDatabase

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            login = findViewById(R.id.register);
            emailL = findViewById(R.id.username);
            passL = findViewById(R.id.password);
            loginL = findViewById(R.id.loginL);
            forgot = findViewById(R.id.forgotpassword);
            progressDialog = new ProgressDialog(this);
            mAuth =FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            if(mAuth.getCurrentUser() != null){
                startActivity(new Intent(MainActivity.this, two.class));
                finish();
            }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, register.class));
                }
            });
            loginL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perFormLogin();
                }
            });
            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, forgot_password.class);
                    startActivity(intent);
                }
            });
            return insets;
        });
    }



    private void perFormLogin() {
        String email = emailL.getText().toString();
        String password = passL.getText().toString();
        if(!email.matches(emailPattern)){
            emailL.setError("Enter correct email");
        } else if (email.isEmpty()) {
            emailL.setError("Email is required");
        } else if(password.isEmpty() || password.length()<7){
            passL.setError("Password is too short! Minimum 8 character");
        }
        else{
            progressDialog.setMessage("Please wait while Loading....");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();

                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, two.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}