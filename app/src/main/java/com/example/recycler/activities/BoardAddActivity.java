package com.example.recycler.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.models.FBAuth;
import com.example.recycler.utilities.Constants;

public class BoardAddActivity extends AppCompatActivity {

    BoardaddBinding binding;
    FBAuth FBAuth;
    public String TAG = "BoardWriteActivity";
    public String title;
    public String content;
    public String uid;
    public String time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BoardaddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    public void init() {

        registerBtnAction(); // 게시글 등록 버튼
    }

    public void registerBtnAction() {
        binding.registerBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click");

                //변수
                title = binding.titleWrite.getText().toString();
                content = binding.contentWrite.getText().toString();
                uid = FBAuth.getFBAuth();
                time = FBAuth.getTime();

                Log.d(TAG, title);
                Log.d(TAG, content);
                Log.d(TAG, uid);
                Log.d(TAG, time);

                Constants.boardRef
                        .push()
                        .setValue(new BoardModel(title, content, uid, time));


            }
        });
    }
}
