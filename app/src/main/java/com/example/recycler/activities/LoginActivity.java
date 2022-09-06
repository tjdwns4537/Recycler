package com.example.recycler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.MainActivity;
import com.example.recycler.databinding.ActivityLoginBinding;
import com.example.recycler.utilities.FBAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;

    private FirebaseAuth mAuth;

    private String TAG = "LoginActivity";




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setApplicationId("1:822236223355:android:76a60ef632c6b6eece0fde") // Required for Analytics.
//                .setProjectId("com.example.recycler") // Required for Firebase Installations.
//                .setApiKey("AIzaSyDLvmMpy96B4YmChDVuWpK3HdlqEfS94EY") // Required for Auth.
//                .build();
//        FirebaseApp.initializeApp(this, options, "myTest");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Toast.makeText(LoginActivity.this, "No User",
                                    Toast.LENGTH_SHORT).show();
        }


//        mAuth.createUserWithEmailAndPassword("email5@email.com", "qwe123")
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            Toast.makeText(LoginActivity.this, "Auth success.",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Auth failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

//                mAuth.signInWithEmailAndPassword("email5@email.com", "@qwe123")
//                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "성공");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    Toast.makeText(LoginActivity.this, "login success",Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Log.d(TAG, "실패");
//                                    Toast.makeText(LoginActivity.this, "login fail",Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });

            }
        });

//
//
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid();
//        }

    }
}
