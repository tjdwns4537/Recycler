package com.example.recycler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.MainActivity;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBAuth;

public class BoardAddActivity extends AppCompatActivity {

    public BoardaddBinding binding;
    public FBAuth mAuth;
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
        mAuth = new FBAuth();
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
                uid = mAuth.getUid();
                time = mAuth.getTime();

                Log.d(TAG, title);
                Log.d(TAG, content);
                Log.d(TAG, uid);
                Log.d(TAG, time);

                Constants.boardRef
                        .push()
                        .setValue(new BoardModel(title, content, uid, time));

                finish();
            }
        });

        binding.ImagePlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100) {
            binding.ImagePlus.setImageURI(data.getData());
        }
    }
}
