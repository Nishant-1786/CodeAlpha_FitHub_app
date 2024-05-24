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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class register extends AppCompatActivity {
    private EditText userR, emailR, nameR, passR, mobile, dob, weight;
    private Button RegisterR;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;


   // public String Username = userR.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            TextView login = findViewById(R.id.logi);
            userR = findViewById(R.id.userR);
            emailR = findViewById(R.id.emailR);
            nameR = findViewById(R.id.nameR);
            passR = findViewById(R.id.passR);
            mobile = findViewById(R.id.mobile);
            dob = findViewById(R.id.birth);
            weight = findViewById(R.id.weight);
            RegisterR = findViewById(R.id.RegisterR);
            progressDialog = new ProgressDialog(this);
            mAuth =FirebaseAuth.getInstance();
            fstore = FirebaseFirestore.getInstance();
            database = FirebaseDatabase.getInstance();
            usersRef = database.getReference("users");


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(register.this,MainActivity.class));
                }
            });
            RegisterR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PerformAuth();
                }
            });
            return insets;
        });
    }
    public void PerformAuth() {
        String email = emailR.getText().toString();
        String password = passR.getText().toString();
        String Name = nameR.getText().toString();
        String Username = userR.getText().toString();
        String Dob = dob.getText().toString();
        String Weight = weight.getText().toString();
        String Mobile = mobile.getText().toString();
        if(!email.matches(emailPattern)){
            emailR.setError("Enter correct email");
        } else if (email.isEmpty()) {
            emailR.setError("Email Required");
        } else if(password.isEmpty() || password.length()<7){
            passR.setError("Password is too short! Minimum 8 character");
        }
        else if(Name.isEmpty()){
            nameR.setError("Name should not be empty");
        } else if (Username.isEmpty()) {
            userR.setError("Username should not be empty");
        }
        else{
            progressDialog.setMessage("Please wait while Registering....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            UserProfile userProfile = new UserProfile(Username, email, Mobile, Name, Dob, Weight);
                            usersRef.child(userId).setValue(userProfile);
                            sendUserToNextActivity();
                        }
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(register.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserToNextActivity() {
        Intent intent = new Intent(register.this, two.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


