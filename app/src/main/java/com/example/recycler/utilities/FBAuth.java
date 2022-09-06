package com.example.recycler.utilities;

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

    private FirebaseAuth auth;

    public String getUid() {
        auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser().getUid().toString();
    }

    public String getTime() {
        SimpleDateFormat resultTime = new SimpleDateFormat("yyyy.MM.dd HH");
        return resultTime.format(new Date());
    }
}
