package com.example.recycler.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.R;
import com.example.recycler.databinding.ActivityBoardInsideBinding;
import com.example.recycler.databinding.BoardaddBinding;

public class BoardInsideActivity extends AppCompatActivity {

    private String TAG = BoardInsideActivity.class.getSimpleName();

    ActivityBoardInsideBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBoardInsideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("title").toString();
        String content = getIntent().getStringExtra("content").toString();
        String time = getIntent().getStringExtra("time").toString();

        binding.titleArea.setText(title);
        binding.textArea.setText(content);
        binding.timeArea.setText(time);

        Log.d(TAG, title);
        Log.d(TAG, content);
        Log.d(TAG, time);

    }
}
