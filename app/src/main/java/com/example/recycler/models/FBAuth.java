package com.example.recycler.models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.recycler.activities.BoardAddActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

public class FBAuth {

    String TAG = "util";

    public String getFBAuth() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser resultAuth = mAuth.getCurrentUser();
//        updateUI(currentUser);
//        resultAuth.getUid().toString();

        mAuth.signInAnonymously()
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = null;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());

                        }
                        //return user;
                    }
                });


    }

    public String getTime() {
        SimpleDateFormat resultTime = new SimpleDateFormat("yyyy.MM.dd HH");
        return resultTime.format(new Date());
    }
}
