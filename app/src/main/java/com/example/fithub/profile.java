package com.example.fithub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private TextView textwelcome, textfulname, textemail, textdob, textweight, textmobile;
    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth fauth;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textwelcome = findViewById(R.id.username);
        textfulname = findViewById(R.id.name);
        textmobile = findViewById(R.id.mobile);
        textdob = findViewById(R.id.dob);
        textemail = findViewById(R.id.email);
        textweight = findViewById(R.id.weight);
        imageView = findViewById(R.id.dp);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();


        fauth = FirebaseAuth.getInstance();
        FirebaseUser user = fauth.getCurrentUser();

        if (user != null) {
            String userID = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID);
            getUserData();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getUserData() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    UserProfile user = dataSnapshot.getValue(UserProfile.class);
                    if (user != null) {
                        displayUserProfile(user);
                    } else {
                        Log.d("UserProfile", "User data is null");
                        Toast.makeText(profile.this, "User data is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("UserProfile", "No data available");
                    Toast.makeText(profile.this, "No user data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Log.e("UserProfile", "Error getting user data: ", databaseError.toException());
                Toast.makeText(profile.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserProfile(UserProfile user) {
        textwelcome.setText(user.getUsername());
        textfulname.setText(user.getName());
        textdob.setText(user.getDob());
        textweight.setText(user.getWeight());
        textemail.setText(user.getEmail());
        textmobile.setText(user.getPhone());
    }
}
