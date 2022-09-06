package com.example.recycler.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.databinding.ActivityJoinBinding;
import com.example.recycler.databinding.ActivityLoginBinding;

public class JoinActivity extends AppCompatActivity {

    private ActivityJoinBinding joinBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        joinBinding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(joinBinding.getRoot());
    }

}
