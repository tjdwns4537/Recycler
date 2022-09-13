package com.example.recycler.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.recycler.R;
import com.example.recycler.databinding.ActivityBoardInsideBinding;
import com.example.recycler.databinding.BoardaddBinding;
import com.example.recycler.models.BoardModel;
import com.example.recycler.utilities.Constants;
import com.example.recycler.utilities.FBdatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BoardInsideActivity extends AppCompatActivity {

    private String TAG = BoardInsideActivity.class.getSimpleName();

    ActivityBoardInsideBinding binding;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();
    StorageReference pathRef = storageRef.child("images/");

    String title;
    String content;
    String time;
    String uid;
    String photoName;
    ImageView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardInsideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = binding.ImageArea;

        title = getIntent().getStringExtra("title").toString();
        content = getIntent().getStringExtra("content").toString();
        time = getIntent().getStringExtra("time").toString();
        uid = getIntent().getStringExtra("uid").toString();
        if (getIntent().getStringExtra("photoName") != null) {
            photoName = getIntent().getStringExtra("photoName").toString();
            FirbaseGetImageData2(photoName);
        }

        binding.titleArea.setText(title);
        binding.textArea.setText(content);
        binding.timeArea.setText(time);

        Log.d(TAG, title);
        Log.d(TAG, content);
        Log.d(TAG, time);
    }


    public void FirbaseGetImageData2(String photoName){

        String resultPath = photoName.substring(photoName.lastIndexOf("/")+1);

        if (pathRef == null) {
            Log.d(TAG, "저장된 사진 없음");
        } else {
            StorageReference submitProfile = storageRef.child("images/" + resultPath);
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d(TAG, uri.toString());
                    Glide.with(BoardInsideActivity.this).load(uri).into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}
