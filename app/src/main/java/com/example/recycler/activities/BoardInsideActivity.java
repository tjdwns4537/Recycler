package com.example.recycler.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.R;

public class BoardInsideActivity extends AppCompatActivity {

    private String TAG = BoardInsideActivity.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_inside);

        String title = getIntent().getStringExtra("title").toString();
        String content = getIntent().getStringExtra("content").toString();
        String time = getIntent().getStringExtra("time").toString();

        Log.d(TAG, title);
        Log.d(TAG, content);
        Log.d(TAG, time);

    }
}
